package org.config;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class TransferConfig {

    @ConfigProperty(name = "transfer.direction.in")
    String directionIn;

    @ConfigProperty(name = "transfer.ident.type")
    String identType;

    public String getDirectionIn() {
        return directionIn;
    }

    public String getIdentType() {
        return identType;
    }
}
