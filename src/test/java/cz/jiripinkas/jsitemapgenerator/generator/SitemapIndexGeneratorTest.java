package cz.jiripinkas.jsitemapgenerator.generator;

import cz.jiripinkas.jsitemapgenerator.WebPage;
import cz.jiripinkas.jsitemapgenerator.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class SitemapIndexGeneratorTest {

	private SitemapIndexGenerator sitemapIndexGenerator;

	@BeforeEach
	void setUp() {
		sitemapIndexGenerator = SitemapIndexGenerator.of("http://javalibs.com");
		sitemapIndexGenerator.addPage(WebPage.of("sitemap-plugins.xml"));
		sitemapIndexGenerator.addPage(WebPage.of("sitemap-archetypes.xml"));
	}
	
	@Test
	void testConstructUrl() {
		WebPage webPage = WebPage.builder().name("sitemap-plugins.xml").build();
		String constructUrl = sitemapIndexGenerator.constructUrl(webPage);
		assertEquals("<sitemap>\n<loc>http://javalibs.com/sitemap-plugins.xml</loc>\n</sitemap>\n", constructUrl);
	}

	@Test
	void testConstructSitemapIndex() throws SAXException, IOException {
		String sitemapIndex = sitemapIndexGenerator.toString();
		ByteArrayInputStream sitemapXml = new ByteArrayInputStream(sitemapIndex.getBytes("UTF-8"));
		TestUtil.testSitemapXsd(sitemapXml, new File("src/test/resources/siteindex.xsd"));
	}

	@Test
	void testConstructAndSaveSitemap() throws SAXException, IOException {
		File tmpFile = File.createTempFile("test", "sitemap");
		sitemapIndexGenerator.toFile(tmpFile);
		try {
			TestUtil.testSitemapXsdFile(tmpFile, new File("src/test/resources/siteindex.xsd"));
		} finally {
			tmpFile.delete();
		}
	}

	@Test
	void testSitemapPathWithSpecialCharacters() {
		sitemapIndexGenerator.addPage(WebPage.builder()
				.name("/page?arg1='test'&arg2=<test>&arg3=\"test\"")
				.build());

		String actualSitemapIndex = sitemapIndexGenerator.toString();
		String expectedSitemapIndex =
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
						"<sitemapindex xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n" +
						"<sitemap>\n" +
						"<loc>http://javalibs.com/page?arg1=&apos;test&apos;&amp;arg2=&lt;test&gt;&amp;arg3=&quot;test&quot;</loc>\n" +
						"</sitemap>\n" +
						"<sitemap>\n" +
						"<loc>http://javalibs.com/sitemap-archetypes.xml</loc>\n" +
						"</sitemap>\n" +
						"<sitemap>\n" +
						"<loc>http://javalibs.com/sitemap-plugins.xml</loc>\n" +
						"</sitemap>\n" +
						"</sitemapindex>";
		assertEquals(expectedSitemapIndex, actualSitemapIndex);
	}

	@Test
	void testConstructSitemapIndexWithRedundantSlash() throws SAXException, IOException {
		sitemapIndexGenerator.addPage("/test");
		String actualSitemapIndex = sitemapIndexGenerator.toString();
		String expectedSitemapIndex = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
				"<sitemapindex xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n" +
				"<sitemap>\n" +
				"<loc>http://javalibs.com/sitemap-archetypes.xml</loc>\n" +
				"</sitemap>\n" +
				"<sitemap>\n" +
				"<loc>http://javalibs.com/sitemap-plugins.xml</loc>\n" +
				"</sitemap>\n" +
				"<sitemap>\n" +
				"<loc>http://javalibs.com/test</loc>\n" +
				"</sitemap>\n" +
				"</sitemapindex>";
		assertEquals(expectedSitemapIndex, actualSitemapIndex);
	}

}
