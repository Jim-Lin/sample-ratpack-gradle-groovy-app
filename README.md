this is a sample of a `Groovy` `Ratpack` app built with `Gradle`<br />
the app provides a simple google customer search RESTful api<br />
it also could build `Docker` image, run on Docker container and test with `Robot Framework`

## get project from Github
```bash
$ git clone https://github.com/Jim-Lin/sample-ratpack-gradle-groovy-app.git
```

## run project on local
modify your google api key and customer search id in `sample-ratpack-gradle-groovy-app/src/ratpack/ratpack.properties`

```text
google_search_key=...
google_search_cx=...
```

before run this project, you need install [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

```bash
$ cd sample-ratpack-gradle-groovy-app
$ ./gradlew run

:compileJava
:compileGroovy
:prepareBaseDir
:processResources
:classes
:configureRun
:run
[main] INFO ratpack.server.RatpackServer - Ratpack started for http://localhost:5050
```
in your browser go to [http://localhost:5050/keyword](http://localhost:5050/keyword)<br />
it will return 10 default `Ruckus Wireless` keyword search data on google in json format

```json
{
    "data": [
        {
            "title": "Ruckus Wireless",
            "link": "http://www.ruckuswireless.com/"
        },
        {
            "title": "Ruckus Support",
            "link": "https://support.ruckuswireless.com/"
        }, ...
    ]
}
```

if you want to search `docker` only 1 data on google<br />
you can use path parameter `docker` and query parameter `limit=1`<br />
in your browser try [http://localhost:5050/keyword/docker?limit=1](http://localhost:5050/keyword/docker?limit=1)

```json
{
    "data": [
        {
            "title": "Docker - Build, Ship, and Run Any App, Anywhere",
            "link": "https://www.docker.com/"
        }
    ]
}
```


## build project to Docker image
before build this project to Docker image, you need install [Docker](http://docs.docker.com/mac/started/)

starts a stopped docker machine

```bash
$ boot2docker up
# or docker-machine
$ docker-machine start
```

* by Docker

```bash
$ docker build -t sample-app sample-ratpack-gradle-groovy-app

Sending build context to Docker daemon 83.88 MB
Sending build context to Docker daemon 
Step 0 : FROM ubuntu:14.04
 ---> 91e54dfb1179
Step 1 : MAINTAINER Jim-Lin <acgsong.tw@yahoo.com.tw>
 ---> Using cache
 ---> b2c59dc68db4
Step 2 : RUN apt-get update && apt-get install -y software-properties-common
 ---> Using cache
 ---> ec790366206d
...
```
* by Gradle(option)

i also integrated a Docker plugin for Gradle<br />
see more [de.gesellix.docker](https://plugins.gradle.org/plugin/de.gesellix.docker)

```bash
$ cd sample-ratpack-gradle-groovy-app
$ ./gradlew buildImage

:tarBuildcontextForBuildImage
:buildImage
...
```

## run image on Docker Container
show images

```bash
$ docker images

REPOSITORY          TAG                 IMAGE ID            CREATED             VIRTUAL SIZE
sample-app          latest              bceecb6fa8c9        2 minutes ago       832.7 MB
ubuntu              14.04               91e54dfb1179        8 days ago          188.4 M
```

run sample-app image on container in background, bind a container’ s 5050 port to host and mount container `/sample-app/result` to local `result` folder

```bash
$ docker run -v $(pwd)/result:/sample-app/result -d -p 5050 sample-app
# print container ID
1efcccef0186425ef6535c4f56335fbe55ca2a1f5874a1efbe447499e134d396
```

wait log information a little time until `[main] INFO ratpack.server.RatpackServer - Ratpack started for http://localhost:5050`

```bash
$ docker logs 1efcccef0186

Downloading file:/sample-app/gradle/wrapper/gradle-2.1-bin.zip
...
[main] INFO ratpack.server.RatpackServer - Ratpack started for http://localhost:5050
```

show running containers and can see what port number mapping container’ s 5050 port

```bash
$ docker ps

CONTAINER ID        IMAGE               COMMAND             CREATED              STATUS              PORTS                     NAMES
1efcccef0186        sample-app:latest   "./gradlew run"     About a minute ago   Up About a minute   0.0.0.0:32772->5050/tcp   sleepy_archimedes 
```

check host ip

```bash
$ boot2docker ip

192.168.59.103

# or docker-machine
$ docker-machine ip
```

then, you can search like<br />
http://192.168.59.103:32772/keyword<br />
or<br />
http://192.168.59.103:32772/keyword/docker?limit=1<br />
above the same as local result

## test project by ROBOT FRAMEWORK
i install robotframework, requests, and robotframework-requests in Dockerfile to test this project RESTful api

test case is that the default `Ruckus Wireless` keyword search and limit 1 data to check the link is `http://www.ruckuswireless.com/` or not

```text
*** Test Cases ***
Get Request with Url Parameters
    [Tags]    get
    Create Session    sample    http://localhost:5050
    ${params}=    Create Dictionary    limit=1
    ${resp}=    Get Request    sample    /keyword    params=${params}
    Should Be Equal As Strings    ${resp.status_code}    200
    ${jsondata}=    To Json    ${resp.content}
    Should be Equal    ${jsondata['data'][0]['link']}    http://www.ruckuswireless.com/
```

in your browser go to http://192.168.59.103:32772

```text
==============================================================================
Keyword Search                                                                
==============================================================================
Get Request with Url Parameters                                       | PASS |
------------------------------------------------------------------------------
Keyword Search                                                        | PASS |
1 critical test, 1 passed, 0 failed
1 test total, 1 passed, 0 failed
==============================================================================
Output:  /sample-app/result/output.xml
Log:     /sample-app/result/log.html
Report:  /sample-app/result/report.html
```

check test report

```bash
$ cd result
$ ls
log.html	output.xml	report.html
$ open report.html
```

## ref.
* [example-ratpack-gradle-groovy-app](https://github.com/ratpack/example-ratpack-gradle-groovy-app)
* [My First Ratpack App: What I Learned](https://objectpartners.com/2015/05/12/my-first-ratpack-app-what-i-learned/)
* [Building Web Apps in Ratpack](http://www.slideshare.net/danveloper/slides-27337436)
* [httpbuilder](https://github.com/jgritman/httpbuilder)
* [gradle-docker-plugin](https://github.com/gesellix/gradle-docker-plugin)
* [robot-test](https://github.com/sharrechen/robot-test)
* [robotframework-requests](https://github.com/bulkan/robotframework-requests)
