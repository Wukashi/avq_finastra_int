package org.accenture.payment.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.resteasy.reactive.server.multipart.FormValue;
import org.jboss.resteasy.reactive.server.multipart.MultipartFormDataInput;

import java.util.Collection;
import java.util.Map;

@ApplicationScoped
public class MessageServiceMultipart {

    public String processMultipart(MultipartFormDataInput mpfData) {
        Map<String, Collection<FormValue>> values = mpfData.getValues();

        FormValue xml = values.get("pain001").iterator().next();

        return xml.getValue();
    }
}
