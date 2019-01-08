# Java sitemap generator


This library generates a web sitemap and can ping Google that it has
changed. This project has been inspired by [sitemapgen4j](https://code.google.com/p/sitemapgen4j/),
but is much more focused on traditional web sitemap and ease of use.

## Typical usage:

First add this library to classpath:

    <dependency>
      <groupId>cz.jiripinkas</groupId>
      <artifactId>jsitemapgenerator</artifactId>
      <version>3.10</version>
    </dependency>

If you want to use "ping google / bing" functionality, also add this library to classpath:

    <dependency>
        <groupId>com.squareup.okhttp3</groupId>
        <artifactId>okhttp</artifactId>
        <version>3.12.1</version> <!-- latest version should be fine -->
    </dependency>

### Typical usage (web sitemap):

```java
String sitemap = SitemapGenerator.of("https://example.com")
    .addPage(WebPage.of("foo.html")) // simplest way of creating web page
    .addPage(WebPage.builder().name("bar.html").build()) // builder is more complex
    .addPage(WebPage.builder().maxPriorityRoot().build()) // builder has lots of useful methods
    .constructSitemapString();
```

or sitemap in gzip format:

```java
byte[] sitemap = SitemapGenerator.of("https://example.com")
    .addPage(WebPage.builder().maxPriorityRoot().build())
    .addPage(WebPage.of("foo.html"))
    .addPage(WebPage.of("bar.html"))
    .constructSitemapGzip();
```

you can set default settings (for the subsequent WebPages):

```java
String sitemap = SitemapGenerator.of("https://example.com")
    .addPage(WebPage.builder().maxPriorityRoot().build()) // URL will be: "/"
    .defaultExtension("html")
    .defaultDir("dir1")
    .addPage(WebPage.of("foo")) // URL will be: "dir1/foo.html"
    .addPage(WebPage.of("bar"))) // URL will be: "dir1/bar.html"
    .defaultDir("dir2")
    .addPage(WebPage.of("hello")) // URL will be: "dir2/hello.html"
    .addPage(WebPage.of("yello"))) // URL will be: "dir2/yello.html"
    // btw. specifying dir and / or extension on WebPage overrides default settings
    .addPage(WebPage.builder().dir("dir3").extension(null).name("test").build()) // "dir3/test"
    .resetDefaultDir() // resets default dir
    .resetDefaultExtension() // resets default extension
    .addPage(WebPage.of("mypage")) // URL will be: "mypage"
    .constructSitemapString();
```

or with list of pages:

```java
List<String> pages = Arrays.asList("firstPage", "secondPage", "otherPage");
String sitemap = SitemapGenerator.of("http://example.com")
        .addPage(WebPage.builder().nameRoot().priorityMax().build())
        .defaultDir("dirName")
        .addPages(pages, page -> WebPage.of(page))
        .constructSitemapString();
```

or list of pages in complex data type:

```java
class News {
    private String name;
    public News(String name) { this.name = name; }
    public String getName() { return name; }
}
List<News> newsList = Arrays.asList(new News("a"), new News("b"), new News("c"));
String sitemap = SitemapGenerator.of("http://example.com")
        .addPage(WebPage.builder().nameRoot().priorityMax().build())
        .defaultDir("news")
        .addPages(newsList, news -> WebPage.of(news::getName))
        .constructSitemapString();
```

or to store it to file & ping google:

```java
// create web sitemap for web http://www.javavids.com
SitemapGenerator sg = SitemapGenerator.of("https://example.com");
// add some URLs
sg.addPage(WebPage.builder().maxPriorityRoot().changeFreqNever().lastModNow().build())
  .addPage(WebPage.of("foo.html"))
  .addPage(WebPage.of("bar.html"));
// generate sitemap and save it to file /var/www/sitemap.xml
File file = new File("/var/www/sitemap.xml");
sg.constructAndSaveSitemap(file);
// inform Google that this sitemap has changed
sg.pingGoogle(); // this requires okhttp in classpath!!!
```

### How to create sitemap index:

```java
String sitemapIndex = SitemapIndexGenerator.of("https://javalibs.com")
    .addPage(WebPage.of("sitemap-plugins.xml"))
    .addPage(WebPage.of("sitemap-archetypes.xml"))
    .constructSitemapString();
```

### How to create RSS channel:

... RSS ISN'T sitemap :-), but it's basically just a list of links (like sitemap) and if you need sitemap, then probably you also need RSS

```java
String rss = RssGenerator.of("https://topjavablogs.com", "Top Java Blogs", "Best Java Blogs")
    .addPage(WebPage.rssBuilder()
        .pubDate(new Date())
        .title("News Title")
        .description("News Description")
        .link("page-name")
        .build())
    .constructRss();
```

### How to create robots.txt:

... robots.txt ISN'T sitemap :-), but inside it you reference your sitemap and if you need sitemap, then you probably need robots.txt as well :-)

```java
String robotsTxt = RobotsTxtGenerator.of("https://example.com")
        .addSitemap("sitemap.xml")
        .addRule(RobotsRule.builder().userAgentAll().allowAll().build())
        .constructRobotsTxtString();
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