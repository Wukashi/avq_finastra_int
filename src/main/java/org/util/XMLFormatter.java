package org.util;

import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import org.fin_avq_gen.ip.InstantPayment;

import java.io.StringWriter;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XMLFormatter {

    private static final Pattern INSTANT_PAYMENT_TAG_PATTERN = Pattern.compile("<[^:]*:?InstantPayment([^>]*)>");
    private static final Pattern XMLNS_PATTERN = Pattern.compile("xmlns:(\\w+)=\"([^\"]*)\"");
    private static final Pattern DOCUMENT_TAG_PATTERN = Pattern.compile("<(\\w+:)?Document[^>]*>");
    private static final Pattern NS_PREFIX_PATTERN = Pattern.compile("</?ns\\d+:");

    public static String getXmlOut(Marshaller marshaller, InstantPayment instantPayment, boolean removePrefixes) throws JAXBException {
        String rawXml = marshalInstantPayment(marshaller, instantPayment);
        Optional<XmlNamespaces> namespacesOpt = extractNamespaceUris(rawXml);

        if (namespacesOpt.isEmpty()) {
            return rawXml;
        }

        XmlNamespaces namespaces = namespacesOpt.get();

        String cleanedXml = removeInstantPaymentNamespace(rawXml);
        cleanedXml = replaceDocumentTag(cleanedXml, namespaces);

        if (removePrefixes) {
            cleanedXml = removeGeneratedPrefixes(cleanedXml);
        }

        return cleanedXml;
    }



    private static String marshalInstantPayment(Marshaller marshaller, InstantPayment instantPayment) throws JAXBException {
        StringWriter writer = new StringWriter();
        marshaller.marshal(instantPayment, writer);
        return writer.toString();
    }

    private static Optional<XmlNamespaces> extractNamespaceUris(String xml) {
        Matcher tagMatcher = INSTANT_PAYMENT_TAG_PATTERN.matcher(xml);
        if (!tagMatcher.find()) {
            return Optional.empty();
        }

        String tagAttributes = tagMatcher.group(1);
        Matcher xmlnsMatcher = XMLNS_PATTERN.matcher(tagAttributes);

        String xsiUri = null;
        String defaultUri = null;

        if (xmlnsMatcher.find()) {
            xsiUri = xmlnsMatcher.group(2);
        }
        if (xmlnsMatcher.find()) {
            defaultUri = xmlnsMatcher.group(2);
        }

        if (xsiUri != null && defaultUri != null) {
            return Optional.of(new XmlNamespaces(defaultUri, xsiUri));
        }

        return Optional.empty();
    }

    private static String removeInstantPaymentNamespace(String xml) {
        return INSTANT_PAYMENT_TAG_PATTERN.matcher(xml)
                .replaceFirst("<InstantPayment>");
    }

    private static String replaceDocumentTag(String xml, XmlNamespaces namespaces) {
        return DOCUMENT_TAG_PATTERN.matcher(xml)
                .replaceFirst("<Document xmlns=\"" + namespaces.defaultUri + "\" xmlns:xsi=\"" + namespaces.xsiUri + "\">");
    }

    private static String removeGeneratedPrefixes(String xml) {
        return NS_PREFIX_PATTERN.matcher(xml).replaceAll("<");
    }

    private static class XmlNamespaces {
        String defaultUri;
        String xsiUri;

        XmlNamespaces(String defaultUri, String xsiUri) {
            this.defaultUri = defaultUri;
            this.xsiUri = xsiUri;
        }
    }
}