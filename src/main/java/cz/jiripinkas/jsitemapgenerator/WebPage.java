package cz.jiripinkas.jsitemapgenerator;

import cz.jiripinkas.jsitemapgenerator.exception.InvalidPriorityException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Supplier;

public class WebPage implements Comparable<WebPage> {
    private String name;
    /**
     * Optional dir. In getName() it's added to name: "dir/name"
     */
    private String dir;
    /**
     * Optional dir. In getName() it's added to name: "name.extension"
     */
    private String extension;
    private Map<String, String> alternateNames;
    private Date lastMod;
    private ChangeFreq changeFreq;
    private Double priority;
    private static final double MIN_PRIORITY = 0.0;
    private static final double MAX_PRIORITY = 1.0;
    private String shortDescription;
    private String shortName;

    private List<Image> images;

    /**
     * Method for creating WebPage only with name
     * (this can be useful for really simple sitemaps
     * or with combination of default settings
     * set on SitemapGenerator)
     *
     * @param name Name (cannot be null)
     * @return WebPage instance
     * @throws NullPointerException When name is null
     */
    public static WebPage of(String name) {
        Objects.requireNonNull(name);
        WebPage webPage = new WebPage();
        webPage.setName(name);
        return webPage;
    }

    /**
     * Method for creating WebPage only with name
     * (this can be useful for really simple sitemaps
     * or with combination of default settings
     * set on SitemapGenerator)
     *
     * @param names Names (cannot be null), will be connected using "/"
     * @return WebPage instance
     * @throws NullPointerException When name is null
     */
    public static WebPage of(String... names) {
        Objects.requireNonNull(names);
        WebPage webPage = new WebPage();
        webPage.setName(String.join("/", names));
        return webPage;
    }

    /**
     * Method for creating WebPage only with name
     * (this can be useful for really simple sitemaps
     * or with combination of default settings
     * set on SitemapGenerator)
     *
     * @param nameSupplier Name supplier (cannot return null!!!)
     * @return WebPage instance
     * @throws NullPointerException When nameSupplier or nameSupplier.get() returns null
     */
    public static WebPage of(Supplier<String> nameSupplier) {
        Objects.requireNonNull(nameSupplier);
        Objects.requireNonNull(nameSupplier.get());
        WebPage webPage = new WebPage();
        webPage.setName(nameSupplier.get());
        return webPage;
    }

    public WebPage addImage(Image image) {
        if (images == null) {
            images = new ArrayList<>();
        }
        images.add(image);
        return this;
    }

    public WebPage addAlternateName(String language, String name) {
        if (alternateNames == null) {
            alternateNames = new HashMap<>();
        }
        alternateNames.put(language, name);
        return this;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAlternateNames(Map<String, String> alternateNames) {
        this.alternateNames = alternateNames;
    }

    public void setLastMod(Date lastMod) {
        this.lastMod = lastMod;
    }

    public void setChangeFreq(ChangeFreq changeFreq) {
        this.changeFreq = changeFreq;
    }

    public void setPriority(Double priority) {
        if (priority < MIN_PRIORITY || priority > MAX_PRIORITY) {
            throw new InvalidPriorityException("Priority must be between " + MIN_PRIORITY + " and " + MAX_PRIORITY);
        }
        this.priority = priority;
    }

    /**
     * Constructs name from dir and extension (if available), used in SitemapGenerators
     *
     * @return Name
     */
    public String constructName() {
        String result = name;
        if (dir != null) {
            result = UrlUtil.connectUrlParts(dir, result);
        }
        if (extension != null) {
            result = result + "." + extension;
        }
        return result;
    }

    /**
     * Constructs shortName from dir and extension (if available), used in RssGenerator
     *
     * @return Name
     */
    public String constructShortName() {
        String result = shortName;
        if (dir != null) {
            result = UrlUtil.connectUrlParts(dir, result);
        }
        if (extension != null) {
            result = result + "." + extension;
        }
        return result;
    }

    public String getName() {
        return name;
    }

    public Map<String, String> getAlternateNames() {
        return alternateNames;
    }

    public Date getLastMod() {
        return lastMod;
    }

    public ChangeFreq getChangeFreq() {
        return changeFreq;
    }

    public Double getPriority() {
        return priority;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    private static final Comparator<Double> PRIORITY_COMPARATOR = (a, b) -> {
        if (a == null && b == null) {
            return 0;
        } else if (a == null) {
            return 1;
        } else if (b == null) {
            return -1;
        }
        return -Double.compare(a, b);
    };

    private static final Comparator<String> SHORT_NAME_COMPARATOR = (a, b) -> {
        if (a == null && b == null) {
            return 0;
        } else if (a == null) {
            return 1;
        } else if (b == null) {
            return -1;
        }
        return a.compareTo(b);
    };

    /**
     * Compare WebPage first by priority (in descending order - higher priority is first), then by shortName (in ascending order).
     * Priority and / or shortName can be null. WebPages with null priority are at the end.
     *
     * @param o Other WebPage
     * @return -1, 0, 1
     */
    public int compareTo(WebPage o) {
        int result;
        // first compare by priority
        result = PRIORITY_COMPARATOR.compare(this.getPriority(), o.getPriority());
        // next compare by shortName
        if (result == 0) {
            result = SHORT_NAME_COMPARATOR.compare(this.getShortName(), o.getShortName());
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WebPage webPage = (WebPage) o;
        return Objects.equals(name, webPage.name) &&
                Objects.equals(priority, webPage.priority);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, priority);
    }

    public static WebPageBuilder builder() {
        return new WebPageBuilder();
    }

    public static class WebPageBuilder {
        private WebPage webPage = new WebPage();

        /**
         * Sets WebPage name
         *
         * @param name Name
         * @return this
         */
        public WebPageBuilder name(String name) {
            webPage.setName(name);
            return this;
        }

        /**
         * Sets WebPage name
         *
         * @param supplier Supplier
         * @return this
         */
        public WebPageBuilder name(Supplier<String> supplier) {
            webPage.setName(supplier.get());
            return this;
        }

        /**
         * Sets WebPage name
         *
         * @param nameAndDirs Dirs and name, for example: ["a", "b", "xxx"] will be transformed to name: "a/b/xxx"
         * @return this
         */
        public WebPageBuilder name(String... nameAndDirs) {
            webPage.setName(String.join("/", nameAndDirs));
            return this;
        }

        /**
         * Sets WebPage alternate name
         *
         * @param language Alternate language
         * @param name     Name
         * @return this
         */
        public WebPageBuilder alternateName(String language, String name) {
            webPage.addAlternateName(language, name);
            return this;
        }

        /**
         * Sets WebPage alternate name
         *
         * @param language Alternate language
         * @param supplier Supplier
         * @return this
         */
        public WebPageBuilder alternateName(String language, Supplier<String> supplier) {
            webPage.addAlternateName(language, supplier.get());
            return this;
        }

        /**
         * Sets prefix dir to name. Final name will be "dirName/name"
         *
         * @param dirName Dir name
         * @return this
         */
        public WebPageBuilder dir(String dirName) {
            webPage.setDir(dirName);
            return this;
        }

        /**
         * Sets prefix dirs to name. For dirs: ["a", "b", "c"], the final name will be "a/b/c/name"
         *
         * @param dirNames Dir names
         * @return this
         */
        public WebPageBuilder dir(String... dirNames) {
            webPage.setDir(String.join("/", dirNames));
            return this;
        }

        /**
         * Sets suffix extension. Final name will be "name.extension"
         *
         * @param extension Extension
         * @return this
         */
        public WebPageBuilder extension(String extension) {
            webPage.setExtension(extension);
            return this;
        }

        /**
         * Sets WebPage name to ""
         *
         * @return this
         */
        public WebPageBuilder nameRoot() {
            webPage.setName("");
            return this;
        }

        /**
         * Sets WebPage name to "" and priority to MAX_PRIORITY (1.0)
         *
         * @return this
         */
        public WebPageBuilder maxPriorityRoot() {
            webPage.setName("");
            webPage.setPriority(MAX_PRIORITY);
            return this;
        }


        /**
         * Sets WebPage priority
         *
         * @param priority Priority
         * @return this
         */
        public WebPageBuilder priority(Double priority) {
            webPage.setPriority(priority);
            return this;
        }

        /**
         * Sets WebPage lastMod
         *
         * @param lastMod LastMod
         * @return this
         */
        public WebPageBuilder lastMod(Date lastMod) {
            webPage.setLastMod(lastMod);
            return this;
        }

        /**
         * Sets lastMod
         *
         * @param lastMod LastMod
         * @return this
         */
        public WebPageBuilder lastMod(LocalDateTime lastMod) {
            webPage.setLastMod(Timestamp.valueOf(lastMod));
            return this;
        }


        /**
         * Sets WebPage lastMod to current date (new Date())
         *
         * @return this
         */
        public WebPageBuilder lastModNow() {
            webPage.setLastMod(new Date());
            return this;
        }

        /**
         * Sets WebPage priority to maximum (1.0)
         *
         * @return this
         */
        public WebPageBuilder priorityMax() {
            webPage.setPriority(MAX_PRIORITY);
            return this;
        }

        /**
         * Sets WebPage changeFreq
         *
         * @param changeFreq ChangeFreq
         * @return this
         */
        public WebPageBuilder changeFreq(ChangeFreq changeFreq) {
            webPage.setChangeFreq(changeFreq);
            return this;
        }

        /**
         * Sets WebPage changeFreq to ALWAYS
         *
         * @return this
         */
        public WebPageBuilder changeFreqAlways() {
            webPage.setChangeFreq(ChangeFreq.ALWAYS);
            return this;
        }

        /**
         * Sets WebPage changeFreq to HOURLY
         *
         * @return this
         */
        public WebPageBuilder changeFreqHourly() {
            webPage.setChangeFreq(ChangeFreq.HOURLY);
            return this;
        }

        /**
         * Sets WebPage changeFreq to DAILY
         *
         * @return this
         */
        public WebPageBuilder changeFreqDaily() {
            webPage.setChangeFreq(ChangeFreq.DAILY);
            return this;
        }

        /**
         * Sets WebPage changeFreq to WEEKLY
         *
         * @return this
         */
        public WebPageBuilder changeFreqWeekly() {
            webPage.setChangeFreq(ChangeFreq.WEEKLY);
            return this;
        }

        /**
         * Sets WebPage changeFreq to MONTHLY
         *
         * @return this
         */
        public WebPageBuilder changeFreqMonthly() {
            webPage.setChangeFreq(ChangeFreq.MONTHLY);
            return this;
        }

        /**
         * Sets WebPage changeFreq to YEARLY
         *
         * @return this
         */
        public WebPageBuilder changeFreqYearly() {
            webPage.setChangeFreq(ChangeFreq.YEARLY);
            return this;
        }

        /**
         * Sets WebPage changeFreq to NEVER
         *
         * @return this
         */
        public WebPageBuilder changeFreqNever() {
            webPage.setChangeFreq(ChangeFreq.NEVER);
            return this;
        }

        public WebPageBuilder images(List<Image> images) {
            webPage.setImages(images);
            return this;
        }

        /**
         * Returns current webPage
         *
         * @return WebPage
         */
        public WebPage build() {
            return webPage;
        }

    }

    public static RssItemBuilder rssBuilder() {
        return new RssItemBuilder();
    }

    public static class RssItemBuilder {

        private WebPage webPage = new WebPage();

        /**
         * Sets pubDate
         *
         * @param pubDate PubDate
         * @return this
         */
        public RssItemBuilder pubDate(Date pubDate) {
            webPage.setLastMod(pubDate);
            return this;
        }

        /**
         * Sets pubDate
         *
         * @param pubDate PubDate
         * @return this
         */
        public RssItemBuilder pubDate(LocalDateTime pubDate) {
            webPage.setLastMod(Timestamp.valueOf(pubDate));
            return this;
        }

        /**
         * Sets Title
         *
         * @param title Title
         * @return this
         */
        public RssItemBuilder title(String title) {
            webPage.setName(title);
            return this;
        }

        /**
         * Sets Title surrounded by CDATA
         *
         * @param title Title
         * @return this
         */
        public RssItemBuilder titleCdata(String title) {
            webPage.setName("<![CDATA[ " + title + " ]]>");
            return this;
        }

        /**
         * Sets Description
         *
         * @param description Description
         * @return this
         */
        public RssItemBuilder description(String description) {
            webPage.setShortDescription(description);
            return this;
        }


        /**
         * Sets Description surrounded by CDATA
         *
         * @param description Description
         * @return this
         */
        public RssItemBuilder descriptionCdata(String description) {
            webPage.setShortDescription("<![CDATA[ " + description + " ]]>");
            return this;
        }

        /**
         * Sets Link
         *
         * @param link Link
         * @return this
         */
        public RssItemBuilder link(String link) {
            webPage.setShortName(link);
            return this;
        }

        /**
         * Sets WebPage name
         *
         * @param nameAndDirs Dirs and name, for example: ["a", "b", "xxx"] will be transformed to name: "a/b/xxx"
         * @return this
         */
        public RssItemBuilder name(String... nameAndDirs) {
            webPage.setName(String.join("/", nameAndDirs));
            return this;
        }

        /**
         * Sets prefix dir to name. Final name will be "dirName/name"
         *
         * @param dirName Dir name
         * @return this
         */
        public RssItemBuilder dir(String dirName) {
            webPage.setDir(dirName);
            return this;
        }

        /**
         * Sets prefix dirs to name. For dirs: ["a", "b", "c"], the final name will be "a/b/c/name"
         *
         * @param dirNames Dir names
         * @return this
         */
        public RssItemBuilder dir(String... dirNames) {
            webPage.setDir(String.join("/", dirNames));
            return this;
        }

        /**
         * Sets suffix extension. Final name will be "name.extension"
         *
         * @param extension Extension
         * @return this
         */
        public RssItemBuilder extension(String extension) {
            webPage.setExtension(extension);
            return this;
        }

        /**
         * Returns current webPage
         *
         * @return WebPage
         */
        public WebPage build() {
            return webPage;
        }

    }

    public static ImageBuilder imageBuilder() {
        return new ImageBuilder();
    }

    public static class ImageBuilder {

        private Image image = new Image();

        public ImageBuilder caption(String caption) {
            image.setCaption(caption);
            return this;
        }

        public ImageBuilder geoLocation(String geoLoation) {
            image.setGeoLocation(geoLoation);
            return this;
        }

        public ImageBuilder license(String license) {
            image.setLicense(license);
            return this;
        }

        public ImageBuilder loc(String loc) {
            image.setLoc(loc);
            return this;
        }

        public ImageBuilder title(String title) {
            image.setTitle(title);
            return this;
        }

        public Image build() {
            return image;
        }

    }

}
