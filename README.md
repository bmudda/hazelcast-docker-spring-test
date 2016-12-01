# hazelcast-docker-test

Contains a simple application that starts a hazelcast instance. The main purpose of this is to test hazelcast with spring boot.

1. [Status](#status)
* [Releases](#releases)
* [Requirements](#requirements)
* [Build from source](#building)
* [Usage](#usage)

## <a id="status"></a>Status

This is release candidate code, tested against Hazelcast 3.7+ releases.

## <a id="releases"></a>Releases

* [1.0-RC1](https://github.com/bmudda/hazelcast-docker-spring-test/releases/tag/1.0-RC1): Tested against Hazelcast 3.7+ releases, Spring boot 1.4.2.RELEASE

## <a id="requirements"></a>Requirements

* Java 7+
* [Hazelcast 3.6+](https://hazelcast.org/)
* [Spring boot 1.4.2.RELEASE](http://docs.spring.io/spring-boot/docs/1.4.2.RELEASE/reference/htmlsingle/#getting-started-gradle-installation)

## <a id="building"></a>Building from source

* From the root of this project, build a Jar : `./gradlew build`

## <a id="usage"></a>Usage
* Locate the tcp-ip section in hazelcast.xml file located in the docs folder, copy it to a desired location and adjust the member IP addresses to your need
```
<member>127.0.0.1:5701</member>
<member>127.0.0.1:5702</member>
```
* Run the jar file from the root of this project folder `java -jar build/libs/hazelcast-docker-spring-test-1.0-RC1.jar` from inside your root project
