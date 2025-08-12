package org.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.ProcessingException;
import jakarta.xml.bind.*;
import org.config.TransferConfig;
import org.fin_avq_gen.*;
import org.fin_avq_gen.ip.InstantPayment;
import org.fin_avq_gen.pacs.*;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

@ApplicationScoped
public class XmlMapperServiceDOM {

    @Inject
    TransferConfig config;

    public String processDOM(InputStream inputXmlStream) throws Exception {
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


            //marshal to DOM
            DocumentBuilderFactory dbf = javax.xml.parsers.DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            org.w3c.dom.Document domDoc = db.newDocument();


            JAXBContext context = JAXBContext.newInstance(InstantPayment.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(ip, domDoc);


            //namespace/prefix delete
            removeNamespace(domDoc.getDocumentElement());


            //xmlns:xsi add
            addXsiNamespaceDocument(domDoc);


            //transform and out
            StringWriter writer = new StringWriter();
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(domDoc), new StreamResult(writer));
            return writer.toString();
        } catch (JAXBException | ClassCastException | TransformerException e) {
            throw new ProcessingException("Error during XML processing", e);
        }
    }


    // namespace removing method for processDOM
    private static void removeNamespace(org.w3c.dom.Node node) {
        if (node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
            node.setPrefix(null);
        }

        org.w3c.dom.NamedNodeMap attrs = node.getAttributes();
        if (attrs != null) {
            for (int i = attrs.getLength() - 1; i >= 0; i--) {
                org.w3c.dom.Node attr = attrs.item(i);
                if (attr.getNodeName().startsWith("xmlns") || attr.getPrefix() != null) {
                    attrs.removeNamedItem(attr.getNodeName());
                }
            }
        }

        org.w3c.dom.Node child = node.getFirstChild();
        while (child != null) {
            removeNamespace(child);
            child = child.getNextSibling();
        }
    }


    // attribute xmlns:xsi add to Document
    private static void addXsiNamespaceDocument(org.w3c.dom.Document domDoc) {
        NodeList documentNodes = domDoc.getElementsByTagName("Document");
        if (documentNodes.getLength() > 0) {
            Element documentElem = (Element) documentNodes.item(0);
            documentElem.setAttributeNS(
                    "http://www.w3.org/2000/xmlns/",
                    "xmlns:xsi",
                    "http://www.w3.org/2001/XMLSchema-instance"
            );
        }
    }
}
