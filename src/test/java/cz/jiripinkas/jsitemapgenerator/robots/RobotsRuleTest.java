package cz.jiripinkas.jsitemapgenerator.robots;

import org.junit.Test;

import static org.junit.Assert.*;

public class RobotsRuleTest {

    @Test
    public void testBuilderOK() {
        RobotsRule robotsRule = RobotsRule.builder().allowAll().userAgentAll().build();
        assertEquals("/", robotsRule.getAllows().get(0));
        assertEquals("*", robotsRule.getUserAgent());
        assertTrue(robotsRule.getDisallows().isEmpty());
    }

    @Test(expected = RobotsRuleException.class)
    public void testBuilderMissingUserAgent() {
        RobotsRule.builder().allowAll().disallowAll().build();
    }

    @Test(expected = RobotsRuleException.class)
    public void testBuilderMissingAllowAndDisallow() {
        RobotsRule.builder().userAgentAll().build();
    }
}