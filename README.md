# Java sitemap generator


This library generates a web sitemap and can ping Google that it has
changed. This project has been inspired by [sitemapgen4j](https://code.google.com/p/sitemapgen4j/),
but is much more focused on traditional web sitemap and ease of use.

## Typical usage:

First add this library to classpath:

    <dependency>
      <groupId>cz.jiripinkas</groupId>
      <artifactId>jsitemapgenerator</artifactId>
      <version>3.6</version>
    </dependency>

If you want to use "ping google / bing" functionality, also add this library to classpath:

    <dependency>
        <groupId>com.squareup.okhttp3</groupId>
        <artifactId>okhttp</artifactId>
        <version>3.12.0</version>
    </dependency>

### Typical usage (web sitemap):

```java
String sitemap = SitemapGenerator.of("https://example.com")
    .addPage(WebPage.builder().nameRoot().priorityMax().changeFreqNever().lastModNow().build())
    .addPage(WebPage.builder().name("foo.html").build())
    .addPage(WebPage.builder().name("bar.html").build())
    .constructSitemapString();
```

or sitemap in gzip format:

```java
byte[] sitemap = SitemapGenerator.of("https://example.com")
    .addPage(WebPage.builder().nameRoot().priorityMax().changeFreqNever().lastModNow().build())
    .addPage(WebPage.builder().name("foo.html").build())
    .addPage(WebPage.builder().name("bar.html").build())
    .constructSitemapGzip();
```

or to store it to file & ping google:

```java
// create web sitemap for web http://www.javavids.com
SitemapGenerator sg = SitemapGenerator.of("https://example.com");
// add some URLs
sg.addPage(WebPage.builder().nameRoot().priorityMax().changeFreqNever().lastModNow().build())
    .addPage(WebPage.builder().name("foo.html").build())
    .addPage(WebPage.builder().name("bar.html").build());
// generate sitemap and save it to file /var/www/sitemap.xml
File file = new File("/var/www/sitemap.xml");
sg.constructAndSaveSitemap(file);
// inform Google that this sitemap has changed
sg.pingGoogle(); // this requires okhttp in classpath!!!
```

or with list of pages:

```java
File file = new File("/var/www/sitemap.xml");
List<String> pages = Arrays.asList("firstPage", "secondPage", "otherPage");
// create web sitemap for web http://www.javavids.com
SitemapGenerator.of("http://example.com")
    .addPage(WebPage.builder().nameRoot().priorityMax().changeFreqNever().lastModNow().build())
    .addPages(urls, page -> WebPage.builder().dir("dirName").name(page).priorityMax().changeFreqNever().lastModNow().build())
    .constructAndSaveSitemap(file);
```

### How to create sitemap index:

```java
String sitemapIndex = SitemapIndexGenerator.of("https://javalibs.com")
    .addPage(WebPage.builder().name("sitemap-plugins.xml").build())
    .addPage(WebPage.builder().name("sitemap-archetypes.xml").build())
    .constructSitemapString();
```

### How to create RSS channel:

```java
String rss = RssGenerator.of("https://topjavablogs.com", "Top Java Blogs", "Best Java Blogs")
    .addPage(WebPage.rssBuilder()
        .pubDate(new Date())
        .title("News Title")
        .description("News Description")
        .name("page-name")
        .build())
    .constructRss();
```

## My other projects:

- [javalibs: (not only) Maven Central search engine](https://javalibs.com)
- [Java video tutorials](https://javavids.com)
- [Website monitoring software](http://sitemonitoring.sourceforge.net/)
- [Java školení](https://www.java-skoleni.cz)
- [SQL školení](https://www.sql-skoleni.cz)

## What I used to upload jsitemapgenerator to Maven Central:

- https://central.sonatype.org/pages/working-with-pgp-signatures.html
- https://central.sonatype.org/pages/apache-maven.html