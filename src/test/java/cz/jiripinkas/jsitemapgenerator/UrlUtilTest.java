package cz.jiripinkas.jsitemapgenerator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UrlUtilTest {

    @Test
    void connectUrlParts() {
        assertAll(
                () -> assertEquals("https://javalibs.com", UrlUtil.connectUrlParts("https://javalibs.com", null)),
                () -> assertEquals("https://javalibs.com/page", UrlUtil.connectUrlParts("https://javalibs.com", "page")),
                () -> assertEquals("https://javalibs.com/page", UrlUtil.connectUrlParts("https://javalibs.com", "/page"))
        );
    }

    @Test
    void escapeXmlSpecialCharacters() {
        assertEquals("/page?arg1=&apos;test&apos;&amp;arg2=&lt;test&gt;&amp;arg3=&quot;test&quot;", UrlUtil.escapeXmlSpecialCharacters("/page?arg1='test'&arg2=<test>&arg3=\"test\""));
        assertNull(UrlUtil.escapeXmlSpecialCharacters(null));
    }

}