package cz.jiripinkas.jsitemapgenerator.generator;

import cz.jiripinkas.jsitemapgenerator.AbstractSitemapGenerator;
import cz.jiripinkas.jsitemapgenerator.WebPage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Sitemap index generator
 * https://www.sitemaps.org/protocol.html#index
 */
public class SitemapIndexGenerator extends AbstractSitemapGenerator<SitemapIndexGenerator> {

    /**
     * This constructor is public, because sometimes somebody wants SitemapIndexGenerator to be
     * a Spring bean and Spring wants to create a proxy which requires public constructor.
     * But you shouldn't call this constructor on your own, use {@link SitemapIndexGenerator#of(String)} instead.
     * @param baseUrl Base URL
     */
    public SitemapIndexGenerator(String baseUrl) {
        super(baseUrl);
    }

    public static SitemapIndexGenerator of(String baseUrl) {
        return new SitemapIndexGenerator(baseUrl);
    }

    /**
     * Construct sitemap to String array
     *
     * @return String array
     */
    @Override
    public String[] toStringArray() {
        List<String> out = new ArrayList<>();
        out.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        out.add("<sitemapindex xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");
        List<WebPage> values = new ArrayList<>(urls.values());
        Collections.sort(values);
        for (WebPage webPage : values) {
            out.add(constructUrl(webPage));
        }
        out.add("</sitemapindex>");
        return out.toArray(new String[]{});
    }

    /**
     * Construct URL from WebPage
     *
     * @param webPage WebPage object
     * @return String URL
     */
    protected String constructUrl(WebPage webPage) {
        StringBuilder out = new StringBuilder();
        out.append("<sitemap>\n");
        out.append("<loc>");
        out.append(getAbsoluteUrl(webPage.constructName()));
        out.append("</loc>\n");
        if (webPage.getLastMod() != null) {
            out.append("<lastmod>");
            out.append(dateFormat.format(webPage.getLastMod()));
            out.append("</lastmod>\n");
        }
        out.append("</sitemap>\n");
        return out.toString();
    }

}
