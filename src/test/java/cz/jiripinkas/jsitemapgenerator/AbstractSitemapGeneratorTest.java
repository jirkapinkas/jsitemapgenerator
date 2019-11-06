package cz.jiripinkas.jsitemapgenerator;

import cz.jiripinkas.jsitemapgenerator.generator.SitemapIndexGenerator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AbstractSitemapGeneratorTest {

    private SitemapIndexGenerator sitemapIndexGenerator;

    @Before
    public void setUp() {
        sitemapIndexGenerator = SitemapIndexGenerator.of("http://javalibs.com");
        sitemapIndexGenerator.addPage(WebPage.builder().name("sitemap-plugins.xml").lastModNow().build());
        sitemapIndexGenerator.addPage(WebPage.builder().name("sitemap-archetypes.xml").lastModNow().build());
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