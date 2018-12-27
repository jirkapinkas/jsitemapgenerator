package cz.jiripinkas.jsitemapgenerator.generator;

import cz.jiripinkas.jsitemapgenerator.AbstractSitemapGenerator;
import cz.jiripinkas.jsitemapgenerator.ChangeFreq;
import cz.jiripinkas.jsitemapgenerator.Image;
import cz.jiripinkas.jsitemapgenerator.WebPage;
import cz.jiripinkas.jsitemapgenerator.exception.InvalidPriorityException;
import cz.jiripinkas.jsitemapgenerator.exception.InvalidUrlException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.function.Function;

public class SitemapGenerator extends AbstractSitemapGenerator {

    public enum AdditionalNamespace {
        IMAGE
    }

    private StringBuilder additionalNamespacesStringBuilder = new StringBuilder();

    private ChangeFreq defaultChangeFreq;

    private Double defaultPriority;

    private String dir;

    private String extension;

    /**
     * @deprecated Use {@link #of(String)}
     * @param baseUrl Base URL
     */
    @Deprecated
    public SitemapGenerator(String baseUrl) {
        super(baseUrl);
    }

    /**
     * @deprecated Use {@link #of(String, AdditionalNamespace[])} )}
     * @param baseUrl Base URL
     * @param additionalNamespaces Additional parameters
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
     * @param baseUrl Base URL
     * @return Instance of SitemapGenerator
     */
    public static SitemapGenerator of(String baseUrl) {
        return new SitemapGenerator(baseUrl);
    }

    /**
     * Helper method to create an instance of SitemapGenerator
     * @param baseUrl Base URL
     * @param additionalNamespaces Additional parameters
     * @return Instance of SitemapGenerator
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
            if(dir != null) {
                webPage.setName(dir + "/" + webPage.getName());
            }
            if(extension != null) {
                webPage.setName(webPage.getName() + "." + extension);
            }
            if(defaultPriority != null && webPage.getPriority() == null) {
                webPage.setPriority(defaultPriority);
            }
            if(defaultChangeFreq != null && webPage.getChangeFreq() == null) {
                webPage.setChangeFreq(defaultChangeFreq);
            }
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
            out.append(toUrl(baseUrl, webPage.getName()));
        } catch (MalformedURLException e) {
            throw new InvalidUrlException(e);
        }
        out.append("</loc>\n");
        if (webPage.getAlternateNames() != null) {
            try {
                for (Map.Entry<String, String> entry : webPage.getAlternateNames().entrySet()) {
                    out.append("<xhtml:link rel=\"alternate\" hreflang=\"");
                    out.append(entry.getKey());
                    out.append("\" href=\"");
                    out.append(toUrl(baseUrl, entry.getValue()));
                    out.append("\"/>\n");
                }
            } catch (MalformedURLException e) {
                throw new InvalidUrlException(e);
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

    private String toUrl(String baseUrl, String name) throws MalformedURLException {
        if (name == null) {
            return escapeXmlSpecialCharacters(new URL(baseUrl).toString());
        }
        if (baseUrl.endsWith("/")) {
            while (name.startsWith("/")) {
                name = name.substring(1);
            }
        }
        return escapeXmlSpecialCharacters(new URL(baseUrl + name).toString());
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
    @Override
    public <T> SitemapGenerator addPages(Collection<T> webPages, Function<T, WebPage> mapper) {
        for (T element : webPages) {
            addPage(mapper.apply(element));
        }
        return this;
    }


    /**
     * Sets default prefix dir to name. Final name will be "dirName/name"
     * @param dirName Dir name
     * @return this
     */
    public SitemapGenerator dir(String dirName) {
        dir = dirName;
        return this;
    }

    /**
     * Sets default prefix dirs to name. For dirs: ["a", "b", "c"], the final name will be "a/b/c/name"
     * @param dirNames Dir names
     * @return this
     */
    public SitemapGenerator dir(String ... dirNames) {
        dir = String.join("/", dirNames);
        return this;
    }

    /**
     * Sets default suffix extension. Final name will be "name.extension"
     * @param extension Extension
     * @return this
     */
    public SitemapGenerator extension(String extension) {
        this.extension = extension;
        return this;
    }

    /**
     * Sets default priority to maximum (1.0)
     *
     * @return this
     */
    public SitemapGenerator defaultPriorityMax() {
        defaultPriority = 1.0;
        return this;
    }

    /**
     * Sets default priority
     * @param priority Default priority
     */
    public void setDefaultPriority(Double priority) {
        if (priority < 0.0 || priority > 1.0) {
            throw new InvalidPriorityException("Priority must be between 0 and 1.0");
        }
        defaultPriority = priority;
    }


    /**
     * Sets default changeFreq
     *
     * @param changeFreq ChangeFreq
     * @return this
     */
    public SitemapGenerator defaultChangeFreq(ChangeFreq changeFreq) {
        defaultChangeFreq = changeFreq;
        return this;
    }

    /**
     * Sets default changeFreq to ALWAYS
     *
     * @return this
     */
    public SitemapGenerator defaultChangeFreqAlways() {
        defaultChangeFreq = ChangeFreq.ALWAYS;
        return this;
    }

    /**
     * Sets default changeFreq to HOURLY
     *
     * @return this
     */
    public SitemapGenerator defaultChangeFreqHourly() {
        defaultChangeFreq = ChangeFreq.HOURLY;
        return this;
    }

    /**
     * Sets default changeFreq to DAILY
     *
     * @return this
     */
    public SitemapGenerator defaultChangeFreqDaily() {
        defaultChangeFreq = ChangeFreq.DAILY;
        return this;
    }

    /**
     * Sets default changeFreq to WEEKLY
     *
     * @return this
     */
    public SitemapGenerator defaultChangeFreqWeekly() {
        defaultChangeFreq = ChangeFreq.WEEKLY;
        return this;
    }

    /**
     * Sets default changeFreq to MONTHLY
     *
     * @return this
     */
    public SitemapGenerator defaultChangeFreqMonthly() {
        defaultChangeFreq = ChangeFreq.MONTHLY;
        return this;
    }

    /**
     * Sets default changeFreq to YEARLY
     *
     * @return this
     */
    public SitemapGenerator defaultChangeFreqYearly() {
        defaultChangeFreq = ChangeFreq.YEARLY;
        return this;
    }

    /**
     * Sets default changeFreq to NEVER
     *
     * @return this
     */
    public SitemapGenerator defaultChangeFreqNever() {
        defaultChangeFreq = ChangeFreq.NEVER;
        return this;
    }

}
