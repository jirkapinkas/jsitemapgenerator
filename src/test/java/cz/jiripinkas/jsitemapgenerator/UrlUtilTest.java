package cz.jiripinkas.jsitemapgenerator;

import org.junit.Test;

import static org.junit.Assert.*;

public class UrlUtilTest {

    @Test
    public void connectUrlParts() {
        assertEquals("https://javalibs.com", UrlUtil.connectUrlParts("https://javalibs.com", null));
        assertEquals("https://javalibs.com/page", UrlUtil.connectUrlParts("https://javalibs.com", "page"));
        assertEquals("https://javalibs.com/page", UrlUtil.connectUrlParts("https://javalibs.com", "/page"));
    }

}