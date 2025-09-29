# My Portfolio Backend

This is the backend service for my personal portfolio, built using **Java / Spring Boot**.

---

## Table of Contents

- [Features](#features)  
- [Tech Stack](#tech-stack)  
- [Getting Started](#getting-started)  
  - [Prerequisites](#prerequisites)  
  - [Installation](#installation)  
  - [Running the Application](#running-the-application)  
  - [Docker Setup](#docker-setup)  
- [Configuration](#configuration)  
- [API Endpoints](#api-endpoints)  
- [Database](#database)  
- [Testing](#testing)  
- [Deployment](#deployment)  
- [Contributing](#contributing)  
- [License](#license)  
- [Contact](#contact)

---

## Features

- CRUD APIs for portfolio data (projects, blogs, skills, etc.)  
- RESTful design  
- Configuration support  
- Dockerized deployment  
- (Optional) Authentication / Authorization — *add if relevant*

---

## Tech Stack

- Java  
- Spring Boot  
- Maven  
- Docker  
- (Optional) Spring Data JPA, Hibernate, etc.  
- (Optional) H2 / MySQL / PostgreSQL (whichever DB you use)  

---

## Getting Started

### Prerequisites

Make sure you have installed:

- Java (version 17+ or the version your project uses)  
- Maven  
- (Optional) Docker & Docker Compose  
- (Optional) A relational database (MySQL / PostgreSQL) if not using an embedded DB  

### Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/roeun-pheanith/my-portfolio-backend.git
   cd my-portfolio-backend
2. (Optional) configure your database settings in
   src/main/resources/application.properties (or application.yml).
3. Build the application:
   ```bash
   mvn clean install
 Running the application:
   You can run it via Meven
   ```bash
   mvn spring-boot:run
  or run the generate JAR:
  ```bash
  java -jar target/my-portfolio-backend-0.0.1-SNAPSHOT.jar
  The server should start (by default) on port 8080 (unless overridden via config).
 Docker setup you already have the Dockerfile in the repo. To build and run with Docker:
  ```bash
  # build the image
  docker build -t my-portfolio-backend .
  
  # run the container
  docker run -d -p 8080:8080 my-portfolio-backend
  (Optional: use docker-compose if you have multiple services like database + backend.)
 Configuration
  You can configure properties in application.properties or application.yml, for example:
  server.port=8080
  spring.datasource.url=jdbc:mysql://localhost:3306/portfolio_db
  spring.datasource.username=yourusername
  spring.datasource.password=yourpassword
  spring.jpa.hibernate.ddl-auto=update

  Add any other custom properties you use (JWT secrets, file storage, external APIs, etc.).
 API Endpoints
  HTTP Method	Path	Description
  GET	/api/projects	Get all projects
  GET	/api/projects/{id}	Get project by ID
  POST	/api/projects	Create new project
  PUT / PATCH	/api/projects/{id}	Update project
  DELETE	/api/projects/{id}	Delete project
  
  (Add endpoints for blogs, skills, contacts, etc. as applicable.)
  
  Include sample request / response bodies if you like.

