package org.resource;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.xml.bind.*;

import org.service.TransferService;
import org.fin_avq_gen.FndtMsgType;
import org.fin_avq_gen.Hdr;
import org.fin_avq_gen.ip.InstantPayment;
import org.fin_avq_gen.pacs.Document;
import org.util.XMLFormatter;

@RequestScoped
@Path("/transfer_xmlns_to_doc")
public class TransferXmlnsToDoc {

    @Inject
    TransferService service;

    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public String receiveTransferRawDoc(String xml) throws JAXBException {
        InstantPayment response = service.process(xml);

        JAXBContext jaxbContext = JAXBContext.newInstance(
                FndtMsgType.class, Document.class, Hdr.class, InstantPayment.class
        );
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);

        return XMLFormatter.getXmlOut(marshaller, response, false);
    }
}
