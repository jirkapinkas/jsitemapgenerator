package cz.jiripinkas.jsitemapgenerator.generator;

import cz.jiripinkas.jsitemapgenerator.W3CDateFormat;
import cz.jiripinkas.jsitemapgenerator.WebPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class SitemapPathTest {

    private DateFormat dateFormat;

    @BeforeEach
    void setUp() {
        dateFormat = new W3CDateFormat();
    }

    @Test
    void testSitemapPaths() {
        SitemapGenerator sitemapGenerator = SitemapGenerator.of("http://www.javavids.com/");
        Date lastModDate = new Date();
        sitemapGenerator.addPage(WebPage.builder()
                .name("/index.php")
                .priority(1.0)
                .changeFreqNever()
                .lastMod(lastModDate)
                .build());
        sitemapGenerator.addPage(WebPage.of("basepath", "latest.php"));
        sitemapGenerator.addPage(WebPage.of("basepath", "contact.php"));

        String actualSitemap = sitemapGenerator.toString();
        final String expectedSitemap = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n"
                + "<url>\n"
                + "<loc>http://www.javavids.com/index.php</loc>\n"
                + "<lastmod>" + dateFormat.format(lastModDate) + "</lastmod>\n"
                + "<changefreq>never</changefreq>\n"
                + "<priority>1.0</priority>\n"
                + "</url>\n"
                + "<url>\n"
                + "<loc>http://www.javavids.com/basepath/contact.php</loc>\n"
                + "</url>\n" + "<url>\n"
                + "<loc>http://www.javavids.com/basepath/latest.php</loc>\n"
                + "</url>\n"
                + "</urlset>";
        assertEquals(expectedSitemap, actualSitemap);
    }

    @Test
    void testSitemapPaths2() {
        SitemapGenerator sitemapGenerator = SitemapGenerator.of("http://www.javavids.com/");
        sitemapGenerator.addPage(WebPage.builder()
                .name("/x0")
                .priority(1.0)
                .build());
        sitemapGenerator.addPage(WebPage.builder()
                .name("/a0")
                .priority(1.0)
                .build());
        sitemapGenerator.addPage(WebPage.builder()
                .name("/x1")
                .priority(0.5)
                .build());
        sitemapGenerator.addPage(WebPage.builder()
                .name("/a1")
                .priority(0.5)
                .build());
        sitemapGenerator.addPage(WebPage.of("x2"));
        sitemapGenerator.addPage(WebPage.of("a2"));

        String actualSitemap = sitemapGenerator.toString();
        final String expectedSitemap = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n" +
                "<url>\n" +
                "<loc>http://www.javavids.com/a0</loc>\n" +
                "<priority>1.0</priority>\n" +
                "</url>\n" +
                "<url>\n" +
                "<loc>http://www.javavids.com/x0</loc>\n" +
                "<priority>1.0</priority>\n" +
                "</url>\n" +
                "<url>\n" +
                "<loc>http://www.javavids.com/a1</loc>\n" +
                "<priority>0.5</priority>\n" +
                "</url>\n" +
                "<url>\n" +
                "<loc>http://www.javavids.com/x1</loc>\n" +
                "<priority>0.5</priority>\n" +
                "</url>\n" +
                "<url>\n" +
                "<loc>http://www.javavids.com/a2</loc>\n" +
                "</url>\n" +
                "<url>\n" +
                "<loc>http://www.javavids.com/x2</loc>\n" +
                "</url>\n" +
                "</urlset>";
        assertEquals(expectedSitemap, actualSitemap);
    }

    @Test
    void testSitemapPathWithSpecialCharacters() {
        SitemapGenerator sitemapGenerator = SitemapGenerator.of("http://www.javavids.com/");
        sitemapGenerator.addPage(WebPage.of("/page?arg1='test'&arg2=<test>&arg3=\"test\""));

        String actualSitemap = sitemapGenerator.toString();
        String expectedSitemap =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n" +
                "<url>\n" +
                "<loc>http://www.javavids.com/page?arg1=&apos;test&apos;&amp;arg2=&lt;test&gt;&amp;arg3=&quot;test&quot;</loc>\n" +
                "</url>\n" +
                "</urlset>";
        assertEquals(expectedSitemap, actualSitemap);
    }

}
