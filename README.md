# MGV-Project
파이널 프로젝트 - 영화관 웹 서비스 구축

# TASK-FORCE

## 🦹‍ Team
|이진서|이재권|진현국|서준호|오민선|신하원|
|:---:|:---:|:---:|:---:|:---:|:---:|
|회원|스토어|극장|영화,예매|고객센터|게시판|
|[jinseo](https://github.com/hia0706)|[ljg10sky](https://github.com/ljg10sky)|[hyungook](https://github.com/hyungook-jin)|[MooingcowJoon](https://github.com/MooingcowJoon)|[sunminohh](https://github.com/sunminohh)|[Ha!won](https://github.com/Shinhawo)|
|![](https://avatars.githubusercontent.com/u/94761254?v=4)|![](https://avatars.githubusercontent.com/u/130030785?v=4)|![](https://avatars.githubusercontent.com/u/130140565?v=4)|![](https://avatars.githubusercontent.com/u/130448178?v=4)|![](https://avatars.githubusercontent.com/u/130140763?v=4)|![](https://avatars.githubusercontent.com/u/122660720?v=4)|

## 📚 STACKS
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> <img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black"> <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"> <img src="https://img.shields.io/badge/thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white"> <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
## 🛠Tools 
<img src="https://img.shields.io/badge/apachemaven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white"> <img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white"> <img src="https://img.shields.io/badge/sourcetree-0052CC?style=for-the-badge&logo=sourcetree&logoColor=white">

## Collaborations
<img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white"> <img src="https://img.shields.io/badge/notion-000000?style=for-the-badge&logo=notion&logoColor=white">

## 📝 Ground Rule
- 매 회의때 자신의 진행중인 기능 진척도 말하기
- 정해진 목표까지 진행하고 시간이 남을 때 간략하게 회의하고 진행하기
- 서로 존중해주기
- 팀원의 의견을 들을 때 가능한 방향으로 먼저 생각하기
- 오류나 해결되지 않는 문제는 멘토 혹은 강사님에게 질문하기

## 👥 Git & Github rule

### `Branch rule`
**main**
- 조장 외 건드리지 말 것!
- 필요한 상황에서는 꼭 말하고 작업하기

**develop**
- main이라고 생각하고 개발하기
- main브랜치보다 앞서야 한다.
- 팀에서 유지하는 최신 코드다.

**feature**
- develop브랜치에서 분기하여 만든다.
- 각자 이름으로 feature브랜치를 만들며, 브랜치명은 `'feature/이름'`으로 한다.
  + ex) feature/minsu

### `Commit rule`

- `Feat`: 기능 추가, 삭제, 변경(or ✨ emoji) - 제품 코드 수정 발생
- `Fix`: 버그 수정(or 🚑 emoji) - 제품 코드 수정 발생
- `Doc`: 문서 추가, 삭제, 변경(or 📚 emoji) - 코드 수정 없음
- `Style`: 코드 형식, 정렬, 주석 등의 변경, eg; 세미콜론 추가(or 🎨 emoji) - 제품 코드 수정 발생, 하지만 동작에 영향을 주는 변경은 없음
- `Etc`: 위에 해당하지 않는 모든 변경, eg. 빌드 스크립트 수정, 패키지 배포 설정 변경 - 코드 수정 없음
- `Init`: 첫 프로젝트 커밋

---

# 주제
## 영화관 사이트 클론

### 서비스 설명
메가박스, CGV 영화관 멀티플렉스 서비스

### 기간
23.07.31 ~ 23.09.12 (6주)

### 서비스 핵심 기능
- 이메일을 이용한 본인인증
- 파티게시판과 실시간 알림 기능
- 영화 예매 서비스
- 실시간 1:1 상담톡 기능

#### 회원가입 / 로그인
- 회원가입 아이디 중복확인, 만 12세 이상 가입 가능
- 본인인증으로 이메일 인증 완료 시 회원가입 완료
- OAuth2.0 을 이용한 카카오 소셜로그인 및 회원가입 기능

#### 마이페이지
- 이메일인증 후 회원정보수정과 프로필 사진 등록 및 회원탈퇴 기능

#### 예매/구매내역
- 예매내역에서 예매한 내역을 확인할 수 있고 예매취소가 가능하다.
- 구매내역에서 스토어, 장바구니 구매내역과 취소내역을 확인할 수 있고 구매취소가 가능하다.

#### 영화
- 모든 사용자는 영화를 확인할 수 있고 좋아요 기능과 상세페이지를 확인할 수 있다.
- 관리자는 영화 등록 및 수정 스케쥴 관리를 할 수 있다.

#### 예매
- 비회원은 이용불가하며 회원일 때 이용가능하다.
- 결제는 토스페이로 진행

#### 극장
- 사용자는 지역별 극장 목록과 각 극장의 상세 정보를 확인할 수 있고, 자주가는 극장 등록, 삭제가 가능하다.
- 관리자는 극장 등록, 수정, 삭제를 할 수 있다.

#### 스토어
- 카테고리 별로 상품을 분류해 해당 카테고리를 클릭 시 카테고리에 맞는 상품목록이 출력된다.
- 원하는 상품을 클릭 시 해당 상품의 상세페이지를 출력하며 상품의 수량 및 결제금액을 확인할 수 있다.

#### 결제
- 장바구니 페이지에서 바로구매로 토스 demo를 이용하여 결제기능을 구현

#### 게시판
- 사용자는 새 게시글을 등록하거나, 댓글 달기, 댓글 삭제, 게시물 수정, 삭제, 신고등을 할 수 있다.
- 관리자는 게시물을 삭제하거나 복구시킬 수 있고, 신고된 게시물의 신고 정보를 확인하여 신고처리를 하거나, 복구할 수 있다.
- 파티 게시판을 이용하여 다른 사용자와 영화를 볼 수 있다.

#### 고객센터
- 사용자는 분실물 문의, 1:1 문의글을 등록할 수 있고, 영화관에 등록 되어있는 자주 묻는 질문, 공지사항을 확인할 수 있다.
- 관리자는 사용자가 등록한 분실물 문의, 1:1 문의글에 답변할 수 있고, 영화관 사이트에 자주 묻는 질문, 공지사항을 등록할 수 있다.
- 1:1 문의 채팅을 이용할 수 있다.

#### 이벤트
- 사용자는 등록되어 있는 이벤트 목록을 확인할 수 있다.
- 관리자는 이벤트 등록 및 수정 삭제가 가능하다.

## 설계
- DB ERD : [Link](https://www.erdcloud.com/d/r7eZogTkYjgkTEZsr)
