package cz.jiripinkas.jsitemapgenerator;

import cz.jiripinkas.jsitemapgenerator.exception.InvalidUrlException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Abstract Generator
 * @param <I> Concrete implementation of AbstractGenerator, for example SitemapGenerator
 */
public abstract class AbstractGenerator <I extends AbstractGenerator> {

    protected Map<String, WebPage> urls = new TreeMap<>();

    protected String baseUrl;

    /**
     * Construct web sitemap.
     *
     * @param baseUrl All URLs must start with this baseUrl, for example
     *                http://www.javavids.com
     * @param root    If Base URL is root (for example http://www.javavids.com or if
     *                it's some path like http://www.javalibs.com/blog)
     */
    public AbstractGenerator(String baseUrl, boolean root) {
        try {
            new URL(baseUrl);
        } catch (MalformedURLException e) {
            throw new InvalidUrlException(e);
        }

        if (root && !baseUrl.endsWith("/")) {
            baseUrl += "/";
        }
        this.baseUrl = baseUrl;
    }

    /**
     * Construct web sitemap. Root = true.
     *
     * @param baseUrl All URLs must start with this baseUrl, for example
     *                http://www.javavids.com
     */
    public AbstractGenerator(String baseUrl) {
        this(baseUrl, true);
    }

    /**
     * Add single page to sitemap
     *
     * @param webPage single page
     * @return this
     */
    public I addPage(WebPage webPage) {
        beforeAddPageEvent(webPage);
        urls.put(baseUrl + webPage.constructName(), webPage);
        return getThis();
    }

    /**
     * Add single page to sitemap. This method calls addPage(WebPage.of(name))
     *
     * @param name single page
     * @return this
     */
    public I addPage(String name) {
        return addPage(WebPage.of(name));
    }

    /**
     * Add single page to sitemap.
     * @param supplier Supplier method which sneaks any checked exception
     *                 https://www.baeldung.com/java-sneaky-throws
     *                 Allows for calling method which performs some operation and then returns name of page.
     * @return this
     */
    public I addPage(StringSupplierWithException<String> supplier) {
        try {
            addPage(supplier.get());
        } catch (Exception e) {
            sneakyThrow(e);
        }
        return getThis();
    }


    /**
     * This method is called before adding a page to urls.
     * It can be used to change webPage attributes
     * @param webPage WebPage
     */
    protected void beforeAddPageEvent(WebPage webPage) {

    }

    /**
     * Add collection of pages to sitemap
     *
     * @param webPages Collection of pages
     * @return this
     */
    public I addPages(Collection<WebPage> webPages) {
        for (WebPage webPage : webPages) {
            addPage(webPage);
        }
        return getThis();
    }

    /**
     * Add collection of pages to sitemap
     *
     * @param webPagesSupplier Collection of pages supplier
     * @return this
     */
    public I addPages(Supplier<Collection<WebPage>> webPagesSupplier) {
        for (WebPage webPage : webPagesSupplier.get()) {
            addPage(webPage);
        }
        return getThis();
    }

    /**
     * Add collection of pages to sitemap
     *
     * @param <T> This is the type parameter
     * @param webPages Collection of pages
     * @param mapper Mapper function which transforms some object to WebPage
     * @return this
     */
    public <T> I addPages(Collection<T> webPages, Function<T, WebPage> mapper) {
        for (T element : webPages) {
            addPage(mapper.apply(element));
        }
        return getThis();
    }

    /**
     * Add collection of pages to sitemap
     *
     * @param <T> This is the type parameter
     * @param webPages Collection of pages
     * @param mapper Mapper function which transforms some object to String. This will be passed to WebPage.of(name)
     * @return this
     */
    public <T> I addPageNames(Collection<T> webPages, Function<T, String> mapper) {
        for (T element : webPages) {
            addPage(WebPage.of(mapper.apply(element)));
        }
        return getThis();
    }

    /**
     * Add collection of pages to sitemap
     *
     * @param <T> This is the type parameter
     * @param webPagesSupplier Collection of pages supplier
     * @param mapper Mapper function which transforms some object to WebPage
     * @return this
     */
    public <T> I addPages(Supplier<Collection<T>> webPagesSupplier, Function<T, WebPage> mapper) {
        for (T element : webPagesSupplier.get()) {
            addPage(mapper.apply(element));
        }
        return getThis();
    }

    /**
     * Add collection of pages to sitemap
     *
     * @param <T> This is the type parameter
     * @param webPagesSupplier Collection of pages supplier
     * @param mapper Mapper function which transforms some object to String. This will be passed to WebPage.of(name)
     * @return this
     */
    public <T> I addPageNames(Supplier<Collection<T>> webPagesSupplier, Function<T, String> mapper) {
        for (T element : webPagesSupplier.get()) {
            addPage(WebPage.of(mapper.apply(element)));
        }
        return getThis();
    }

    /**
     * Run some method
     * @param runnable Runnable method which sneaks any checked exception
     *                 https://www.baeldung.com/java-sneaky-throws
     *                 Usage:
     *                 SitemapIndexGenerator.of(homepage)
     *                      .run(() -&gt; methodToCall())
     *                      .addPage(WebPage.of("test))
     *                      .toString()
     * @return this
     */
    public I run(RunnableWithException runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            sneakyThrow(e);
        }
        return getThis();
    }

    /**
     * Run some method. Argument is current generator,
     * which allows to access current generator in run() method.
     * @param consumer Consumer method which sneaks any checked exception
     *                 https://www.baeldung.com/java-sneaky-throws
     *                 Usage:
     *                 SitemapIndexGenerator.of(homepage)
     *                      .run(currentGenerator -&gt; { ... })
     *                      .addPage(WebPage.of("test))
     *                      .toString()
     * @return this
     */
    public I run(GeneratorConsumerWithException<I> consumer) {
        try {
            consumer.accept(getThis());
        } catch (Exception e) {
            sneakyThrow(e);
        }
        return getThis();
    }


    @SuppressWarnings("unchecked")
    protected I getThis() {
        return (I)this;
    }

    public interface RunnableWithException {
        void run() throws Exception;
    }

    public interface GeneratorConsumerWithException<T> {
        void accept(T t) throws Exception;
    }

    public interface StringSupplierWithException<String> {
        String get() throws Exception;
    }

    /**
     * Sneak exception https://www.baeldung.com/java-sneaky-throws
     * @param e Exception
     * @param <E> Type parameter
     * @throws E Sneaked exception
     */
    @SuppressWarnings("unchecked")
    private static <E extends Throwable> void sneakyThrow(Throwable e) throws E {
        throw (E) e;
    }

}
