package org.example;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "OutputXml")
public class OutputXml {
    public String messageId;
    public String settlementDate;
    public String debtorName;
    public String debtorIban;
    public String creditorName;
    public String creditorIban;
    public String amount;
    public String serviceLevel;
    public String chargeBearer;
    public String StrtNm;
    public String BldgNb;
    public String Ctry;
}
