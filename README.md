# Library Management System

A beginner-friendly library management web application built with Java and
Spring Boot.

## Features

- Dashboard with book, member, and active-loan totals
- Add books and track total/available copies
- Register library members
- Issue a book for 14 days
- Return a book and restore its available copy
- View loan history and overdue status
- Form validation and duplicate ISBN/email checks
- Persistent local H2 database
- Automated service tests and GitHub Actions CI

## Technology

- Java 17
- Spring Boot 4.1.0
- Spring MVC and Thymeleaf
- Spring Data JPA and Hibernate
- H2 database
- Maven Wrapper
- JUnit and AssertJ

You do not install Spring Boot separately. It is a set of Maven dependencies
declared in `pom.xml`. Maven downloads those dependencies for the project.

## 1. Prerequisites

Install:

1. JDK 17 or newer
2. Git
3. An IDE such as IntelliJ IDEA Community Edition or VS Code with the Java
   Extension Pack

This computer already has JDK 17 at:

```text
C:\Program Files\Java\jdk-17.0.19
```

To set `JAVA_HOME` permanently in PowerShell:

```powershell
[Environment]::SetEnvironmentVariable(
    "JAVA_HOME",
    "C:\Program Files\Java\jdk-17.0.19",
    "User"
)
```

Close and reopen the terminal afterward. Check the installation:

```powershell
java -version
$env:JAVA_HOME
git --version
```

## 2. Open The Project

Open the `library-management-system` folder in your IDE. The important folders
are:

```text
src/main/java                 Java application code
src/main/resources/templates Thymeleaf HTML pages
src/main/resources/static    CSS and other browser assets
src/test/java                 Automated tests
pom.xml                       Dependencies and build configuration
```

## 3. Run The Application

From the project folder:

```powershell
.\mvnw.cmd spring-boot:run
```

Open [http://localhost:8080](http://localhost:8080).

Stop the server with `Ctrl+C`.

The first run may take longer because Maven downloads the dependencies.

## 4. Use The Application

1. Open **Books** and add catalogue records.
2. Open **Members** and register borrowers.
3. Open **Loans**, select a book and member, and issue the book.
4. Use **Return** when the member brings the book back.
5. Open **Dashboard** to see totals and recent activity.

Two books and two members are inserted automatically only when the database is
empty.

## 5. Database

Application data is stored in:

```text
data/librarydb.mv.db
```

The `data` folder is ignored by Git so personal test data is not published.

For development, open [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
and use:

```text
JDBC URL: jdbc:h2:file:./data/librarydb
User Name: sa
Password: (leave empty)
```

## 6. Architecture

The request flow is:

```text
Browser -> Controller -> Service -> Repository -> H2 database
```

- **Entity** classes map Java objects to database tables.
- **Repository** interfaces provide database operations through Spring Data.
- **LibraryService** contains borrowing and returning business rules.
- **LibraryController** handles browser requests and chooses HTML templates.
- **Thymeleaf** renders server data into HTML.

The central borrowing operation is transactional. Book inventory and the new
loan are saved as one unit; if an error occurs, both changes roll back.

## 7. Run Tests

```powershell
.\mvnw.cmd test
```

The tests use an in-memory database, so they do not modify your development
data.

Create the executable JAR:

```powershell
.\mvnw.cmd package
```

Run the packaged application:

```powershell
java -jar target\library-management-system-0.0.1-SNAPSHOT.jar
```

## 8. Publish To GitHub

Create an empty repository on GitHub named `library-management-system`. Do not
add a README or `.gitignore` on GitHub because this project already has them.

Then run:

```powershell
git init
git add .
git commit -m "Build library management system"
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/library-management-system.git
git push -u origin main
```

Replace `YOUR_USERNAME` with your GitHub username. GitHub may ask you to sign in
through the browser or use a personal access token.

For future updates:

```powershell
git add .
git commit -m "Describe the change"
git push
```

The workflow in `.github/workflows/ci.yml` runs the tests automatically after
each push to `main`.

## Useful Next Features

- Search and filter books
- Edit and delete books/members
- Login and role-based access with Spring Security
- Fine calculation for overdue loans
- PostgreSQL for production
- REST API and a separate frontend
- Deployment to a cloud hosting service

## Official References

- [Spring Boot](https://spring.io/projects/spring-boot/)
- [Spring Initializr](https://start.spring.io/)
- [Spring Data JPA guide](https://spring.io/guides/gs/accessing-data-jpa/)
- [Spring MVC guide](https://spring.io/guides/gs/serving-web-content/)
