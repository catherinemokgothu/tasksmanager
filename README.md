# Tasks Manager (Spring Boot + H2)

A minimal REST API to create and list tasks using Spring Boot, Spring Data JPA (Hibernate) and an in-memory H2 database.

## Requirements

- Java 17+ (project uses Java 21 in pom.xml)
- Maven 3.6+

## Build & Run

Build the project:

```bash
mvn clean package
```

Run using Maven (development):

```bash
mvn spring-boot:run
```

Or run the packaged jar:

```bash
java -jar target/tasksmanager-1.0-SNAPSHOT.jar
```

The application listens on port 8080 by default.

## H2 Console

The H2 web console is enabled at: `http://localhost:8080/h2-console`

Use the JDBC URL configured in `src/main/resources/application.properties`:

```
spring.datasource.url=jdbc:h2:mem:tasksdb;DB_CLOSE_DELAY=-1
```

- Username: `sa`
- Password: (empty)

Note: data is ephemeral and will be lost when the application stops.

## Endpoints

Base path: `/tasks`

### GET /tasks

- Description: Returns a JSON array of all tasks.
- Response (200):

Example:

```json
[
  {
    "id": 1,
    "title": "Buy milk",
    "description": "2 liters"
  }
]
```

### POST /tasks

- Description: Create a new task.
- Request body (application/json):

Example request:

```json
{
  "title": "Buy milk",
  "description": "2 liters"
}
```

- Response (201 Created): returns the saved task including `id` and sets `Location` header to `/tasks/{id}`.

Example response:

```json
{
  "id": 1,
  "title": "Buy milk",
  "description": "2 liters"
}
```

## Curl examples

Get all tasks:

```bash
curl -s -X GET "http://localhost:8080/tasks" | jq .
```

Create a task:

```bash
curl -s -X POST "http://localhost:8080/tasks" -H "Content-Type: application/json" -d '{"title":"Buy milk","description":"2 liters"}'
```

## Code overview

- Controller: `src/main/java/org/example/controller/TaskController.java`
- Model: `src/main/java/org/example/model/Task.java`
- Repository: `src/main/java/org/example/repository/TaskRepository.java`
- Application entry: `src/main/java/org/example/TasksManagerApplication.java`
- Application properties: `src/main/resources/application.properties`

## Troubleshooting

- If port 8080 is in use, change `server.port` in `application.properties`.
- If H2 console is blank, make sure the JDBC URL matches the `spring.datasource.url` in `application.properties`.

## License / Contact

This is an example project. For questions, open an issue or contact the maintainer.
