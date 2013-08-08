package cz.jiripinkas.jsitemapgenerator;

/**
 * How frequently the page is likely to change. This value provides general
 * information to search engines and may not correlate exactly to how often they
 * crawl the page. The value {@link #ALWAYS} should be used to describe
 * documents that change each time they are accessed. The value {@link #NEVER}
 * should be used to describe archived URLs.
 */
public enum ChangeFreq {

	ALWAYS, HOURLY, DAILY, WEEKLY, MONTHLY, YEARLY, NEVER;

	private String lowerCase;

	private ChangeFreq() {
		lowerCase = name().toLowerCase();
	}

	@Override
	public String toString() {
		return lowerCase;
	}
}
