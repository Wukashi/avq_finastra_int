package org.accenture.payment.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.jms.JMSContext;
import jakarta.jms.Queue;

@ApplicationScoped
public class MessageSender {

    @Inject
    JMSContext jmsContext;

    public void sendXml(String queueName, String xmlContent) {
        Queue queue = jmsContext.createQueue(queueName);
        jmsContext.createProducer().send(queue, xmlContent);
    }
}