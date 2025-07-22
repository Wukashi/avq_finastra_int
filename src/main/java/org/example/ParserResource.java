package org.example;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.xml.bind.*;

import java.io.StringReader;
import java.io.StringWriter;

@Path("/parse")
public class ParserResource {

    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public String parseXml(String inputXml) throws Exception {
        JAXBContext context = JAXBContext.newInstance(InputXml.class, OutputXml.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        InputXml input = (InputXml) unmarshaller.unmarshal(new StringReader(inputXml));

        OutputXml output = new OutputXml();
        output.messageId = input.messageId;
        output.settlementDate = input.settlementDate;
        output.debtorName = input.debtorName;
        output.debtorIban = input.debtorIban;
        output.creditorName = input.creditorName;
        output.creditorIban = input.creditorIban;
        output.amount = input.amount;
        output.serviceLevel = input.serviceLevel;
        output.chargeBearer = input.chargeBearer;
        output.StrtNm = input.AdrLine[0];
        output.BldgNb = input.AdrLine[1];
        output.Ctry = input.Ctry;

        StringWriter writer = new StringWriter();
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(output, writer);

        return writer.toString();
    }
}
