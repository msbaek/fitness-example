package function;

import fitnesse.responders.run.SuiteResponder;
import fitnesse.wiki.*;

public class FitnessExample {
    public String testableHtml(PageData pageData, boolean includeSuiteSetup) throws Exception {
        return new TestableHtmlMaker(pageData, includeSuiteSetup).invoke();
    }

    private class TestableHtmlMaker {
        private PageData pageData;
        private boolean includeSuiteSetup;
        private WikiPage wikiPage;
        private final StringBuffer buffer;

        public TestableHtmlMaker(PageData pageData, boolean includeSuiteSetup) {
            this.pageData = pageData;
            this.includeSuiteSetup = includeSuiteSetup;
            wikiPage = pageData.getWikiPage();
            buffer = new StringBuffer();
        }

        public String invoke() throws Exception {
            if (pageData.hasAttribute("Test")) {
                if (includeSuiteSetup) {
                    String pageName = SuiteResponder.SUITE_SETUP_NAME;
                    String mode = "setup";
                    WikiPage suiteSetup = PageCrawlerImpl.getInheritedPage(pageName, wikiPage);
                    if (suiteSetup != null) {
                        includePage(suiteSetup, mode);
                    }
                }
                String pageName = "SetUp";
                String mode = "setup";
                WikiPage setup = PageCrawlerImpl.getInheritedPage(pageName, wikiPage);
                if (setup != null) {
                    includePage(setup, mode);
                }
            }

            buffer.append(pageData.getContent());
            if (pageData.hasAttribute("Test")) {
                String pageName = "TearDown";
                String mode = "teardown";
                WikiPage teardown = PageCrawlerImpl.getInheritedPage(pageName, wikiPage);
                if (teardown != null) {
                    includePage(teardown, mode);
                }
                if (includeSuiteSetup) {
                    String pageName1 = SuiteResponder.SUITE_TEARDOWN_NAME;
                    String mode1 = "teardown";
                    WikiPage suiteTeardown = PageCrawlerImpl.getInheritedPage(pageName1, wikiPage);
                    if (suiteTeardown != null) {
                        includePage(suiteTeardown, mode1);
                    }
                }
            }

            pageData.setContent(buffer.toString());
            return pageData.getHtml();
        }

        private void includePage(WikiPage suiteSetup, String mode) throws Exception {
            WikiPagePath pagePath = wikiPage.getPageCrawler().getFullPath(suiteSetup);
            String pagePathName = PathParser.render(pagePath);
            buffer.append("!include -" + mode + " .").append(pagePathName).append("\n");
        }
    }
}
