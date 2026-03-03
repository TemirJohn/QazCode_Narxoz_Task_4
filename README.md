# Task_4 — Orders REST API

Spring Boot приложение для управления клиентами и заказами с PostgreSQL.

---

## Стек технологий

- Java 17
- Spring Boot 4.0.3 (Web MVC, Data JPA, Validation)
- PostgreSQL 15
- Flyway (миграции БД)
- Lombok
- SpringDoc OpenAPI (Swagger UI)
- Docker / Docker Compose

---

## Быстрый старт (Docker Compose)

> Самый простой способ — запустить всё одной командой.

### Требования

- [Docker](https://www.docker.com/) + Docker Compose

### Запуск

```bash
docker-compose up --build
```

Приложение поднимется на `http://localhost:8080`.  
PostgreSQL будет доступен на порту `5433` (хост) → `5432` (контейнер).

### Остановка

```bash
docker-compose down
```

---

## Локальный запуск (без Docker)

### Требования

- Java 17+
- PostgreSQL (локально или через Docker)
- Gradle 9+ (или используйте `./gradlew`)

### 1. Запустить только базу данных

```bash
docker-compose up postgres -d
```

### 2. Настроить подключение

Файл `src/main/resources/application.properties` уже настроен для локального запуска:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5433/orders
spring.datasource.username=admin
spring.datasource.password=admin
```

### 3. Собрать и запустить

```bash
./gradlew bootRun
```

или

```bash
./gradlew build -x test
java -jar build/libs/Task_4-0.0.1-SNAPSHOT.jar
```

---

## Тестирование

```bash
./gradlew test
```

---

## API документация (Swagger UI)

После запуска откройте в браузере:

```
http://localhost:8080/swagger-ui/index.html
```

---

## Переменные окружения (для Docker)

| Переменная                    | Значение по умолчанию                    |
|-------------------------------|------------------------------------------|
| `SPRING_DATASOURCE_URL`       | `jdbc:postgresql://postgres:5432/orders` |
| `SPRING_DATASOURCE_USERNAME`  | `admin`                                  |
| `SPRING_DATASOURCE_PASSWORD`  | `admin`                                  |

---

## Структура базы данных

Миграции применяются автоматически при старте через Flyway.

**`customers`** — клиенты (id, name, email, created_at)  
**`orders`** — заказы (id, customer_id, amount, status, created_at)

Статусы заказов: `NEW` → `PAID` или `NEW` → `CANCELLED`

---

## Тестирование через Postman

Файл коллекции: `Task4_Orders_API.postman_collection.json`

### Импорт коллекции

1. Откройте Postman
2. Нажмите **Import** (верхний левый угол)
3. Перетащите файл `Task4_Orders_API.postman_collection.json` или нажмите **Choose Files** и выберите его
4. Коллекция появится на боковой панели под названием **Task_4 — Orders API**

---

## Endpoints

### Customers

| Метод    | URL                  | Описание                  |
|----------|----------------------|---------------------------|
| `POST`   | `/customers`         | Создать клиента            |
| `GET`    | `/customers`         | Список клиентов (pageable) |
| `GET`    | `/customers/{id}`    | Получить клиента по ID     |
| `PUT`    | `/customers/{id}`    | Обновить клиента           |
| `DELETE` | `/customers/{id}`    | Удалить клиента            |

### Orders

| Метод    | URL                  | Описание                   |
|----------|----------------------|----------------------------|
| `POST`   | `/orders`            | Создать заказ               |
| `GET`    | `/orders`            | Список заказов (pageable)   |
| `GET`    | `/orders/{id}`       | Получить заказ по ID        |
| `PUT`    | `/orders/{id}`       | Обновить заказ              |
| `DELETE` | `/orders/{id}`       | Удалить заказ               |
| `POST`   | `/orders/{id}/pay`   | Оплатить заказ (NEW → PAID) |
| `POST`   | `/orders/{id}/cancel`| Отменить заказ (NEW → CANCELLED) |