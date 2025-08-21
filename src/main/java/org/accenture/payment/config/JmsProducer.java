package org.accenture.payment.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSContext;

@ApplicationScoped
public class JmsProducer {

    @Inject
    ConnectionFactory connectionFactory;

    @Produces
    @ApplicationScoped
    public JMSContext produceJmsContext() {
        return connectionFactory.createContext(JMSContext.AUTO_ACKNOWLEDGE);
    }
}
