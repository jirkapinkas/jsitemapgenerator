package cz.jiripinkas.jsitemapgenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.jiripinkas.jsitemapgenerator.exception.GWTException;

public class WebSitemapGenerator extends AbstractGenerator {

	private static final Logger logger = LoggerFactory.getLogger(WebSitemapGenerator.class);

	private W3CDateFormat dateFormat = new W3CDateFormat();

	public WebSitemapGenerator(String baseUrl) {
		super(baseUrl);
	}
	/**
	 * Construct sitemap into array of Strings
	 * 
	 * @return sitemap
	 */
	public String[] constructSitemap() {
		ArrayList<String> out = new ArrayList<String>();
		out.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		out.add("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");
		Collection<WebPage> values = urls.values();
		for (WebPage webSitemapUrl : values) {
			out.add(webSitemapUrl.constructUrl(dateFormat, baseUrl));
		}
		out.add("</urlset>");
		return out.toArray(new String[] {});
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
	 */
	public void constructAndSaveSitemap(File file) throws IOException {
		String[] sitemap = constructSitemap();
		saveSitemap(file, sitemap);
	}

	/**
	 * Ping Google that sitemap has changed. Will call this URL:
	 * http://www.google.com/webmasters/tools/ping?sitemap=<URL Encoded
	 * sitemapUrl>
	 * 
	 */
	public void pingGoogle(String sitemapUrl) {
		try {
			String pingUrl = "http://www.google.com/webmasters/tools/ping?sitemap=" + URLEncoder.encode(sitemapUrl, "UTF-8");
			logger.debug("Will ping this URL: " + pingUrl);
			// ping Google
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
}
