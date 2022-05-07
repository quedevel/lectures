# 자바 ORM 표준 JPA 프로그래밍 - 기본편

### Java 11 이상에서 수행해야 할 작업은 클래스 경로 또는 모듈 경로에 Java EE API 사본을 포함하는 것입니다. 예를 들어 다음과 같이 JAX-B API를 Maven 종속성으로 추가

```xml
<!-- API, java.xml.bind module -->
<dependency>
    <groupId>jakarta.xml.bind</groupId>
    <artifactId>jakarta.xml.bind-api</artifactId>
    <version>2.3.2</version>
</dependency>
 
<!-- Runtime, com.sun.xml.bind module -->
<dependency>
    <groupId>org.glassfish.jaxb</groupId>
    <artifactId>jaxb-runtime</artifactId>
    <version>2.3.2</version>
</dependency>
```

```groovy
dependencies {
    // JAX-B dependencies for JDK 9+
    implementation "jakarta.xml.bind:jakarta.xml.bind-api:2.3.2"
    implementation "org.glassfish.jaxb:jaxb-runtime:2.3.2"
}
```


## JPA에서 가장 중요한 2가지

* 객체와 관계형 데이베이스 매핑하기(Object Relational Mapping)
* **영속성 컨텍스트**


<br>


## 영속성 컨텍스트

* JPA를 이해하는데 가장 중요한 용어
* "엔티티를 영구 저장하는 환경"이라는 뜻
* 영속성 컨텍스트는 논리적인 개념
* 눈에 보이지 않는다.

<br>

## 엔티티의 생명주기

* 비영속 : 영속성 컨텍스트와 전혀 관계가 없는 새로운 상태
* 영속 : 영속성 컨텍스트에 관리되는 상태
...

## 엔티티 조회, 1차 캐시

## 플러시는!?
* 영속성 컨텍스트를 비우지 않음
* 영속성 컨텍스트의 변경내용을 데이터베이스에 동기화
* 트랜잭션이라는 작업 단위가 중요 -> 커밋 직전에만 동기화하면 됨


## 변경 감지 (Dirty Checking)
commit시 스냅샷을 비교한다.