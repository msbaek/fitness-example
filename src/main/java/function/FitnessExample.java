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
                if (includeSuiteSetup)
                    includeInherited(SuiteResponder.SUITE_SETUP_NAME, "setup");
                includeInherited("SetUp", "setup");
            }

            buffer.append(pageData.getContent());
            if (pageData.hasAttribute("Test")) {
                includeInherited("TearDown", "teardown");
                if (includeSuiteSetup)
                    includeInherited(SuiteResponder.SUITE_TEARDOWN_NAME, "teardown");
            }

            pageData.setContent(buffer.toString());
            return pageData.getHtml();
        }

        private void includeInherited(String pageName, String mode) throws Exception {
            WikiPage suiteSetup = PageCrawlerImpl.getInheritedPage(pageName, wikiPage);
            if (suiteSetup != null)
                includePage(suiteSetup, mode);
        }

        private void includePage(WikiPage suiteSetup, String mode) throws Exception {
            WikiPagePath pagePath = wikiPage.getPageCrawler().getFullPath(suiteSetup);
            String pagePathName = PathParser.render(pagePath);
            buffer.append("!include -" + mode + " .").append(pagePathName).append("\n");
        }
    }
}
