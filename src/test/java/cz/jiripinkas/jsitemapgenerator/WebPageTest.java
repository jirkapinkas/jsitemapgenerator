package cz.jiripinkas.jsitemapgenerator;

import org.junit.Assert;
import org.junit.Test;

import cz.jiripinkas.jsitemapgenerator.exception.InvalidPriorityException;
import cz.jiripinkas.jsitemapgenerator.exception.InvalidUrlException;
import cz.jiripinkas.jsitemapgenerator.sitemap.WebSitemapGenerator;

public class WebPageTest {

	@Test
	public void testConstructUrlEmptyPage() {
		String url = new WebPage().constructUrl(new W3CDateFormat(), "http://www.javavids.com/");
		Assert.assertEquals("<url>\n<loc>http://www.javavids.com/</loc>\n</url>\n", url);
	}

	@Test
	public void testConstructUrlNotEmptyPage() {
		String url = new WebPage().setName("latest.php").constructUrl(new W3CDateFormat(), "http://www.javavids.com/");
		Assert.assertEquals("<url>\n<loc>http://www.javavids.com/latest.php</loc>\n</url>\n", url);
	}

	@Test(expected = InvalidUrlException.class)
	public void testConstruct() {
		new WebSitemapGenerator("www.javavids.com");
	}

	@Test(expected = InvalidPriorityException.class)
	public void testLowPriority() {
		new WebPage().setPriority(-1.0);
	}

	@Test(expected = InvalidPriorityException.class)
	public void testHighPriority() {
		new WebPage().setPriority(10.0);
	}

}
