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

- [javalibs: (not only) Maven Central search engine](https://javalibs.com)
- [Java video tutorials](https://javavids.com)
- [Website monitoring software](http://sitemonitoring.sourceforge.net/)
- [Java školení](https://www.java-skoleni.cz)
- [SQL školení](https://www.sql-skoleni.cz)

## What I used to upload jsitemapgenerator to Maven Central:

- https://central.sonatype.org/pages/working-with-pgp-signatures.html
- https://central.sonatype.org/pages/apache-maven.html