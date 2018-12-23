# Java sitemap generator


This library generates a web sitemap and can ping Google that it has
changed. This project has been inspired by [sitemapgen4j](https://code.google.com/p/sitemapgen4j/),
but is much more focused on traditional web sitemap and ease of use.

## Typical usage:

First add this library to classpath:

    <dependency>
      <groupId>cz.jiripinkas</groupId>
      <artifactId>jsitemapgenerator</artifactId>
      <version>3.0</version>
    </dependency>

If you want to use "ping google / bing" functionality, also add this library to classpath:

    <dependency>
        <groupId>com.squareup.okhttp3</groupId>
        <artifactId>okhttp</artifactId>
        <version>3.12.0</version>
    </dependency>

### How to create a sitemap:


    // create web sitemap for web http://www.javavids.com
    SitemapGenerator sitemapGenerator = new SitemapGenerator("http://www.javavids.com");
    // add some URLs
    sitemapGenerator.addPage(WebPage.builder()
        .name("index.php")
        .priorityMax()
        .changeFreqNever()
        .lastModNow()
        .build()
    );
    sitemapGenerator.addPage(WebPage.builder()
        .name("latest.php")
        .build()
    );
    sitemapGenerator.addPage(WebPage.builder()
        .name("contact.php")
        .build()
    );
    // generate sitemap and save it to file /var/www/sitemap.xml
    File file = new File("/var/www/sitemap.xml");
    sitemapGenerator.constructAndSaveSitemap(file);
    // inform Google that this sitemap has changed
    sitemapGenerator.pingGoogle();

### How to create a sitemap index:

    SitemapIndexGenerator sitemapIndexGenerator = new SitemapIndexGenerator("http://javalibs.com");
    sitemapIndexGenerator.addPage(WebPage.builder()
        .name("sitemap-plugins.xml")
        .build()
    );
    sitemapIndexGenerator.addPage(WebPage.builder()
        .name("sitemap-archetypes.xml")
        .build()
    );
    System.out.println(sitemapIndexGenerator.constructSitemapString());

### How to create RSS channel:

    RssGenerator rssGenerator = new RssGenerator("http://www.topjavablogs", "Top Java Blogs", "Best Java Blogs");
    rssGenerator.addPage(WebPage.rssBuilder()
        .pubDate(new Date())
        .title("News Title")
        .description("News Description")
        .name("page-name")
        .build()
    );
    System.out.println(rssGenerator.constructRss());

## My other projects:
<ul>
	<li><a href="https://www.javalibs.com" target="_blank" title="javalibs">javalibs: (not only) Maven Central search engine</a></li>
	<li><a href="https://www.javavids.com" target="_blank" title="Java video tutorials">Java video tutorials</a> (free online tutorials)</li>
	<li><a href="https://sitemonitoring.sourceforge.net/" target="_blank" title="Website monitoring software">Website monitoring software</a> (free OSS software)</li>
	<li><a href="https://www.java-skoleni.cz" target="_blank" title="Java skoleni">Java skoleni</a> (in Czech)</li>
	<li><a href="https://www.sql-skoleni.cz" target="_blank" title="SQL skoleni">SQL skoleni</a> (in Czech)</li>
</ul>
