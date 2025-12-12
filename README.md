# Career Review (이력서 첨삭 플랫폼)

이력서를 작성, 관리하고 다른 사용자로부터 피드백을 받을 수 있는 웹 애플리케이션입니다.
Git의 버전 관리 시스템에서 영감을 받아, 이력서 수정 시 새로운 버전을 생성하여 히스토리를 관리할 수 있는 기능을 제공합니다.

## 🚀 주요 기능

### 1. 이력서 관리 (Resume Management)
- **구조화된 데이터 입력**: 기본 정보, 자기소개서, 경력, 학력, 프로젝트, 기술 스택(Skill) 등 섹션별로 체계적인 입력 가능
- **동적 폼(Dynamic Form)**: 자바스크립트를 활용하여 경력, 프로젝트 등의 항목을 자유롭게 추가/삭제 가능
- **기술 스택 시각화**: 상/중/하 레벨에 따른 직관적인 뱃지 디자인 적용

### 2. 버전 관리 시스템 (Version Control)
- **히스토리 저장**: 이력서를 수정할 때마다 덮어쓰지 않고 새로운 버전(v1, v2...)으로 저장
- **커밋 메시지**: 수정 시 변경 사항(Commit Message)을 기록하여 변경 이력 추적 가능
- **버전 탐색**: 사이드바를 통해 이전 버전의 이력서 내용을 언제든지 조회 가능

### 3. 공개 범위 설정 (Visibility)
- **Public / Private**: 이력서별로 공개/비공개 설정 가능
- **내 이력서 관리**: '내 이력서' 페이지에서 내가 작성한 이력서의 최신 버전을 한눈에 모아보고 상태 관리

### 4. 피드백 시스템 (Feedback)
- **첨삭 요청**: 공개된 이력서에 대해 다른 사용자들이 피드백(댓글) 작성 가능
- **상호 작용**: 작성자와 리뷰어 간의 소통 지원

## 🛠 기술 스택 (Tech Stack)

- **Backend**: Java 17, Spring Boot 3.x, Spring Data JPA, Spring Security
- **Frontend**: Thymeleaf, Bootstrap 5, JavaScript (Vanilla)
- **Database**: H2 (Dev), MySQL (Prod)
- **Build Tool**: Gradle

## 📂 프로젝트 구조

```
src/main/java/com/mysite/career/review
├── resume          # 이력서 도메인 (Controller, Service, Entity, DTO)
├── user            # 사용자 관리 (로그인, 회원가입)
├── feedback        # 피드백 도메인
├── audit           # 공통 엔티티 (생성일/수정일)
└── config          # Security 등 설정 파일
```

## 📝 실행 방법

1. 프로젝트 클론
   ```bash
   git clone https://github.com/your-repo/Spring_Project.git
   ```
2. 빌드 및 실행
   ```bash
   ./gradlew bootRun
   ```
3. 접속
   - 브라우저에서 `http://localhost:8080` 접속
