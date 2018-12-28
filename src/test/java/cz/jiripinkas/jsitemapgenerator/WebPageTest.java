package cz.jiripinkas.jsitemapgenerator;

import org.junit.Test;

import cz.jiripinkas.jsitemapgenerator.exception.InvalidPriorityException;
import cz.jiripinkas.jsitemapgenerator.exception.InvalidUrlException;
import cz.jiripinkas.jsitemapgenerator.generator.SitemapGenerator;

import static org.junit.Assert.*;

public class WebPageTest {

	@Test(expected = InvalidUrlException.class)
	public void testConstruct() {
		SitemapGenerator.of("www.javavids.com");
	}

	@Test(expected = InvalidPriorityException.class)
	public void testLowPriority() {
		new WebPage().setPriority(-1.0);
	}

	@Test(expected = InvalidPriorityException.class)
	public void testHighPriority() {
		new WebPage().setPriority(10.0);
	}

	@Test
	public void testPrefixDirAndSuffix() {
		WebPage build = WebPage.builder().dir("dir").name("name").extension("html").build();
		assertEquals("dir/name.html", build.constructName());
	}

	@Test
	public void testPrefixDirs() {
		WebPage build = WebPage.builder().dir("dir1", "dir2", "dir3").name("name").extension("html").build();
		assertEquals("dir1/dir2/dir3/name.html", build.constructName());
	}

	@Test
	public void testDirs() {
		WebPage build = WebPage.builder().name("dir1", "dir2", "dir3", "name").build();
		assertEquals("dir1/dir2/dir3/name", build.constructName());
	}

	@Test
	public void testNameRoot() {
		WebPage build = WebPage.builder().nameRoot().build();
		assertEquals("", build.constructName());
	}

}
