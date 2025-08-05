package lukasz;

import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import org.fin_avq_gen.InstantPayment;

import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XMLFormatter {
    public static String getXmlOut(Marshaller marshaller, InstantPayment instantPayment) throws JAXBException {
        StringWriter writer = new StringWriter();
        marshaller.marshal(instantPayment, writer);
        String xmlOut = writer.toString();

        String nsUri1 = null;
        String nsUri2 = null;

        Pattern tagPattern = Pattern.compile("<[^:]*:?InstantPayment([^>]*)>");
        Matcher tagMatcher = tagPattern.matcher(xmlOut);

        if (tagMatcher.find()) {
            String tagAttrs = tagMatcher.group(1);

            Pattern xmlnsPattern = Pattern.compile("xmlns:(\\w+)=\"([^\"]*)\"");
            Matcher xmlnsMatcher = xmlnsPattern.matcher(tagAttrs);

            if (xmlnsMatcher.find()) {
                nsUri1 = xmlnsMatcher.group(2);
            }
            if (xmlnsMatcher.find()) {
                nsUri2 = xmlnsMatcher.group(2);
            }

            xmlOut = xmlOut.replaceFirst("<[^:]*:?InstantPayment[^>]*>", "<InstantPayment>");
        }

        if (nsUri2 != null && nsUri1 != null) {
            xmlOut = xmlOut.replaceAll(
                    "<(\\w+:)?Document[^>]*>",
                    "<Document xmlns=\"" + nsUri2 + "\" xmlns:xsi=\"" + nsUri1 + "\">"
            );
        }

        xmlOut = xmlOut.replaceAll("</?ns\\d+:", "<");

        return xmlOut;
    }
}
