package cz.jiripinkas.jsitemapgenerator;

/**
 * Supported search engines for ping functionality
 */
public enum SearchEngine {

    GOOGLE("Google", "https://www.google.com/ping?sitemap="),
    BING("Bing", "https://www.bing.com/ping?sitemap=");

    private String prettyName;

    private String pingUrl;

    SearchEngine(String prettyName, String pingUrl) {
        this.prettyName = prettyName;
        this.pingUrl = pingUrl;
    }

    public String getPrettyName() {
        return prettyName;
    }

    public String getPingUrl() {
        return pingUrl;
    }
}
