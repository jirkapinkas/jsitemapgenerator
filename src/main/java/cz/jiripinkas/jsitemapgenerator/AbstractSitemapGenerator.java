package cz.jiripinkas.jsitemapgenerator;

import cz.jiripinkas.jsitemapgenerator.exception.GWTException;
import cz.jiripinkas.jsitemapgenerator.exception.InvalidPriorityException;
import cz.jiripinkas.jsitemapgenerator.exception.InvalidUrlException;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.zip.GZIPOutputStream;

public abstract class AbstractSitemapGenerator <T extends AbstractGenerator> extends AbstractGenerator <T> {

	protected W3CDateFormat dateFormat = new W3CDateFormat();

	private ChangeFreq defaultChangeFreq;

	private Double defaultPriority;

	private String defaultDir;

	private String defaultExtension;

	private Date defaultLastMod;

	private HttpClient httpClient;

	public AbstractSitemapGenerator(String baseUrl) {
		super(baseUrl);
		httpClient = new HttpClient();
	}

	public abstract String[] toStringArray();

	/**
	 * Construct sitemap into single String
	 *
	 * @return sitemap
	 * @deprecated Use {@link #toString()} instead
	 */
	@Deprecated
	public String constructSitemapString() {
		return toString();
	}

	/**
	 * Construct sitemap into single String
	 *
	 * @return sitemap
	 */
	public String toString() {
		String[] sitemapArray = toStringArray();
		StringBuilder result = new StringBuilder();
		for (String line : sitemapArray) {
			result.append(line);
		}
		return result.toString();
	}

	private ByteArrayOutputStream gzipIt(InputStream inputStream) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		try {
			try(GZIPOutputStream gzos = new GZIPOutputStream(outputStream);
				InputStream in = inputStream) {
				int len;
				while ((len = in.read(buffer)) > 0) {
					gzos.write(buffer, 0, len);
				}
			}
		} catch (IOException ex) {
			throw new RuntimeException("Cannot perform gzip", ex);
		}
		return outputStream;
	}

	/**
	 * Construct sitemap into gzipped file
	 *
	 * @return byte array
	 * @deprecated Use {@link #toGzipByteArray()} instead
	 */
	@Deprecated
	public byte[] constructSitemapGzip() {
		return toGzipByteArray();
	}

	/**
	 * Construct sitemap into gzipped file
	 *
	 * @return byte array
	 */
	public byte[] toGzipByteArray() {
		String sitemap = this.toString();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(sitemap.getBytes(StandardCharsets.UTF_8));
		ByteArrayOutputStream outputStream = gzipIt(inputStream);
		return outputStream.toByteArray();
	}

	/**
	 * Save sitemap to output file
	 *
	 * @param file
	 *            Output file
	 * @param sitemap
	 *            Sitemap as array of Strings (created by constructSitemap()
	 *            method)
	 * @throws IOException
	 *             when error
	 * @deprecated Use {@link #toFile(Path)} instead
	 */
	@Deprecated
	public void saveSitemap(File file, String[] sitemap) throws IOException {
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			for (String string : sitemap) {
				writer.write(string);
			}
		}
	}

	/**
	 * Construct and save sitemap to output file
	 *
	 * @param file
	 *            Output file
	 * @throws IOException
	 *             when error
	 */
	public void toFile(File file) throws IOException {
		if (file.exists()) {
			if (file.isDirectory()) {
				throw new IOException("File '" + file + "' exists but is a directory");
			}
			if (!file.canWrite()) {
				throw new IOException("File '" + file + "' cannot be written to");
			}
		} else {
			final File parent = file.getParentFile();
			if (parent != null && (!parent.mkdirs() && !parent.isDirectory())) {
				throw new IOException("Directory '" + parent + "' could not be created");
			}
		}
		String[] sitemap = toStringArray();
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			for (String string : sitemap) {
				writer.write(string);
			}
		}
	}

	/**
	 * Construct and save sitemap to output file
	 *
	 * @param path
	 *            Output file
	 * @throws IOException
	 *             when error
	 */
	public void toFile(Path path) throws IOException {
		toFile(path.toFile());
	}

	/**
	 * Construct and save sitemap to output file
	 *
	 * @param file
	 *            Output file
	 * @throws IOException
	 *             when error
	 * @deprecated Use {@link #toFile(File)} instead
	 */
	@Deprecated
	public void constructAndSaveSitemap(File file) throws IOException {
		toFile(file);
	}

	/**
	 * Construct and save sitemap to output file
	 *
	 * @param path
	 *            Output file
	 * @throws IOException
	 *             when error
	 * @deprecated Use {@link #toFile(Path)} instead
	 */
	@Deprecated
	public void constructAndSaveSitemap(Path path) throws IOException {
		toFile(path);
	}

	/**
	 * Ping Google that sitemap has changed. Will call this URL:
	 * https://www.google.com/ping?sitemap=URL_Encoded_sitemapUrl
	 *
	 * @param sitemapUrl
	 *            sitemap url
	 */
	public void pingGoogle(String sitemapUrl) {
		ping("https://www.google.com/ping?sitemap=", sitemapUrl, "Google");
	}

	/**
	 * Ping Bing that sitemap has changed. Will call this URL:
	 * http://www.bing.com/ping?sitemap=URL_Encoded_sitemapUrl
	 *
	 * @param sitemapUrl
	 *            sitemap url
	 *
	 */
	public void pingBing(String sitemapUrl) {
		ping("http://www.bing.com/ping?sitemap=", sitemapUrl, "Bing");
	}

	private void ping(String resourceUrl, String sitemapUrl, String serviceName) {
		try {
			String pingUrl = resourceUrl + URLEncoder.encode(sitemapUrl, "UTF-8");
			// ping Google / Bing
			int returnCode = httpClient.get(pingUrl);
			if (returnCode != 200) {
				throw new GWTException(serviceName + " could not be informed about new sitemap!");
			}
		} catch (Exception ex) {
			throw new GWTException(serviceName + " could not be informed about new sitemap!");
		}
	}

	/**
	 * Ping Google that sitemap has changed. Sitemap must be on this location:
	 * baseUrl/sitemap.xml (for example http://www.javavids.com/sitemap.xml)
	 */
	public void pingGoogle() {
		pingGoogle(baseUrl + "sitemap.xml");
	}

	/**
	 * Ping Google that sitemap has changed. Sitemap must be on this location:
	 * baseUrl/sitemap.xml (for example http://www.javavids.com/sitemap.xml)
	 */
	public void pingBing() {
		pingBing(baseUrl + "sitemap.xml");
	}

	/**
	 * Escape special characters in XML
	 * @param url Url to be escaped
	 * @return Escaped url
	 */
	protected String escapeXmlSpecialCharacters(String url) {
		// https://stackoverflow.com/questions/1091945/what-characters-do-i-need-to-escape-in-xml-documents
		return url
				.replace("&", "&amp;") // must be escaped first!!!
				.replace("\"", "&quot;")
				.replace("'", "&apos;")
				.replace("<", "&lt;")
				.replace(">", "&gt;");
	}

	@Override
	protected void beforeAddPageEvent(WebPage webPage) {
		if(defaultDir != null && webPage.getDir() == null) {
			webPage.setName(UrlUtil.connectUrlParts(defaultDir, webPage.constructName()));
		}
		if(defaultExtension != null && webPage.getExtension() == null) {
			webPage.setName(webPage.constructName() + "." + defaultExtension);
		}
		if(defaultPriority != null && webPage.getPriority() == null) {
			webPage.setPriority(defaultPriority);
		}
		if(defaultChangeFreq != null && webPage.getChangeFreq() == null) {
			webPage.setChangeFreq(defaultChangeFreq);
		}
		if(defaultLastMod != null && webPage.getLastMod() == null) {
			webPage.setLastMod(defaultLastMod);
		}
	}

	/**
	 * Sets default prefix dir to name for all subsequent WebPages. Final name will be "dirName/name"
	 * @param dirName Dir name
	 * @return this
	 */
	public T defaultDir(String dirName) {
		defaultDir = dirName;
		return getThis();
	}

	/**
	 * Sets default prefix dirs to name for all subsequent WebPages. For dirs: ["a", "b", "c"], the final name will be "a/b/c/name"
	 * @param dirNames Dir names
	 * @return this
	 */
	public T defaultDir(String ... dirNames) {
		defaultDir = String.join("/", dirNames);
		return getThis();
	}

	/**
	 * Reset default dir value
	 * @return this
	 */
	public T resetDefaultDir() {
		defaultDir = null;
		return getThis();
	}

	/**
	 * Sets default suffix extension for all subsequent WebPages. Final name will be "name.extension"
	 * @param extension Extension
	 * @return this
	 */
	public T defaultExtension(String extension) {
		defaultExtension = extension;
		return getThis();
	}

	/**
	 * Reset default extension value
	 * @return this
	 */
	public T resetDefaultExtension() {
		defaultExtension = null;
		return getThis();
	}

	/**
	 * Sets default priority for all subsequent WebPages to maximum (1.0)
	 *
	 * @return this
	 */
	public T defaultPriorityMax() {
		defaultPriority = 1.0;
		return getThis();
	}

	/**
	 * Sets default priority for all subsequent WebPages
	 * @param priority Default priority
	 * @return this
	 */
	public T defaultPriority(Double priority) {
		if (priority < 0.0 || priority > 1.0) {
			throw new InvalidPriorityException("Priority must be between 0 and 1.0");
		}
		defaultPriority = priority;
		return getThis();
	}

	/**
	 * Reset default priority
	 * @return this
	 */
	public T resetDefaultPriority() {
		defaultPriority = null;
		return getThis();
	}

	/**
	 * Sets default changeFreq for all subsequent WebPages
	 *
	 * @param changeFreq ChangeFreq
	 * @return this
	 */
	public T defaultChangeFreq(ChangeFreq changeFreq) {
		defaultChangeFreq = changeFreq;
		return getThis();
	}

	/**
	 * Sets default changeFreq to ALWAYS for all subsequent WebPages
	 *
	 * @return this
	 */
	public T defaultChangeFreqAlways() {
		defaultChangeFreq = ChangeFreq.ALWAYS;
		return getThis();
	}

	/**
	 * Sets default changeFreq to HOURLY for all subsequent WebPages
	 *
	 * @return this
	 */
	public T defaultChangeFreqHourly() {
		defaultChangeFreq = ChangeFreq.HOURLY;
		return getThis();
	}

	/**
	 * Sets default changeFreq to DAILY for all subsequent WebPages
	 *
	 * @return this
	 */
	public T defaultChangeFreqDaily() {
		defaultChangeFreq = ChangeFreq.DAILY;
		return getThis();
	}

	/**
	 * Sets default changeFreq to WEEKLY for all subsequent WebPages
	 *
	 * @return this
	 */
	public T defaultChangeFreqWeekly() {
		defaultChangeFreq = ChangeFreq.WEEKLY;
		return getThis();
	}

	/**
	 * Sets default changeFreq to MONTHLY for all subsequent WebPages
	 *
	 * @return this
	 */
	public T defaultChangeFreqMonthly() {
		defaultChangeFreq = ChangeFreq.MONTHLY;
		return getThis();
	}

	/**
	 * Sets default changeFreq to YEARLY for all subsequent WebPages
	 *
	 * @return this
	 */
	public T defaultChangeFreqYearly() {
		defaultChangeFreq = ChangeFreq.YEARLY;
		return getThis();
	}

	/**
	 * Sets default changeFreq to NEVER for all subsequent WebPages
	 *
	 * @return this
	 */
	public T defaultChangeFreqNever() {
		defaultChangeFreq = ChangeFreq.NEVER;
		return getThis();
	}

	/**
	 * Reset default changeFreq
	 * @return this
	 */
	public T resetDefaultChangeFreq() {
		defaultChangeFreq = null;
		return getThis();
	}

	/**
	 * Sets default lastMod for all subsequent WebPages
	 * @param lastMod lastMod
	 * @return this
	 */
	public T defaultLastMod(Date lastMod) {
		defaultLastMod = lastMod;
		return getThis();
	}

	/**
	 * Sets default lastMod for all subsequent WebPages
	 * @param lastMod lastMod
	 * @return this
	 */
	public T defaultLastMod(LocalDateTime lastMod) {
		defaultLastMod = Timestamp.valueOf(lastMod);
		return getThis();
	}

	/**
	 * Sets default lastMod = new Date() for all subsequent WebPages
	 * @return this
	 */
	public T defaultLastModNow() {
		defaultLastMod = new Date();
		return getThis();
	}

	/**
	 * Reset default lastMod
	 * @return this
	 */
	public T resetDefaultLastMod() {
		defaultLastMod = null;
		return getThis();
	}

	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	/**
	 * Get absolute URL:
	 * If webPageName is null, return baseUrl.
	 * If webPageName is not null, check if webPageName is absolute (can be URL from CDN) or relative URL.
	 * If it's relative URL, prepend baseUrl and return result
	 * @param webPageName WebPageName
	 * @return Correct URL
	 */
	protected String getAbsoluteUrl(String webPageName) {
		try {
			String resultString;
			if (webPageName != null) {
				URI uri = new URI(webPageName);
				String stringUrl;
				if(uri.isAbsolute()) {
					stringUrl = webPageName;
				} else {
					stringUrl = UrlUtil.connectUrlParts(baseUrl, webPageName);
				}
				resultString = stringUrl;
			} else {
				resultString = baseUrl;
			}
			return new URL(resultString).toString();
		} catch (MalformedURLException | URISyntaxException e) {
			throw new InvalidUrlException(e);
		}
	}

}
