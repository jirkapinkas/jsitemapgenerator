package cz.jiripinkas.jsitemapgenerator;

import java.util.Date;

public class RssItemBuilder {

	private WebPage webPage = new WebPage();

	/**
	 * Sets pubDate
	 * 
	 * @param pubDate
	 *            PubDate
	 * @return this
	 */
	public RssItemBuilder pubDate(Date pubDate) {
		webPage.setLastMod(pubDate);
		return this;
	}

	/**
	 * Sets Title
	 * 
	 * @param title
	 *            Title
	 * @return this
	 */
	public RssItemBuilder title(String title) {
		webPage.setName(title);
		return this;
	}

	/**
	 * Sets Description
	 * 
	 * @param description
	 *            Description
	 * @return this
	 */
	public RssItemBuilder description(String description) {
		webPage.setShortDescription(description);
		return this;
	}

	/**
	 * Sets Name
	 * 
	 * @param name
	 *            Name
	 * @return this
	 */
	public RssItemBuilder name(String name) {
		webPage.setShortName(name);
		return this;
	}

	/**
	 * Returns current webPage
	 * 
	 * @return WebPage
	 */
	public WebPage build() {
		return webPage;
	}

}
