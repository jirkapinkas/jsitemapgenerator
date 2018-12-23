package cz.jiripinkas.jsitemapgenerator;

import java.util.Date;

/**
 * 
 * Builder for easier WebPage construction.
 * @deprecated use {@link WebPage.WebPageBuilder instead}
 */
@Deprecated
public class WebPageBuilder {

	private WebPage webPage = new WebPage();

	/**
	 * Sets WebPage name
	 * 
	 * @param name
	 *            Name
	 * @return this
	 */
	public WebPageBuilder name(String name) {
		webPage.setName(name);
		return this;
	}

	/**
	 * Sets WebPage priority
	 * 
	 * @param priority
	 *            Priority
	 * @return this
	 */
	public WebPageBuilder priority(Double priority) {
		webPage.setPriority(priority);
		return this;
	}

	/**
	 * Sets WebPage lastMod
	 * 
	 * @param lastMod
	 *            LastMod
	 * @return this
	 */
	public WebPageBuilder lastMod(Date lastMod) {
		webPage.setLastMod(lastMod);
		return this;
	}

	/**
	 * Sets WebPage lastMod to current date (new Date())
	 * 
	 * @return this
	 */
	public WebPageBuilder lastModNow() {
		webPage.setLastMod(new Date());
		return this;
	}

	/**
	 * Sets WebPage priority to maximum (1.0)
	 * 
	 * @return this
	 */
	public WebPageBuilder priorityMax() {
		webPage.setPriority(1.0);
		return this;
	}

	/**
	 * Sets WebPage changeFreq
	 * 
	 * @param changeFreq
	 *            ChangeFreq
	 * @return this
	 */
	public WebPageBuilder changeFreq(ChangeFreq changeFreq) {
		webPage.setChangeFreq(changeFreq);
		return this;
	}

	/**
	 * Sets WebPage changeFreq to ALWAYS
	 * 
	 * @return this
	 */
	public WebPageBuilder changeFreqAlways() {
		webPage.setChangeFreq(ChangeFreq.ALWAYS);
		return this;
	}

	/**
	 * Sets WebPage changeFreq to HOURLY
	 * 
	 * @return this
	 */
	public WebPageBuilder changeFreqHourly() {
		webPage.setChangeFreq(ChangeFreq.HOURLY);
		return this;
	}

	/**
	 * Sets WebPage changeFreq to DAILY
	 * 
	 * @return this
	 */
	public WebPageBuilder changeFreqDaily() {
		webPage.setChangeFreq(ChangeFreq.DAILY);
		return this;
	}

	/**
	 * Sets WebPage changeFreq to WEEKLY
	 * 
	 * @return this
	 */
	public WebPageBuilder changeFreqWeekly() {
		webPage.setChangeFreq(ChangeFreq.WEEKLY);
		return this;
	}

	/**
	 * Sets WebPage changeFreq to MONTHLY
	 * 
	 * @return this
	 */
	public WebPageBuilder changeFreqMonthly() {
		webPage.setChangeFreq(ChangeFreq.MONTHLY);
		return this;
	}

	/**
	 * Sets WebPage changeFreq to YEARLY
	 * 
	 * @return this
	 */
	public WebPageBuilder changeFreqYearly() {
		webPage.setChangeFreq(ChangeFreq.YEARLY);
		return this;
	}

	/**
	 * Sets WebPage changeFreq to NEVER
	 * 
	 * @return this
	 */
	public WebPageBuilder changeFreqNever() {
		webPage.setChangeFreq(ChangeFreq.NEVER);
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
