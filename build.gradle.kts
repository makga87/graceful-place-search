plugins {
    java
    id("org.springframework.boot") version "2.7.18"
    id("io.spring.dependency-management") version "1.1.5"
}

group = "com.graceful.place.search"
version = "1.0.0-RELEASE"

tasks.bootJar {
    enabled = true
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(11)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web") {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-tomcat")
    }
    implementation("org.springframework.boot:spring-boot-starter-undertow")
    compileOnly("org.projectlombok:lombok")
    testCompileOnly ("org.projectlombok:lombok")
    testAnnotationProcessor ("org.projectlombok:lombok")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.apache.httpcomponents:httpclient:4.5.14")

    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("javax.cache:cache-api")
    implementation("org.ehcache:ehcache:3.10.0")

    implementation("org.jsoup:jsoup:1.18.1")
    implementation("org.apache.commons:commons-text:1.12.0")

}

tasks.withType<Test> {
    useJUnitPlatform()
}
