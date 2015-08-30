# Pull base image
FROM ubuntu:14.04
MAINTAINER Jim-Lin <acgsong.tw@yahoo.com.tw>

# Install Prerequisites
RUN apt-get update && apt-get install -y software-properties-common

# Install Java
RUN \
  echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | debconf-set-selections && \
  add-apt-repository -y ppa:webupd8team/java && \
  apt-get update && \
  apt-get install -y oracle-java8-installer && \
  rm -rf /var/lib/apt/lists/* && \
  rm -rf /var/cache/oracle-jdk8-installer

# Install pip
RUN sudo apt-get update && apt-get -y install python-pip

# Install robotframework
RUN sudo pip install robotframework

# Install requests
RUN sudo pip install requests

# Install robotframework-requests
RUN sudo pip install -U robotframework-requests

WORKDIR /sample-app
ADD . /sample-app

CMD ["./gradlew", "run"]

EXPOSE 5050