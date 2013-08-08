package com.javavids.jsitemapgenerator;

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
import org.junit.Test;
import org.xml.sax.SAXException;

import com.javavids.jsitemapgenerator.WebSitemapGenerator;
import com.javavids.jsitemapgenerator.WebSitemapUrl;

public class WebSitemapGeneratorTest {

	private WebSitemapGenerator webSitemapGenerator;

	@Before
	public void setUp() throws Exception {
		webSitemapGenerator = new WebSitemapGenerator("http://www.javavids.com");
		webSitemapGenerator.addUrl(new WebSitemapUrl().setUrl("http://www.javavids.com").setPriority(1.0).setChangeFreq(ChangeFreq.NEVER).setLastMod(new Date()));
		webSitemapGenerator.addUrl(new WebSitemapUrl().setUrl("http://www.javavids.com/latest.php"));
		webSitemapGenerator.addUrl(new WebSitemapUrl().setUrl("http://www.javavids.com/contact.php"));
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
		String[] sitemap = webSitemapGenerator.constructSitemap();
		
		for (String string : sitemap) {
			System.out.println(string);
		}

		StringBuilder sb = new StringBuilder();
		for (String s : sitemap) {
			sb.append(s);
		}
		ByteArrayInputStream sitemapXml = new ByteArrayInputStream(sb.toString().getBytes("UTF-8"));
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

	@Test
	public void testPingGoogle() throws Exception {
		webSitemapGenerator.pingGoogle("http://www.javavids.com/sitemap.xml");
	}

}
