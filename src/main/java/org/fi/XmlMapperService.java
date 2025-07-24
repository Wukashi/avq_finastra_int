package org.fi;

import generated.Hdr;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;

public class XmlMapperService {

    public InputStream process(InputStream inputXmlStream) throws Exception {
        // Parsowanie inputa
        Document doc = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder().parse(inputXmlStream);

        doc.getDocumentElement().normalize();

        // Pobieranie wartosci z inputa
        String direction = "IN";
        String identTp = "BICFI";
        String identValue = doc.getElementsByTagName("BICFI").item(0).getTextContent();

        // Mapowanie danych do output
        Hdr hdr = new Hdr();

        // Direction
        hdr.setDirection(direction);

        // Ident
        Hdr.Ident ident = new Hdr.Ident();
        ident.setTp(identTp);
        ident.setVal(identValue);
        hdr.getIdent().add(ident);



        // Generowanie output.xml
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JAXBContext context = JAXBContext.newInstance(Hdr.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(hdr, outputStream);

        return new ByteArrayInputStream(outputStream.toByteArray());
    }
}
