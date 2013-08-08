<h1>Java sitemap generator</h1>

<p>This library generates a web sitemap and can ping Google that it has changed.</p>

<h2>Typical usage:</h2>

<code>
webSitemapGenerator = new WebSitemapGenerator("http://www.javavids.com");
webSitemapGenerator.addUrl(new WebSitemapUrl().setUrl("http://www.javavids.com")
                   .setPriority(1.0).setChangeFreq(ChangeFreq.NEVER).setLastMod(new Date()));<br>
webSitemapGenerator.addUrl(new WebSitemapUrl().setUrl("http://www.javavids.com/latest.php"));<br>
webSitemapGenerator.addUrl(new WebSitemapUrl().setUrl("http://www.javavids.com/contact.php"));<br>
File file = new File("/var/www/sitemap.xml");<br>
webSitemapGenerator.constructAndSaveSitemap(file);<br>
webSitemapGenerator.pingGoogle("http://www.javavids.com/sitemap.xml");<br>
</code>
