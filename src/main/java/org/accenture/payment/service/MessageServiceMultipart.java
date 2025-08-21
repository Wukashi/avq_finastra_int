package org.accenture.payment.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.jms.*;

import org.jboss.resteasy.reactive.server.multipart.FormValue;
import org.jboss.resteasy.reactive.server.multipart.MultipartFormDataInput;

import java.util.Collection;
import java.util.Map;

@ApplicationScoped
public class MessageServiceMultipart {

    @Inject
    ConnectionFactory connectionFactory;

    public String processMultipart(MultipartFormDataInput mpfData) {
        Map<String, Collection<FormValue>> values = mpfData.getValues();
        FormValue xml = values.get("pain001").iterator().next();

        return xml.getValue();
    }

    public void sendMessage(String queueName, MultipartFormDataInput mpfData) {
        try (JMSContext artemisJmsContext = connectionFactory.createContext(Session.AUTO_ACKNOWLEDGE)) {
            Queue artemisQueue = artemisJmsContext.createQueue(queueName);
            String message = processMultipart(mpfData);

            artemisJmsContext.createProducer().send(artemisQueue, message);
            System.out.println("Message: " + message + "sent to Artemis:" + artemisQueue);
        }
    }
}
