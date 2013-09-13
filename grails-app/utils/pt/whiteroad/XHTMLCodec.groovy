package pt.whiteroad

import org.w3c.dom.Document
import org.w3c.tidy.Tidy

class XHTMLCodec {
    /**
     * Encodes a date as a string with the format: yyyy-mm-dd (RFC 3339)
     * */
    static encode = { String theTarget ->
        ByteArrayInputStream bai = new ByteArrayInputStream(theTarget.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Tidy tidy = new Tidy();
        tidy.setXHTML(true);
        tidy.setPrintBodyOnly(true);
        tidy.setQuiet(true);
        tidy.setShowWarnings(false);
        tidy.setIndentContent(false);
        tidy.setSmartIndent(false);
        tidy.setIndentAttributes(false);
        tidy.setWraplen(0);
        tidy.setInputEncoding("UTF8");
        tidy.setOutputEncoding("UTF8");
        Document doc = tidy.parseDOM(bai, null);
        tidy.pprint(doc, out);

        String tidied = new String(out.toByteArray());
        String result = tidied.substring(tidied.indexOf("<body>")+"<body>".length(), tidied.indexOf("</body>")).trim();
        return result
    }

}
