package cz.jiripinkas.jsitemapgenerator.generator;

import cz.jiripinkas.jsitemapgenerator.ChangeFreq;
import cz.jiripinkas.jsitemapgenerator.Image;
import cz.jiripinkas.jsitemapgenerator.ImageBuilder;
import cz.jiripinkas.jsitemapgenerator.WebPage;
import cz.jiripinkas.jsitemapgenerator.generator.SitemapGenerator.AdditionalNamespace;
import cz.jiripinkas.jsitemapgenerator.util.TestUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Date;

public class SitemapGeneratorTest {

	private SitemapGenerator sitemapGenerator;

	@Before
	public void setUp() throws Exception {
		sitemapGenerator = new SitemapGenerator("http://www.javavids.com");
		sitemapGenerator.addPage(new WebPage().setName("index.php").setPriority(1.0).setChangeFreq(ChangeFreq.NEVER).setLastMod(new Date()));
		sitemapGenerator.addPage(new WebPage().setName("latest.php"));
		sitemapGenerator.addPage(new WebPage().setName("contact.php"));
	}

	@Test
	public void testConstructImage() {
		Image image = new Image();
		image.setLoc("http://example.com/image");
		String imageString = sitemapGenerator.constructImage(image);
		Assert.assertEquals("<image:image>\n<image:loc>http://example.com/image</image:loc>\n</image:image>\n", imageString);
	}

	@Test
	public void testConstructSitemapWithImagesHeader() {
		sitemapGenerator = new SitemapGenerator("http://www.javavids.com", new AdditionalNamespace[] { AdditionalNamespace.IMAGE });
		String sitemapString = sitemapGenerator.constructSitemapString();
		Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" xmlns:image=\"http://www.google.com/schemas/sitemap-image/1.1\" >\n</urlset>", sitemapString);
	}
	
	@Test
	public void testConstructSitemapWithImages() {
		sitemapGenerator = new SitemapGenerator("http://www.javavids.com", new AdditionalNamespace[] { AdditionalNamespace.IMAGE });
		WebPage webPage = WebPage.builder().name("").build();
		webPage.addImage(new ImageBuilder().loc("http://www.javavids.com/favicon.ico").build());
		sitemapGenerator.addPage(webPage);
		String sitemapString = sitemapGenerator.constructSitemapString();
		Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" xmlns:image=\"http://www.google.com/schemas/sitemap-image/1.1\" >\n<url>\n<loc>http://www.javavids.com/</loc>\n<image:image>\n<image:loc>http://www.javavids.com/favicon.ico</image:loc>\n</image:image>\n</url>\n</urlset>", sitemapString);
	}

	@Test
	public void testConstructUrlEmptyPage() {
		String url = sitemapGenerator.constructUrl(new WebPage());
		Assert.assertEquals("<loc>http://www.javavids.com/</loc>\n", url);
	}

	@Test
	public void testConstructUrlNotEmptyPage() {
		String url = sitemapGenerator.constructUrl(new WebPage().setName("latest.php"));
		Assert.assertEquals("<loc>http://www.javavids.com/latest.php</loc>\n", url);
	}

	@Test
	public void testConstructSitemap() throws Exception {
		String sitemap = sitemapGenerator.constructSitemapString();
		ByteArrayInputStream sitemapXml = new ByteArrayInputStream(sitemap.getBytes("UTF-8"));
		TestUtil.testSitemapXsd(sitemapXml, new File("sitemap.xsd"));
	}

	@Test
	public void testSaveSitemap() throws Exception {
		File tmpFile = File.createTempFile("test", "sitemap");
		sitemapGenerator.saveSitemap(tmpFile, sitemapGenerator.constructSitemap());
		try {
			TestUtil.testSitemapXsdFile(tmpFile, new File("sitemap.xsd"));
		} finally {
			tmpFile.delete();
		}
	}

	@Test
	public void testConstructAndSaveSitemap() throws Exception {
		File tmpFile = File.createTempFile("test", "sitemap");
		sitemapGenerator.constructAndSaveSitemap(tmpFile);
		try {
			TestUtil.testSitemapXsdFile(tmpFile, new File("sitemap.xsd"));
		} finally {
			tmpFile.delete();
		}
	}

	@Ignore
	@Test
	public void testPingGoogle() throws Exception {
		sitemapGenerator.pingGoogle("http://jsitemapgenerator.jiripinkas.cz/sitemap.xml");
	}

}
