package cz.jiripinkas.jsitemapgenerator;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

import cz.jiripinkas.jsitemapgenerator.sitemap.WebSitemapGenerator;

public class WebSitemapGeneratorTest {

	private WebSitemapGenerator webSitemapGenerator;

	@Before
	public void setUp() throws Exception {
		webSitemapGenerator = new WebSitemapGenerator("http://www.javavids.com");
		webSitemapGenerator.addPage(new WebPage().setName("index.php").setPriority(1.0).setChangeFreq(ChangeFreq.NEVER).setLastMod(new Date()));
		webSitemapGenerator.addPage(new WebPage().setName("latest.php"));
		webSitemapGenerator.addPage(new WebPage().setName("contact.php"));
	}

	private void testSitemapXsd(InputStream sitemapXml) throws SAXException, IOException {
		Source xmlFile = new StreamSource(sitemapXml);
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = schemaFactory.newSchema(new File("sitemap.xsd"));
		Validator validator = schema.newValidator();
		validator.validate(xmlFile);
	}

	private void testSitemapXsdFile(File file) throws IOException, SAXException {
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
			testSitemapXsd(inputStream);
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
	}

	@Test
	public void testConstructSitemap() throws Exception {
		String sitemap = webSitemapGenerator.constructSitemapString();
		ByteArrayInputStream sitemapXml = new ByteArrayInputStream(sitemap.getBytes("UTF-8"));
		testSitemapXsd(sitemapXml);
	}

	@Test
	public void testSaveSitemap() throws Exception {
		File tmpFile = File.createTempFile("test", "sitemap");
		webSitemapGenerator.saveSitemap(tmpFile, webSitemapGenerator.constructSitemap());
		try {
			testSitemapXsdFile(tmpFile);
		} finally {
			tmpFile.delete();
		}
	}

	@Test
	public void testConstructAndSaveSitemap() throws Exception {
		File tmpFile = File.createTempFile("test", "sitemap");
		webSitemapGenerator.constructAndSaveSitemap(tmpFile);
		try {
			testSitemapXsdFile(tmpFile);
		} finally {
			tmpFile.delete();
		}
	}
	
	@Ignore
	@Test
	public void testPingGoogle() throws Exception {
		webSitemapGenerator.pingGoogle("http://jsitemapgenerator.jiripinkas.cz/sitemap.xml");
	}


}
