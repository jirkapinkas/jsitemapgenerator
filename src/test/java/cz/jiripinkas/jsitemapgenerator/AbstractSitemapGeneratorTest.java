package cz.jiripinkas.jsitemapgenerator;

import cz.jiripinkas.jsitemapgenerator.generator.SitemapIndexGenerator;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class AbstractSitemapGeneratorTest {

    private SitemapIndexGenerator sitemapIndexGenerator;

    @Before
    public void setUp() {
        sitemapIndexGenerator = SitemapIndexGenerator.of("http://javalibs.com");
        sitemapIndexGenerator.addPage(WebPage.builder().name("sitemap-plugins.xml").lastMod(LocalDateTime.of(2018, 1, 1, 0, 0)).build());
        sitemapIndexGenerator.addPage(WebPage.builder().name("sitemap-archetypes.xml").lastMod(LocalDateTime.of(2018, 1, 1, 0, 0)).build());
    }

    @Test
    public void toPrettyString() {
        String actualSitemapIndex = sitemapIndexGenerator.toPrettyString(2);
        String expectedSitemapIndex = "<sitemapindex xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n" +
                "  <sitemap>\n" +
                "    <loc>http://javalibs.com/sitemap-archetypes.xml</loc>\n" +
                "    <lastmod>2018-01-01</lastmod>\n" +
                "  </sitemap>\n" +
                "  <sitemap>\n" +
                "    <loc>http://javalibs.com/sitemap-plugins.xml</loc>\n" +
                "    <lastmod>2018-01-01</lastmod>\n" +
                "  </sitemap>\n" +
                "</sitemapindex>\n";
        assertEquals(expectedSitemapIndex, actualSitemapIndex);
    }

    @Test
    public void getAbsoluteUrlRelativeCheck() {
        String absoluteUrl = sitemapIndexGenerator.getAbsoluteUrl("relativeUrl");
        assertEquals("http://javalibs.com/relativeUrl", absoluteUrl);
    }

    @Test
    public void getAbsoluteUrlAbsoluteCheck() {
        String absoluteUrl = sitemapIndexGenerator.getAbsoluteUrl("https://cdn.com");
        assertEquals("https://cdn.com", absoluteUrl);
    }

    @Test
    public void getAbsoluteUrlBaseUrlCheck() {
        String absoluteUrl = sitemapIndexGenerator.getAbsoluteUrl(null);
        assertEquals("http://javalibs.com/", absoluteUrl);
    }
}