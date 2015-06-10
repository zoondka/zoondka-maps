FROM frolvlad/alpine-oraclejdk8:slim

# Boot

RUN apk add --update bash openssl docker && \
    wget -O /usr/bin/boot https://github.com/boot-clj/boot/releases/download/2.0.0/boot.sh && \
    chmod +x /usr/bin/boot

ENV BOOT_VERSION 2.1.0
ENV BOOT_AS_ROOT yes
ENV BOOT_JVM_OPTIONS -Xmx2g

# download & install deps, cache REPL and web deps
RUN /usr/bin/boot web -s doesnt/exist repl -e '(System/exit 0)'
RUN rm -rf target

WORKDIR /zoondka-maps
ADD . ./

EXPOSE 8090
RUN /usr/bin/boot prod
CMD /usr/bin/java -jar target/zoondka-maps.jar
