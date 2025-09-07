# Membership API Suite

A dual‑API system implementing a **Membership Program** using **Flask** (Python) and **Spring Boot** (Java), and uses **SQLite** database.

---

## 📁 Project Structure

```
project-root/
├── database
│   ├── application_database_python.db
│   ├── application_database.db
│   └── Tables_Schema.md
├── flask_api
│   ├── app
│   ├── firstclubenv
│   ├── instance
│   ├── logs
│   ├── requirements.txt
│   └── run.py
├── springboot_api
│   ├── HELP.md
│   ├── logs
│   ├── mvnw
│   ├── mvnw.cmd
│   ├── pom.xml
│   ├── src
│   └── target
├── postman
│   └── First Club Membership.postman_collection.json
└── README.md
```

## 📌 Overview

This project includes two RESTful APIs:

- **Flask API**: Python-based microservice for managing member details.
- **Spring Boot API**: Java-based microservice for handling membership-related logic.
- **SQLite** is used for database.

Included in this repo:
- Full API source code (Python + Java)
- SQLite schema and optional `.db` file
- Postman collection for testing endpoints
- Optional Docker setup for easy launch

---

## 🚀 Getting Started

### 🔧 Manual Setup

#### 1. Clone the repository
```
    git clone https://github.com/harishinjamuri/Membership-Program.git
    cd Membership-Program
```

#### 2. Run Flask API
    cd flask-api
    source firstclubenv/bin/activate # to activate the Virtual Environment
    pip install -r requirements.txt
    flask run

#### 3. Run Spring Boot API

    cd springboot-api
    ./mvnw spring-boot:run
#### 4. Database
    Use the included application_database.db file or create your own by changing the file in the configs.

### 🧪 API Testing with Postman

## Import the collection from:

`postman/First_Club_Membership.postman_collection.json`

Set environment variables if needed:

`base_url_flask = http://localhost:5000`

`base_url_spring = http://localhost:5050`


### 🗂 Folder Descriptions

| Folder               | Purpose                               |
| -------------------- | ------------------------------------- |
| `flask-api/`         | Python Flask REST API for member data |
| `springboot-api/`    | Java Spring Boot API for memberships  |
| `database/`         | SQLite DB and schema                  |
| `postman/`           | API testing collection                |

#### Table Schema
`database/Tables_Schema.md`


### Configurations

#### Flask ( Python ) 
File Path: `flask_api/app/config.py`
Database File Name:  `sqlite_db_name` 
Port : `port`   

#### SpringBoot ( JAVA ) 
File Path: `springboot_api/src/main/resources/application.properties`
Database File Name:  `spring.datasource.url` 
Port : `server.port`   

