package cz.jiripinkas.jsitemapgenerator;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import cz.jiripinkas.jsitemapgenerator.exception.InvalidPriorityException;
import cz.jiripinkas.jsitemapgenerator.exception.InvalidUrlException;

public class WebPage {
	private String name;
	private Date lastMod;
	private ChangeFreq changeFreq;
	private Double priority;
	private String shortDescription;
	private String shortName;

	public String constructUrl(W3CDateFormat dateFormat, String baseUrl) {
		StringBuilder out = new StringBuilder();
		out.append("<url>\n");
		out.append("<loc>");
		try {
			if (name != null) {
				out.append(new URL(baseUrl + name).toString());
			} else {
				out.append(new URL(baseUrl).toString());
			}
		} catch (MalformedURLException e) {
			throw new InvalidUrlException(e);
		}
		out.append("</loc>\n");
		if (lastMod != null) {
			out.append("<lastmod>");
			out.append(dateFormat.format(lastMod));
			out.append("</lastmod>\n");
		}
		if (changeFreq != null) {
			out.append("<changefreq>");
			out.append(changeFreq);
			out.append("</changefreq>\n");
		}
		if (priority != null) {
			out.append("<priority>");
			out.append(priority);
			out.append("</priority>\n");
		}
		out.append("</url>\n");
		return out.toString();
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

}
