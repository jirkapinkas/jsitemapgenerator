package cz.jiripinkas.jsitemapgenerator.generator;

import cz.jiripinkas.jsitemapgenerator.HttpClient;
import cz.jiripinkas.jsitemapgenerator.Image;
import cz.jiripinkas.jsitemapgenerator.WebPage;
import cz.jiripinkas.jsitemapgenerator.exception.GWTException;
import cz.jiripinkas.jsitemapgenerator.util.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class SitemapGeneratorTest {

	private SitemapGenerator sitemapGenerator;

	@Before
	public void setUp() {
		sitemapGenerator = SitemapGenerator.of("http://www.javavids.com");
		sitemapGenerator.addPage(WebPage.builder().name("index.php").priority(1.0).changeFreqNever().lastMod(LocalDateTime.of(2019, 1, 1, 0, 0)).build());
		sitemapGenerator.addPage(WebPage.builder().name("latest.php").build());
		sitemapGenerator.addPage(WebPage.builder().name("contact.php").build());
	}

	@Test
	public void testConstructImage() {
		Image image = new Image();
		image.setLoc("http://example.com/image");
		String imageString = sitemapGenerator.constructImage(image);
		assertEquals("<image:image>\n<image:loc>http://example.com/image</image:loc>\n</image:image>\n", imageString);
	}

	@Test
	public void testConstructImageWithBaseUrl() {
		Image image = new Image();
		image.setLoc("/image");
		String imageString = sitemapGenerator.constructImage(image);
		assertEquals("<image:image>\n<image:loc>http://www.javavids.com/image</image:loc>\n</image:image>\n", imageString);
	}

	@Test
	public void testConstructSitemapWithImagesHeader() {
		SitemapGenerator sitemapGenerator = SitemapGenerator.of("http://www.javavids.com");
		sitemapGenerator.addPage(WebPage.builder().images(new ArrayList<>()).build());
		String sitemapString = sitemapGenerator.toString();
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
				"<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" xmlns:image=\"http://www.google.com/schemas/sitemap-image/1.1\" >\n" +
				"<url>\n" +
				"<loc>http://www.javavids.com/</loc>\n" +
				"</url>\n" +
				"</urlset>";
		assertEquals(expected, sitemapString);
	}
	
	@Test
	public void testConstructSitemapWithImages() {
		sitemapGenerator = SitemapGenerator.of("http://www.javavids.com");
		WebPage webPage = WebPage.builder().nameRoot().build();
		webPage.addImage(WebPage.imageBuilder().loc("http://www.javavids.com/favicon.ico").build());
		sitemapGenerator.addPage(webPage);
		String sitemapString = sitemapGenerator.toString();
		assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" xmlns:image=\"http://www.google.com/schemas/sitemap-image/1.1\" >\n<url>\n<loc>http://www.javavids.com/</loc>\n<image:image>\n<image:loc>http://www.javavids.com/favicon.ico</image:loc>\n</image:image>\n</url>\n</urlset>", sitemapString);
	}

	@Test
	public void testConstructUrlEmptyPage() {
		String url = sitemapGenerator.constructUrl(new WebPage());
		assertEquals("<loc>http://www.javavids.com/</loc>\n", url);
	}

	@Test
	public void testConstructUrlNotEmptyPage() {
		String url = sitemapGenerator.constructUrl(WebPage.builder().name("latest.php").build());
		assertEquals("<loc>http://www.javavids.com/latest.php</loc>\n", url);
	}

	@Test
	public void testConstructAlternateUrls() {
		String url = SitemapGenerator.of("http://www.javavids.com")
				.constructUrl(WebPage.builder()
				.name("latest.php")
				.alternateName("de", "latest-de.php")
				.alternateName("es", "latest-es.php")
				.build());
		assertEquals("<loc>http://www.javavids.com/latest.php</loc>\n<xhtml:link rel=\"alternate\" hreflang=\"de\" href=\"http://www.javavids.com/latest-de.php\"/>\n" +
				"<xhtml:link rel=\"alternate\" hreflang=\"es\" href=\"http://www.javavids.com/latest-es.php\"/>\n", url);
	}

	@Test
	public void testConstructAlternateUrls2() {
		String url = SitemapGenerator.of("http://www.javavids.com").constructUrl(WebPage.builder()
				.name("latest.php")
				.alternateName("de", () -> "latest-de.php")
				.alternateName("es", () -> "latest-es.php")
				.build());
		assertEquals("<loc>http://www.javavids.com/latest.php</loc>\n<xhtml:link rel=\"alternate\" hreflang=\"de\" href=\"http://www.javavids.com/latest-de.php\"/>\n" +
				"<xhtml:link rel=\"alternate\" hreflang=\"es\" href=\"http://www.javavids.com/latest-es.php\"/>\n", url);
	}

	@Test
	public void testConstructSitemap() throws Exception {
		String sitemap = sitemapGenerator.toString();
		ByteArrayInputStream sitemapXml = new ByteArrayInputStream(sitemap.getBytes(StandardCharsets.UTF_8));
		TestUtil.testSitemapXsd(sitemapXml, new File("src/test/resources/sitemap.xsd"));
	}

	@Test
	public void testSaveSitemap() throws Exception {
		File tmpFile = File.createTempFile("test", "sitemap");
		sitemapGenerator.toFile(tmpFile);
		String actualSitemap = String.join("\n", Files.readAllLines(tmpFile.toPath()));
		String expectedSitemap = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
				"<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n" +
				"<url>\n" +
				"<loc>http://www.javavids.com/index.php</loc>\n" +
				"<lastmod>2019-01-01</lastmod>\n" +
				"<changefreq>never</changefreq>\n" +
				"<priority>1.0</priority>\n" +
				"</url>\n" +
				"<url>\n" +
				"<loc>http://www.javavids.com/contact.php</loc>\n" +
				"</url>\n" +
				"<url>\n" +
				"<loc>http://www.javavids.com/latest.php</loc>\n" +
				"</url>\n" +
				"</urlset>";
		assertEquals(expectedSitemap, actualSitemap);
		try {
			TestUtil.testSitemapXsdFile(tmpFile, new File("src/test/resources/sitemap.xsd"));
		} finally {
			tmpFile.delete();
		}
	}

	@Test
	public void testConstructAndSaveSitemap() throws Exception {
		File tmpFile = File.createTempFile("test", "sitemap");
		sitemapGenerator.toFile(tmpFile);
		try {
			TestUtil.testSitemapXsdFile(tmpFile, new File("src/test/resources/sitemap.xsd"));
		} finally {
			tmpFile.delete();
		}
	}

	@Test
	public void test() {
		String sitemap = SitemapGenerator.of("https://javalibs.com")
				.defaultDir("dir1")
				.defaultChangeFreqWeekly()
				.addPage(WebPage.of("a"))
				.addPage(WebPage.of("b"))
				.defaultDir("dir2")
				.defaultChangeFreqYearly()
				.addPage(WebPage.of("x"))
				.addPage(WebPage.of("y"))
				.addPage(WebPage.builder().dir("dir3").name("z").changeFreqNever().build())
				.toString();
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
				"<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n" +
				"<url>\n" +
				"<loc>https://javalibs.com/dir1/a</loc>\n" +
				"<changefreq>weekly</changefreq>\n" +
				"</url>\n" +
				"<url>\n" +
				"<loc>https://javalibs.com/dir1/b</loc>\n" +
				"<changefreq>weekly</changefreq>\n" +
				"</url>\n" +
				"<url>\n" +
				"<loc>https://javalibs.com/dir2/x</loc>\n" +
				"<changefreq>yearly</changefreq>\n" +
				"</url>\n" +
				"<url>\n" +
				"<loc>https://javalibs.com/dir2/y</loc>\n" +
				"<changefreq>yearly</changefreq>\n" +
				"</url>\n" +
				"<url>\n" +
				"<loc>https://javalibs.com/dir3/z</loc>\n" +
				"<changefreq>never</changefreq>\n" +
				"</url>\n" +
				"</urlset>";
		assertEquals(expected, sitemap);
	}

//	@Rule
//	public MockitoRule mockitoRule = MockitoJUnit.rule();

	@Test
	public void testPingGoogleSuccess() throws Exception {
		HttpClient httpClientMock = Mockito.mock(HttpClient.class);
		SitemapGenerator sitemapGenerator = SitemapGenerator.of("https://www.example.com/");
		sitemapGenerator.setHttpClient(httpClientMock);
		Mockito.when(httpClientMock.get(Mockito.anyString()))
				.thenReturn(500);
		Mockito.when(httpClientMock.get("https://www.google.com/ping?sitemap=https%3A%2F%2Fwww.example.com%2Fsitemap.xml"))
				.thenReturn(200);
		sitemapGenerator.pingGoogle();
		Mockito.verify(httpClientMock).get("https://www.google.com/ping?sitemap=https%3A%2F%2Fwww.example.com%2Fsitemap.xml");
	}

	@Test(expected = GWTException.class)
	public void testPingGoogleError() throws Exception {
		HttpClient httpClientMock = Mockito.mock(HttpClient.class);
		SitemapGenerator sitemapGenerator = SitemapGenerator.of("https://www.example.com/");
		sitemapGenerator.setHttpClient(httpClientMock);
		Mockito.when(httpClientMock.get(Mockito.anyString()))
				.thenReturn(500);
//		Mockito.doReturn(500).when(httpClientMock).get(Mockito.any());
		sitemapGenerator.pingGoogle();
	}

	@Test
	public void testRemoveRedundantSlashes1() {
		String actualSitemap = SitemapGenerator.of("https://javalibs.com/")
				.addPage(WebPage.of("/test"))
				.toString();
		String expectedSitemap = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
				"<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n" +
				"<url>\n" +
				"<loc>https://javalibs.com/test</loc>\n" +
				"</url>\n" +
				"</urlset>";
		assertEquals(expectedSitemap, actualSitemap);
	}

	@Test
	public void testRemoveRedundantSlashes2() {
		String actualSitemap = SitemapGenerator.of("https://javalibs.com/")
				.defaultDir("testDir")
				.addPage(WebPage.of("/testPage"))
				.toString();
		String expectedSitemap = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
				"<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n" +
				"<url>\n" +
				"<loc>https://javalibs.com/testDir/testPage</loc>\n" +
				"</url>\n" +
				"</urlset>";
		assertEquals(expectedSitemap, actualSitemap);
	}

}
