package cz.jiripinkas.jsitemapgenerator.generator;

import cz.jiripinkas.jsitemapgenerator.ChangeFreq;
import cz.jiripinkas.jsitemapgenerator.W3CDateFormat;
import cz.jiripinkas.jsitemapgenerator.WebPage;
import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.util.Date;

import static org.junit.Assert.*;

public class SitemapPathTest {

    private DateFormat dateFormat;

    @Before
    public void setUp() {
        dateFormat = new W3CDateFormat();
    }

    @Test
    public void testSitemapPaths() {
        SitemapGenerator sitemapGenerator = new SitemapGenerator("http://www.javavids.com/");
        Date lastModDate = new Date();
        sitemapGenerator.addPage(new WebPage()
                .setName("/index.php")
                .setPriority(1.0)
                .setChangeFreq(ChangeFreq.NEVER)
                .setLastMod(lastModDate));
        sitemapGenerator.addPage(new WebPage().setName("basepath/latest.php"));
        sitemapGenerator.addPage(new WebPage().setName("/basepath/contact.php"));

        String sitemap = sitemapGenerator.constructSitemapString();
        final String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
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
        assertEquals(sitemap, expected);
    }

    @Test
    public void testSitemapPaths2() {
        SitemapGenerator sitemapGenerator = new SitemapGenerator("http://www.javavids.com/");
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
        sitemapGenerator.addPage(WebPage.builder().name("x2").build());
        sitemapGenerator.addPage(WebPage.builder().name("a2").build());

        String sitemap = sitemapGenerator.constructSitemapString();
        final String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
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
        assertEquals(sitemap, expected);
    }

}
