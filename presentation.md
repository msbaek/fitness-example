<!-- $theme: gaia -->

# 큰 함수를 리팩토링하기
# Tech Infra 개발본부 
# 백명석

---

# 1. Characterization Test

```
vim에서 
- \\\ -> \
- \\ -> \

remove magic number([-]*\\d+)
```

---

# 2. extract method object - TestableHtmlMaker

큰 함수
- 함수 내에서 사용되는 일련의 변수들
- 변수들에 동작하는 기능 블록들

-->

Class
- 일련의 변수(멤버 변수들)
- 이에 동작하는 메소드들

---

# 3. extract fields - wikiPage, buffer

- extract method object 후의 작업
- 여러 곳에서 사용되는 변수들을 멤버 변수로
- 함수 추출시 파라미터 개수 감소, void 반환 증대

---

# 4. extract method - includePage

extract method의 3단계
- extract variable - setup, teardown
	- move line up
- extract method
- inline extracted variable


rename variables

---

# 5. extract method - includeInherited

  5.1 extract variable - setup, teardown
  5.2 move line up
  5.3 extract method
  5.4 inline extracted variable
  5.5 change signature - remove wikiPage from parameter
  5.5 rename variables(pageName)

---

# 6. Extract method - includeSetups, includeTeardowns

---

# 7. merge if statement

---

# 8. extract method - isTestPage

---

# 9. change StringBuffer to String
- 1 rename buffer to content
- 2 fix compile error
- 3 CQS
	- 3.1 invoke에 String을 반환받도록 수정
	- 3.2 fix compile error

---

10. extract method - surroundPageWithSetUpsAndTearDowns

---

11. rename - SetUpTearDownSurrounder#surround
