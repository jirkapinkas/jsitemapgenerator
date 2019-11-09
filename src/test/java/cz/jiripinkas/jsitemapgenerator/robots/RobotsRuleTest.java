package cz.jiripinkas.jsitemapgenerator.robots;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RobotsRuleTest {

    @Test
    void testBuilderOK() {
        RobotsRule robotsRule = RobotsRule.builder().allowAll().userAgentAll().build();
        assertEquals("/", robotsRule.getAllows().get(0));
        assertEquals("*", robotsRule.getUserAgent());
        assertTrue(robotsRule.getDisallows().isEmpty());
    }

    @Test
    void testBuilderMissingUserAgent() {
        assertThrows(RobotsRuleException.class, () -> {
            RobotsRule.builder().allowAll().disallowAll().build();
        });
    }

    @Test
    void testBuilderMissingAllowAndDisallow() {
        assertThrows(RobotsRuleException.class, () -> {
            RobotsRule.builder().userAgentAll().build();
        });
    }
}