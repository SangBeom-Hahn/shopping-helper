# 손그림 의류 검색 서비스

### 환경설정
- Spring Boot 3.3.3
- Gradle
- Java 17

### 의존성 정보
- Spring Web
- Lombok
- Spring Data JPA
- H2 Database
- MySQL Driver
- Validation

### 협업 방식

1. 브랜치는 항상 develop 브랜치에서 만든다.
2. 새로운 기능을 추가하기 위해 브랜치를 만든다면 이름을 명확히 작성한다. ex) feature/1-login
3. 깃 브랜치 전략을 따른다.
4. 피드백이나 도움이 필요할 때, 그리고 기능 구현이 완료되어 merge 준비가 됐을 때는 pull request(PR)을 생성한다.
5. 팀원들의 PR에 대한 코드 리뷰 후 master 브랜치로 merge를 한다.
6. stg 브랜치에서 운영환경으로 이관하기 전 최종 테스트를 취한다.

---

### 깃 커밋 컨벤션

1. 커밋 메세지는 제목, 본문으로 나눈다.
2. 제목 작성 후 본문 작성시 Enter로 한칸 띄운다.
3. 제목은 한글로 간결하게 요점만 작성한다.
4. 본문은 예시 형식을 따르고 제목에 전반적인 내용을 담고 본문에 세세하게 작성한다.

#### 제목 타입

- feat: 새로운 기능 추가
- fix: 버그 수정
- docs: 문서 수정
- refactor: 코드 리펙토링
- test: 테스트 코드 작성
- style: 단순 수정
- chore: 코드 수정 없이 설정 변경

#### 커밋 예시
ex)

feat: 회원 CRUD 기능을 구현
* feat: Member Repository 기능 및 테스트 구현

* feat: Member Service 기능 및 테스트 구현

* feat: Member Controller 기능 및 인수테스트 구현

---

### Pull Request 제목 작성
#### Pull Request 본문은 커밋 메세지를 상세하게 작성한다는 가정하에 생략한다.

- "작업 내용 요약" | {작업 브랜치} => {develop} 
  - ex) 로그인 기능 구현 | {feature/login} => {develop}

---
