package cz.jiripinkas.jsitemapgenerator;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import cz.jiripinkas.jsitemapgenerator.exception.GWTException;
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

	/**
	 * Ping Google that sitemap has changed. Will call this URL:
	 * http://www.google
	 * .com/webmasters/tools/ping?sitemap=URL_Encoded_sitemapUrl
	 * 
	 * @param sitemapUrl
	 *            sitemap url
	 */
	public void pingGoogle(String sitemapUrl) {
		ping("http://www.google.com/webmasters/tools/ping?sitemap=", sitemapUrl);
	}

	/**
	 * Ping Bing that sitemap has changed. Will call this URL:
	 * http://www.bing.com/ping?sitemap=URL_Encoded_sitemapUrl
	 * 
	 * @param sitemapUrl
	 *            sitemap url
	 * 
	 */
	public void pingBing(String sitemapUrl) {
		ping("http://www.bing.com/ping?sitemap=", sitemapUrl);
	}

	private void ping(String resourceUrl, String sitemapUrl) {
		try {
			String pingUrl = resourceUrl + URLEncoder.encode(sitemapUrl, "UTF-8");
			// ping Bing
			int returnCode = HttpClientUtil.get(pingUrl);
			if (returnCode != 200) {
				throw new GWTException("Google could not be informed about new sitemap!");
			}
		} catch (Exception ex) {
			throw new GWTException("Google could not be informed about new sitemap!");
		}
	}

	/**
	 * Ping Google that sitemap has changed. Sitemap must be on this location:
	 * baseUrl/sitemap.xml (for example http://www.javavids.com/sitemap.xml)
	 */
	public void pingGoogle() {
		pingGoogle(baseUrl + "sitemap.xml");
	}

	/**
	 * Ping Google that sitemap has changed. Sitemap must be on this location:
	 * baseUrl/sitemap.xml (for example http://www.javavids.com/sitemap.xml)
	 */
	public void pingBing() {
		pingBing(baseUrl + "sitemap.xml");
	}

}
