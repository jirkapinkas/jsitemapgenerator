package cz.jiripinkas.jsitemapgenerator.generator;

import cz.jiripinkas.jsitemapgenerator.AbstractSitemapGenerator;
import cz.jiripinkas.jsitemapgenerator.WebPage;
import cz.jiripinkas.jsitemapgenerator.exception.InvalidUrlException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

public class SitemapIndexGenerator extends AbstractSitemapGenerator {

	/**
	 * @deprecated use {@link #of(String)}
	 * @param baseUrl Base url
	 */
	@Deprecated
	public SitemapIndexGenerator(String baseUrl) {
		super(baseUrl);
	}

	public static SitemapIndexGenerator of(String baseUrl) {
		return new SitemapIndexGenerator(baseUrl);
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

	/**
	 * Add single page to sitemap
	 *
	 * @param webPage single page
	 * @return this
	 */
	@Override
	public SitemapIndexGenerator addPage(WebPage webPage) {
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
	public SitemapIndexGenerator addPages(Collection<WebPage> webPages) {
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
	public <T> SitemapIndexGenerator addPages(Collection<T> webPages, Function<T, WebPage> mapper) {
		for (T element : webPages) {
			addPage(mapper.apply(element));
		}
		return this;
	}


}
