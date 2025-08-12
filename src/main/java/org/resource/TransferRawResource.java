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

import java.io.StringWriter;

@RequestScoped
@Path("/transfer_raw")
public class TransferRawResource {

    @Inject
    TransferService service;

    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public String receiveTransferRaw(String xml) throws JAXBException {
        InstantPayment response = service.process(xml);

        JAXBContext jaxbContext = JAXBContext.newInstance(
                FndtMsgType.class, Document.class, Hdr.class, InstantPayment.class
        );
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);

        StringWriter writer = new StringWriter();
        marshaller.marshal(response, writer);
        return writer.toString();
    }
}
