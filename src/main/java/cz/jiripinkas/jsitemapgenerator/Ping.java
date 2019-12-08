package cz.jiripinkas.jsitemapgenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Ping class which stores configuration, defining what will happen during ping call.
 */
public class Ping {

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

    public enum HttpClientType {
        OK_HTTP, APACHE_HTTP_CLIENT, REST_TEMPLATE
    }

    private List<SearchEngine> searchEngines;

    private String sitemapUrl;

    private HttpClientType httpClientType;

    private Object httpClientImplementation;

    public Ping(List<SearchEngine> searchEngines, String sitemapUrl, HttpClientType httpClientType, Object httpClientImplementation) {
        this.searchEngines = searchEngines;
        this.sitemapUrl = sitemapUrl;
        this.httpClientType = httpClientType;
        this.httpClientImplementation = httpClientImplementation;
    }

    public List<SearchEngine> getSearchEngines() {
        return searchEngines;
    }

    public String getSitemapUrl() {
        return sitemapUrl;
    }

    public HttpClientType getHttpClientType() {
        return httpClientType;
    }

    public Object getHttpClientImplementation() {
        return httpClientImplementation;
    }

    public static PingBuilder builder() {
        return new PingBuilder();
    }

    public static class PingBuilder {
        private List<SearchEngine> searchEngines;

        private String sitemapUrl;

        private HttpClientType httpClientType;

        private Object httpClientImplementation;

        private PingBuilder() {
            searchEngines = new ArrayList<>();
            sitemapUrl = "sitemap.xml";
        }

        /**
         * Set up search engines which will be pinged.
         * @param searchEngines Search engines
         * @return PingBuilder
         */
        public PingBuilder engines(SearchEngine ... searchEngines) {
            this.searchEngines.addAll(Arrays.asList(searchEngines));
            return this;
        }

        /**
         * Set up sitemap url, which can be absolute or relative.
         * If you do not call this method, default sitemapUrl will be "sitemap.xml"
         * @param sitemapUrl Sitemap url
         * @return PingBuilder
         */
        public PingBuilder sitemapUrl(String sitemapUrl) {
            this.sitemapUrl = sitemapUrl;
            return this;
        }

        /**
         * For ping will be used build-in OkHttpClient.
         * Note: You don't have to call this method explicitly, it's default!
         * This mechanism requires this dependency:
         * https://javalibs.com/artifact/com.squareup.okhttp3/okhttp
         * @return PingBuilder
         */
        public PingBuilder httpClientDefault() {
            this.httpClientType = null;
            this.httpClientImplementation = null;
            return this;
        }

        /**
         * For ping will be used custom OkHttpClient.
         * This mechanism requires this dependency:
         * https://javalibs.com/artifact/com.squareup.okhttp3/okhttp
         * @param okHttpClient Custom OkHttpClient
         * @return PingBuilder
         */
        public PingBuilder httpClientOkHttp(Object okHttpClient) {
            this.httpClientType = HttpClientType.OK_HTTP;
            this.httpClientImplementation = okHttpClient;
            return this;
        }

        /**
         * For ping will be used custom RestTemplate.
         * This mechanism requires this dependency:
         * https://javalibs.com/artifact/org.springframework/spring-web
         * @param restTemplate Custom RestTemplate
         * @return PingBuilder
         */
        public PingBuilder httpClientRestTemplate(Object restTemplate) {
            this.httpClientType = HttpClientType.REST_TEMPLATE;
            this.httpClientImplementation = restTemplate;
            return this;
        }

        /**
         * For ping will be used CloseableHttpClient.
         * This mechanism requires this dependency:
         * https://javalibs.com/artifact/org.apache.httpcomponents/httpclient
         * @param closeableHttpClient Custom CloseableHttpClient
         * @return PingBuilder
         */
        public PingBuilder httpClientApacheHttpClient(Object closeableHttpClient) {
            this.httpClientType = HttpClientType.APACHE_HTTP_CLIENT;
            this.httpClientImplementation = closeableHttpClient;
            return this;
        }

        /**
         * Build Ping object. Before call to this method at least one search engine
         * must be set up using {@link #engines(SearchEngine...)} method!
         * @return Ping
         */
        public Ping build() {
            if(searchEngines.isEmpty()) {
                throw new UnsupportedOperationException("Must provide at least one search engine!");
            }
            return new Ping(searchEngines, sitemapUrl, httpClientType, httpClientImplementation);
        }

    }

}
