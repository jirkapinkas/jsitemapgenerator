<h1>Java sitemap generator</h1>

<p>
	This library generates a web sitemap and can ping Google that it has
	changed. This project has been inspired by <a
		href="https://code.google.com/p/sitemapgen4j/" target="_blank">sitemapgen4j</a>,
	but is much more focused on traditional web sitemap and ease of use.
</p>

<h2>Typical usage:</h2>

<p>First add this library to classpath:</p>

<pre>
<code>
&lt;dependency&gt;
  &lt;groupId&gt;cz.jiripinkas&lt;/groupId&gt;
  &lt;artifactId&gt;jsitemapgenerator&lt;/artifactId&gt;
  &lt;version&gt;2.0&lt;/version&gt;
&lt;/dependency&gt;
</code>
</pre>

<p>If you want to use "ping google / bing" functionality, also add this library to classpath:</p>

<pre>
<code>
&lt;dependency&gt;
  &lt;groupId&gt;org.apache.httpcomponents&lt;/groupId&gt;
  &lt;artifactId&gt;httpclient&lt;/artifactId&gt;
  &lt;version&gt;4.2.2&lt;/version&gt;
&lt;/dependency&gt;
</code>
</pre>

<h3>How to create a sitemap:</h3>


<pre>
<code>
// create web sitemap for web http://www.javavids.com
SitemapGenerator sitemapGenerator = new SitemapGenerator("http://www.javavids.com");
// add some URLs
sitemapGenerator.addPage(new WebPageBuilder().name("index.php")
   .priorityMax().changeFreqNever().lastModNow().build());
sitemapGenerator.addPage(new WebPageBuilder().name("latest.php").build());
sitemapGenerator.addPage(new WebPageBuilder().name("contact.php").build());
// generate sitemap and save it to file /var/www/sitemap.xml
File file = new File("/var/www/sitemap.xml");
sitemapGenerator.constructAndSaveSitemap(file);
// inform Google that this sitemap has changed
sitemapGenerator.pingGoogle();
</code>
</pre>

<h3>How to create a sitemap index:</h3>

<pre>
<code>
SitemapIndexGenerator sitemapIndexGenerator = new SitemapIndexGenerator("http://javalibs.com");
sitemapIndexGenerator.addPage(new WebPageBuilder().name("sitemap-plugins.xml").build());
sitemapIndexGenerator.addPage(new WebPageBuilder().name("sitemap-archetypes.xml").build());
System.out.println(sitemapIndexGenerator.constructSitemapString());
</code>
</pre>

<h3>How to create RSS channel:</h3>

<pre>
<code>
RssGenerator rssGenerator = new RssGenerator("http://www.topjavablogs", 
                                             "Top Java Blogs", "Best Java Blogs");
rssGenerator.addPage(new RssItemBuilder().pubDate(new Date()).title("News Title")
                        .description("News Description").name("page-name").build());
System.out.println(rssGenerator.constructRss());
</code>
</pre>

<h2>My other projects:</h2>
<ul>
	<li><a href="http://www.javavids.com" target="_blank" title="Java video tutorials">Java video tutorials</a> (free online tutorials)</li>
	<li><a href="http://sitemonitoring.sourceforge.net/" target="_blank" title="Website monitoring software">Website monitoring software</a> (free OSS software)</li>
	<li><a href="http://www.java-skoleni.cz" target="_blank" title="Java skoleni">Java skoleni</a> (in Czech)</li>
	<li><a href="http://www.sql-skoleni.cz" target="_blank" title="SQL skoleni">SQL skoleni</a> (in Czech)</li>
</ul>
