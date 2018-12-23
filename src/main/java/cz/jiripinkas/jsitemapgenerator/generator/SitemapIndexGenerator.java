package cz.jiripinkas.jsitemapgenerator.generator;

import cz.jiripinkas.jsitemapgenerator.AbstractSitemapGenerator;
import cz.jiripinkas.jsitemapgenerator.WebPage;
import cz.jiripinkas.jsitemapgenerator.exception.InvalidUrlException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

public class SitemapIndexGenerator extends AbstractSitemapGenerator {

	public SitemapIndexGenerator(String baseUrl) {
		super(baseUrl);
	}

	@Override
	public String[] constructSitemap() {
		ArrayList<String> out = new ArrayList<>();
		out.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		out.add("<sitemapindex xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");
		ArrayList<WebPage> values = new ArrayList<>(urls.values());
		Collections.sort(values);
		for (WebPage webPage : values) {
			out.add(constructUrl(webPage));
		}
		out.add("</sitemapindex>");
		return out.toArray(new String[] {});
	}
	
	String constructUrl(WebPage webPage) {
		StringBuilder out = new StringBuilder();
		out.append("<sitemap>\n");
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
		out.append("</sitemap>\n");
		return out.toString();
	}

}
