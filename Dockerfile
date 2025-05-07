# ---- Стадия сборки ----
FROM maven:3.9.4-eclipse-temurin-21-alpine AS build

WORKDIR /home/maven/src
ARG module

# Копируем всё
COPY . /home/maven/src

# Собираем только нужный модуль и его зависимости (без тестов)
RUN mvn --batch-mode -f pom.xml \
    -pl "${module}" -am package \
    -DskipTests

# ---- Стадия рантайма ----
FROM eclipse-temurin:21-jdk AS runtime

# Устанавливаем системные библиотеки
RUN apt-get update \
 && apt-get install -y libfreetype6 \
 && rm -rf /var/lib/apt/lists/*

WORKDIR /app
ARG module

# Копируем любой JAR из target/<module> — не важно, SNAPSHOT или нет,
# он переименуется в app.jar
COPY --from=build /home/maven/src/${module}/target/${module}-*.jar ./app.jar

# Точка входа
ENTRYPOINT ["java","-jar","app.jar"]
