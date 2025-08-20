package org.accenture.payment;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class AvaloqFinastraIntegrationApp {

    public static void main(String ... args) {
        Quarkus.run(args);
    }
}