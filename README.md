# Java sitemap generator


This library generates a web sitemap and can ping Google that it has
changed. This project has been inspired by [sitemapgen4j](https://code.google.com/p/sitemapgen4j/),
but is much more focused on traditional web sitemap and ease of use.

## Typical usage:

First add this library to classpath:

    <dependency>
      <groupId>cz.jiripinkas</groupId>
      <artifactId>jsitemapgenerator</artifactId>
      <version>3.1</version>
    </dependency>

If you want to use "ping google / bing" functionality, also add this library to classpath:

    <dependency>
        <groupId>com.squareup.okhttp3</groupId>
        <artifactId>okhttp</artifactId>
        <version>3.12.0</version>
    </dependency>

### How to create a sitemap:

    // create web sitemap for web http://www.javavids.com
    SitemapGenerator sg = SitemapGenerator.of("http://www.javavids.com");
    // add some URLs
    sg.addPage(WebPage.builder().nameRoot().priorityMax().changeFreqNever().lastModNow().build())
      .addPage(WebPage.builder().name("latest.php").build())
      .addPage(WebPage.builder().name("contact.php").build());
    // generate sitemap and save it to file /var/www/sitemap.xml
    File file = new File("/var/www/sitemap.xml");
    sg.constructAndSaveSitemap(file);
    // inform Google that this sitemap has changed
    sg.pingGoogle();

### How to create a sitemap populated with list of pages:

    File file = new File("/var/www/sitemap.xml");
    List<String> pages = Arrays.asList("firstPage", "secondPage", "otherPage");
    // create web sitemap for web http://www.javavids.com
    SitemapGenerator.of("http://www.javavids.com")
        .addPage(WebPage.builder().nameRoot().priorityMax().changeFreqNever().lastModNow().build())
        .addPages(urls, page -> WebPage.builder().name("dir/" + page).priorityMax().changeFreqNever().lastModNow().build())
        .constructAndSaveSitemap(file);

### How to create a sitemap index:

    SitemapIndexGenerator sitemapIndexGenerator = SitemapIndexGenerator.of("http://javalibs.com");
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

    RssGenerator rssGenerator = RssGenerator.of("http://www.topjavablogs", "Top Java Blogs", "Best Java Blogs");
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