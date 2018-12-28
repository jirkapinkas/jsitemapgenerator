package cz.jiripinkas.jsitemapgenerator.robots;

import org.junit.Test;

import static org.junit.Assert.*;

public class RobotsRuleTest {

    @Test
    public void testBuilderOK() {
        RobotsRule.builder().allowAll().userAgentAll().build();
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