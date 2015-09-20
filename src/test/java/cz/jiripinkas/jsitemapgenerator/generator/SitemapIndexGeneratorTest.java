package cz.jiripinkas.jsitemapgenerator.generator;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import cz.jiripinkas.jsitemapgenerator.WebPage;
import cz.jiripinkas.jsitemapgenerator.WebPageBuilder;
import cz.jiripinkas.jsitemapgenerator.generator.SitemapIndexGenerator;
import cz.jiripinkas.jsitemapgenerator.util.TestUtil;

public class SitemapIndexGeneratorTest {

	private SitemapIndexGenerator sitemapIndexGenerator;

	@Before
	public void setUp() throws Exception {
		sitemapIndexGenerator = new SitemapIndexGenerator("http://javalibs.com");
		sitemapIndexGenerator.addPage(new WebPageBuilder().name("sitemap-plugins.xml").lastModNow().build());
		sitemapIndexGenerator.addPage(new WebPageBuilder().name("sitemap-archetypes.xml").lastModNow().build());
	}
	
	@Test
	public void testConstructUrl() {
		WebPage webPage = new WebPageBuilder().name("sitemap-plugins.xml").build();
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
