package cz.jiripinkas.jsitemapgenerator.generator;

import cz.jiripinkas.jsitemapgenerator.AbstractSitemapGenerator;
import cz.jiripinkas.jsitemapgenerator.Image;
import cz.jiripinkas.jsitemapgenerator.UrlUtil;
import cz.jiripinkas.jsitemapgenerator.WebPage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SitemapGenerator extends AbstractSitemapGenerator<SitemapGenerator> {

    public enum AdditionalNamespace {
        IMAGE, XHTML
    }

    /**
     * This constructor is public, because sometimes somebody wants SitemapGenerator to be
     * a Spring bean and Spring wants to create a proxy which requires public constructor.
     * But you shouldn't call this constructor on your own, use {@link SitemapGenerator#of(String)} instead.
     * @param baseUrl Base URL
     */
    public SitemapGenerator(String baseUrl) {
        super(baseUrl);
    }

    /**
     * Construct additional namespaces string
     *
     * @param additionalNamespaceList
     * @return
     */
    private String constructAdditionalNamespacesString(List<AdditionalNamespace> additionalNamespaceList) {
        String result = "";
        if (additionalNamespaceList.contains(AdditionalNamespace.IMAGE)) {
            result += " xmlns:image=\"http://www.google.com/schemas/sitemap-image/1.1\" ";
        }
        if (additionalNamespaceList.contains(AdditionalNamespace.XHTML)) {
            result += " xmlns:xhtml=\"http://www.w3.org/1999/xhtml\" ";
        }
        return result;
    }

    /**
     * Helper method to create an instance of SitemapGenerator
     *
     * @param baseUrl Base URL
     * @return Instance of SitemapGenerator
     */
    public static SitemapGenerator of(String baseUrl) {
        return new SitemapGenerator(baseUrl);
    }

    /**
     * Construct sitemap into array of Strings. The URLs will be ordered using
     * priority in descending order (URLs with higher priority will be at the
     * top).
     *
     * @return sitemap
     */
    @Override
    public String[] toStringArray() {
        // auto-detect additional namespaces
        List<AdditionalNamespace> additionalNamespaces = new ArrayList<>();
        boolean hasImages = urls.values().stream()
                .anyMatch(webPage -> webPage.getImages() != null);
        if (hasImages) {
            additionalNamespaces.add(AdditionalNamespace.IMAGE);
        }
        boolean hasAlternateNames = urls.values().stream()
                .anyMatch(webPage -> webPage.getAlternateNames() != null);
        if (hasAlternateNames) {
            additionalNamespaces.add(AdditionalNamespace.XHTML);
        }

        List<String> out = new ArrayList<>();
        out.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        out.add("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\"" + constructAdditionalNamespacesString(additionalNamespaces) + ">\n");
        List<WebPage> values = new ArrayList<>(urls.values());
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

    protected String constructImage(Image image) {
        StringBuilder out = new StringBuilder();
        out.append("<image:image>\n");
        if (image.getLoc() != null) {
            out.append("<image:loc>");
            out.append(getAbsoluteUrl(image.getLoc()));
            out.append("</image:loc>\n");
        }
        if (image.getCaption() != null) {
            out.append("<image:caption>");
            out.append(UrlUtil.escapeXmlSpecialCharacters(image.getCaption()));
            out.append("</image:caption>\n");
        }
        if (image.getGeoLocation() != null) {
            out.append("<image:geo_location>");
            out.append(UrlUtil.escapeXmlSpecialCharacters(image.getGeoLocation()));
            out.append("</image:geo_location>\n");
        }
        if (image.getTitle() != null) {
            out.append("<image:title>");
            out.append(UrlUtil.escapeXmlSpecialCharacters(image.getTitle()));
            out.append("</image:title>\n");
        }
        if (image.getLicense() != null) {
            out.append("<image:license>");
            out.append(UrlUtil.escapeXmlSpecialCharacters(image.getLicense()));
            out.append("</image:license>\n");
        }
        out.append("</image:image>\n");
        return out.toString();
    }

    protected String constructUrl(WebPage webPage) {
        StringBuilder out = new StringBuilder();
        out.append("<loc>");
        out.append(getAbsoluteUrl(webPage.constructName()));
        out.append("</loc>\n");
        if (webPage.getAlternateNames() != null) {
            for (Map.Entry<String, String> entry : webPage.getAlternateNames().entrySet()) {
                out.append("<xhtml:link rel=\"alternate\" hreflang=\"");
                out.append(UrlUtil.escapeXmlSpecialCharacters(entry.getKey()));
                out.append("\" href=\"");
                out.append(getAbsoluteUrl(entry.getValue()));
                out.append("\"/>\n");
            }
        }
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

}
