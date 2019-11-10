package cz.jiripinkas.jsitemapgenerator.generator;

import cz.jiripinkas.jsitemapgenerator.WebPage;
import cz.jiripinkas.jsitemapgenerator.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class RssGeneratorTest {

    private RssGenerator rssGenerator;

    @BeforeEach
    void setUp() {
        rssGenerator = RssGenerator.of("http://www.topjavablogs.com", "Top Java Blogs", "News from Java community");
    }

    @Test
    void testConstructRssEmptyItemsShouldThrowException() {
        try {
            String rss = rssGenerator.toString();
            ByteArrayInputStream xml = new ByteArrayInputStream(rss.getBytes(StandardCharsets.UTF_8));
            TestUtil.testSitemapXsd(xml, new File("src/test/resources/rss20.xsd"));
        } catch (Exception e) {
            assertEquals("cvc-complex-type.2.4.b: The content of element 'channel' is not complete. One of '{image, textInput, skipHours, skipDays, item}' is expected.", e.getMessage());
        }
    }

    @Test
    void testConstructRssWithItems() throws SAXException, IOException {
        rssGenerator.addPage(WebPage.rssBuilder()
                .title("latest news")
                .description("description")
                .link("latest-news")
                .pubDate(new Date())
                .build()
        );
        String rss = rssGenerator.toString();
        ByteArrayInputStream xml = new ByteArrayInputStream(rss.getBytes(StandardCharsets.UTF_8));
        TestUtil.testSitemapXsd(xml, new File("src/test/resources/rss20.xsd"));
    }

    @Test
    void testConstructRssWithItemsLocalDateTime() {
        String actualRss = RssGenerator.of("http://www.topjavablogs.com", "Top Java Blogs", "News from Java community")
                .addPage(WebPage.rssBuilder()
                        .title("latest news")
                        .description("description")
                        .link("latest-news")
                        .pubDate(LocalDateTime.of(2000, 1, 1, 1, 0))
                        .build())
                .toString();
        String expectedRss = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
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
        assertEquals(expectedRss, actualRss);
    }

    @Test
    void testConstructRssWithItemsLocalDateTimeWithRedundantSlash() {
        String actualRss = RssGenerator.of("http://www.topjavablogs.com", "Top Java Blogs", "News from Java community")
                .addPage(WebPage.rssBuilder()
                        .title("latest news")
                        .description("description")
                        .link("/latest-news")
                        .pubDate(LocalDateTime.of(2000, 1, 1, 1, 0))
                        .build())
                .toString();
        String expectedRss = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
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
        assertEquals(expectedRss, actualRss);
    }

}
