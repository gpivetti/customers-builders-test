FROM maven:3.6.0-jdk-11-slim AS maven
LABEL MAINTAINER="gapivetti@gmail.com"
ENTRYPOINT ["mvn", "clean", "package", "-Dmaven.test.skip=true"]