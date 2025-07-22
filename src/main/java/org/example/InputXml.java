package org.example;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "InputXml")
public class InputXml {
    public String messageId;
    public String settlementDate;
    public String debtorName;
    public String debtorIban;
    public String creditorName;
    public String creditorIban;
    public String amount;
    public String serviceLevel;
    public String chargeBearer;
    public String Ctry;
    public String[] AdrLine;
}
