package function;

import fitnesse.wiki.*;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class FitnessExampleTest {
    private PageData pageData;
    private PageCrawler crawler;
    private WikiPage root;
    private WikiPage testPage;
    private String expectedResultForTestCase = "<div class=\"setup\">\n" +
            "\t<div style=\"float: right;\" class=\"meta\"><a href=\"javascript:expandAll();\">Expand All</a> | <a href=\"javascript:collapseAll();\">Collapse All</a></div>\n" +
            "\t<a href=\"javascript:toggleCollapsable('');\">\n" +
            "\t\t<img src=\"/files/images/collapsableOpen.gif\" class=\"left\" id=\"img\"/>\n" +
            "\t</a>\n" +
            "&nbsp;<span class=\"meta\">Set Up: <a href=\"SuiteSetUp\">.SuiteSetUp</a> <a href=\"SuiteSetUp?edit&amp;redirectToReferer=true&amp;redirectAction=\">(edit)</a></span>\n" +
            "\t<div class=\"collapsable\" id=\"\">suiteSetUp</div>\n" +
            "</div>\n" +
            "<div class=\"setup\">\n" +
            "\t<div style=\"float: right;\" class=\"meta\"><a href=\"javascript:expandAll();\">Expand All</a> | <a href=\"javascript:collapseAll();\">Collapse All</a></div>\n" +
            "\t<a href=\"javascript:toggleCollapsable('');\">\n" +
            "\t\t<img src=\"/files/images/collapsableOpen.gif\" class=\"left\" id=\"img\"/>\n" +
            "\t</a>\n" +
            "&nbsp;<span class=\"meta\">Set Up: <a href=\"SetUp\">.SetUp</a> <a href=\"SetUp?edit&amp;redirectToReferer=true&amp;redirectAction=\">(edit)</a></span>\n" +
            "\t<div class=\"collapsable\" id=\"\">setup</div>\n" +
            "</div>\n" +
            "<span class=\"meta\">variable defined: TEST_SYSTEM=slim</span><br/>the content!include -teardown <a href=\"TearDown\">.TearDown</a><br/><div class=\"teardown\">\n" +
            "\t<div style=\"float: right;\" class=\"meta\"><a href=\"javascript:expandAll();\">Expand All</a> | <a href=\"javascript:collapseAll();\">Collapse All</a></div>\n" +
            "\t<a href=\"javascript:toggleCollapsable('');\">\n" +
            "\t\t<img src=\"/files/images/collapsableOpen.gif\" class=\"left\" id=\"img\"/>\n" +
            "\t</a>\n" +
            "&nbsp;<span class=\"meta\">Tear Down: <a href=\"SuiteTearDown\">.SuiteTearDown</a> <a href=\"SuiteTearDown?edit&amp;redirectToReferer=true&amp;redirectAction=\">(edit)</a></span>\n" +
            "\t<div class=\"collapsable\" id=\"\">suiteTearDown</div>\n" +
            "</div>\n";

    private String expectedResultForNonTestCase = "<div class=\"setup\">\n"+
            "\t<div style=\"float: right;\" class=\"meta\"><a href=\"javascript:expandAll();\">Expand All</a> | <a href=\"javascript:collapseAll();\">Collapse All</a></div>\n"+
            "\t<a href=\"javascript:toggleCollapsable('');\">\n"+
            "\t\t<img src=\"/files/images/collapsableOpen.gif\" class=\"left\" id=\"img\"/>\n"+
            "\t</a>\n"+
            "&nbsp;<span class=\"meta\">Set Up: <a href=\"SetUp\">.SetUp</a> <a href=\"SetUp?edit&amp;redirectToReferer=true&amp;redirectAction=\">(edit)</a></span>\n"+
            "\t<div class=\"collapsable\" id=\"\">setup</div>\n"+
            "</div>\n"+
            "<div class=\"setup\">\n"+
            "\t<div style=\"float: right;\" class=\"meta\"><a href=\"javascript:expandAll();\">Expand All</a> | <a href=\"javascript:collapseAll();\">Collapse All</a></div>\n"+
            "\t<a href=\"javascript:toggleCollapsable('');\">\n"+
            "\t\t<img src=\"/files/images/collapsableOpen.gif\" class=\"left\" id=\"img\"/>\n"+
            "\t</a>\n"+
            "&nbsp;<span class=\"meta\">Set Up: <a href=\"SuiteSetUp\">.SuiteSetUp</a> <a href=\"SuiteSetUp?edit&amp;redirectToReferer=true&amp;redirectAction=\">(edit)</a></span>\n"+
            "\t<div class=\"collapsable\" id=\"\">suiteSetUp</div>\n"+
            "</div>\n"+
            "<div class=\"setup\">\n"+
            "\t<div style=\"float: right;\" class=\"meta\"><a href=\"javascript:expandAll();\">Expand All</a> | <a href=\"javascript:collapseAll();\">Collapse All</a></div>\n"+
            "\t<a href=\"javascript:toggleCollapsable('');\">\n"+
            "\t\t<img src=\"/files/images/collapsableOpen.gif\" class=\"left\" id=\"img\"/>\n"+
            "\t</a>\n"+
            "&nbsp;<span class=\"meta\">Set Up: <a href=\"SetUp\">.SetUp</a> <a href=\"SetUp?edit&amp;redirectToReferer=true&amp;redirectAction=\">(edit)</a></span>\n"+
            "\t<div class=\"collapsable\" id=\"\">setup</div>\n"+
            "</div>\n"+
            "<span class=\"meta\">variable defined: TEST_SYSTEM=slim</span><br/>the content!include -teardown <a href=\"TearDown\">.TearDown</a><br/><div class=\"teardown\">\n"+
            "\t<div style=\"float: right;\" class=\"meta\"><a href=\"javascript:expandAll();\">Expand All</a> | <a href=\"javascript:collapseAll();\">Collapse All</a></div>\n"+
            "\t<a href=\"javascript:toggleCollapsable('');\">\n"+
            "\t\t<img src=\"/files/images/collapsableOpen.gif\" class=\"left\" id=\"img\"/>\n"+
            "\t</a>\n"+
            "&nbsp;<span class=\"meta\">Tear Down: <a href=\"SuiteTearDown\">.SuiteTearDown</a> <a href=\"SuiteTearDown?edit&amp;redirectToReferer=true&amp;redirectAction=\">(edit)</a></span>\n"+
            "\t<div class=\"collapsable\" id=\"\">suiteTearDown</div>\n"+
            "</div>\n"+
            "<div class=\"teardown\">\n"+
            "\t<div style=\"float: right;\" class=\"meta\"><a href=\"javascript:expandAll();\">Expand All</a> | <a href=\"javascript:collapseAll();\">Collapse All</a></div>\n"+
            "\t<a href=\"javascript:toggleCollapsable('');\">\n"+
            "\t\t<img src=\"/files/images/collapsableOpen.gif\" class=\"left\" id=\"img\"/>\n"+
            "\t</a>\n"+
            "&nbsp;<span class=\"meta\">Tear Down: <a href=\"TearDown\">.TearDown</a> <a href=\"TearDown?edit&amp;redirectToReferer=true&amp;redirectAction=\">(edit)</a></span>\n"+
            "\t<div class=\"collapsable\" id=\"\">teardown</div>\n"+
            "</div>\n";

    @Before
    public void setUp() throws Exception {
        root = InMemoryPage.makeRoot("RooT");
        crawler = root.getPageCrawler();
        testPage = addPage("TestPage", "!define TEST_SYSTEM {slim}\n" + "the content");
        addPage("SetUp", "setup");
        addPage("TearDown", "teardown");
        addPage("SuiteSetUp", "suiteSetUp");
        addPage("SuiteTearDown", "suiteTearDown");

        crawler.addPage(testPage, PathParser.parse("ScenarioLibrary"), "scenario library 2");

        pageData = testPage.getData();
    }

    private WikiPage addPage(String pageName, String content) throws Exception {
        return crawler.addPage(root, PathParser.parse(pageName), content);
    }

    private String removeMagicNumber(String expectedResult) {
        return expectedResult.replaceAll("[-]*\\d+", "");
    }

    @Test
    public void testableHtml() throws Exception {
        generateHtmlAndAssert(true, expectedResultForTestCase);
        generateHtmlAndAssert(false, expectedResultForNonTestCase);
    }

    private void generateHtmlAndAssert(boolean includeSuiteSetup, String expectedResult) throws Exception {
        String testableHtml = new FitnessExample().testableHtml(pageData, includeSuiteSetup);
        assertThat(removeMagicNumber(testableHtml), is(removeMagicNumber(expectedResult)));
    }
}
