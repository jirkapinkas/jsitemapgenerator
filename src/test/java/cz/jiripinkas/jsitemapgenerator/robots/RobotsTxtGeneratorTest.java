package cz.jiripinkas.jsitemapgenerator.robots;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RobotsTxtGeneratorTest {

    @Test
    void constructRobotsTxtString() {
        String actual = RobotsTxtGenerator.of("https://example.com")
                .addSitemap("sitemap.xml")
                .addRule(RobotsRule.builder().userAgentAll().allowAll().build())
                .toString();
        String expected = "Sitemap: https://example.com/sitemap.xml\n" +
                "User-agent: *\n" +
                "Allow: /";
        assertEquals(expected, actual);
    }

    @Test
    void constructRobotsTxtStringWithRedundantSlash() {
        String actual = RobotsTxtGenerator.of("https://example.com")
                .addSitemap("/sitemap.xml")
                .addRule(RobotsRule.builder().userAgentAll().allowAll().build())
                .toString();
        String expected = "Sitemap: https://example.com/sitemap.xml\n" +
                "User-agent: *\n" +
                "Allow: /";
        assertEquals(expected, actual);
    }

}