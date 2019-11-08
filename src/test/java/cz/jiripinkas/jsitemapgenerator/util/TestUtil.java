package cz.jiripinkas.jsitemapgenerator.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

public class TestUtil {

	public static void testSitemapXsd(InputStream sitemapXml, File xsd) throws SAXException, IOException {
		Source xmlFile = new StreamSource(sitemapXml);
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = schemaFactory.newSchema(xsd);
		Validator validator = schema.newValidator();
		validator.validate(xmlFile);
	}

	public static void testSitemapXsdFile(File file, File xsd) throws IOException, SAXException {
		try (FileInputStream inputStream = new FileInputStream(file)) {
			testSitemapXsd(inputStream, xsd);
		}
	}

}
