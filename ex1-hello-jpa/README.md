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