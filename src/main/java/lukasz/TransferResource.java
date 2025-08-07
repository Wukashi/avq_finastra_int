package lukasz;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.xml.bind.*;

import org.fin_avq_gen.FndtMsgType;
import org.fin_avq_gen.Hdr;
import org.fin_avq_gen.ip.InstantPayment;
import org.fin_avq_gen.pacs.Document;

@RequestScoped
@Path("/transfer")
public class TransferResource {

    @Inject
    TransferService service;

    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.TEXT_PLAIN)
    public String receiveTransfer(String xml) throws JAXBException {
        InstantPayment response = service.process(xml);

        JAXBContext jaxbContext = JAXBContext.newInstance(
                FndtMsgType.class, Document.class, Hdr.class, InstantPayment.class
        );
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        return XMLFormatter.getXmlOut(marshaller, response);
    }
}
