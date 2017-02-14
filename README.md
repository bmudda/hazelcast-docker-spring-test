# hazelcast-docker-test

Contains a simple application that starts a hazelcast instance. The main purpose of this is to test hazelcast with spring boot.

1. [Status](#status)
* [Releases](#releases)
* [Requirements](#requirements)
* [Build from source](#building)
* [Usage](#usage)
* [Docker info](#docker)
* [Notes](#notes)

## <a id="status"></a>Status

This is release candidate code, tested against Hazelcast 3.7+ and Consul v0.7.4 releases

## <a id="releases"></a>Releases

* [1.0-RC1](https://github.com/bmudda/hazelcast-docker-spring-test/releases/tag/1.0-RC1): Tested against Hazelcast 3.7+ releases, Spring boot 1.4.2.RELEASE
* [1.0-RC2](https://github.com/bmudda/hazelcast-docker-spring-test/releases/tag/1.0-RC2): Tested against Hazelcast 3.7.3 releases, Spring boot 1.2.5.RELEASE, Consul v0.7.4

## <a id="requirements"></a>Requirements

* Java 7+
* [Hazelcast 3.6+](https://hazelcast.org/)
* [Spring boot 1.2.5.RELEASE](http://docs.spring.io/spring-boot/docs/1.2.5.RELEASE/reference/htmlsingle/#getting-started-gradle-installation)
* [hazelcast-consul-discovery-spi RC-6](https://github.com/bitsofinfo/hazelcast-consul-discovery-spi/releases/tag/1.0-RC6)
* [docker-discovery-registrator-consul RC-2](https://github.com/bitsofinfo/docker-discovery-registrator-consul/releases/tag/1.0-RC2)

## <a id="building"></a>Building from source

* From the root of this project, build a Jar : `./gradlew build --refresh-dependencies -x test`

## <a id="usage"></a>Usage
* Have Consul running and available somewhere on your network, start it such as: (adjust paths below)
```
consul agent -server -bootstrap-expect 1 -data-dir /tmp/consul -config-dir /path/to/consul.d/ -ui-dir /path/to/consul-web-ui -bind=0.0.0.0 -client=0.0.0.0
```
* On your Docker host ensure Registrator is running such as:
```
docker run -d --name=registrator --net=host --volume=/var/run/docker.sock:/tmp/docker.sock  gliderlabs/registrator:latest -ip [DOCKER_HOST_IP] consul://[YOUR_CONSUL_IP]:8500
```

* Note the `-ip` option for Registrator command above is optional. Read the [IP and Port](http://gliderlabs.com/registrator/latest/user/services/#ip-and-port) section in documentation.


## <a id="docker"></a>Docker info

To run this application in a docker container use the Dockerfile included in this project to build a docker image.

* From the root of this project build a docker image : `./gradlew --parallel --refresh-dependencies --configure-on-demand buildTagDockerImage -x test -PimageTag=latest`
* Run the application in a docker container using the following command.
```
docker run -e "SERVICE_TAGS=dev,myUniqueId001" --rm=true -P hazelcast-docker-spring-test:latest java -DMY_SERVICE_NAME=hazelcast-docker-spring-test -DMY_UNIQUE_TAG=myUniqueId001  -DSERVICE_NAME_STRATEGY=org.bitsofinfo.docker.discovery.registrator.consul.MultiServiceNameSinglePortStrategy -jar /hzdocker/hazelcast-docker-spring-test.jar -consulserverip [YOUR_CONSUL_IP] -consulserverport [YOUR_CONSUL_PORT]
```

## <a id="notes"></a>Notes
**[DOCKER_IMAGE_ID]** is the docker image ID that is generated when building docker image. You can find the image id by running `docker images` from command line. It will output something similar to:
```
REPOSITORY                      TAG                 IMAGE ID            CREATED             SIZE
hazelcast-docker-spring-test    latest              98292c7753e3        2 minutes ago       139.8 MB
java                            8u92-jre-alpine     bd8e525f9770        2 weeks ago         107.8 MB
gliderlabs/registrator          latest              3b59190c6c80        10 months ago       23.78 MB
```

**/hzdocker/hazelcast-docker-spring-test.jar** is created when building the docker image. Please refer to *Dockerfile* on line 17.

```
COPY build/libs/hazelcast-docker-spring-test-1.0-RC1.jar /hzdocker/hazelcast-docker-spring-test.jar
```

If you have issue with docker host when running the docker image build on Mac, please refer to this github issue:
[docker for mac 1.12 cannot connect](https://github.com/bmuschko/gradle-docker-plugin/issues/235)

Specifically, the comment by bitsofinfo solved the problem for me for Mac

```
brew install socat
nohup socat -4 TCP-LISTEN:2375,fork UNIX-CONNECT:/var/run/docker.sock &
```
