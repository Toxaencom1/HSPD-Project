### Project HSPD (Historical stock prices data)

Для запуска приложения с базой нужно указать данные в файле **.env.prod**
**API_KEY** это ваш api token с портала  polygon.io
**SIGNING_KEY** это любая зашифрованная фраза  Base64 формата для JWT токена
**DB_USERNAME** и **DB_PASSWORD** можно указать на ваше усмотрение так же как **APP_PORT** (порт под которым можно достучаться в запущенное приложение, внутри контейнера всегда 8080) и **DB_PORT** (порт для базы данных, который устанавливает как порт для хоста так и внутренний порт контейнера)

~~~ bash
APP_PORT=8080
DB_PORT=5432  
API_KEY=  
SIGNING_KEY=  
DB_USERNAME=  
DB_PASSWORD=
~~~

Далее можно запускать docker-compose с .env.prod файлом переменных окружения
~~~ bash
docker-compose --env-file .env.prod up --build
~~~

после этого будет доступен **Swagger** путь:порт/swagger-ui/index.html

можно будет обратиться к такой ручке /api/info/test для получения справки по вставке тестовых первоначальных данных для тестирования приложения

