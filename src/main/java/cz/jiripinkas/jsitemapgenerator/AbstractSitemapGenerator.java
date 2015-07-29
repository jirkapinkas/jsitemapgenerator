package cz.jiripinkas.jsitemapgenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLEncoder;

import cz.jiripinkas.jsitemapgenerator.exception.GWTException;

public abstract class AbstractSitemapGenerator extends AbstractGenerator {

	protected W3CDateFormat dateFormat = new W3CDateFormat();

	public AbstractSitemapGenerator(String baseUrl) {
		super(baseUrl);
	}

	public abstract String[] constructSitemap();

	/**
	 * Construct sitemap into single String
	 * 
	 * @return sitemap
	 */
	public String constructSitemapString() {
		String[] sitemapArray = constructSitemap();
		StringBuilder result = new StringBuilder();
		for (String line : sitemapArray) {
			result.append(line);
		}
		return result.toString();
	}

	/**
	 * Save sitemap to output file
	 * 
	 * @param file
	 *            Output file
	 * @param sitemap
	 *            Sitemap as array of Strings (created by constructSitemap()
	 *            method)
	 * @throws IOException
	 *             when error
	 */
	public void saveSitemap(File file, String[] sitemap) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		for (String string : sitemap) {
			writer.write(string);
		}
		writer.close();
	}

	/**
	 * Construct and save sitemap to output file
	 * 
	 * @param file
	 *            Output file
	 * @throws IOException
	 *             when error
	 */
	public void constructAndSaveSitemap(File file) throws IOException {
		String[] sitemap = constructSitemap();
		saveSitemap(file, sitemap);
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
