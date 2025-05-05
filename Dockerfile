FROM maven:3.9.4-eclipse-temurin-21-alpine AS build

WORKDIR /home/maven/src
ARG module

COPY . /home/maven/src

RUN mvn --batch-mode -f pom.xml \
    -pl "${module}" -am package \
    -DskipTests

FROM eclipse-temurin:21-jdk AS runtime

RUN apt-get update \
 && apt-get install -y libfreetype6 \
 && rm -rf /var/lib/apt/lists/*

WORKDIR /app
ARG module

COPY --from=build /home/maven/src/${module}/target/${module}-*.jar ./app.jar

ENTRYPOINT ["java","-jar","app.jar"]
