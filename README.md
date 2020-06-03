# Java sitemap generator


This library generates a web sitemap and can ping Google that it has
changed (also it can generate RSS feed and robots.txt). It has friendly, 
easy to use Java 8 functional API and is AWS-lambda friendly.

## Typical usage:

Add this library to classpath:

    <dependency>
      <groupId>cz.jiripinkas</groupId>
      <artifactId>jsitemapgenerator</artifactId>
      <version>4.5</version>
    </dependency>

If you want to use "ping google / bing" functionality, also add this library to classpath:

    <dependency>
        <groupId>com.squareup.okhttp3</groupId>
        <artifactId>okhttp</artifactId>
        <version>4.2.2</version> <!-- latest version should be fine, get latest version from https://javalibs.com/artifact/com.squareup.okhttp3/okhttp -->
    </dependency>

### Typical usage (web sitemap):

```java
String sitemap = SitemapGenerator.of("https://example.com")
    .addPage("foo2.html") // simplest way how to add page - shorthand for addPage(WebPage.of("foo2.html"))
    .addPage(WebPage.of("foo1.html")) // same as addPage("foo1.html")
    .addPage(WebPage.builder().name("bar.html").build()) // builder is more complex
    .addPage(WebPage.builder().maxPriorityRoot().build()) // builder has lots of useful methods
    .toString();
```

or sitemap in gzip format:

```java
byte[] sitemap = SitemapGenerator.of("https://example.com")
    .addPage(WebPage.builder().maxPriorityRoot().build())
    .addPage("foo.html")
    .addPage("bar.html")
    .toGzipByteArray();
```

you can set default settings (for the subsequent WebPages):

```java
String sitemap = SitemapGenerator.of("https://example.com")
    .addPage(WebPage.builder().maxPriorityRoot().build()) // URL will be: "/"
    .defaultExtension("html")
    .defaultDir("dir1")
    .addPage("foo") // URL will be: "dir1/foo.html"
    .addPage("bar") // URL will be: "dir1/bar.html"
    .defaultDir("dir2")
    .addPage("hello") // URL will be: "dir2/hello.html"
    .addPage("yello") // URL will be: "dir2/yello.html"
    // btw. specifying dir and / or extension on WebPage overrides default settings
    .addPage(WebPage.builder().dir("dir3").extension(null).name("test").build()) // "dir3/test"
    .resetDefaultDir() // resets default dir
    .resetDefaultExtension() // resets default extension
    .addPage(WebPage.of("mypage")) // URL will be: "mypage"
    .toString();
```

or with list of pages:

```java
List<String> pages = Arrays.asList("firstPage", "secondPage", "otherPage");
String sitemap = SitemapGenerator.of("https://example.com")
        .addPage(WebPage.builder().nameRoot().priorityMax().build())
        .defaultDir("dirName")
        .addPages(pages, page -> WebPage.of(page))
        .toString();
```

or list of pages in complex data type:

```java
class News {
    private String name;
    public News(String name) { this.name = name; }
    public String getName() { return name; }
}
List<News> newsList = Arrays.asList(new News("a"), new News("b"), new News("c"));
String sitemap = SitemapGenerator.of("https://example.com")
        .addPage(WebPage.builder().nameRoot().priorityMax().build())
        .defaultDir("news")
        .addPages(newsList, news -> WebPage.of(news::getName))
        .toString();
```

or to store it to file & ping Google:

```java
Ping ping = Ping.builder()
        .engines(Ping.SearchEngine.GOOGLE)
        .build();
SitemapGenerator.of("https://example.com")
    .addPage(WebPage.builder().maxPriorityRoot().changeFreqNever().lastModNow().build())
    .addPage("foo.html")
    .addPage("bar.html")
    // generate sitemap and save it to file ./sitemap.xml
    .toFile(Paths.get("sitemap.xml"))
    // inform Google that this sitemap has changed
    .ping(ping); // this requires okhttp in classpath!!!
    .callOnSuccess(() -> System.out.println("Pinged Google")) // what will happen on success
    .catchOnFailure(e -> System.out.println("Could not ping Google!")); // what will happen on error
```

Note: To ping Google / Bing, you can either use built-in support (requires OkHttp in classpath!!!), 
or you can use your own http client implementation. Supported http clients: Custom OkHttpClient, 
CloseableHttpClient (Apache Http Client), RestTemplate (from Spring). To use your own http client 
implementation just call on PingBuilder method: httpClient*() and pass inside your implementation.

### How to create sitemap index:

```java
String sitemapIndex = SitemapIndexGenerator.of("https://javalibs.com")
    .addPage("sitemap-plugins.xml")
    .addPage("sitemap-archetypes.xml")
    .toString();
```

### How to create RSS channel:

... RSS ISN'T sitemap :-), but it's basically just a list of links (like sitemap) and if you need sitemap, 
then probably you also need RSS. Note: RssGenerator has lots of common methods with SitemapGenerator.

```java
String rss = RssGenerator.of("https://topjavablogs.com", "Top Java Blogs", "Best Java Blogs")
    .addPage(WebPage.rssBuilder()
        .pubDate(LocalDateTime.now())
        .title("News Title")
        .description("News Description")
        .link("page-name")
        .build())
    .toString();
```

### How to create robots.txt:

... robots.txt ISN'T sitemap :-), but inside it you reference your sitemap and if you need sitemap, then you probably need robots.txt as well :-)

```java
String robotsTxt = RobotsTxtGenerator.of("https://example.com")
        .addSitemap("sitemap.xml")
        .addRule(RobotsRule.builder().userAgentAll().allowAll().build())
        .toString();
```

### How to check sitemap:

- https://hub.docker.com/r/jirkapinkas/sitemap_validator
    - this tool will crawl any sitemap.xml, parse and check each URL
    - usable with Docker
    - can be easily used in CI/CD pipeline
    - perfect for testing generated sitemap
    
### Best practices & performance

- SitemapGenerator (and other Generator classes) are builders, thus they're not immutable.
- Also having SitemapGenerator as singleton and at the same time calling addPage() and toString() (in multiple threads) isn't really advised. SitemapGenerator operations aren't thread-safe (with one exception: SitemapGenerator.of(), which creates new instance of SitemapGenerator).
- When you call addPage(), you store it to Map, where key is page's URL (so you cannot have two items with the same URL in sitemap).
- toString(), toFile(), toGzipByteArray() methods (terminal operations) generate final sitemap from the Map of objects. So when creating sitemap, most time will be spent executing terminal operation.
- If you need raw speed for accessing sitemap, I suggest to:
    - either save sitemap to external file and then just get the data from file
    - or cache the result of terminal operation

## My other projects:

- [javalibs: (not only) Maven Central search engine](https://javalibs.com)
- [Java video tutorials](https://javavids.com)
- [Java školení](https://www.java-skoleni.cz)
- [SQL školení](https://www.sql-skoleni.cz)

## What I used to upload jsitemapgenerator to Maven Central:

- https://central.sonatype.org/pages/working-with-pgp-signatures.html
- https://central.sonatype.org/pages/apache-maven.html