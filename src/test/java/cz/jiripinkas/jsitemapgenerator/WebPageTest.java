package cz.jiripinkas.jsitemapgenerator;

import org.junit.Test;

import cz.jiripinkas.jsitemapgenerator.exception.InvalidPriorityException;
import cz.jiripinkas.jsitemapgenerator.exception.InvalidUrlException;
import cz.jiripinkas.jsitemapgenerator.sitemap.WebSitemapGenerator;

public class WebPageTest {

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
