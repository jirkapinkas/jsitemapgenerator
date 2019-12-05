package cz.jiripinkas.jsitemapgenerator.robots;

import java.util.ArrayList;
import java.util.List;

public class RobotsRule {

    private String userAgent;

    private List<String> allows;

    private List<String> disallows;

    public RobotsRule() {
        allows = new ArrayList<>();
        disallows = new ArrayList<>();
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public List<String> getAllows() {
        return allows;
    }

    public void setAllows(List<String> allows) {
        this.allows = allows;
    }

    public List<String> getDisallows() {
        return disallows;
    }

    public void setDisallows(List<String> disallows) {
        this.disallows = disallows;
    }

    public static RobotsRuleBuilder builder() {
        return new RobotsRuleBuilder();
    }

    public static class RobotsRuleBuilder {

        private RobotsRule rule;

        public RobotsRuleBuilder() {
            rule = new RobotsRule();
        }

        public RobotsRuleBuilder userAgent(String userAgent) {
            rule.setUserAgent(userAgent);
            return this;
        }

        public RobotsRuleBuilder userAgentAll() {
            rule.setUserAgent("*");
            return this;
        }

        public RobotsRuleBuilder allows(List<String> allows) {
            rule.getAllows().addAll(allows);
            return this;
        }

        public RobotsRuleBuilder allow(String allow) {
            rule.getAllows().add(allow);
            return this;
        }

        public RobotsRuleBuilder allowAll() {
            rule.getAllows().add("/");
            return this;
        }

        public RobotsRuleBuilder disallows(List<String> disallows) {
            rule.getDisallows().addAll(disallows);
            return this;
        }

        public RobotsRuleBuilder disallow(String disallow) {
            rule.getDisallows().add(disallow);
            return this;
        }

        public RobotsRuleBuilder disallowAll() {
            rule.getDisallows().add("/");
            return this;
        }

        public RobotsRule build() {
            if (rule.getUserAgent() == null) {
                throw new RobotsRuleException("UserAgent is not specified!");
            }
            if (rule.getAllows().isEmpty() && rule.getDisallows().isEmpty()) {
                throw new RobotsRuleException("Either allows and / or disallows must be present!");
            }
            return rule;
        }

    }
}
