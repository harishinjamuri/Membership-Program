# 📄 Database Schema


## 🧑 USERS

| Column          | Data Type | Description                   |
| --------------- | --------- | ----------------------------- |
| id              | STRING    | Primary Key                   |
| name            | STRING    | User's name                   |
| email           | TEXT      | User's email                  |
| phone_number   | STRING    | User's contact number         |
| date_of_birth  | TIMESTAMP | Date of birth                 |
| last_login     | TIMESTAMP | Last login time               |
| status         | BOOLEAN   | Account active/inactive       |
| password_hash  | STRING    | Encrypted password            |
| user_type      | STRING    | User role ("ADMIN", "NORMAL") |
| created_at     | TIMESTAMP | Record creation time          |
| updated_at     | TIMESTAMP | Record update time            |


```
CREATE TABLE users (
        name VARCHAR(100) NOT NULL, 
        email TEXT NOT NULL, 
        phone_number VARCHAR(20), 
        date_of_birth DATETIME, 
        last_login DATETIME, 
        status BOOLEAN, 
        password_hash VARCHAR(255) NOT NULL, 
        user_type VARCHAR(10), 
        id VARCHAR(36) NOT NULL, 
        created_at DATETIME, 
        updated_at DATETIME, 
        PRIMARY KEY (id), 
        UNIQUE (email)
);
```
---

## 💳 MEMBERSHIP_PLANS

| Column         | Data Type | Description             |
| -------------- | --------- | ----------------------- |
| id             | STRING    | Primary Key             |
| name           | STRING    | Plan name               |
| description    | TEXT      | Description of the plan |
| price          | FLOAT     | Price of the plan       |
| is_active     | BOOLEAN   | Availability status     |
| duration_days | INTEGER   | Validity in days        |
| created_at    | TIMESTAMP | Record creation time    |
| updated_at    | TIMESTAMP | Record update time      |

```
CREATE TABLE membership_plans (
        name VARCHAR(50) NOT NULL, 
        description TEXT NOT NULL, 
        price FLOAT, 
        is_active BOOLEAN, 
        duration_days INTEGER, 
        id VARCHAR(36) NOT NULL, 
        created_at DATETIME, 
        updated_at DATETIME, 
        PRIMARY KEY (id)
);
```
---

## 🏆 MEMBERSHIP_TIERS

| Column               | Data Type | Description                       |
| -------------------- | --------- | --------------------------------- |
| id                   | STRING    | Primary Key                       |
| name                 | STRING    | Tier name                         |
| description          | TEXT      | Description of the tier           |
| is_active           | BOOLEAN   | Whether the tier is active        |
| tier_level          | STRING    | Optional level label (e.g., GOLD) |
| min_monthly_orders | INTEGER   | Min monthly orders to qualify     |
| min_monthly_spend  | FLOAT     | Min monthly spend to qualify      |
| min_total_orders   | INTEGER   | Lifetime order minimum            |
| min_total_spend    | FLOAT     | Lifetime spend minimum            |
| created_at          | TIMESTAMP | Record creation time              |
| updated_at          | TIMESTAMP | Record update time                |

```
CREATE TABLE membership_tiers (
        name VARCHAR(50) NOT NULL, 
        description TEXT NOT NULL, 
        is_active BOOLEAN, 
        tier_level VARCHAR(10), 
        min_monthly_orders INTEGER, 
        min_monthly_spend FLOAT, 
        min_total_orders INTEGER, 
        min_total_spend FLOAT, 
        id VARCHAR(36) NOT NULL, 
        created_at DATETIME, 
        updated_at DATETIME, 
        PRIMARY KEY (id)
);
```
---

## 🎖 TIER_BENEFITS

| Column         | Data Type | Description                         |
| -------------- | --------- | ----------------------------------- |
| id             | STRING    | Primary Key                         |
| description    | TEXT      | Description of the benefit          |
| tier_id       | STRING    | Associated tier                     |
| is_active     | BOOLEAN   | Whether benefit is active           |
| benefit_type  | STRING    | Type of benefit (e.g., discount)    |
| benefit_value | JSON      | Structured value (e.g., % or limit) |
| sort_order    | INTEGER   | Ordering for UI display             |
| created_at    | TIMESTAMP | Record creation time                |
| updated_at    | TIMESTAMP | Record update time                  |

```
CREATE TABLE tier_benefits (
        description TEXT NOT NULL, 
        tier_id VARCHAR(36) NOT NULL, 
        is_active BOOLEAN, 
        benefit_type VARCHAR(36) NOT NULL, 
        benefit_value JSON NOT NULL, 
        sort_order INTEGER, 
        id VARCHAR(36) NOT NULL, 
        created_at DATETIME, 
        updated_at DATETIME, 
        PRIMARY KEY (id)
);
```
---

## 🏠 ADDRESS_BOOK
| Column      | Data Type | Description                         |
| ----------- | --------- | ----------------------------------- |
| id          | STRING    | Primary Key                         |
| name        | TEXT      | Name for the address                |
| user_id    | STRING    | User ID                             |
| pincode     | STRING    | Postal code                         |
| address     | TEXT      | Full address                        |
| city        | STRING    | City                                |
| state       | STRING    | State                               |
| country     | STRING    | Country                             |
| is_default | BOOLEAN   | Whether this is the default address |
| created_at | TIMESTAMP | Record creation time                |
| updated_at | TIMESTAMP | Record update time                  |

```
CREATE TABLE address_book (
        name TEXT NOT NULL, 
        user_id VARCHAR(36) NOT NULL, 
        pincode VARCHAR(36) NOT NULL, 
        address TEXT NOT NULL, 
        city VARCHAR(36) NOT NULL, 
        state VARCHAR(36) NOT NULL, 
        country VARCHAR(36) NOT NULL, 
        is_default BOOLEAN, 
        id VARCHAR(36) NOT NULL, 
        created_at DATETIME, 
        updated_at DATETIME, 
        PRIMARY KEY (id)
);
```
---

## 🧾 SUBSCRIPTION

| Column               | Data Type | Description                        |
| -------------------- | --------- | ---------------------------------- |
| id                   | STRING    | Primary Key                        |
| user_id             | STRING    | Subscribed user                    |
| plan_id             | STRING    | Linked plan ID                     |
| tier_id             | STRING    | Linked membership tier             |
| current_tier        | STRING    | Current tier name/level            |
| benefits             | JSON      | List of active benefits            |
| start_date          | TIMESTAMP | Subscription start                 |
| end_date            | TIMESTAMP | Subscription end                   |
| auto_renew          | BOOLEAN   | Whether it renews automatically    |
| subscription_number | STRING    | Unique subscription number         |
| total_saved         | FLOAT     | Amount saved through subscription  |
| total_spent         | FLOAT     | Amount spent while subscribed      |
| is_active           | BOOLEAN   | Whether the subscription is active |
| created_at          | TIMESTAMP | Record creation time               |
| updated_at          | TIMESTAMP | Record update time                 |

```
CREATE TABLE subscription (
        user_id VARCHAR(36) NOT NULL, 
        plan_id VARCHAR(36) NOT NULL, 
        tier_id VARCHAR(36) NOT NULL, 
        current_tier VARCHAR(10), 
        benefits JSON NOT NULL, 
        start_date DATETIME, 
        end_date DATETIME, 
        auto_renew BOOLEAN, 
        subscription_number VARCHAR(36) NOT NULL, 
        total_saved FLOAT, 
        total_spent FLOAT, 
        is_active BOOLEAN, 
        id VARCHAR(36) NOT NULL, 
        created_at DATETIME, 
        updated_at DATETIME, 
        PRIMARY KEY (id)
);
```
---

## 🗂 CATEGORY

| Column      | Data Type | Description                    |
| ----------- | --------- | ------------------------------ |
| id          | STRING    | Primary Key                    |
| name        | STRING    | Category name                  |
| description | TEXT      | Description of the category    |
| is_active  | BOOLEAN   | Whether the category is active |
| created_at | TIMESTAMP | Record creation time           |
| updated_at | TIMESTAMP | Record update time             |

```
CREATE TABLE category (
        name VARCHAR(100) NOT NULL, 
        description TEXT NOT NULL, 
        is_active BOOLEAN, 
        id VARCHAR(36) NOT NULL, 
        created_at DATETIME, 
        updated_at DATETIME, 
        PRIMARY KEY (id), 
        UNIQUE (name)
);
```
---

## 🛍 PRODUCT

| Column                    | Data Type | Description                                  |
| ------------------------- | --------- | -------------------------------------------- |
| id                        | STRING    | Primary Key                                  |
| name                      | STRING    | Product name                                 |
| description               | TEXT      | Product description                          |
| category_id              | STRING    | Foreign key to Category                      |
| stock_quantity           | INTEGER   | Inventory count                              |
| discount_id              | STRING    | Linked discount (nullable)                   |
| price                     | FLOAT     | Price of the product                         |
| is_membership_exclusive | BOOLEAN   | If product is for members only               |
| min_tier_required       | INTEGER   | Minimum tier level required to view/purchase |
| created_at               | TIMESTAMP | Record creation time                         |
| updated_at               | TIMESTAMP | Record update time                           |

```
CREATE TABLE product (
        name VARCHAR(100) NOT NULL, 
        description TEXT NOT NULL, 
        category_id VARCHAR(36) NOT NULL, 
        stock_quantity INTEGER NOT NULL, 
        discount_id VARCHAR(36), 
        price FLOAT NOT NULL, 
        is_membership_exclusive BOOLEAN, 
        min_tier_required INTEGER NOT NULL, 
        id VARCHAR(36) NOT NULL, 
        created_at DATETIME, 
        updated_at DATETIME, 
        PRIMARY KEY (id), 
        UNIQUE (name)
);
```

---

## 🛒 ORDER

| Column                 | Data Type | Description                        |
| ---------------------- | --------- | ---------------------------------- |
| id                     | STRING    | Primary Key                        |
| user_id               | STRING    | User who placed the order          |
| status                 | STRING    | Order status                       |
| total_amount          | FLOAT     | Total price after discounts + fees |
| total_savings         | FLOAT     | Amount saved                       |
| delivery_address      | TEXT      | Delivery address                   |
| delivery_fee          | FLOAT     | Fee for delivery                   |
| delivery_fee_applied | BOOLEAN   | Whether fee was applied            |
| items                  | JSON      | Ordered items                      |
| discounts              | JSON      | Applied discounts                  |
| subtotal               | FLOAT     | Price before discounts/fees        |
| created_at            | TIMESTAMP | Record creation time               |
| updated_at            | TIMESTAMP | Record update time                 |

```
CREATE TABLE IF NOT EXISTS "order" (
        user_id VARCHAR(36) NOT NULL, 
        status VARCHAR(10) NOT NULL, 
        total_amount FLOAT, 
        total_savings FLOAT, 
        delivery_address TEXT NOT NULL, 
        delivery_fee FLOAT, 
        delivery_fee_applied BOOLEAN, 
        items JSON NOT NULL, 
        discounts JSON NOT NULL, 
        subtotal FLOAT, 
        id VARCHAR(36) NOT NULL, 
        created_at DATETIME, 
        updated_at DATETIME, 
        PRIMARY KEY (id)
);
```

---

## 📦 ORDER_ITEM

| Column            | Data Type | Description                   |
| ----------------- | --------- | ----------------------------- |
| id                | STRING    | Primary Key                   |
| order_id         | STRING    | Associated order ID           |
| product_id       | STRING    | Product ID                    |
| product_name     | STRING    | Product name                  |
| status            | STRING    | Item status                   |
| quantity          | INTEGER   | Quantity ordered              |
| unit_price       | FLOAT     | Price per unit                |
| total_price      | FLOAT     | Total price (before discount) |
| discount_applied | FLOAT     | Discount applied on this item |
| created_at       | TIMESTAMP | Record creation time          |
| updated_at       | TIMESTAMP | Record update time            |

```
CREATE TABLE order_item (
        order_id VARCHAR(36) NOT NULL, 
        product_id VARCHAR(36) NOT NULL, 
        product_name VARCHAR(100) NOT NULL, 
        status VARCHAR(10) NOT NULL, 
        quantity INTEGER, 
        unit_price FLOAT, 
        total_price FLOAT, 
        discount_applied FLOAT, 
        id VARCHAR(36) NOT NULL, 
        created_at DATETIME, 
        updated_at DATETIME, 
        PRIMARY KEY (id)
);
```
---

## 💸 DISCOUNT
| Column                  | Data Type | Description                  |
| ----------------------- | --------- | ---------------------------- |
| id                      | STRING    | Primary Key                  |
| code                    | STRING    | Discount code                |
| name                    | STRING    | Discount name                |
| description             | TEXT      | Description                  |
| discount_type          | STRING    | Type (flat, percentage)      |
| value                   | FLOAT     | Discount value               |
| applies_to             | JSON      | What the discount applies to |
| start_date             | TIMESTAMP | Start time                   |
| end_date               | TIMESTAMP | Expiry                       |
| is_active              | BOOLEAN   | Active or inactive           |
| usage_count            | INTEGER   | Total used times             |
| min_order_value       | FLOAT     | Minimum order value to apply |
| max_discount_amount   | FLOAT     | Max allowed discount         |
| min_tier               | INTEGER   | Required membership tier     |
| created_at             | TIMESTAMP | Record creation time         |
| updated_at             | TIMESTAMP | Record update time           |

```
CREATE TABLE discount (
        code VARCHAR(36) NOT NULL, 
        name VARCHAR(100) NOT NULL, 
        description TEXT NOT NULL, 
        discount_type VARCHAR(50) NOT NULL, 
        value FLOAT, 
        applies_to JSON NOT NULL, 
        start_date DATETIME NOT NULL, 
        end_date DATETIME NOT NULL, 
        is_active BOOLEAN, 
        usage_count INTEGER, 
        min_order_value FLOAT, 
        max_discount_amount FLOAT, 
        id VARCHAR(36) NOT NULL, 
        created_at DATETIME, 
        updated_at DATETIME, min_tier [INTEGER], 
        PRIMARY KEY (id)
);
```
---

## 🎁 DISCOUNT_USAGE

| Column           | Data Type | Description                      |
| ---------------- | --------- | -------------------------------- |
| id               | STRING    | Primary Key                      |
| discount_id     | STRING    | Associated discount ID           |
| user_id         | STRING    | User who used the discount       |
| order_id        | STRING    | Order where discount was applied |
| discount_amount | FLOAT     | Discount value applied           |
| used_at         | TIMESTAMP | When discount was used           |
| created_at      | TIMESTAMP | Record creation time             |
| updated_at      | TIMESTAMP | Record update time               |

```
CREATE TABLE discount_usage (
        discount_id VARCHAR(36) NOT NULL, 
        user_id VARCHAR(36) NOT NULL, 
        order_id VARCHAR(36) NOT NULL, 
        discount_amount FLOAT, 
        used_at DATETIME, 
        id VARCHAR(36) NOT NULL, 
        created_at DATETIME, 
        updated_at DATETIME, 
        PRIMARY KEY (id)
);
```
---

## 🔔 NOTIFICATIONS

| Column             | Data Type | Description                   |
| ------------------ | --------- | ----------------------------- |
| id                 | STRING    | Primary Key                   |
| user_id           | STRING    | Target user ID                |
| is_read           | BOOLEAN   | Read/unread status            |
| notification_type | STRING    | Type/category of notification |
| title              | STRING    | Notification title            |
| message            | TEXT      | Full message                  |
| created_at        | TIMESTAMP | Record creation time          |
| updated_at        | TIMESTAMP | Record update time            |

```
CREATE TABLE notifications (
        user_id VARCHAR(36) NOT NULL, 
        is_read BOOLEAN, 
        notification_type VARCHAR(36) NOT NULL, 
        title VARCHAR(50) NOT NULL, 
        message TEXT NOT NULL, 
        id VARCHAR(36) NOT NULL, 
        created_at DATETIME, 
        updated_at DATETIME, 
        PRIMARY KEY (id)
);
```
---

## 📈 USER_TIER_METRICS

| Column       | Data Type | Description                 |
| ------------ | --------- | --------------------------- |
| id           | STRING    | Primary Key                 |
| user_id     | STRING    | Associated user             |
| month_year  | STRING    | Month (format: YYYY-MM)     |
| order_count | INTEGER   | Orders placed in that month |
| total_spent | FLOAT     | Total spend in that month   |
| created_at  | TIMESTAMP | Record creation time        |
| updated_at  | TIMESTAMP | Record update time          |

```
CREATE TABLE user_tier_metrics (
        user_id VARCHAR(36) NOT NULL, 
        month_year VARCHAR(7) NOT NULL, 
        order_count INTEGER, 
        total_spent FLOAT, 
        id VARCHAR(36) NOT NULL, 
        created_at DATETIME, 
        updated_at DATETIME, 
        PRIMARY KEY (id)
);
```