package cz.jiripinkas.jsitemapgenerator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RssGenerator extends AbstractGenerator {

	private String webTitle;

	private String webDescription;

	public RssGenerator(String baseUrl, String webTitle, String webDescription) {
		super(baseUrl);
		this.webTitle = webTitle;
		this.webDescription = webDescription;
	}

	public String constructRss() {
		StringBuilder builder = new StringBuilder();
		builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" + "\n");
		builder.append("<rss version=\"2.0\">" + "\n");
		builder.append("<channel>" + "\n");
		builder.append("<title>" + webTitle + "</title>" + "\n");
		builder.append("<description>" + webDescription + "</description>" + "\n");
		builder.append("<link>" + baseUrl + "</link>" + "\n");

		List<WebPage> webPages = new ArrayList<WebPage>(urls.values());

		Collections.sort(webPages, new Comparator<WebPage>() {

			public int compare(WebPage o1, WebPage o2) {
				return -o1.getLastMod().compareTo(o2.getLastMod());
			}
		});

		Date latestDate = webPages.get(0).getLastMod();

		builder.append("<lastBuildDate>" + new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.ENGLISH).format(latestDate) + " +0000</lastBuildDate>" + "\n");
		builder.append("<pubDate>" + new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.ENGLISH).format(latestDate) + " +0000</pubDate>" + "\n");
		builder.append("<ttl>1800</ttl>" + "\n");

		for (WebPage webPage : webPages) {
			builder.append("<item>" + "\n");

			builder.append("<title>");
			builder.append(webPage.getName());
			builder.append("</title>" + "\n");

			builder.append("<description>");
			builder.append(webPage.getShortDescription());
			builder.append("</description>" + "\n");

			builder.append("<link>");
			builder.append(baseUrl + webPage.getShortName());
			builder.append("</link>" + "\n");

			builder.append("<pubDate>");
			builder.append(new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.ENGLISH).format(webPage.getLastMod()) + " +0000");
			builder.append("</pubDate>" + "\n");

			builder.append("</item>" + "\n");
		}
		builder.append("</channel>" + "\n");
		builder.append("</rss>" + "\n");
		return builder.toString();
	}

}
