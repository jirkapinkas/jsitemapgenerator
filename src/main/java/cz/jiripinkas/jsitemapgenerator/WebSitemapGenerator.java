package cz.jiripinkas.jsitemapgenerator;

import java.util.ArrayList;
import java.util.Collections;

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
		for (WebPage webSitemapUrl : values) {
			out.add(webSitemapUrl.constructUrl(dateFormat, baseUrl));
		}
		out.add("</urlset>");
		return out.toArray(new String[] {});
	}

}
