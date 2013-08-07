package com.javavids.jsitemapgenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class WebSitemapGenerator {

	private Map<String, WebSitemapUrl> urls = new TreeMap<String, WebSitemapUrl>();

	private String baseUrl;

	private W3CDateFormat dateFormat = new W3CDateFormat();

	public WebSitemapGenerator(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public void addUrl(WebSitemapUrl url) {
		String strurl = url.getUrl().toString();
		if (!strurl.startsWith(baseUrl)) {
			throw new RuntimeException("URL " + strurl + " must start with base URL: " + url);
		}
		urls.put(url.getUrl().toString(), url);
	}

	public void addUrls(Collection<WebSitemapUrl> urls) {
		for (WebSitemapUrl url : urls) {
			addUrl(url);
		}
	}

	public String[] constructSitemap() {
		ArrayList<String> out = new ArrayList<String>();
		out.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		out.add("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");
		Collection<WebSitemapUrl> values = urls.values();
		for (WebSitemapUrl webSitemapUrl : values) {
			out.add(webSitemapUrl.constructUrl(dateFormat));
		}
		out.add("</urlset>");
		return out.toArray(new String[] {});
	}

	public void saveSitemap(File file, String[] sitemap) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		for (String string : sitemap) {
			writer.write(string);
		}
		writer.close();
	}

	public void constructAndSaveSitemap(File file) throws IOException {
		String[] sitemap = constructSitemap();
		saveSitemap(file, sitemap);
	}

	public void pingGoogle(String sitemapUrl) throws Exception {
		// TODO change sysout to logging
		try {
			String pingUrl = "http://www.google.com/webmasters/tools/ping?sitemap=" + URLEncoder.encode(sitemapUrl, "UTF-8");
			System.out.println("will ping this URL: " + pingUrl);
			// ping Google
			int returnCode = HttpClientUtil.get(pingUrl);
			if (returnCode != 200) {
				throw new Exception("google could not be informed about new sitemap!");
			}
		} catch (Exception ex) {
			throw new Exception("google could not be informed about new sitemap!");
		}
	}
}
