package cz.jiripinkas.jsitemapgenerator.sitemap;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import cz.jiripinkas.jsitemapgenerator.AbstractSitemapGenerator;
import cz.jiripinkas.jsitemapgenerator.W3CDateFormat;
import cz.jiripinkas.jsitemapgenerator.WebPage;
import cz.jiripinkas.jsitemapgenerator.exception.InvalidUrlException;

public class WebSitemapGenerator extends AbstractSitemapGenerator {

	private W3CDateFormat dateFormat = new W3CDateFormat();

	public WebSitemapGenerator(String baseUrl) {
		super(baseUrl);
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
		ArrayList<String> out = new ArrayList<String>();
		out.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		out.add("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");
		ArrayList<WebPage> values = new ArrayList<WebPage>(urls.values());
		Collections.sort(values);
		for (WebPage webPage : values) {
			out.add(constructUrl(webPage));
		}
		out.add("</urlset>");
		return out.toArray(new String[] {});
	}

	private String constructUrl(WebPage webPage) {
		StringBuilder out = new StringBuilder();
		out.append("<url>\n");
		out.append("<loc>");
		try {
			if (webPage.getName() != null) {
				out.append(new URL(baseUrl + webPage.getName()).toString());
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
		out.append("</url>\n");
		return out.toString();
	}

}
