# Sprint_7

Для запуска и создания отчета выполни

mvn clean test "-Dmaven.test.failure.ignore=true"
#удаляет старый отчет и создает новый

allure serve target/allure-results
#открывает отчет в браузере