package cz.jiripinkas.jsitemapgenerator.generator;

import cz.jiripinkas.jsitemapgenerator.Image;
import cz.jiripinkas.jsitemapgenerator.WebPage;
import cz.jiripinkas.jsitemapgenerator.generator.SitemapGenerator.AdditionalNamespace;
import cz.jiripinkas.jsitemapgenerator.util.TestUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class SitemapGeneratorTest {

	private SitemapGenerator sitemapGenerator;

	@Before
	public void setUp() {
		sitemapGenerator = SitemapGenerator.of("http://www.javavids.com");
		sitemapGenerator.addPage(WebPage.builder().name("index.php").priority(1.0).changeFreqNever().lastMod(new Date()).build());
		sitemapGenerator.addPage(WebPage.builder().name("latest.php").build());
		sitemapGenerator.addPage(WebPage.builder().name("contact.php").build());
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
		sitemapGenerator = SitemapGenerator.of("http://www.javavids.com", new AdditionalNamespace[] { AdditionalNamespace.IMAGE });
		String sitemapString = sitemapGenerator.constructSitemapString();
		Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" xmlns:image=\"http://www.google.com/schemas/sitemap-image/1.1\" >\n</urlset>", sitemapString);
	}
	
	@Test
	public void testConstructSitemapWithImages() {
		sitemapGenerator = SitemapGenerator.of("http://www.javavids.com", new AdditionalNamespace[] { AdditionalNamespace.IMAGE });
		WebPage webPage = WebPage.builder().nameRoot().build();
		webPage.addImage(WebPage.imageBuilder().loc("http://www.javavids.com/favicon.ico").build());
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
		String url = sitemapGenerator.constructUrl(WebPage.builder().name("latest.php").build());
		Assert.assertEquals("<loc>http://www.javavids.com/latest.php</loc>\n", url);
	}

	@Test
	public void testConstructAlternateUrls() {
		String url = sitemapGenerator.constructUrl(WebPage.builder()
				.name("latest.php")
				.alternateName("de", "latest-de.php")
				.alternateName("es", "latest-es.php")
				.build());
		Assert.assertEquals("<loc>http://www.javavids.com/latest.php</loc>\n<xhtml:link rel=\"alternate\" hreflang=\"de\" href=\"http://www.javavids.com/latest-de.php\"/>\n" +
				"<xhtml:link rel=\"alternate\" hreflang=\"es\" href=\"http://www.javavids.com/latest-es.php\"/>\n", url);
	}

	@Test
	public void testConstructAlternateUrls2() {
		String url = sitemapGenerator.constructUrl(WebPage.builder()
				.name("latest.php")
				.alternateName("de", () -> "latest-de.php")
				.alternateName("es", () -> "latest-es.php")
				.build());
		Assert.assertEquals("<loc>http://www.javavids.com/latest.php</loc>\n<xhtml:link rel=\"alternate\" hreflang=\"de\" href=\"http://www.javavids.com/latest-de.php\"/>\n" +
				"<xhtml:link rel=\"alternate\" hreflang=\"es\" href=\"http://www.javavids.com/latest-es.php\"/>\n", url);
	}

	@Test
	public void testConstructSitemap() throws Exception {
		String sitemap = sitemapGenerator.constructSitemapString();
		ByteArrayInputStream sitemapXml = new ByteArrayInputStream(sitemap.getBytes(StandardCharsets.UTF_8));
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
