package com.javavids.jsitemapgenerator;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class WebSitemapUrl {
	private URL url;
	private Date lastMod;
	private ChangeFreq changeFreq;
	private Double priority;

	public String constructUrl(W3CDateFormat dateFormat) {
		StringBuilder out = new StringBuilder();
		out.append("<url>\n");
		out.append("<loc>");
		out.append(url);
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

	public WebSitemapUrl setUrl(URL url) {
		this.url = url;
		return this;
	}

	public WebSitemapUrl setUrl(String url) {
		try {
			this.url = new URL(url);
		} catch (MalformedURLException e) {
			throw new RuntimeException("Must add only valid URL!", e);
		}
		return this;
	}

	public WebSitemapUrl setLastMod(Date lastMod) {
		this.lastMod = lastMod;
		return this;
	}

	public WebSitemapUrl setChangeFreq(ChangeFreq changeFreq) {
		this.changeFreq = changeFreq;
		return this;
	}

	public WebSitemapUrl setPriority(Double priority) {
		this.priority = priority;
		return this;
	}

	public URL getUrl() {
		return url;
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
