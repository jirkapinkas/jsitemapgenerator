package cz.jiripinkas.jsitemapgenerator;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import cz.jiripinkas.jsitemapgenerator.exception.InvalidUrlException;

public abstract class AbstractGenerator {

	protected Map<String, WebPage> urls = new TreeMap<String, WebPage>();

	protected String baseUrl;

	/**
	 * Construct web sitemap.
	 * 
	 * @param baseUrl
	 *            All URLs must start with this baseUrl, for example
	 *            http://www.javavids.com
	 */
	public AbstractGenerator(String baseUrl) {
		try {
			new URL(baseUrl);
		} catch (MalformedURLException e) {
			throw new InvalidUrlException(e);
		}

		if (!baseUrl.endsWith("/")) {
			baseUrl += "/";
		}
		this.baseUrl = baseUrl;
	}

	/**
	 * Add single page to sitemap
	 * 
	 * @param webPage
	 *            single page
	 */
	public void addPage(WebPage webPage) {
		urls.put(baseUrl + webPage.getName(), webPage);
	}

	/**
	 * Add collection of pages to sitemap
	 * 
	 * @param webPages
	 *            Collection of pages
	 */
	public void addPages(Collection<WebPage> webPages) {
		for (WebPage webPage : webPages) {
			addPage(webPage);
		}
	}

}
