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
  &lt;version&gt;1.1&lt;/version&gt;
&lt;/dependency&gt;
</code>
</pre>

<p>Next create a sitemap:</p>


<pre>
<code>
// create web sitemap for web http://www.javavids.com
WebSitemapGenerator webSitemapGenerator = new WebSitemapGenerator("http://www.javavids.com");
// add some URLs
webSitemapGenerator.addPage(new WebPage().setName("index.php")
                   .setPriority(1.0).setChangeFreq(ChangeFreq.NEVER).setLastMod(new Date()));
webSitemapGenerator.addPage(new WebPage().setName("latest.php"));
webSitemapGenerator.addPage(new WebPage().setName("contact.php"));
// generate sitemap and save it to file /var/www/sitemap.xml
File file = new File("/var/www/sitemap.xml");
webSitemapGenerator.constructAndSaveSitemap(file);
// inform Google that this sitemap has changed
webSitemapGenerator.pingGoogle();
</code>
</pre>

<h2></h2>


<h2>My other projects:</h2>
<ul>
	<li><a href="http://www.javavids.com" target="_blank" title="Java video tutorials">Java video tutorials</a> (free online tutorials)</li>
	<li><a href="http://sitemonitoring.sourceforge.net/" target="_blank" title="Website monitoring software">Website monitoring software</a> (free OSS software)</li>
	<li><a href="http://www.java-skoleni.cz" target="_blank" title="Java školené">Java školení</a> (in Czech)</li>
	<li><a href="http://www.sql-skoleni.cz" target="_blank" title="Java školení">SQL školení</a> (in Czech)</li>
</ul>
