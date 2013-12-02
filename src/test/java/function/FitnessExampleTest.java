package function;

import fitnesse.wiki.*;
import org.junit.Before;
import org.junit.Test;

public class FitnessExampleTest {
    private PageData pageData;
    private PageCrawler crawler;
    private WikiPage root;
    private WikiPage testPage;

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
        String testableHtml = new FitnessExample().testableHtml(pageData, true);
        System.out.printf("testableHtml=[%s]\n", testableHtml);
    }
}
