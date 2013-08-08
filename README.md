<h1>Java sitemap generator</h1>

<p>This library generates a web sitemap and can ping Google that it has changed.</p>

<h2>Typical usage:</h2>

<div class="highlight">
<pre>
<code>
webSitemapGenerator = new WebSitemapGenerator("http://www.javavids.com");
webSitemapGenerator.addUrl(new WebSitemapUrl().setUrl("http://www.javavids.com")
                   .setPriority(1.0).setChangeFreq(ChangeFreq.NEVER).setLastMod(new Date()));
webSitemapGenerator.addUrl(new WebSitemapUrl().setUrl("http://www.javavids.com/latest.php"));
webSitemapGenerator.addUrl(new WebSitemapUrl().setUrl("http://www.javavids.com/contact.php"));
File file = new File("/var/www/sitemap.xml");
webSitemapGenerator.constructAndSaveSitemap(file);
webSitemapGenerator.pingGoogle("http://www.javavids.com/sitemap.xml");
</code>
</pre>
</div>