package lukasz;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.xml.bind.*;

import org.fin_avq_gen.FndtMsgType;
import org.fin_avq_gen.Hdr;
import org.fin_avq_gen.ip.InstantPayment;
import org.fin_avq_gen.pacs.Document;

import java.io.StringReader;

@ApplicationScoped
public class TransferService {

    @Inject
    TransferConfig config;

    public InstantPayment process(String xml) throws JAXBException {
        FndtMsgType fndtMsgType = extractFndtMsgType(xml);
        if (fndtMsgType == null) {
            throw new BadRequestException("Invalid XML input.");
        }

        Document inPacs008 = extractDocument(fndtMsgType);
        return buildResponse(inPacs008);
    }

    private FndtMsgType extractFndtMsgType(String xml) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(FndtMsgType.class, Document.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader reader = new StringReader(xml);

        Object result = unmarshaller.unmarshal(reader);
        if (result instanceof JAXBElement<?> element) {
            Object value = element.getValue();
            if (value instanceof FndtMsgType msgType) {
                return msgType;
            }
        }
        return null;
    }

    private Document extractDocument(FndtMsgType fndtMsgType) {
        Object any = fndtMsgType.getMsg().getPmnt().getAny();
        if (any instanceof JAXBElement<?> element) {
            Object value = element.getValue();
            if (value instanceof Document doc) {
                return doc;
            }
        }
        throw new BadRequestException("Unexpected payload structure.");
    }

    private InstantPayment buildResponse(Document inPacs008) {
        String bicfi = inPacs008.getFIToFICstmrCdtTrf()
                                .getGrpHdr()
                                .getInstgAgt()
                                .getFinInstnId()
                                .getBICFI();

        Hdr.Ident ident = new Hdr.Ident();
        ident.setTp(config.getIdentType());
        ident.setVal(bicfi);

        Hdr hdr = new Hdr();
        hdr.setDirection(config.getDirectionIn());
        hdr.getIdent().add(ident);

        Document outDoc = new Document();
        outDoc.setFIToFICstmrCdtTrf(inPacs008.getFIToFICstmrCdtTrf());

        InstantPayment instantPayment = new InstantPayment();
        instantPayment.setHdr(hdr);
        instantPayment.setDocument(outDoc);

        return instantPayment;
    }
}
