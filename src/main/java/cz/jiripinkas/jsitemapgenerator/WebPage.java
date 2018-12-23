package cz.jiripinkas.jsitemapgenerator;

import cz.jiripinkas.jsitemapgenerator.exception.InvalidPriorityException;

import java.util.*;

public class WebPage implements Comparable<WebPage> {
    private String name;
    private Date lastMod;
    private ChangeFreq changeFreq;
    private Double priority;
    private String shortDescription;
    private String shortName;

    private List<Image> images;

    public void addImage(Image image) {
        if (images == null) {
            images = new ArrayList<>();
        }
        images.add(image);
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

    public WebPage setShortName(String shortName) {
        this.shortName = shortName;
        return this;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public WebPage setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
        return this;
    }

    public WebPage setName(String name) {
        this.name = name;
        return this;
    }

    public WebPage setLastMod(Date lastMod) {
        this.lastMod = lastMod;
        return this;
    }

    public WebPage setChangeFreq(ChangeFreq changeFreq) {
        this.changeFreq = changeFreq;
        return this;
    }

    public WebPage setPriority(Double priority) {
        if (priority < 0.0 || priority > 1.0) {
            throw new InvalidPriorityException("Priority must be between 0 and 1.0");
        }
        this.priority = priority;
        return this;
    }

    public String getName() {
        return name;
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
     * @param o
     * @return
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
}
