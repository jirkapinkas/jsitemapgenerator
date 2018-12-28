package cz.jiripinkas.jsitemapgenerator;

import cz.jiripinkas.jsitemapgenerator.exception.InvalidUrlException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

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
        urls.put(baseUrl + webPage.getName(), webPage);
        return getThis();
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

    @SuppressWarnings("unchecked")
    I getThis() {
        return (I)this;
    }


}
