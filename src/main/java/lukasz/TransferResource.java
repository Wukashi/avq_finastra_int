package lukasz;


import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.xml.bind.*;
import org.fin_avq_gen.FndtMsgType;
import org.fin_avq_gen.Hdr;
import org.fin_avq_gen.InstantPayment;
import org.fin_avq_gen.pacs.Document;

import java.io.StringReader;

@Path("/transfer")
public class TransferResource {

    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.TEXT_PLAIN)
    public String receiveTransfer(String xml) throws JAXBException {

        JAXBContext jaxbContext = JAXBContext.newInstance(
                FndtMsgType.class,
                Document.class,
                Hdr.class,
                InstantPayment.class
        );

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader stringReader = new StringReader(xml);

        JAXBElement<FndtMsgType> jaxbElement = (JAXBElement<FndtMsgType>) unmarshaller.unmarshal(stringReader);
        FndtMsgType fndtMsgType = jaxbElement.getValue();

        JAXBElement<Document> anyJAXBElement = (JAXBElement<Document>) fndtMsgType.getMsg().getPmnt().getAny();
        Document inPacs008 = anyJAXBElement.getValue();

        Hdr hdr = new Hdr();
        Hdr.Ident ident = new Hdr.Ident();
        hdr.setDirection(Constants.DIRECTION_IN);
        ident.setTp(Constants.IDENT_TYPE_BICFI);
        ident.setVal(inPacs008.getFIToFICstmrCdtTrf().getGrpHdr().getInstgAgt().getFinInstnId().getBICFI());
        hdr.getIdent().add(ident);

        Document outPacs008 = new Document();
        outPacs008.setFIToFICstmrCdtTrf(inPacs008.getFIToFICstmrCdtTrf());

        InstantPayment instantPayment = new InstantPayment();
        instantPayment.setHdr(hdr);
        instantPayment.setDocument(outPacs008);

        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        String xmlOut = XMLFormatter.getXmlOut(marshaller, instantPayment);

        System.out.println("xmlOut: " + xmlOut);

        return xmlOut;
    }
}
