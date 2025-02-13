콘서트, 뮤지컬 , 스포츠 티켓를 예매 할 수 있는 서비스

# 무엇을 경험하려는 건가요?

- 누군가 시키는 일이 아닌, 아이템 선정부터 설계 구현까지 혼자서 진행하고 마무리해보는 경험
- <2024 KBO 포스트 시즌> 예매 실패로 인해 생격난 “이 서비스는 대체 어떻게 만들어졌길래 인터파크 서버시간으로 들어가도 대기표를 받을까?” 에 대한 궁금증 해소 경험
- 대기열 시스템 개념과 구현하는 경험


# 서비스는 어떻게 구성되어 있나요?
### 서버 구성도
![서버구성도](https://github.com/user-attachments/assets/31b25a04-1443-4069-9096-ddc2d0d90409)


# WIKI
| 제목                                                                                                  | 일자             |
|-----------------------------------------------------------------------------------------------------|----------------|
| [Home](https://github.com/f-lab-edu/getto-ticket/wiki)                                              | 2024-10-10 (목) |
| [01. 초기화](https://github.com/f-lab-edu/getto-ticket/wiki/01.-%EC%B4%88%EA%B8%B0%ED%99%94)           | 2024-10-10 (목) |
| [02. 요구사항](https://github.com/f-lab-edu/getto-ticket/wiki/02.-%EC%9A%94%EA%B5%AC%EC%82%AC%ED%95%AD) | 2025-02-06 (목) |
| [03. UseCase](https://github.com/f-lab-edu/getto-ticket/wiki/03.-UseCase)                           | 2025-02-06 (목) |
| [04. API](https://github.com/f-lab-edu/getto-ticket/wiki/04.-API)                                   | 2025-02-06 (목) |
| [05. ERD](https://github.com/f-lab-edu/getto-ticket/wiki/05.-ERD)                                   | 2025-02-06 (목) |


# 프로젝트 회고
프로젝트를 구상할 때 고민했으면 좋았을텐데싶은 만들고 난 뒤에 후회와   
그 당시 고민의 결론이 났었던 고민들에 대한 회고

| 주제                                                                                                                                                                                                                                                                  |
|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [프로젝트 시작하기](https://github.com/f-lab-edu/getto-ticket/wiki/9.-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%ED%9A%8C%EA%B3%A0#%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EC%8B%9C%EC%9E%91%ED%95%98%EA%B8%B0)|
| [PR의 책임과 분리](https://github.com/f-lab-edu/getto-ticket/wiki/9.-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%ED%9A%8C%EA%B3%A0#pr%EC%9D%98-%EC%B1%85%EC%9E%84%EA%B3%BC-%EB%B6%84%EB%A6%AC)|
| [MySQL에서 왜 sequence를 찾아?](https://github.com/f-lab-edu/getto-ticket/wiki/9%E2%80%902.-MySQL%EC%97%90%EC%84%9C-%EC%99%9C-sequence%EB%A5%BC-%EC%B0%BE%EC%95%84%3F)|
| [Redis 단일 인스턴스 성능과 역할 분리](https://github.com/f-lab-edu/getto-ticket/wiki/9.-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%ED%9A%8C%EA%B3%A0#redis-%EB%8B%A8%EC%9D%BC-%EC%9D%B8%EC%8A%A4%ED%84%B4%EC%8A%A4-%EC%84%B1%EB%8A%A5%EA%B3%BC-%EC%97%AD%ED%95%A0-%EB%B6%84%EB%A6%AC)|
| [대기열 생성 전략](https://github.com/f-lab-edu/getto-ticket/wiki/9.-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%ED%9A%8C%EA%B3%A0#%EB%8C%80%EA%B8%B0%EC%97%B4-%EC%83%9D%EC%84%B1-%EC%A0%84%EB%9E%B5)|
| [Lock 전략 선택](https://github.com/f-lab-edu/getto-ticket/wiki/9.-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%ED%9A%8C%EA%B3%A0#lock-%EC%A0%84%EB%9E%B5-%EC%84%A0%ED%83%9D)|
| [Redis Key 보안](https://github.com/f-lab-edu/getto-ticket/wiki/9.-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%ED%9A%8C%EA%B3%A0#redis-key-%EB%B3%B4%EC%95%88)|
| [프로젝트 종료하기](https://github.com/f-lab-edu/getto-ticket/wiki/9.-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%ED%9A%8C%EA%B3%A0#%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EC%A2%85%EB%A3%8C%ED%95%98%EA%B8%B0)|


## 프로젝트 시작하기
익숙한 기술, 이미 정의되어 있는 요구사항과 설계가 있는 상황에서 개발을 해오다 프로젝트 주제부터 주체적으로 정하려니 막막함이 앞섰다.  
막막함 때문에 무엇도 선택하지 못하고 있을 때 작년 KBO 포스트시즌 티켓팅 실패라는 경험 덕분에 고민이 단순해졌다.  
“인터파크 예매 시스템을 이해하면 티켓팅 성공에 가까워질 수 있지 않을까?” 라는 지극히 개인적인 욕심과 개발자만 떠올릴 수 있는 궁금증으로 이 프로젝트를 시작할 수 있게 되었다.

**선택이유**
- 티켓팅에 실패한 개발자 동지들의 공감대를 형성할 수 있다
- 참고할 서비스와 프로젝트들이 있다
- 가설과 검증을 통해 대기열 시스템을 구현하고 경험해볼 수 있다

### 자매품) 정말 마음대로 설계해도 될까?
회사에서 업무 진행시 팀에서 배정된 역할만 담당했기에 기술적 고민과 선택은 개인이 아닌 팀원들과의 대화 또는 코더였다.  
하지만 이 프로젝트는 온전히 스스로 고민해야 하고 의사 결정도 타인이 수긍할 수 있는 근거가 있어야 했기에 낯선 상황뿐이였다.  
더군다나 고민 없이 사용하던 스프링 버전, 데이터베이스, 라이브러리들이 매번 선택으로 다가오니 고민을 해결해줄 기준이 필요했다.

**선택기준**
- 공식 지원 종료된 것은 사용하지 않는다
- Spring에서 지원하는 라이브러리와 동일한 기능을 지원하는 라이브러리는 추가하지 않는다
- 메이저 버전을 변경하고 싶다면 가용성이 큰 버전을 사용한다
- 이전 회사에서 익숙한, 오래된 것에서 벗어나 새로운 지식을 습득할 가치가 있는 것이 있다면 선택한다

### 자매품) 개발자의 궁금증을 요구사항으로
서비스가 궁금하다고 하여 처음부터 만들 수는 없었다.  
멘토링이 끝나기 전에 대기열 시스템을 다 만들어야 했기 때문에 절대적인 시간과 지식을 습득할 시간, 구현할 시간, 삽질할 시간이 부족했다.  
빠르고 간결하게 궁금증을 작성하는 것부터 시작하여 이슈를 만들어갔다.  
그러나 막상 작성된 요구사항을 기준으로 개발을 하다보니 기능간 결합도가 강한 부분과 필요하지 않는 요구사항들이 생겼다.

> “어디서 부터 잘못된 걸까?”

팔짱을 낀채 모니터를 뚫어져라 쳐다보아도 뭐가 문제인지 알 수 없었다.  
나의 지식의 한계로 발생한 문제였고, 상황을 해결할 방법으로 빠른길도 없었다.  
요구사항이란 무엇인가부터 시작했고 기존에 작성한 문서의 문제점을 찾게되었다.

<a href="https://github.com/f-lab-edu/getto-ticket/wiki/9%E2%80%901.-%EC%9E%90%EB%A7%A4%ED%92%88)-%EA%B0%9C%EB%B0%9C%EC%9E%90%EC%9D%98-%EA%B6%81%EA%B8%88%EC%A6%9D%EC%9D%84-%EC%9A%94%EA%B5%AC%EC%82%AC%ED%95%AD%EC%9C%BC%EB%A1%9C">본문보기</a>

<br>

## PR의 책임과 분리
자매품 시리즈 상황을 먼저 접하고 PR을 올렸다면 PR에 올라간 `Files Changed` 개수가 몇십개가 되는 사태는 벌어지지는 않았을텐데..  
덕분에 브랜치마다 rebase, merge의 늪에 빠졌었다. 각설은 접어두고, PR을 올릴때마다 PR의 책임이 커졌다 이유는 아래와 같다.

### `Files Changed` 개수가 몇십개가 된 이유

1. ‘A 기능을 수행하기 위해서는 B 기능도 함께 수행되어야 하는데 함께 PR을 올려야 리뷰어가 코드 맥락 파악할 수 있지 않을까’ 라는 생각
2. 구체적이지 않고 추상적이였던 요구사항
3. 구체적이지 않은 요구사항을 범주로한 그룹화 및 이슈 생성
4. 2번 요구사항 + 1번의 생각 = 기능간 결합도가 높아지고 PR의 책임이 커짐

[본문보기](https://github.com/f-lab-edu/getto-ticket/wiki/9.-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%ED%9A%8C%EA%B3%A0#pr%EC%9D%98-%EC%B1%85%EC%9E%84%EA%B3%BC-%EB%B6%84%EB%A6%AC)

<br>

## MySQL에서 왜 sequence를 찾아?
모든 의사결정에는 타당한 이유가 있어야하지만 이 프로젝트의 데이터베이스는 여태 회사에서 써온 MariaDB가 아닌 (단순히)오픈소스 데이터베이스 1위인 MySQL를 써보고 싶었다.  
하지만 왜 1위인지, MariaDB와 무엇이 다른점이 있는지, 언제 구분하여 사용해야하는지 알지 못한 선택이였다.  
고민이 짧았던 선택은 MySQL에서 sequence를 찾는 부끄러운 결과를 남길 수 밖에.. 문제될 것 없을거라 생각했던 데이터베이스 선택하기 위한 과정을 기록해본다.

**목차**
- [1. MySQL에 `SEQUENCE`가 없는 이유](https://github.com/f-lab-edu/getto-ticket/wiki/9%E2%80%902.-MySQL%EC%97%90%EC%84%9C-%EC%99%9C-sequence%EB%A5%BC-%EC%B0%BE%EC%95%84%3F#mysql%EC%97%90-sequence%EA%B0%80-%EC%97%86%EB%8A%94-%EC%9D%B4%EC%9C%A0)
- [2. `AUTO_INCREMENT` VS `SEQUENCE`](https://github.com/f-lab-edu/getto-ticket/wiki/9%E2%80%902.-MySQL%EC%97%90%EC%84%9C-%EC%99%9C-sequence%EB%A5%BC-%EC%B0%BE%EC%95%84%3F#auto_increment-vs-sequence)
- [3. `AUTO_INCREMENT`가 `SEQUENCE` 대체제가 될 수 없는 이유](https://github.com/f-lab-edu/getto-ticket/wiki/9%E2%80%902.-MySQL%EC%97%90%EC%84%9C-%EC%99%9C-sequence%EB%A5%BC-%EC%B0%BE%EC%95%84%3F#auto_increment%EA%B0%80-sequence-%EB%8C%80%EC%B2%B4%EC%A0%9C%EA%B0%80-%EB%90%A0-%EC%88%98-%EC%97%86%EB%8A%94-%EC%9D%B4%EC%9C%A0)
- [4. `SEQUENCE`를 사용하려는 이유](https://github.com/f-lab-edu/getto-ticket/wiki/9%E2%80%902.-MySQL%EC%97%90%EC%84%9C-%EC%99%9C-sequence%EB%A5%BC-%EC%B0%BE%EC%95%84%3F#sequence%EB%A5%BC-%EC%82%AC%EC%9A%A9%ED%95%98%EB%A0%A4%EB%8A%94-%EC%9D%B4%EC%9C%A0)
- [5. `SEQUENCE`가 동시성과 병목현상을 어떻게 보장해주지?](https://github.com/f-lab-edu/getto-ticket/wiki/9%E2%80%902.-MySQL%EC%97%90%EC%84%9C-%EC%99%9C-sequence%EB%A5%BC-%EC%B0%BE%EC%95%84%3F#sequence%EA%B0%80-%EB%8F%99%EC%8B%9C%EC%84%B1%EA%B3%BC-%EB%B3%91%EB%AA%A9%ED%98%84%EC%83%81%EC%9D%84-%EC%96%B4%EB%96%BB%EA%B2%8C-%EB%B3%B4%EC%9E%A5%ED%95%B4%EC%A3%BC%EC%A7%80)
- [6. `SEQUENCE`의 한계, 단점은 없을까?](https://github.com/f-lab-edu/getto-ticket/wiki/9%E2%80%902.-MySQL%EC%97%90%EC%84%9C-%EC%99%9C-sequence%EB%A5%BC-%EC%B0%BE%EC%95%84%3F#sequence%EC%9D%98-%ED%95%9C%EA%B3%84-%EB%8B%A8%EC%A0%90%EC%9D%80-%EC%97%86%EC%9D%84%EA%B9%8C)
- [7. 하지만 `SEQUENCE` 하나 때문에 MariaDB를 선택해야 할까? ](https://github.com/f-lab-edu/getto-ticket/wiki/9%E2%80%902.-MySQL%EC%97%90%EC%84%9C-%EC%99%9C-sequence%EB%A5%BC-%EC%B0%BE%EC%95%84%3F#%ED%95%98%EC%A7%80%EB%A7%8C-sequence-%ED%95%98%EB%82%98-%EB%95%8C%EB%AC%B8%EC%97%90-mariadb%EB%A5%BC-%EC%84%A0%ED%83%9D%ED%95%B4%EC%95%BC-%ED%95%A0%EA%B9%8C)
- [8. MySQL VS MariaDB ](https://github.com/f-lab-edu/getto-ticket/wiki/9%E2%80%902.-MySQL%EC%97%90%EC%84%9C-%EC%99%9C-sequence%EB%A5%BC-%EC%B0%BE%EC%95%84%3F#mysql-vs-mariadb)
- [9. `AUTO_INCREMENT`가 `SEQUENCE` 대체제가 될 수 있는 이유 ](https://github.com/f-lab-edu/getto-ticket/wiki/9%E2%80%902.-MySQL%EC%97%90%EC%84%9C-%EC%99%9C-sequence%EB%A5%BC-%EC%B0%BE%EC%95%84%3F#auto_increment%EA%B0%80-sequence-%EB%8C%80%EC%B2%B4%EC%A0%9C%EA%B0%80-%EB%90%A0-%EC%88%98-%EC%9E%88%EB%8A%94-%EC%9D%B4%EC%9C%A0)
- [10. MySQL을 사용해야 할 때 ](https://github.com/f-lab-edu/getto-ticket/wiki/9%E2%80%902.-MySQL%EC%97%90%EC%84%9C-%EC%99%9C-sequence%EB%A5%BC-%EC%B0%BE%EC%95%84%3F#mysql%EC%9D%84-%EC%82%AC%EC%9A%A9%ED%95%B4%EC%95%BC-%ED%95%A0-%EB%95%8C)
- [11. MariaDB을 사용해야 할 때 ](https://github.com/f-lab-edu/getto-ticket/wiki/9%E2%80%902.-MySQL%EC%97%90%EC%84%9C-%EC%99%9C-sequence%EB%A5%BC-%EC%B0%BE%EC%95%84%3F#mariadb%EC%9D%84-%EC%82%AC%EC%9A%A9%ED%95%B4%EC%95%BC-%ED%95%A0-%EB%95%8C)
- [12. 결론](https://github.com/f-lab-edu/getto-ticket/wiki/9%E2%80%902.-MySQL%EC%97%90%EC%84%9C-%EC%99%9C-sequence%EB%A5%BC-%EC%B0%BE%EC%95%84%3F#%EA%B2%B0%EB%A1%A0)

[본문보기](https://github.com/f-lab-edu/getto-ticket/wiki/9%E2%80%902.-MySQL%EC%97%90%EC%84%9C-%EC%99%9C-sequence%EB%A5%BC-%EC%B0%BE%EC%95%84%3F)

<br>

## Redis 단일 인스턴스 성능과 역할 분리
Redis의 ‘R’도 몰랐을 적에 “고민만 하고 공부만 하다가 시간에 쫓기지 말고 일단 만들어보자!”로 만들었다가  
한대의 Redis는 캐싱 데이터, 대기열 데이터, 메세지 브로커 역할을 맡았다.  
그러다 보니 정말 이게 최선인가? 싶었고 기억하고자 회고 주제중 하나로 삼게되었다.

### 단일 인스턴스를 선택한 이유

- 물리적인 Redis 서버를 확장 비용 부담
- 분산환경을 구축하려고 딥다이브 및 삽질하느라 대기열을 만들지 못할 것이라 판단
- 빠른 개발 속도

[본문보기](https://github.com/f-lab-edu/getto-ticket/wiki/9.-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%ED%9A%8C%EA%B3%A0#redis-%EB%8B%A8%EC%9D%BC-%EC%9D%B8%EC%8A%A4%ED%84%B4%EC%8A%A4-%EC%84%B1%EB%8A%A5%EA%B3%BC-%EC%97%AD%ED%95%A0-%EB%B6%84%EB%A6%AC)

<br>

## 대기열 생성 전략
프론트가 필요하지 않는데도 대기열에 필요하지도 않는 CRUD API를 만드는 작업을 끝내고나니 대기열을 만들어야 했다.  
처음엔 티켓 예매니까 티켓 예매에 대한 대기열만 생각했지 현실 세계에서 겪었던 ‘대기’의 경험은 떠올리지 못했었다.  
또한 블로그에서 설명하는 은행 창구, 놀이동산 메커니즘을 이해는 가능하나 이를 단계적으로 구체화 하는 논리력, 문제 해결 능력이 부족했다.  
또다시 시간에 쫓기는 개발을 하겠지만 ‘지식을 습득할 시간’에서 부터 시작해야했다.

### 대기열이란?

여러 프로세스에서 공유 자원을 동시에 요청할 때 발생할 동시성, 병목을 위해 고안된 방법으로 고객의 대기 시간을 관리하고 요청을 효율적으로 처리하기 위한 시스템이다.

[본문보기](https://github.com/f-lab-edu/getto-ticket/wiki/9.-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%ED%9A%8C%EA%B3%A0#%EB%8C%80%EA%B8%B0%EC%97%B4-%EC%83%9D%EC%84%B1-%EC%A0%84%EB%9E%B5)

<br>

## Lock 전략 선택
좌석 예매를 할 때 동시성을 제어하려면 Lock을 걸어야 한다는 새로운 키워드를 얻었다. 그럼 DB와 Redis 중 어느 곳에 Lock을 걸어야 시스템이 안정적이고 예상한 방향으로 동작할지 궁금했다. 하지만 선택을 하려면 Lock에 대해서 제대로 알고 적재적소에 배치해야 했다.

### 일단 Lock이 뭘까?
Lock은 여러 실행 단위가 공유된 자원을 동시에 접근할 때, 경쟁 상태에 놓이게 되는데 것을 방지하고 데이터의 일관성과 무결성을 보장하기 위한 제어 수단이다.


[본문보기](https://github.com/f-lab-edu/getto-ticket/wiki/9.-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%ED%9A%8C%EA%B3%A0#lock-%EC%A0%84%EB%9E%B5-%EC%84%A0%ED%83%9D)

<br>

## Redis Key 보안
사용자를 특정할 수 있는 정보는 pk일거라 생각하고 민감정보를 key로 넣어도 되는지 고민했었다.

### 하지만

- 민감정보가 db의 pk가 될 수 있지만 시퀀스도 pk가 될 수 있다
- redis의 key로 사용자의 민감정보를 사용하는 것은 보안측면으로도 좋은 선택이 아님
- 네임스페이스와 암호화를 통해 key를 설정할 수 있지만 암호화된 문자열에 대한 추가적인 관리가 필요함
- 암호화 문자열이 들어간 key는 사용성이 떨어짐

### 결론

- redis key에 민감정보 대신 db 사용자 테이블 설계를 변경
- 사용자의 시퀀스와 네임스페이스를 혼합하여 key로 사용

<br>

## 프로젝트 종료하기

체크항목 | 실행 여부 | 문제점
-- | -- | --
개발 전 요구사항 작성하였는가? | X | 1. 개발 단계에서 필요한데로 요구사항이 수정되어짐 <br> 2. 계속되는 수정으로 무엇을 위한 시스템인지 목적을 잃어감
개발 전 API Spec 작성하였는가? | O | 1. API의 책임이 커져서 API 목적이 명확하지 않음 <br> 2. 대기열 시스템 구현이 목적인 어플리케이션에서 상품 등록, 수정, 삭제와 같은 불필요한 API 발생
개발 전 시퀀스 다이어그램 작성하였는가? | X | 객체 간 메시지를 시간의 흐름으로 나눌 수 없을만큼 역할이 비대해진 API
개발 전 서버 구성도 그렸는가? | X | 서버 간 논리적, 물리적 배치에 따른 어플리케이션 비용을 측정하기 어려워짐
개발 전 ERD 작성하였는가? | O | 컬럼 정의서를 작성하지 않아 리뷰 받을 시 설명을 반복해야 하는 어려움 발생
개발 전 WBS 작성 및 실행하였는가? | X | 현재 개발 진척률를 체크하지 않아 누적된 딜레이
개발 중 기능별 테스트 코드를 작성하였는가? | O | 1. 추상적인 요구사항 때문에 성공/실패 조건이 명확하지 않음 <br> 2. 테스트 라이브러리에 대한 이해도 없이 작성된 코드 <br> 3. 대기열 기능 테스트만 이뤄지고 성능 테스트가 이뤄지지 않음
기술적 고민과 결정 과정을 기록하였는가? | X | 한번쯤 문제를 되돌아 보았으면 연쇄적으로 발생하지 않았을 문제들인데 동일 또는 유사한 문제 발생<br>(예: PR과 API의 책임이 비대해졌던 것)
트러블 슈팅을 작성하였는가? | X | 차후 리팩토링 시 동일한 문제 발생 가능해짐
이 프로젝트를 완성했다고 말할 수 있는가? | X | 대기열 시스템 구축이라는 프로젝트 목적을 반영하고 있지 않으므로 미완성

[본문보기](https://github.com/f-lab-edu/getto-ticket/wiki/9.-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%ED%9A%8C%EA%B3%A0#%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EC%A2%85%EB%A3%8C%ED%95%98%EA%B8%B0)