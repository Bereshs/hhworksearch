# HHWorkSearch версия 0.3.0

## Сервис помощи поиска работы с использованием HeadHunter API

Сервис анализирет предложенные вакансии на headhunter и отправлет брокеру сообщение с рекомендованной для отклика вакансией и сопроводительным письмом.

### Логика работы:
- Кажды час, в рабочее время запрашиваются вакансии для резюме.
- Из предложенных удаляются вакансии в заголовках или описании которых присутсвуют слова из фильтра.
- Удаляются ранее показанные вакансии.
- Проверяется наличие ключевых слов хард скилов и на основании этих слов готовится сопроводительное письмо.
- Формируется отклик на вакансию
- Ежедневно формируется отчет о количестве откликов и отказов.
- Ежедневный отчет отправляется в очередь брокеру.

### Для настрйоки испльзуется API
- /api/filter - настройка фильтров
- /api/negotiations -  работа с откликами
- /api/resume/skill_set -  работа с хард скилами
- /api/resume -  работа с резюме
- /api/vacancy - работа с вакансиями
- /swagger-ui.html - полное описание

### Начало работы

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

### История версий
#### 0.3.0
- Добавлено управление через web
- Добавлен openFeignClient для формирования запросов к внутреннему api
- Добавлены rest для openFeignClient RestFilterController, RestMessageController, RestSkillController, путь "/api/client"
- Добавлены web контроллеры EditElementController, FilterSettingsController, NegotiationSettingController
- старые контроллеры FilterPropertyController, ResumeController, VacancyController помечены как @Deprecated
- FilterScope изменен на Enum
- properties заменен на yaml
- DailyReportDo заменен на DailyReportService ReportDto
- удален seurity.properties настройка через web: /parametersettings