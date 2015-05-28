FROM debian:wheezy
MAINTAINER Dave Yarwood <dave@adzerk.com>

ENV DEBIAN_FRONTEND noninteractive

# Oracle Java 8

RUN apt-get update \
    && apt-get install -y curl wget openssl ca-certificates \
    && cd /tmp \
    && wget -qO jdk8.tar.gz \
       --header "Cookie: oraclelicense=accept-securebackup-cookie" \
       http://download.oracle.com/otn-pub/java/jdk/8u25-b17/jdk-8u25-linux-x64.tar.gz \
    && tar xzf jdk8.tar.gz -C /opt \
    && mv /opt/jdk* /opt/java \
    && rm /tmp/jdk8.tar.gz \
    && update-alternatives --install /usr/bin/java java /opt/java/bin/java 100 \
    && update-alternatives --install /usr/bin/javac javac /opt/java/bin/javac 100

ENV JAVA_HOME /opt/java

# Boot

RUN curl -s https://api.github.com/repos/boot-clj/boot/releases \
    | grep 'download_url.*boot\.sh' | head -1 |sed 's@^.*[:] @wget -O /usr/bin/boot @' \
    | bash \
    && chmod +x /usr/bin/boot

ENV BOOT_AS_ROOT yes
ENV BOOT_JVM_OPTIONS -Xmx2g

# download & install deps, cache REPL and web deps
RUN /usr/bin/boot web -s doesnt/exist repl -e '(System/exit 0)'
RUN rm -rf target

WORKDIR /zoondka-maps
ADD . /zoondka-maps

EXPOSE 8090
RUN /usr/bin/boot prod
CMD /opt/java/bin/java -jar target/zoondka-maps.jar
