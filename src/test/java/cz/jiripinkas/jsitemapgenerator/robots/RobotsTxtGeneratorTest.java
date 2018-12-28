package cz.jiripinkas.jsitemapgenerator.robots;

import org.junit.Test;

import static org.junit.Assert.*;

public class RobotsTxtGeneratorTest {

    @Test
    public void constructRobotsTxtString() {
        String robotsTxtString = RobotsTxtGenerator.of("https://example.com")
                .addSitemap("sitemap.xml")
                .addRule(RobotsRule.builder().userAgentAll().allowAll().build())
                .constructRobotsTxtString();
        String expected = "Sitemap: https://example.com/sitemap.xml\n" +
                "User-agent: *\n" +
                "Allow: /";
        assertEquals(expected, robotsTxtString);
    }

}