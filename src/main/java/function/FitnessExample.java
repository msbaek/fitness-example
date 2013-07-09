package function;

import fitnesse.responders.run.SuiteResponder;
import fitnesse.wiki.*;

public class FitnessExample {
    public String testableHtml(PageData pageData, boolean includeSuiteSetup) throws Exception {
        return new SetUpTearDownSurrounder(pageData, includeSuiteSetup).surround();
    }

    private class SetUpTearDownSurrounder {
        private PageData pageData;
        private boolean includeSuiteSetup;
        private WikiPage wikiPage;
        private String content;

        public SetUpTearDownSurrounder(PageData pageData, boolean includeSuiteSetup) {
            this.pageData = pageData;
            this.includeSuiteSetup = includeSuiteSetup;
            wikiPage = pageData.getWikiPage();
            content = new String();
        }

        public String surround() throws Exception {
            if (ifTestPage())
                surroundPageWithSetUpsAndTearDowns();
            return pageData.getHtml();
        }

        private void surroundPageWithSetUpsAndTearDowns() throws Exception {
            content = includeSetups();
            content += pageData.getContent();
            content += includeTearDowns();
            pageData.setContent(content);
        }

        private boolean ifTestPage() throws Exception {
            return pageData.hasAttribute("Test");
        }

        private String includeTearDowns() throws Exception {
            String tearDowns = includeInherited("TearDown", "teardown");
            if (includeSuiteSetup)
                tearDowns += includeInherited(SuiteResponder.SUITE_TEARDOWN_NAME, "teardown");
            return tearDowns;
        }

        private String includeSetups() throws Exception {
            String setUps = "";
            if (includeSuiteSetup)
                setUps += includeInherited(SuiteResponder.SUITE_SETUP_NAME, "setup");
            setUps += includeInherited("SetUp", "setup");
            return setUps;
        }

        private String includeInherited(String pageName, String mode) throws Exception {
            WikiPage suiteSetup = PageCrawlerImpl.getInheritedPage(pageName, wikiPage);
            if (suiteSetup != null)
                return includePage(suiteSetup, mode);
            return "";
        }

        private String includePage(WikiPage suiteSetup, String mode) throws Exception {
            WikiPagePath pagePath = wikiPage.getPageCrawler().getFullPath(suiteSetup);
            String pagePathName = PathParser.render(pagePath);
            return String.format("!include -%s .%s\n", mode, pagePathName);
        }
    }
}
