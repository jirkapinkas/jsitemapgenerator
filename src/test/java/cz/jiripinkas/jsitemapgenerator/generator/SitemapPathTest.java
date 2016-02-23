package cz.jiripinkas.jsitemapgenerator.generator;

import java.text.DateFormat;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cz.jiripinkas.jsitemapgenerator.ChangeFreq;
import cz.jiripinkas.jsitemapgenerator.W3CDateFormat;
import cz.jiripinkas.jsitemapgenerator.WebPage;
import cz.jiripinkas.jsitemapgenerator.generator.SitemapGenerator;

public class SitemapPathTest {

    private SitemapGenerator sitemapGenerator;
    private DateFormat dateFormat = new W3CDateFormat();
    private Date lastModDate;

    @Before
    public void setUp() throws Exception {
        sitemapGenerator = new SitemapGenerator("http://www.javavids.com/");
        lastModDate = new Date();
        sitemapGenerator.addPage(new WebPage().setName("/index.php").setPriority(1.0).setChangeFreq(ChangeFreq.NEVER)
                .setLastMod(lastModDate));
        sitemapGenerator.addPage(new WebPage().setName("basepath/latest.php"));
        sitemapGenerator.addPage(new WebPage().setName("/basepath/contact.php"));
    }

    @Test
    public void testSitemapPaths() {
        String sitemap = sitemapGenerator.constructSitemapString();
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
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
        Assert.assertEquals(sitemap, expected);
    }

}
