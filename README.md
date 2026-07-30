:

📋 Todo-List — управляй задачами по-взрослому
Десктопное приложение на JavaFX с подключением к PostgreSQL. Добавляй, удаляй и храни свои задачи в настоящей базе данных.

https://img.shields.io/badge/Java-17+-orange?logo=java https://img.shields.io/badge/JavaFX-21-blue?logo=openjfx https://img.shields.io/badge/PostgreSQL-16-336791?logo=postgresql https://img.shields.io/badge/license-MIT-green https://img.shields.io/badge/status-active-brightgreen

🎯 Зачем это?
Пет-проект для прокачки навыков: JavaFX, PostgreSQL, JDBC, Maven.

Что умеет:

✅ Добавлять задачи

❌ Удалять задачи (клик по строке)

💾 Хранить всё в PostgreSQL

🔍 Таблица с ID, названием и датой создания

🚀 Быстрый старт
1️⃣ Настройка PostgreSQL (один раз)
Установи PostgreSQL и создай пользователя:

bash
# Linux/macOS
sudo -u postgres psql

# Windows
psql -U postgres
Выполни SQL:

sql
CREATE USER admin WITH PASSWORD 'admin';
CREATE DATABASE todolist_db;
GRANT ALL PRIVILEGES ON DATABASE todolist_db TO admin;
\q
⚡ При первом запуске таблица list создастся автоматически!

2️⃣ Скачай и запусти
Скачай последний релиз и запусти:

bash
java -jar Todo-list-0.1.jar
⚠️ Убедись, что PostgreSQL запущен перед запуском JAR!

🧠 Как пользоваться
Действие	Результат
Ввести задачу и нажать Enter	✅ Добавится в БД
Кликнуть по строке в таблице	❌ Задача удалится
Навести на ? справа сверху	📖 Подсказка
🛠️ Сборка из исходников (для разработчиков)
bash
git clone https://github.com/NeklinYT/Todo-List.git
cd Todo-List
mvn clean package
java -jar target/Todo-list-0.1.jar
📁 Структура
text
Todo-List/
├── src/main/java/com/neklin/
│   ├── App.java                # Точка входа
│   ├── AppController.java      # Контроллер
│   ├── DatabaseController.java # Работа с БД
│   └── TaskGetter.java         # Модель задачи
├── src/main/resources/
│   ├── app-view.fxml           # Интерфейс
│   └── style.css               # Стили
└── pom.xml
🛠️ Технологии
Java 17+

JavaFX 21

PostgreSQL 16

JDBC

Maven

🤝 Поддержать проект
⭐ Поставь звёздочку на GitHub

🐛 Сообщи о баге в Issues

💡 Предложи фичу

🔧 Сделай Pull Request

📞 Контакты
Автор: @NeklinYT

Сделано с ❤️ и ☕

