package org.fi;

import org.fin_avq_gen.*;
import org.fin_avq_gen.ip.InstantPayment;
import org.fin_avq_gen.pacs.*;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.*;


public class XmlMapperService {

    public void process(InputStream inputXmlStream) throws Exception {


        JAXBContext jaxbContext = JAXBContext.newInstance("org.fin_avq_gen:org.fin_avq_gen.pacs");
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();


        //<FndtMsg>
        JAXBElement<FndtMsgType> jaxbElement = (JAXBElement<FndtMsgType>) unmarshaller.unmarshal(inputXmlStream);
        FndtMsgType fndtMsgType = jaxbElement.getValue();


        //<Document> IN
        JAXBElement<Document> anyJAXBElement = (JAXBElement<Document>) fndtMsgType.getMsg().getPmnt().getAny();
        Document inPacs008 = anyJAXBElement.getValue();


        //<Hdr>
        Hdr hdr = new Hdr();
        Hdr.Ident ident = new Hdr.Ident();


        hdr.setDirection("IN");
        ident.setTp("BICFI");
//      ident.setVal("XAVALULU");
        ident.setVal(inPacs008.getFIToFICstmrCdtTrf().getGrpHdr().getInstgAgt().getFinInstnId().getBICFI());
        hdr.getIdent().add(ident);


        //<Document> OUT
        Document outPacs008 = new Document();
        outPacs008.setFIToFICstmrCdtTrf(inPacs008.getFIToFICstmrCdtTrf());


        //<InstantPayment>
        InstantPayment ip = new InstantPayment();
        ip.setHdr(hdr);
        ip.setDocument(outPacs008);


        JAXBContext context = JAXBContext.newInstance(InstantPayment.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        System.out.println("Outgoing message:");
        marshaller.marshal(ip, System.out);

    }
}
