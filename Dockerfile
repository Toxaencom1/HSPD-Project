# Используем легковесный образ JRE
FROM openjdk:17

# Указываем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем JAR файл в контейнер
COPY target/HSPD-0.0.1-SNAPSHOT.jar app.jar

CMD ["java", "-jar", "app.jar"]