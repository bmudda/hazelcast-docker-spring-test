# hazelcast-docker-test

Contains a simple application that starts a hazelcast instance. The main purpose of this is to test hazelcast in docker container.

1. [Status](#status)
* [Releases](#releases)
* [Requirements](#requirements)
* [Build from source](#building)
* [Usage](#usage)
* [Docker info](#docker)
* [Notes](#notes)

## <a id="status"></a>Status

This is release candidate code, tested against Hazelcast 3.7+ releases.

## <a id="releases"></a>Releases

* [1.0-RC1](https://github.com/bmudda/hazelcast-docker-test/releases/tag/1.0-RC1): Tested against Hazelcast 3.7+ releases

## <a id="requirements"></a>Requirements

* Java 7+
* [Hazelcast 3.6+](https://hazelcast.org/)

## <a id="building"></a>Building from source

* From the root of this project, build a Jar : `./gradlew build`

## <a id="usage"></a>Usage
* Locate the tcp-ip section in hazelcast.xml file located in the docs folder, copy it to a desired location and adjust the member IP addresses to your need
```
<member>10.29.29.227:40001</member>
<member>10.29.29.227:40002</member>
```
* Run the jar file from the root of this project folder `java -Dhazelcast.config=/path/to/hazelcast.xml -jar build/libs/hazelcast-docker-test-1.0-RC1.jar` from inside your root project

NOTE: **-Dhazelcast.config** system property configures Hazelcast to use the xml file your provide instead of the default hazelcast.xml file that ships with Hazelcast

## <a id="docker"></a>Docker info

To run this application in a docker container use the Dockerfile included in this project to build a docker image.

* From the root of this project build a docker image : `./gradlew --parallel --configure-on-demand buildTagDockerImage -x test -PimageTag=latest`
* Copy and modify the hazelcast.xml file found in the docs folder of this project.
* Run a application in a docker container using the following command.
```
docker run --rm=true -v /path/to/hazelcastxml/directory:/config -p 40001:5701 [DOCKER_IMAGE_ID] java -Dhazelcast.config=/config/hazelcast.xml -jar /hzdocker/hazelcast-docker-test.jar
```

## <a id="notes"></a>Notes
**[DOCKER_IMAGE_ID]** is the docker image ID that is generated when building docker image. You can find the image id by running `docker images` from command line. It will output something similar to:
```
REPOSITORY              TAG                 IMAGE ID            CREATED             SIZE
hazelcast-docker-test   latest              7ca3883448c8        50 minutes ago      119.7 MB
java                    8u92-jre-alpine     bd8e525f9770        2 weeks ago         107.8 MB
```

**/hzdocker/hazelcast-docker-test.jar** is created when building the docker image. Please refer to *Dockerfile* on line 17.

```
COPY build/libs/hazelcast-docker-test-1.0-RC1.jar /hzdocker/hazelcast-docker-test.jar
```

If you have issue with docker host when running the docker image build on Mac, please refer to this github issue:
[docker for mac 1.12 cannot connect](https://github.com/bmuschko/gradle-docker-plugin/issues/235)

Specifically, the comment by bitsofinfo solved the problem for me for Mac

```
brew install socat
nohup socat -4 TCP-LISTEN:2375,fork UNIX-CONNECT:/var/run/docker.sock &
```
