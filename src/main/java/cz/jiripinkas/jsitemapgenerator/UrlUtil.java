package cz.jiripinkas.jsitemapgenerator;

/**
 * Util class
 */
public final class UrlUtil {

    private UrlUtil() {
    }

    /**
     * Connect two URL parts.
     * If first part ends with "/" and second part starts with "/",
     * for example: urlPart1 = "http://javalibs.com/", urlPart2 = "/page1", return: "http://javalibs.com/page1"
     * If second part is null, just return first part
     *
     * @param urlPart1 First url part
     * @param urlPart2 Second url part
     * @return Safely connected url
     */
    public static String connectUrlParts(String urlPart1, String urlPart2) {
        // if urlPart2 is null, return urlPart1
        if (urlPart2 == null) {
            return urlPart1;
        }
        // if first part doesn't end with "/", add "/"
        if (!urlPart1.endsWith("/")) {
            urlPart1 += "/";
        }
        // if second part ends with "/", remote it
        while (urlPart2.startsWith("/")) {
            urlPart2 = urlPart2.substring(1);
        }
        // return "urlPart1/urlPart2"
        return urlPart1 + urlPart2;
    }

    /**
     * Escape special characters in XML
     *
     * @param url Url to be escaped
     * @return Escaped url
     */
    public static String escapeXmlSpecialCharacters(String url) {
        if (url == null) {
            return null;
        }
        // https://stackoverflow.com/questions/1091945/what-characters-do-i-need-to-escape-in-xml-documents
        return url
                .replace("&", "&amp;") // must be escaped first!!!
                .replace("\"", "&quot;")
                .replace("'", "&apos;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }

}
