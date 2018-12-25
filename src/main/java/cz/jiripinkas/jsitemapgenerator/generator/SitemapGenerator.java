package cz.jiripinkas.jsitemapgenerator.generator;

import cz.jiripinkas.jsitemapgenerator.AbstractSitemapGenerator;
import cz.jiripinkas.jsitemapgenerator.Image;
import cz.jiripinkas.jsitemapgenerator.WebPage;
import cz.jiripinkas.jsitemapgenerator.exception.InvalidUrlException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

public class SitemapGenerator extends AbstractSitemapGenerator {

    public enum AdditionalNamespace {
        IMAGE
    }

    private StringBuilder additionalNamespacesStringBuilder = new StringBuilder();

    /**
     * @deprecated Use {@link #of(String)}
     * @param baseUrl
     */
    @Deprecated
    public SitemapGenerator(String baseUrl) {
        super(baseUrl);
    }

    /**
     * @deprecated Use {@link #of(String, AdditionalNamespace[])} )}
     * @param baseUrl
     */
    @Deprecated
    public SitemapGenerator(String baseUrl, AdditionalNamespace[] additionalNamespaces) {
        this(baseUrl);
        if (Arrays.asList(additionalNamespaces).contains(AdditionalNamespace.IMAGE)) {
            additionalNamespacesStringBuilder.append(" xmlns:image=\"http://www.google.com/schemas/sitemap-image/1.1\" ");
        }
    }

    /**
     * Helper method to create an instance of SitemapGenerator
     * @param baseUrl
     * @return
     */
    public static SitemapGenerator of(String baseUrl) {
        return new SitemapGenerator(baseUrl);
    }

    /**
     * Helper method to create an instance of SitemapGenerator
     * @param baseUrl
     * @return
     */
    public static SitemapGenerator of(String baseUrl, AdditionalNamespace[] additionalNamespaces) {
        return new SitemapGenerator(baseUrl, additionalNamespaces);
    }

    /**
     * Construct sitemap into array of Strings. The URLs will be ordered using
     * priority in descending order (URLs with higher priority will be at the
     * top).
     *
     * @return sitemap
     */
    @Override
    public String[] constructSitemap() {
        ArrayList<String> out = new ArrayList<>();
        out.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        out.add("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\"" + additionalNamespacesStringBuilder.toString() + ">\n");
        ArrayList<WebPage> values = new ArrayList<>(urls.values());
        Collections.sort(values);
        for (WebPage webPage : values) {
            out.add("<url>\n");
            out.add(constructUrl(webPage));
            if (webPage.getImages() != null) {
                for (Image image : webPage.getImages()) {
                    out.add(constructImage(image));
                }
            }
            out.add("</url>\n");
        }
        out.add("</urlset>");
        return out.toArray(new String[]{});
    }

    String constructImage(Image image) {
        StringBuilder out = new StringBuilder();
        out.append("<image:image>\n");
        if (image.getLoc() != null) {
            out.append("<image:loc>");
            out.append(image.getLoc());
            out.append("</image:loc>\n");
        }
        if (image.getCaption() != null) {
            out.append("<image:caption>");
            out.append(image.getCaption());
            out.append("</image:caption>\n");
        }
        if (image.getGeoLocation() != null) {
            out.append("<image:geo_location>");
            out.append(image.getGeoLocation());
            out.append("</image:geo_location>\n");
        }
        if (image.getTitle() != null) {
            out.append("<image:title>");
            out.append(image.getTitle());
            out.append("</image:title>\n");
        }
        if (image.getLicense() != null) {
            out.append("<image:license>");
            out.append(image.getLicense());
            out.append("</image:license>\n");
        }
        out.append("</image:image>\n");
        return out.toString();
    }

    String constructUrl(WebPage webPage) {
        StringBuilder out = new StringBuilder();
        out.append("<loc>");
        try {
            if (webPage.getName() != null) {
                String wpName = webPage.getName();
                if (baseUrl.endsWith("/")) {
                    while (wpName.startsWith("/")) {
                        wpName = wpName.substring(1);
                    }
                }
                out.append(new URL(baseUrl + wpName).toString());
            } else {
                out.append(new URL(baseUrl).toString());
            }
        } catch (MalformedURLException e) {
            throw new InvalidUrlException(e);
        }
        out.append("</loc>\n");
        if (webPage.getLastMod() != null) {
            out.append("<lastmod>");
            out.append(dateFormat.format(webPage.getLastMod()));
            out.append("</lastmod>\n");
        }
        if (webPage.getChangeFreq() != null) {
            out.append("<changefreq>");
            out.append(webPage.getChangeFreq());
            out.append("</changefreq>\n");
        }
        if (webPage.getPriority() != null) {
            out.append("<priority>");
            out.append(webPage.getPriority());
            out.append("</priority>\n");
        }
        return out.toString();
    }

    /**
     * Add single page to sitemap
     *
     * @param webPage single page
     * @return this
     */
    @Override
    public SitemapGenerator addPage(WebPage webPage) {
        urls.put(baseUrl + webPage.getName(), webPage);
        return this;
    }

    /**
     * Add collection of pages to sitemap
     *
     * @param webPages Collection of pages
     * @return this
     */
    @Override
    public SitemapGenerator addPages(Collection<WebPage> webPages) {
        for (WebPage webPage : webPages) {
            addPage(webPage);
        }
        return this;
    }

    /**
     * Add collection of pages to sitemap
     *
     * @param webPages Collection of pages
     * @param mapper Mapper function which transforms some object to WebPage
     * @return this
     */
    public <T> SitemapGenerator addPages(Collection<T> webPages, Function<T, WebPage> mapper) {
        for (T element : webPages) {
            addPage(mapper.apply(element));
        }
        return this;
    }

}
