# Course Management Application
#
A **Java Spring Boot course management application** that provides a
server-side web interface for managing courses. The application
implements course creation, listing, updating, and deletion using
**Spring MVC, Thymeleaf, Spring Data JPA, Hibernate, and MySQL**.

The project was also **containerized with Docker and deployed on AWS
ECS**, with the application image stored in **Amazon ECR** and a MySQL
container used as the database container.

------------------------------------------------------------------------

## Project Overview

The application allows users to manage training courses through a web
interface.

Each course contains:

-   Course name
-   Trainer name
-   Duration
-   Fees

The application follows a layered backend architecture:

``` text
Thymeleaf UI
     │
     ▼
CourseController
     │
     ▼
CourseService
     │
     ▼
ICourseRepo
     │
     ▼
MySQL
```

------------------------------------------------------------------------

# Features

-   Display all courses
-   Add a new course
-   Update an existing course
-   Delete a course
-   Server-side HTML rendering with Thymeleaf
-   JPA entity mapping
-   MySQL database persistence
-   Automatic database schema updates using Hibernate
-   Docker containerization
-   AWS ECS deployment
-   Application image stored in Amazon ECR
-   MySQL container pulled from Docker Hub using the official
    `mysql:8.0` image

------------------------------------------------------------------------

# Tech Stack

## Application

  Technology          Purpose
  ------------------- ---------------------------------
  Java 17             Backend programming language
  Spring Boot 4.1.1   Application framework
  Spring MVC          Web request handling
  Thymeleaf           Server-side HTML rendering
  Spring Data JPA     Database access
  Hibernate           ORM / JPA implementation
  MySQL               Relational database
  Maven               Build and dependency management

## Deployment

  -----------------------------------------------------------------------
  Technology                          Purpose
  ----------------------------------- -----------------------------------
  Docker                              Containerization

  Amazon ECR                          Stores the application Docker image

  Amazon ECS                          Runs the containers

  Docker Hub                          Source of the official MySQL
                                      container image

  AWS networking/security             Allows the application and database
  configuration                       containers to communicate
  -----------------------------------------------------------------------

------------------------------------------------------------------------

# Project Structure

``` text
courseapp/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/subham/courseapp/
│   │   │       ├── CourseappApplication.java
│   │   │       │
│   │   │       ├── controller/
│   │   │       │   └── CourseController.java
│   │   │       │
│   │   │       ├── entity/
│   │   │       │   └── Course.java
│   │   │       │
│   │   │       ├── repo/
│   │   │       │   └── ICourseRepo.java
│   │   │       │
│   │   │       └── service/
│   │   │           ├── CourseService.java
│   │   │           └── ICourseService.java
│   │   │
│   │   └── resources/
│   │       ├── application.properties
│   │       └── templates/
│   │           ├── courseform.html
│   │           └── courselist.html
│   │
│   └── test/
│       └── java/
│           └── com/subham/courseapp/
│               └── CourseappApplicationTests.java
│
├── Dockerfile
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md
```

------------------------------------------------------------------------

# Application Architecture

The application uses a layered design.

### Controller Layer

`CourseController` handles incoming web requests and maps them to
application operations.

Examples:

``` text
GET  /courselist
GET  /showCourseForm
GET  /updateCourseForm?id={id}
POST /registerCourse
GET  /deleteCourse?id={id}
```

### Service Layer

`CourseService` contains the application logic and communicates with the
repository.

### Repository Layer

`ICourseRepo` extends Spring Data's `CrudRepository`:

``` java
public interface ICourseRepo extends CrudRepository<Course, Integer>
```

This provides standard database operations such as:

-   `findAll()`
-   `findById()`
-   `save()`
-   `deleteById()`

### Entity Layer

`Course` is a JPA entity mapped to a database table.

------------------------------------------------------------------------

# Course Entity

The `Course` entity contains:

  Field           Type      Description
  --------------- --------- ----------------------------
  `id`            Integer   Auto-generated primary key
  `courseName`    String    Name of the course
  `trainerName`   String    Name of the trainer
  `duration`      Integer   Course duration
  `fees`          Double    Course fees

The entity uses JPA annotations:

``` java
@Entity
public class Course
```

and:

``` java
@Id
@GeneratedValue(strategy = GenerationType.AUTO)
private Integer id;
```

------------------------------------------------------------------------

# Web Endpoints

This application is primarily a **server-side MVC web application**, not
a REST API.

## View All Courses

``` http
GET /courselist
```

Retrieves all courses and renders:

``` text
courselist.html
```

------------------------------------------------------------------------

## Show Add Course Form

``` http
GET /showCourseForm
```

Displays:

``` text
courseform.html
```

for entering a new course.

------------------------------------------------------------------------

## Register / Save Course

``` http
POST /registerCourse
```

Receives the course form data and saves it through the service and
repository layers.

After saving, the application redirects to:

``` text
/courselist
```

------------------------------------------------------------------------

## Show Update Form

``` http
GET /updateCourseForm?id={id}
```

The course ID is used to retrieve the existing course and populate the
form.

Example:

``` text
/updateCourseForm?id=1
```

The same form is then used to submit the updated course.

------------------------------------------------------------------------

## Delete Course

``` http
GET /deleteCourse?id={id}
```

Deletes the course with the specified ID and redirects back to:

``` text
/courselist
```

------------------------------------------------------------------------

# Database

The application uses **MySQL** with Spring Data JPA and Hibernate.

The local configuration uses:

``` properties
spring.datasource.url=jdbc:mysql://localhost:3306/subhamdb
spring.jpa.hibernate.ddl-auto=update
```

Hibernate automatically updates the database schema based on the JPA
entity.

### Important security note

Database credentials should **not** be committed to GitHub.

Use environment variables or another secure configuration mechanism for:

``` text
spring.datasource.username
spring.datasource.password
spring.datasource.url
```

If a real database password has already been committed to a public
repository, it should be **rotated immediately**.

------------------------------------------------------------------------

# Docker

The application includes a `Dockerfile`:

``` dockerfile
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY target/courseapp.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
```

The Docker image uses:

``` text
Eclipse Temurin Java 17
```

and runs the packaged Spring Boot JAR inside the container.

------------------------------------------------------------------------

# Build the Application

Before building the Docker image, create the executable JAR:

``` bash
mvn clean package
```

This produces:

``` text
target/courseapp.jar
```

The Dockerfile copies this file into the image.

------------------------------------------------------------------------

# Run with Docker Locally

After building the JAR:

``` bash
docker build -t courseapp .
```

Run the application:

``` bash
docker run -p 8080:8080 courseapp
```

The application is then available at:

``` text
http://localhost:8080/courselist
```

For database connectivity, the application container must be able to
reach a MySQL instance using the configured datasource settings.

------------------------------------------------------------------------

# AWS Deployment

The application was containerized and deployed using **Amazon ECS**.

The deployment uses two containers in the ECS task:

``` text
                    ECS Task
                       │
          ┌────────────┴────────────┐
          │                         │
          ▼                         ▼
   Application Container      MySQL Container
          │                         │
          │                         │
     Amazon ECR                 Docker Hub
          │                         │
          ▼                         ▼
   subham:latest                mysql:8.0
```

------------------------------------------------------------------------

## Application Container

The Spring Boot application is packaged into a Docker image and pushed
to **Amazon Elastic Container Registry (ECR)**.

The image reference used by the ECS task is similar to:

``` text
<account-id>.dkr.ecr.<region>.amazonaws.com/subham:latest
```

ECS pulls this image from the private ECR repository when starting the
task.

------------------------------------------------------------------------

## MySQL Container

The MySQL database is run as a second container in the same ECS task.

The image reference is:

``` text
mysql:8.0
```

Because the image does not specify a registry hostname, Docker resolves
it to the default public registry:

``` text
docker.io/library/mysql:8.0
```

Therefore, a separate "Docker Hub" selection is not required when
creating the ECS container definition.

Conceptually:

``` text
Container 1
└── ECR
    └── subham:latest

Container 2
└── Docker Hub
    └── mysql:8.0
```

This demonstrates that **an ECS task can use container images from
different registries**.

------------------------------------------------------------------------

# How the Two Containers Communicate

Because both containers run as part of the same ECS task, they can
communicate through the task's networking environment.

The application can use the MySQL container through the appropriate
MySQL hostname/port configuration.

For a task using the shared network namespace, the application can
communicate with MySQL through:

``` text
localhost:3306
```

The important distinction is:

``` text
localhost
```

inside the application container refers to the task's shared network
namespace when using the ECS networking configuration used for the
deployment.

The application therefore does not need the public IP address of the
MySQL container.

------------------------------------------------------------------------

# AWS Deployment Flow

The deployment process can be summarized as:

``` text
Java/Spring Boot Source Code
          │
          ▼
      Maven Build
          │
          ▼
     courseapp.jar
          │
          ▼
     Docker Build
          │
          ▼
    Docker Image
          │
          ▼
       Amazon ECR
          │
          ▼
      Amazon ECS
          │
     ┌────┴────┐
     │         │
     ▼         ▼
 Spring Boot   MySQL
 Container     Container
     │         │
     └────┬────┘
          │
          ▼
     Running App
```

------------------------------------------------------------------------

# Important Deployment Consideration

Running MySQL as a container inside the same ECS task is useful for
demonstrating containerized application architecture, but it is **not
normally the recommended production architecture for a persistent
database**.

If the ECS task is stopped or replaced, the MySQL container's local data
can be lost unless persistent storage is configured.

For a production system, a managed database such as:

``` text
Amazon RDS for MySQL
```

would generally be a better choice.

The project therefore demonstrates both an important **containerization
concept** and an architectural trade-off between running a database
container and using a managed database service.

------------------------------------------------------------------------

# Skills Demonstrated

This project demonstrates practical experience with several technologies
relevant to a Java Backend Developer role:

### Java

-   Java 17
-   Object-oriented programming
-   Classes and interfaces
-   Encapsulation

### Spring Boot

-   Spring Boot application configuration
-   Spring MVC
-   Dependency Injection
-   Controller layer
-   Service layer

### Database

-   MySQL
-   SQL/database fundamentals
-   JPA
-   Hibernate ORM
-   Entity mapping
-   CRUD operations

### Thymeleaf

-   Server-side HTML rendering
-   Form binding using `@ModelAttribute`
-   Model attributes
-   Dynamic rendering with Thymeleaf

### Docker

-   Dockerfile creation
-   Java application containerization
-   Building Docker images
-   Running containers
-   Container-to-container communication

### AWS

-   Amazon ECR
-   Amazon ECS
-   ECS task definitions
-   Multiple containers within an ECS task
-   Using images from different container registries

### Git/GitHub

-   Source-code version control
-   Repository management
-   Preparing a deployable project for GitHub

------------------------------------------------------------------------

# Future Improvements

Possible improvements include:

-   Add DTOs
-   Add Bean Validation
-   Add centralized exception handling
-   Add unit and integration tests
-   Use constructor injection consistently
-   Replace `Double` with `BigDecimal` for monetary values
-   Add pagination and sorting
-   Add authentication and authorization
-   Add REST APIs for external clients
-   Use a managed MySQL database such as Amazon RDS for production
-   Store database credentials using AWS Secrets Manager or another
    secure mechanism
-   Add CI/CD using GitHub Actions
-   Add health checks and monitoring for ECS
-   Use immutable/versioned Docker image tags instead of relying only on
    `latest`

------------------------------------------------------------------------

# Getting Started

## Prerequisites

Install:

-   Java 17
-   Maven
-   MySQL
-   Docker (optional for containerized execution)
-   Git

## Clone

``` bash
git clone <your-github-repository-url>
cd courseapp
```

## Configure MySQL

Create the database:

``` sql
CREATE DATABASE subhamdb;
```

Configure the application with your own database credentials.

## Run

``` bash
mvn spring-boot:run
```

Open:

``` text
http://localhost:8080/courselist
```

------------------------------------------------------------------------

# Author

**Shubham Gupta**

Java \| Spring Boot \| Spring MVC \| JPA/Hibernate \| MySQL \| Docker \|
AWS ECS \| Amazon ECR \| Git/GitHub
