# Clean Coders Video의 fitness Refactoring 예제

이 프로젝트는 Robert C. Martin이 Clean Coders에서 Function에 대해 설명할 때 사용한 예제입니다.

여러분들도 try-yourself branch에서 작업을 해 보실 수 있습니다. 또 제가 수행한 답에 가까운 작업은 master branch에 반영되었습니다.

# 1. FitnessExample 설치

```
git clone https://github.com/msbaek/fitness-example 
cd fitness-example
git checkout -b try-yourself origin/try-yourself
```

# 2. Characterization Test 추가

- WEWLC의 Characterization Test 기법을 적용
	- 테스트에서 결과를 화면에 출력할 수 있도록 하고,
	- 절대 맞을 수 없는 값(expected result)과 결과를 비교해서 실패하도록 하고,
	- 테스트가 성공하도록 expected result를 수정하여 테스트를 성공시킨다.

# 3. Extract Method Object - TestableHtmlMaker

- 큰 함수는 클래스이다.

# 4. extract field - wikiPage, buffer

- 함수의 여러 곳에서 사용되는 변수들은 큰 함수를 작은 함수로 분리할 때 인자로 사용되어 불편하다.
- 필드로 올려서 인자의 개수를 줄인자.
- 이렇게 하면 extract method 등의 리팩토링을 할 때 매우 수월하다. 특히 한 메소드에서 2개 이상의 변수를 변경하는 경우, 필드로 추출하지 않으면 extract 불가
- 이러한 리팩토링 후에는 반드시 테스트를 수행해서 안전 여부를 확인

# 5. Extract Method
```
WikiPagePath pagePath = wikiPage.getPageCrawler().getFullPath(suiteSetup);
String pagePathName = PathParser.render(pagePath);
buffer.append("!include -setup .").append(pagePathName).append("\n");
```

위 코드가 4군데 중복되고 있다.

## 5.1 Extract Varaible

- 구조적으로 동일하나 다른 점들이 있다. `setup`, `teardown` 등의 상수
- 이에 대해 `extract variabler`를 적용한다.

## 5.2 Move line up

- 상수 정의를 메소드로 추출한 블록 위로 이동

## 5.2 Extract method - includePage

## 5.3 Extract Variable

- 5.1에서 처럼 구조적으로 동일하게 하기 위해 `PageCrawlerImpl.getInheritedPage` 호출에 있는 첫번째 인자를 변수로 추출한다.

## 5.4 Extadct method - includeInherited

```
WikiPage suiteSetup = PageCrawlerImpl.getInheritedPage(pageName, wikiPage);
if (suiteSetup != null) {
    includePage(suiteSetup, mode);
}
```

위 구조를 `includeInherited`로 추출

## 5.5 inline variables after extract method

- 다른 부분을 파라미터 처리하여 갖게 만들기 위해 추출했던 변수들을 인라인하여 코드를 깨끗하게한다.

# 6. Extract method - includeSetups, includeTeardonws

# 7. merge if statement

- if 문장을 합쳐도 테스트가 정상 동작함.
- if 문장을 합쳐서 가독성 증대
- 이를 검증하기 위해 expectedHtmlForNonTestPage 변수를 테스트 케이스에 추가

# 8. Extract method - isTestPage

# 9. Change StringBuffer to String
- 가독성 증대를 위해

```
		includeSetups();
		buffer.append(pageData.getContent());
		includeTearDowns();
		pageData.setContent(buffer.toString());
```

```
		content = includeSetups();
		content += pageData.getContent();
		content += includeTearDowns();
		pageData.setContent(content);
```
	
- 먼저 buffer를 content로 rename
- StringBuffer를 String으로 변경하고 컴파일 오류 제거
- 테스트를 통해 확인

# 10. Extract method - surroundPageWithSetUpsAndTearDowns

# 11. Rename Class#Method - SetUpTearDownSurrounder#surround
