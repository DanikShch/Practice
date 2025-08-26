# API для интернет-магазина

## Содержание

- [Функционал](#Функционал)
- [Примеры хранимых сущностей](#Примеры-хранимых-сущностей)
- [Используемые технологии](#Используемые-технологии)

## Функционал:

### 1. Аутентификация и авторизация

- Регистрация пользователя (email/пароль)
- Вход в систему (JWT токен)
- Восстановление пароля
- Роли: `Admin`, `Customer`, `Guest`
- Управление профилем пользователя

### 2. Управление товарами

- **CRUD операции** для товаров
- Управление категориями товаров (древовидная структура)
- Фильтрация товаров по:
    - Категории
    - Цене (диапазон)
    - Рейтингу
    - Наличию на складе
- Сортировка (по цене, популярности, новизне)

### 3. Корзина покупок

- Добавление/удаление товаров
- Изменение количества товаров
- Промежуточный расчет стоимости
- Временное хранение корзины (для авторизованных и гостей)

### 4. Оформление заказов

- Создание заказа из корзины
- Выбор способа доставки
- Валидация данных заказа
- Подтверждение заказа (email уведомление)

### 5. История заказов

- Просмотр списка заказов пользователя
- Детализация конкретного заказа
- Статусы заказов (`Создан`, `Оплачен`, `В доставке`, `Завершен`)

### 6. Дополнительные функции

- Применение промокодов
- Система рейтингов/отзывов
- Избранные товары
- Поиск по названию/описанию

## Примеры хранимых сущностей

### User

```json
{
  "id": "uuid",
  "email": "string",
  "passwordHash": "string",
  "role": "string",
  "createdAt": "2023-01-01T00:00:00Z",
  "orders": [
    {
      "id": "uuid",
      "userId": "uuid",
      "orderDate": "2023-01-01T00:00:00Z",
      "totalAmount": 0.0,
      "items": [
        {
          "id": "uuid",
          "orderId": "uuid",
          "productId": "uuid",
          "quantity": 1,
          "unitPrice": 0.0
        }
      ]
    }
  ]
}
```

### Product

```json
{
  "id": "uuid",
  "name": "string",
  "description": "string",
  "price": 0.0,
  "stockQuantity": 0,
  "categoryId": "uuid",
  "category": {
    "id": "uuid",
    "name": "string",
    "parentCategoryId": "uuid",
    "parentCategory": {
      "id": "uuid",
      "name": "string",
      "parentCategoryId": null
    },
    "products": []
  },
  "images": [
    {
      "id": "uuid",
      "productId": "uuid",
      "imageUrl": "string"
    }
  ]
}
```

### Category

```json
{
  "id": "uuid",
  "name": "string",
  "parentCategoryId": "uuid",
  "parentCategory": {
    "id": "uuid",
    "name": "string",
    "parentCategoryId": null
  },
  "products": [
    {
      "id": "uuid",
      "name": "string",
      "description": "string",
      "price": 0.0,
      "stockQuantity": 0,
      "categoryId": "uuid",
      "images": []
    }
  ]
}
```

### Cart

```json
{
  "id": "uuid",
  "userId": "uuid",
  "user": {
    "id": "uuid",
    "email": "string",
    "passwordHash": "string",
    "role": "string",
    "createdAt": "2023-01-01T00:00:00Z",
    "orders": []
  },
  "items": [
    {
      "id": "uuid",
      "cartId": "uuid",
      "productId": "uuid",
      "quantity": 1
    }
  ]
}
```

### OrderItem

```json
{
  "id": "uuid",
  "orderId": "uuid",
  "order": {
    "id": "uuid",
    "userId": "uuid",
    "orderDate": "2023-01-01T00:00:00Z",
    "totalAmount": 0.0,
    "items": []
  },
  "productId": "uuid",
  "product": {
    "id": "uuid",
    "name": "string",
    "description": "string",
    "price": 0.0,
    "stockQuantity": 0,
    "categoryId": "uuid",
    "images": []
  },
  "quantity": 1,
  "unitPrice": 0.0
}
```

### PromoCode

```json
{
  "id": "uuid",
  "code": "string",
  "discountValue": 0.0,
  "expiryDate": "2023-12-31T23:59:59Z",
  "isActive": true
}
```

## Используемые технологии

- Java 17+
- Spring Boot
- Spring Web
- Spring Security (JWT)
- Spring Data JPA
- Liquibase / Flyway
- Swagger / OpenAPI (springdoc-openapi)
- JUnit 5
- GitFlow
- OpenCSV
