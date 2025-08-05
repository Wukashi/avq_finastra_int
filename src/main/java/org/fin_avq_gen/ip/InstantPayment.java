package org.fin_avq_gen.ip;


import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.XmlType;
import org.fin_avq_gen.Hdr;
import org.fin_avq_gen.pacs.Document;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "hdr",
        "document",
})


@XmlRootElement(name = "InstantPayment", namespace = "")
public class InstantPayment {
    @XmlElement(name = "Hdr", namespace = "", required = true)
    protected Hdr hdr;
    @XmlElement(name = "Document", namespace = "urn:iso:std:iso:20022:tech:xsd:pacs.008.001.08", required = true)
    protected Document document;

    public Hdr getHdr() {
        return hdr;
    }

    public void setHdr(Hdr hdr){
        this.hdr = hdr;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
}
