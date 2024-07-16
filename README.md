# HHWorkSearch версия 0.2.0

## Сервис помощи поиска работы с использованием HeadHunter API

Сервис анализирет предложенные вакансии на headhunter и отправлет брокеру сообщение с рекомендованной для отклика вакансией и сопроводительным письмом.

### Логика работы:
- Кажды час, в рабочее время запрашиваются вакансии для резюме.
- Из предложенных удаляются вакансии в заголовках или описании которых присутсвуют слова из фильтра.
- Удаляются ранее показанные вакансии.
- Проверяется наличие ключевых слов хард скилов и на основании этих слов готовится сопроводительное письмо.
- Письмо отправлается в очередь брокеру.

### Для настрйоки испльзуется API
- /api/filter - настройка фильтров
- /api/negotiations -  работа с откликами
- /api/resume/skill_set -  работа с хард скилами
- /api/resume -  работа с резюме
- /api/vacancy - работа с вакансиями
- /swagger-ui.html - полное описание

### Начало работы
Создайте в корне приложения файл application.properties<br>
указав корректные значения:
```
app.hh-user-agent= 
app.hh-client-id = 
app.hh-client-secret = 
app.email-email = 
app.email-password = 
app.telegram.token =
app.clientId =
```
```
spring.application.name=HHWorkSearchApplication
server.port=8080
spring.datasource.url=jdbc:postgresql://psql:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.show-sql=false

spring.kafka.bootstrap-servers=kafka:9094
spring.kafka.topic=tmessage


app.hh-api-uri = https://api.hh.ru
app.hh-api-callback=http://localhost:8080/authorization
app.hh-api-token-uri=https://hh.ru/oauth/token
app.hh-vacancy=/vacancy
app.hh-resume=/resume

app.hh-user-agent= 
app.hh-client-id = 
app.hh-client-secret = 
app.email-email = 
app.email-password = 
app.telegram.token =
app.clientId =


spring.web.resources.static-locations=classpath:/frontend/
spring.thymeleaf.prefix=classpath:/frontend/
spring.thymeleaf.suffix=.html
```
Откройте в браузере http://localhost:8080/ и разешите приложению доступ к hh.ru

### Используемые технологии:
- HeadHunter Api (https://api.hh.ru/),
- java 17,
- spring boot,
- postgresSql,
- jpa,
- rest api,
- swagger,
- Thymeleaf
- maven,
- git,


