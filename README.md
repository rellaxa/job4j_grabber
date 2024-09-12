# job4j_grabber

## О проекте

![CI GitHubAction](https://github.com/peterarsentev/job4j_tracker/actions/workflows/maven.yml/badge.svg)

Проект - Агрегатор вакансий.
Система запускается по расписанию - раз в минуту.
Программа считывает все вакансии, относящиеся к Java,
с первых 5 страниц сайта и записывает их в базу.
Первый сайт: career.habr.com.
Раздел: https://career.habr.com/vacancies/java_developer.

1. Период запуска указывается в настройках app.properties.
2. В проект можно добавлять новые сайты без изменения кода.
3. В проекте можно сделать паралллельный парсинг сайтов.