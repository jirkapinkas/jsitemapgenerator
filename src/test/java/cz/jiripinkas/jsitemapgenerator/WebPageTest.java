package cz.jiripinkas.jsitemapgenerator;

import cz.jiripinkas.jsitemapgenerator.exception.InvalidPriorityException;
import cz.jiripinkas.jsitemapgenerator.exception.InvalidUrlException;
import cz.jiripinkas.jsitemapgenerator.generator.SitemapGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class WebPageTest {

	@Test
	void testConstruct() {
		assertThrows(InvalidUrlException.class, () -> {
			SitemapGenerator.of("www.javavids.com");
		});
	}

	@Test
	void testLowPriority() {
		assertThrows(InvalidPriorityException.class, () -> {
			new WebPage().setPriority(-1.0);
		});
	}

	@Test
	void testHighPriority() {
		assertThrows(InvalidPriorityException.class, () -> {
			new WebPage().setPriority(10.0);
		});
	}

	@Test
	void testPrefixDirAndSuffix() {
		WebPage build = WebPage.builder().dir("dir").name("name").extension("html").build();
		assertEquals("dir/name.html", build.constructName());
	}

	@Test
	void testPrefixDirs() {
		WebPage build = WebPage.builder().dir("dir1", "dir2", "dir3").name("name").extension("html").build();
		assertEquals("dir1/dir2/dir3/name.html", build.constructName());
	}

	@Test
	void testDirs() {
		WebPage build = WebPage.builder().name("dir1", "dir2", "dir3", "name").build();
		assertEquals("dir1/dir2/dir3/name", build.constructName());
	}

	@Test
	void testDirs2() {
		WebPage build = WebPage.of("dir1", "dir2", "dir3", "name");
		assertEquals("dir1/dir2/dir3/name", build.constructName());
	}

	@Test
	void testNameRoot() {
		WebPage build = WebPage.builder().nameRoot().build();
		assertEquals("", build.constructName());
	}

	@Test
	void of_name_is_not_null() {
		WebPage webPage = WebPage.of("test");
		assertThat(webPage.getName()).isEqualTo("test");
	}

	@Test
	void of_name_cannot_be_null() {
		assertThrows(NullPointerException.class, () -> {
			String nullString = null;
			WebPage.of(nullString);
		});
	}

}
