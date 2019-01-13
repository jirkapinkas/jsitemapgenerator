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

public class SitemapGenerator extends AbstractSitemapGenerator <SitemapGenerator> {

    public enum AdditionalNamespace {
        IMAGE, XHTML
    }

    private ChangeFreq defaultChangeFreq;

    private Double defaultPriority;

    private String defaultDir;

    private String defaultExtension;

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
    }

    /**
     * Construct additional namespaces string
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
    @Deprecated
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
        // auto-detect additional namespaces
        List<AdditionalNamespace> additionalNamespaces = new ArrayList<>();
        boolean hasImages = urls.values().stream()
                .anyMatch(webPage -> webPage.getImages() != null);
        if(hasImages) {
            additionalNamespaces.add(AdditionalNamespace.IMAGE);
        }
        boolean hasAlternateNames = urls.values().stream()
                .anyMatch(webPage -> webPage.getAlternateNames() != null);
        if(hasAlternateNames) {
            additionalNamespaces.add(AdditionalNamespace.XHTML);
        }

        ArrayList<String> out = new ArrayList<>();
        out.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        out.add("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\"" + constructAdditionalNamespacesString(additionalNamespaces) + ">\n");
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

    @Override
    protected void beforeAddPageEvent(WebPage webPage) {
        if(defaultDir != null && webPage.getDir() == null) {
            webPage.setName(defaultDir + "/" + webPage.constructName());
        }
        if(defaultExtension != null && webPage.getExtension() == null) {
            webPage.setName(webPage.constructName() + "." + defaultExtension);
        }
        if(defaultPriority != null && webPage.getPriority() == null) {
            webPage.setPriority(defaultPriority);
        }
        if(defaultChangeFreq != null && webPage.getChangeFreq() == null) {
            webPage.setChangeFreq(defaultChangeFreq);
        }
    }

    protected String constructImage(Image image) {
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

    protected String constructUrl(WebPage webPage) {
        StringBuilder out = new StringBuilder();
        out.append("<loc>");
        try {
            out.append(toUrl(baseUrl, webPage.constructName()));
        } catch (MalformedURLException e) {
            throw new InvalidUrlException(e);
        }
        out.append("</loc>\n");
        if (webPage.getAlternateNames() != null) {
            try {
                for (Map.Entry<String, String> entry : webPage.getAlternateNames().entrySet()) {
                    out.append("<xhtml:link rel=\"alternate\" hreflang=\"");
                    out.append(escapeXmlSpecialCharacters(entry.getKey()));
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
     * Sets default prefix dir to name for all subsequent WebPages. Final name will be "dirName/name"
     * @param dirName Dir name
     * @return this
     */
    public SitemapGenerator defaultDir(String dirName) {
        defaultDir = dirName;
        return this;
    }

    /**
     * Sets default prefix dirs to name for all subsequent WebPages. For dirs: ["a", "b", "c"], the final name will be "a/b/c/name"
     * @param dirNames Dir names
     * @return this
     */
    public SitemapGenerator defaultDir(String ... dirNames) {
        defaultDir = String.join("/", dirNames);
        return this;
    }

    /**
     * Reset default dir value
     * @return this
     */
    public SitemapGenerator resetDefaultDir() {
        defaultDir = null;
        return this;
    }

    /**
     * Sets default suffix extension for all subsequent WebPages. Final name will be "name.extension"
     * @param extension Extension
     * @return this
     */
    public SitemapGenerator defaultExtension(String extension) {
        defaultExtension = extension;
        return this;
    }

    /**
     * Reset default extension value
     * @return this
     */
    public SitemapGenerator resetDefaultExtension() {
        defaultExtension = null;
        return this;
    }

    /**
     * Sets default priority for all subsequent WebPages to maximum (1.0)
     *
     * @return this
     */
    public SitemapGenerator defaultPriorityMax() {
        defaultPriority = 1.0;
        return this;
    }

    /**
     * Sets default priority for all subsequent WebPages
     * @param priority Default priority
     * @return this
     */
    public SitemapGenerator defaultPriority(Double priority) {
        if (priority < 0.0 || priority > 1.0) {
            throw new InvalidPriorityException("Priority must be between 0 and 1.0");
        }
        defaultPriority = priority;
        return this;
    }

    /**
     * Reset default priority
     * @return this
     */
    public SitemapGenerator resetDefaultPriority() {
        defaultPriority = null;
        return this;
    }

    /**
     * Sets default changeFreq for all subsequent WebPages
     *
     * @param changeFreq ChangeFreq
     * @return this
     */
    public SitemapGenerator defaultChangeFreq(ChangeFreq changeFreq) {
        defaultChangeFreq = changeFreq;
        return this;
    }

    /**
     * Sets default changeFreq to ALWAYS for all subsequent WebPages
     *
     * @return this
     */
    public SitemapGenerator defaultChangeFreqAlways() {
        defaultChangeFreq = ChangeFreq.ALWAYS;
        return this;
    }

    /**
     * Sets default changeFreq to HOURLY for all subsequent WebPages
     *
     * @return this
     */
    public SitemapGenerator defaultChangeFreqHourly() {
        defaultChangeFreq = ChangeFreq.HOURLY;
        return this;
    }

    /**
     * Sets default changeFreq to DAILY for all subsequent WebPages
     *
     * @return this
     */
    public SitemapGenerator defaultChangeFreqDaily() {
        defaultChangeFreq = ChangeFreq.DAILY;
        return this;
    }

    /**
     * Sets default changeFreq to WEEKLY for all subsequent WebPages
     *
     * @return this
     */
    public SitemapGenerator defaultChangeFreqWeekly() {
        defaultChangeFreq = ChangeFreq.WEEKLY;
        return this;
    }

    /**
     * Sets default changeFreq to MONTHLY for all subsequent WebPages
     *
     * @return this
     */
    public SitemapGenerator defaultChangeFreqMonthly() {
        defaultChangeFreq = ChangeFreq.MONTHLY;
        return this;
    }

    /**
     * Sets default changeFreq to YEARLY for all subsequent WebPages
     *
     * @return this
     */
    public SitemapGenerator defaultChangeFreqYearly() {
        defaultChangeFreq = ChangeFreq.YEARLY;
        return this;
    }

    /**
     * Sets default changeFreq to NEVER for all subsequent WebPages
     *
     * @return this
     */
    public SitemapGenerator defaultChangeFreqNever() {
        defaultChangeFreq = ChangeFreq.NEVER;
        return this;
    }

    /**
     * Reset default changeFreq
     * @return this
     */
    public SitemapGenerator resetDefaultChangeFreq() {
        defaultChangeFreq = null;
        return this;
    }

}
