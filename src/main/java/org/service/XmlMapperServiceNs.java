package org.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.ProcessingException;
import jakarta.xml.bind.*;
import org.config.TransferConfig;
import org.fin_avq_gen.FndtMsgType;
import org.fin_avq_gen.Hdr;
import org.fin_avq_gen.ip.InstantPayment;
import org.fin_avq_gen.pacs.Document;

import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;
import java.io.StringWriter;

@ApplicationScoped
public class XmlMapperServiceNs {

    @Inject
    TransferConfig config;

    public String processNs(InputStream inputXmlStream) throws Exception {
        if (inputXmlStream == null) {
            throw new IllegalArgumentException("Input XML stream cannot be null.");
        }

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance("org.fin_avq_gen:org.fin_avq_gen.pacs");
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();


            //<FndtMsg>
            JAXBElement<FndtMsgType> jaxbElement = unmarshaller.unmarshal(
                    new StreamSource(inputXmlStream),
                    FndtMsgType.class
            );
            FndtMsgType fndtMsgType = jaxbElement.getValue();


            //<Document> IN
            Object any = fndtMsgType.getMsg().getPmnt().getAny();

            if (!(any instanceof JAXBElement)) {
                throw new IllegalStateException("Expected JAXBElement in <Any>, but got: " + any.getClass());
            }

            Object value = ((JAXBElement<?>) any).getValue();

            if (!(value instanceof Document)) {
                throw new IllegalStateException("Expected Document inside JAXBElement, but got: " + value.getClass());
            }

            Document inPacs008 = (Document) value;


            //<Hdr>
            Hdr hdr = new Hdr();
            Hdr.Ident ident = new Hdr.Ident();


            hdr.setDirection(config.getDirectionIn());
            ident.setTp(config.getIdentType());
            ident.setVal(inPacs008.getFIToFICstmrCdtTrf().getGrpHdr().getInstgAgt().getFinInstnId().getBICFI());
            hdr.getIdent().add(ident);


            //<Document> OUT
            Document outPacs008 = new Document();
            outPacs008.setFIToFICstmrCdtTrf(inPacs008.getFIToFICstmrCdtTrf());


            //<InstantPayment>
            InstantPayment ip = new InstantPayment();
            ip.setHdr(hdr);
            ip.setDocument(outPacs008);

            StringWriter writer = new StringWriter();
            JAXBContext context = JAXBContext.newInstance(InstantPayment.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(ip, writer);
            return writer.toString();
        } catch (JAXBException | ClassCastException e) {
            throw new ProcessingException("Error during XML processing", e);
        }
    }
}
