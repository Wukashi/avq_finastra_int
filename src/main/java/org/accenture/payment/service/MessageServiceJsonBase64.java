package org.accenture.payment.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@ApplicationScoped
public class MessageServiceJsonBase64 {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String processJsonBase64(String jsonBody) throws Exception {
        JsonNode root = objectMapper.readTree(jsonBody);
        String instantPaymentDataBase64 = root.path("instantPaymentData").asText();
        byte[] decodedBytes = Base64.getDecoder().decode(instantPaymentDataBase64);

        return new String(decodedBytes, StandardCharsets.UTF_8);
    }
}
