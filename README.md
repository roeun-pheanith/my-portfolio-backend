# 🚀 My Portfolio Backend

## 📝 Description

This repository contains the robust backend service for my personal portfolio website. It is designed to provide a secure and efficient **RESTful API** to manage, retrieve, and serve all necessary data for the corresponding frontend application, including projects, skills, contact messages, and more.

The application is built using **Java** and the **Spring Boot** framework, following industry best practices for development and deployment.

---

## ✨ Features

* **RESTful API:** Clean, resource-oriented endpoints for all portfolio data.
* **CRUD Operations:** Full Create, Read, Update, and Delete support for core resources (Projects, Experiences, Skills, etc.).
* **Data Persistence:** Configured to integrate with a relational database (e.g., MySQL, PostgreSQL, or H2).
* **Dockerized Deployment:** Includes a ready-to-use `Dockerfile` for easy containerization and production deployment.
* **Secure Endpoints (Optional):** Placeholder for implementing authentication (e.g., JWT) to secure administrative routes.

---

## 🛠️ Tech Stack

The `my-portfolio-backend` is built with:

* **Language:** Java
* **Framework:** Spring Boot 3+
* **Build Tool:** Apache Maven
* **Data Access:** Spring Data JPA / Hibernate
* **Containerization:** Docker
* **Database:** (Specify your choice, e.g., PostgreSQL or MySQL)

---

## ⚙️ Getting Started

Follow these steps to get a local copy up and running.

### Prerequisites

* **Java Development Kit (JDK):** Version 17+
* **Apache Maven**
* **Git**
* **Docker** (If using containerized setup)

### Installation & Setup

1.  **Clone the Repository**

    ```bash
    git clone [https://github.com/roeun-pheanith/my-portfolio-backend.git](https://github.com/roeun-pheanith/my-portfolio-backend.git)
    cd my-portfolio-backend
    ```

2.  **Configure Database**

    Update your database connection details in the `src/main/resources/application.properties` file.

    ```properties
    # Example Database Configuration
    server.port=8080
    spring.datasource.url=jdbc:mysql://localhost:3306/portfolio_db
    spring.datasource.username=your_username
    spring.datasource.password=your_password
    spring.jpa.hibernate.ddl-auto=update 
    ```

3.  **Build the Application**

    Package the application into an executable JAR file:
    ```bash
    mvn clean install
    ```

### 🏃 Running the Application

#### Option 1: Run with Maven (Development)

```bash
mvn spring-boot:run
