package cz.jiripinkas.jsitemapgenerator.generator;

import cz.jiripinkas.jsitemapgenerator.WebPage;
import cz.jiripinkas.jsitemapgenerator.util.TestUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.Assert.*;

public class RssGeneratorTest {

    private RssGenerator rssGenerator;

    @Before
    public void setUp() {
        rssGenerator = RssGenerator.of("http://www.topjavablogs.com", "Top Java Blogs", "News from Java community");
    }

    @Test
    public void testConstructRssEmptyItemsShouldThrowException() {
        try {
            String rss = rssGenerator.toString();
            ByteArrayInputStream xml = new ByteArrayInputStream(rss.getBytes(StandardCharsets.UTF_8));
            TestUtil.testSitemapXsd(xml, new File("rss20.xsd"));
        } catch (Exception e) {
            assertEquals("cvc-complex-type.2.4.b: The content of element 'channel' is not complete. One of '{image, textInput, skipHours, skipDays, item}' is expected.", e.getMessage());
        }
    }

    @Test
    public void testConstructRssWithItems() throws SAXException, IOException {
        rssGenerator.addPage(WebPage.rssBuilder()
                .title("latest news")
                .description("description")
                .link("latest-news")
                .pubDate(new Date())
                .build()
        );
        String rss = rssGenerator.toString();
        ByteArrayInputStream xml = new ByteArrayInputStream(rss.getBytes(StandardCharsets.UTF_8));
        TestUtil.testSitemapXsd(xml, new File("rss20.xsd"));
    }

    @Test
    public void testConstructRssWithItemsLocalDateTime() {
        String rss = RssGenerator.of("http://www.topjavablogs.com", "Top Java Blogs", "News from Java community")
                .addPage(WebPage.rssBuilder()
                        .title("latest news")
                        .description("description")
                        .link("latest-news")
                        .pubDate(LocalDateTime.of(2000, 1, 1, 1, 0))
                        .build())
                .toString();
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "<rss version=\"2.0\">\n" +
                "<channel>\n" +
                "<title>Top Java Blogs</title>\n" +
                "<link>http://www.topjavablogs.com/</link>\n" +
                "<description>News from Java community</description>\n" +
                "<pubDate>Sat, 1 Jan 2000 01:00:00 +0000</pubDate>\n" +
                "<lastBuildDate>Sat, 1 Jan 2000 01:00:00 +0000</lastBuildDate>\n" +
                "<ttl>1800</ttl>\n" +
                "<item>\n" +
                "<title>latest news</title>\n" +
                "<description>description</description>\n" +
                "<link>http://www.topjavablogs.com/latest-news</link>\n" +
                "<pubDate>Sat, 1 Jan 2000 01:00:00 +0000</pubDate>\n" +
                "</item>\n" +
                "</channel>\n" +
                "</rss>\n";
        assertEquals(expected, rss);
    }

    @Test
    public void testConstructRssWithItemsLocalDateTimeWithRedundantSlash() {
        String rss = RssGenerator.of("http://www.topjavablogs.com", "Top Java Blogs", "News from Java community")
                .addPage(WebPage.rssBuilder()
                        .title("latest news")
                        .description("description")
                        .link("/latest-news")
                        .pubDate(LocalDateTime.of(2000, 1, 1, 1, 0))
                        .build())
                .toString();
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "<rss version=\"2.0\">\n" +
                "<channel>\n" +
                "<title>Top Java Blogs</title>\n" +
                "<link>http://www.topjavablogs.com/</link>\n" +
                "<description>News from Java community</description>\n" +
                "<pubDate>Sat, 1 Jan 2000 01:00:00 +0000</pubDate>\n" +
                "<lastBuildDate>Sat, 1 Jan 2000 01:00:00 +0000</lastBuildDate>\n" +
                "<ttl>1800</ttl>\n" +
                "<item>\n" +
                "<title>latest news</title>\n" +
                "<description>description</description>\n" +
                "<link>http://www.topjavablogs.com/latest-news</link>\n" +
                "<pubDate>Sat, 1 Jan 2000 01:00:00 +0000</pubDate>\n" +
                "</item>\n" +
                "</channel>\n" +
                "</rss>\n";
        assertEquals(expected, rss);
    }

}
