package cz.jiripinkas.jsitemapgenerator.sitemap;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import cz.jiripinkas.jsitemapgenerator.ChangeFreq;
import cz.jiripinkas.jsitemapgenerator.WebPage;
import cz.jiripinkas.jsitemapgenerator.util.TestUtil;

public class WebSitemapGeneratorTest {

	private SitemapGenerator webSitemapGenerator;

	@Before
	public void setUp() throws Exception {
		webSitemapGenerator = new SitemapGenerator("http://www.javavids.com");
		webSitemapGenerator.addPage(new WebPage().setName("index.php").setPriority(1.0).setChangeFreq(ChangeFreq.NEVER).setLastMod(new Date()));
		webSitemapGenerator.addPage(new WebPage().setName("latest.php"));
		webSitemapGenerator.addPage(new WebPage().setName("contact.php"));
	}

	@Test
	public void testConstructUrlEmptyPage() {
		String url = webSitemapGenerator.constructUrl(new WebPage());
		Assert.assertEquals("<url>\n<loc>http://www.javavids.com/</loc>\n</url>\n", url);
	}

	@Test
	public void testConstructUrlNotEmptyPage() {
		String url = webSitemapGenerator.constructUrl(new WebPage().setName("latest.php"));
		Assert.assertEquals("<url>\n<loc>http://www.javavids.com/latest.php</loc>\n</url>\n", url);
	}

	@Test
	public void testConstructSitemap() throws Exception {
		String sitemap = webSitemapGenerator.constructSitemapString();
		ByteArrayInputStream sitemapXml = new ByteArrayInputStream(sitemap.getBytes("UTF-8"));
		TestUtil.testSitemapXsd(sitemapXml, new File("sitemap.xsd"));
	}

	@Test
	public void testSaveSitemap() throws Exception {
		File tmpFile = File.createTempFile("test", "sitemap");
		webSitemapGenerator.saveSitemap(tmpFile, webSitemapGenerator.constructSitemap());
		try {
			TestUtil.testSitemapXsdFile(tmpFile, new File("sitemap.xsd"));
		} finally {
			tmpFile.delete();
		}
	}

	@Test
	public void testConstructAndSaveSitemap() throws Exception {
		File tmpFile = File.createTempFile("test", "sitemap");
		webSitemapGenerator.constructAndSaveSitemap(tmpFile);
		try {
			TestUtil.testSitemapXsdFile(tmpFile, new File("sitemap.xsd"));
		} finally {
			tmpFile.delete();
		}
	}

	@Ignore
	@Test
	public void testPingGoogle() throws Exception {
		webSitemapGenerator.pingGoogle("http://jsitemapgenerator.jiripinkas.cz/sitemap.xml");
	}

}
