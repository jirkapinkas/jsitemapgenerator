package cz.jiripinkas.jsitemapgenerator.robots;

import cz.jiripinkas.jsitemapgenerator.UrlUtil;

import java.util.ArrayList;
import java.util.List;

public class RobotsTxtGenerator {

    private List<RobotsRule> rules = new ArrayList<>();

    private List<String> sitemaps = new ArrayList<>();

    private String baseUrl;

    public static RobotsTxtGenerator of(String baseUrl) {
        RobotsTxtGenerator robotsTxtGenerator = new RobotsTxtGenerator();
        if(!baseUrl.endsWith("/")) {
            baseUrl += "/";
        }
        robotsTxtGenerator.setBaseUrl(baseUrl);
        return robotsTxtGenerator;
    }

    public String[] constructRobotsTxt() {
        ArrayList<String> out = new ArrayList<>();
        sitemaps.forEach(sitemap -> out.add("Sitemap: " + UrlUtil.connectUrlParts(baseUrl, sitemap)));
        rules.forEach(rule -> {
            out.add("User-agent: " + rule.getUserAgent());
            rule.getAllows().forEach(allow -> out.add("Allow: " + allow));
            rule.getDisallows().forEach(disallow -> out.add("Disallow: " + disallow));
        });
        return out.toArray(new String[]{});
    }

    /**
     * Construct robots.txt String
     * @return Robots.txt string
     * @deprecated Use {@link #toString()} instead
     */
    @Deprecated
    public String constructRobotsTxtString() {
        return this.toString();
    }

    /**
     * Construct robots.txt String
     * @return Robots.txt String
     */
    public String toString() {
        return String.join("\n", constructRobotsTxt());
    }

    public RobotsTxtGenerator addSitemap(String sitemap) {
        sitemaps.add(sitemap);
        return this;
    }

    public RobotsTxtGenerator addRule(RobotsRule rule) {
        rules.add(rule);
        return this;
    }

    public List<RobotsRule> getRules() {
        return rules;
    }

    public void setRules(List<RobotsRule> rules) {
        this.rules = rules;
    }

    public List<String> getSitemaps() {
        return sitemaps;
    }

    public void setSitemaps(List<String> sitemaps) {
        this.sitemaps = sitemaps;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
