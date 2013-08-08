<h1>Java sitemap generator</h1>

<p>
	This library generates a web sitemap and can ping Google that it has
	changed. This project has been inspired by <a
		href="https://code.google.com/p/sitemapgen4j/" target="_blank">sitemapgen4j</a>,
	but is much more focused on traditional web sitemap and ease of use.
</p>

<h2>Typical usage:</h2>

<pre>
<code>
WebSitemapGenerator webSitemapGenerator = new WebSitemapGenerator("http://www.javavids.com");
webSitemapGenerator.addUrl(new WebSitemapUrl().setUrl("http://www.javavids.com")
                   .setPriority(1.0).setChangeFreq(ChangeFreq.NEVER).setLastMod(new Date()));
webSitemapGenerator.addUrl(new WebSitemapUrl().setUrl("http://www.javavids.com/latest.php"));
webSitemapGenerator.addUrl(new WebSitemapUrl().setUrl("http://www.javavids.com/contact.php"));
File file = new File("/var/www/sitemap.xml");
webSitemapGenerator.constructAndSaveSitemap(file);
webSitemapGenerator.pingGoogle("http://www.javavids.com/sitemap.xml");
</code>
</pre>


<h2>My other projects:</h2>
<ul>
	<li><a href="http://www.javavids.com" target="_blank" title="Java video tutorials">Java video tutorials</a> (free online tutorials)</li>
	<li><a href="http://sitemonitoring.sourceforge.net/" target="_blank" title="Website monitoring software">Website monitoring software</a> (free OSS software)</li>
	<li><a href="http://www.java-skoleni.cz" target="_blank" title="Java školené">Java školení</a> (in Czech)</li>
	<li><a href="http://www.sql-skoleni.cz" target="_blank" title="Java školení">SQL školení</a> (in Czech)</li>
</ul>
