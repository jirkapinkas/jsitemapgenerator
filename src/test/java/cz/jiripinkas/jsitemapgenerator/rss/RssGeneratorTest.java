package cz.jiripinkas.jsitemapgenerator.rss;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import cz.jiripinkas.jsitemapgenerator.RssItemBuilder;
import cz.jiripinkas.jsitemapgenerator.util.TestUtil;

public class RssGeneratorTest {

	private RssGenerator rssGenerator;

	@Before
	public void setUp() throws Exception {
		rssGenerator = new RssGenerator("http://www.topjavablogs.com", "Top Java Blogs", "News from Java community");
	}

	@Test
	public void testConstructRssEmptyItemsShouldThrowException() throws SAXException, IOException {
		try {
			String rss = rssGenerator.constructRss();
			ByteArrayInputStream xml = new ByteArrayInputStream(rss.getBytes("UTF-8"));
			TestUtil.testSitemapXsd(xml, new File("rss20.xsd"));
		} catch (Exception e) {
			Assert.assertEquals("cvc-complex-type.2.4.b: The content of element 'channel' is not complete. One of '{image, textInput, skipHours, skipDays, item}' is expected.", e.getMessage());
		}
	}

	@Test
	public void testConstructRssWithItems() throws SAXException, IOException {
		rssGenerator.addPage(new RssItemBuilder().name("latest-news").description("description").pubDate(new Date()).title("latest news").build());
		String rss = rssGenerator.constructRss();
		ByteArrayInputStream xml = new ByteArrayInputStream(rss.getBytes("UTF-8"));
		TestUtil.testSitemapXsd(xml, new File("rss20.xsd"));
		System.out.println(rss);
	}

}
