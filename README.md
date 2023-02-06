# Дипломный проект по профессии «Тестировщик»

Дипломный проект представляет собой автоматизацию тестирования комплексного сервиса, взаимодействующего с СУБД и API Банка.

## Запуск автотестов:

1. Склонировать репозиторий: `git clone https://github.com/emine27/Diplom2.0.git`

2. Перейти в папку Diplom

3. Запустить контейнер Docker командой в терминале:

`docker-compose up`

4. Запустить приложение в терминале командами:

#### для MySQL:

`java "-Dspring.datasource.url=jdbc:mysql://localhost:3306/app" -jar artifacts/aqa-shop.jar`

#### для PostgreSQL:

`java "-Dspring.datasource.url=jdbc:postgresql://localhost:5432/app" -jar artifacts/aqa-shop.jar`

5. Запустить авто-тесты в терминале командами:

#### для MySQL:

`./gradlew test "-Ddb.url=jdbc:mysql://localhost:3306/app"`

#### для PostgreSQL:

`./gradlew test "-Ddb.url=jdbc:postgresql://localhost:5432/app"`

*После выполнения всех тестов остановить docker контейнер командой в консоли:* `docker-compose down`
