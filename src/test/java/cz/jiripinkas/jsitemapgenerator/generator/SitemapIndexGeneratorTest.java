package cz.jiripinkas.jsitemapgenerator.generator;

import cz.jiripinkas.jsitemapgenerator.WebPage;
import cz.jiripinkas.jsitemapgenerator.util.TestUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class SitemapIndexGeneratorTest {

	private SitemapIndexGenerator sitemapIndexGenerator;

	@Before
	public void setUp() {
		sitemapIndexGenerator = new SitemapIndexGenerator("http://javalibs.com");
		sitemapIndexGenerator.addPage(WebPage.builder().name("sitemap-plugins.xml").lastModNow().build());
		sitemapIndexGenerator.addPage(WebPage.builder().name("sitemap-archetypes.xml").lastModNow().build());
	}
	
	@Test
	public void testConstructUrl() {
		WebPage webPage = WebPage.builder().name("sitemap-plugins.xml").build();
		String constructUrl = sitemapIndexGenerator.constructUrl(webPage);
		Assert.assertEquals("<sitemap>\n<loc>http://javalibs.com/sitemap-plugins.xml</loc>\n</sitemap>\n", constructUrl);
	}

	@Test
	public void testConstructSitemapIndex() throws SAXException, IOException {
		String sitemapIndex = sitemapIndexGenerator.constructSitemapString();
		ByteArrayInputStream sitemapXml = new ByteArrayInputStream(sitemapIndex.getBytes("UTF-8"));
		TestUtil.testSitemapXsd(sitemapXml, new File("siteindex.xsd"));
	}

	@Test
	public void testConstructAndSaveSitemap() throws SAXException, IOException {
		File tmpFile = File.createTempFile("test", "sitemap");
		sitemapIndexGenerator.constructAndSaveSitemap(tmpFile);
		try {
			TestUtil.testSitemapXsdFile(tmpFile, new File("siteindex.xsd"));
		} finally {
			tmpFile.delete();
		}
	}

}
