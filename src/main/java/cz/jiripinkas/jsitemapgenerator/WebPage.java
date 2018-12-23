package cz.jiripinkas.jsitemapgenerator;

import cz.jiripinkas.jsitemapgenerator.exception.InvalidPriorityException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

	public int compareTo(WebPage o) {
		if (this.getPriority() == null && o.getPriority() == null) {
			return 0;
		}
		if (this.getPriority() == null) {
			return 1;
		}
		if (o.getPriority() == null) {
			return -1;
		}
		return -this.getPriority().compareTo(o.getPriority());
	}
}
