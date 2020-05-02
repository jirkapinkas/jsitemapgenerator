package cz.jiripinkas.jsitemapgenerator.generator;

import cz.jiripinkas.jsitemapgenerator.AbstractGenerator;
import cz.jiripinkas.jsitemapgenerator.UrlUtil;
import cz.jiripinkas.jsitemapgenerator.WebPage;

import java.text.SimpleDateFormat;
import java.util.*;

public class RssGenerator extends AbstractGenerator<RssGenerator> {

    private static final String DATE_PATTERN = "EEE, d MMM yyyy HH:mm:ss";

    private String webTitle;

    private String webDescription;

    private String defaultDir;

    private String defaultExtension;

    /**
     * Creates RssGenerator.
     * This constructor is public, because sometimes somebody wants RssGenerator to be
     * a Spring bean and Spring wants to create a proxy which requires public constructor.
     * But you shouldn't call this constructor on your own, use {@link RssGenerator#of(String, String, String)} instead.
     *
     * @param baseUrl        Base URL
     * @param root           If Base URL is root (for example http://www.javavids.com or if
     *                       it's some path like http://www.javalibs.com/blog)
     * @param webTitle       Web title
     * @param webDescription Web description
     */
    public RssGenerator(String baseUrl, boolean root, String webTitle, String webDescription) {
        super(baseUrl, root);
        this.webTitle = webTitle;
        this.webDescription = webDescription;
    }

    /**
     * Creates RssGenerator. Root = true.
     *
     * @param baseUrl        Base URL
     * @param webTitle       Web title
     * @param webDescription Web description
     */
    private RssGenerator(String baseUrl, String webTitle, String webDescription) {
        super(baseUrl);
        this.webTitle = webTitle;
        this.webDescription = webDescription;
    }

    /**
     * Helper method to create an instance of SitemapGenerator
     *
     * @param baseUrl        Base URL
     * @param root           If Base URL is root (for example http://www.javavids.com or if
     *                       it's some path like http://www.javalibs.com/blog)
     * @param webTitle       Web title
     * @param webDescription Web description
     * @return Instance of RssGenerator
     */
    public static RssGenerator of(String baseUrl, boolean root, String webTitle, String webDescription) {
        return new RssGenerator(baseUrl, root, webTitle, webDescription);
    }

    /**
     * Helper method to create an instance of SitemapGenerator
     *
     * @param baseUrl Base URL
     * @param root    If Base URL is root (for example http://www.javavids.com or if
     *                it's some path like http://www.javalibs.com/blog)
     * @return Instance of RssGenerator
     */
    public static RssGenerator of(String baseUrl, boolean root) {
        return new RssGenerator(baseUrl, root, null, null);
    }

    /**
     * Helper method to create an instance of SitemapGenerator
     *
     * @param baseUrl        Base URL
     * @param webTitle       Web title
     * @param webDescription Web description
     * @return Instance of RssGenerator
     */
    public static RssGenerator of(String baseUrl, String webTitle, String webDescription) {
        return new RssGenerator(baseUrl, webTitle, webDescription);
    }

    /**
     * Helper method to create an instance of SitemapGenerator
     *
     * @param baseUrl Base URL
     * @return Instance of RssGenerator
     */
    public static RssGenerator of(String baseUrl) {
        return new RssGenerator(baseUrl, null, null);
    }

    /**
     * Set Web title
     *
     * @param webTitle Web title
     * @return this
     */
    public RssGenerator webTitle(String webTitle) {
        this.webTitle = webTitle;
        return this;
    }

    /**
     * Set Web description
     *
     * @param webDescription Web description
     * @return this
     */
    public RssGenerator webDescription(String webDescription) {
        this.webDescription = webDescription;
        return this;
    }

    /**
     * This will construct RSS from web pages. Web pages are sorted using
     * lastMod in descending order (latest is first)
     *
     * @return Constructed RSS
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" + "\n")
                .append("<rss version=\"2.0\">" + "\n")
                .append("<channel>" + "\n")
                .append("<title>")
                .append(webTitle)
                .append("</title>" + "\n")
                .append("<link>")
                .append(baseUrl)
                .append("</link>" + "\n")
                .append("<description>")
                .append(webDescription)
                .append("</description>" + "\n");

        List<WebPage> webPages = new ArrayList<>(urls.values());

        webPages.sort(Comparator.comparing(WebPage::getLastMod).reversed());

        Date latestDate = new Date();
        if (!webPages.isEmpty()) {
            latestDate = webPages.get(0).getLastMod();
        }

        builder.append("<pubDate>")
                .append(new SimpleDateFormat(DATE_PATTERN, Locale.ENGLISH).format(latestDate))
                .append(" +0000</pubDate>" + "\n")
                .append("<lastBuildDate>")
                .append(new SimpleDateFormat(DATE_PATTERN, Locale.ENGLISH).format(latestDate))
                .append(" +0000</lastBuildDate>" + "\n")
                .append("<ttl>1800</ttl>" + "\n");

        for (WebPage webPage : webPages) {
            builder.append("<item>" + "\n")

                    .append("<title>")
                    .append(webPage.getName())
                    .append("</title>" + "\n")

                    .append("<description>")
                    .append(webPage.getShortDescription())
                    .append("</description>" + "\n")

                    .append("<link>")
                    .append(UrlUtil.connectUrlParts(baseUrl, webPage.constructShortName()))
                    .append("</link>" + "\n")

                    .append("<pubDate>")
                    .append(new SimpleDateFormat(DATE_PATTERN, Locale.ENGLISH).format(webPage.getLastMod()))
                    .append(" +0000")
                    .append("</pubDate>" + "\n")

                    .append("</item>" + "\n");
        }
        builder.append("</channel>" + "\n")
                .append("</rss>" + "\n");
        return builder.toString();
    }

    @Override
    protected void beforeAddPageEvent(WebPage webPage) {
        if (defaultDir != null && webPage.getDir() == null) {
            webPage.setShortName(UrlUtil.connectUrlParts(defaultDir, webPage.getShortName()));
        }
        if (defaultExtension != null && webPage.getExtension() == null) {
            webPage.setShortName(webPage.getShortName() + "." + defaultExtension);
        }
    }

    /**
     * Sets default prefix dir to name for all subsequent WebPages. Final name will be "dirName/name"
     *
     * @param dirName Dir name
     * @return this
     */
    public RssGenerator defaultDir(String dirName) {
        defaultDir = dirName;
        return getThis();
    }

    /**
     * Sets default prefix dirs to name for all subsequent WebPages. For dirs: ["a", "b", "c"], the final name will be "a/b/c/name"
     *
     * @param dirNames Dir names
     * @return this
     */
    public RssGenerator defaultDir(String... dirNames) {
        defaultDir = String.join("/", dirNames);
        return getThis();
    }

    /**
     * Reset default dir value
     *
     * @return this
     */
    public RssGenerator resetDefaultDir() {
        defaultDir = null;
        return getThis();
    }

    /**
     * Sets default suffix extension for all subsequent WebPages. Final name will be "name.extension"
     *
     * @param extension Extension
     * @return this
     */
    public RssGenerator defaultExtension(String extension) {
        defaultExtension = extension;
        return getThis();
    }

    /**
     * Reset default extension value
     *
     * @return this
     */
    public RssGenerator resetDefaultExtension() {
        defaultExtension = null;
        return getThis();
    }

}
