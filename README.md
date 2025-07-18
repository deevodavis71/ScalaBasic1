# Scala Functional HTTP API Example

This project demonstrates how to build a small functional HTTP API in Scala using modern libraries including http4s,
Cats Effect, Circe, PureConfig, and Doobie, with an in-memory H2 database.

## TLDR;

Shows the following functionality:

* Application properties
* HTTP Routes & API Endpoints
* HTTP Client Request
* Mapping between DTO & Entity Types
* Database Requests
* ScalaTest Unit Tests with Mocking

## Libraries Used

### http4s `0.23.12`

A type-safe, purely functional HTTP server and client library built on top of Cats Effect.  
Used to define and serve REST endpoints.

### Cats Effect `3.5.3`

Provides the `IO` data type and concurrency primitives to manage side effects in a principled, purely functional way.  
Forms the backbone of the application runtime.

### Circe `0.14.6`

A JSON library for Scala that integrates well with http4s and supports automatic derivation of encoders/decoders using
case classes.  
Used to serialize and deserialize JSON payloads.

### Doobie `1.0.0-RC4`

A functional JDBC layer for Scala.  
Used to interact with an in-memory H2 database using `ConnectionIO` values that integrate with Cats Effect.

### PureConfig `0.17.9`

PureConfig is a Scala library for loading configuration files.

# Running the app

```bash
$ sbt run

[info] welcome to sbt 1.11.2 (Homebrew Java 21.0.5)
[info] loading project definition from /Users/steve.davis/Development/scala/ScalaBasic1/project
[info] loading settings for project root from build.sbt...
[info] set current project to ScalaBasic1 (in build file:/Users/steve.davis/Development/scala/ScalaBasic1/)
[info] running (fork) MainApp
[error] SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
[error] SLF4J: Defaulting to no-operation (NOP) logger implementation
[error] SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
[info] Starting the HTTP Server on port 8080 ...
```

# Running the unit tests

```bash
$ sbt test

test
[info] compiling 4 Scala sources to /Users/steve.davis/Development/scala/ScalaBasic1/target/scala-3.3.6/classes ...
[info] done compiling
[info] compiling 2 Scala sources to /Users/steve.davis/Development/scala/ScalaBasic1/target/scala-3.3.6/test-classes ...
[info] done compiling
[info] AppPropertiesTest:
[info] AppConfig
[info] - should load valid config
[info] ToDoServiceTest:
[info] getAllToDos
[info] - should map entities to DTOs
[info] createToDo
Created todo: ToDo(42,New,true)
[info] - should persist and map correctly
[info] Run completed in 906 milliseconds.
[info] Total number of tests run: 3
[info] Suites: completed 2, aborted 0
[info] Tests: succeeded 3, failed 0, canceled 0, ignored 0, pending 0
[info] All tests passed.
```

# Interacting with the app

```bash
$ curl localhost:8080/hello/world
Hello, world

$ curl localhost:8080/todos
[{"title":"Learn Cats","status":"pending"},{"title":"Use http4s","status":"done"}]

$ curl localhost:8080/todos-request
"Sent request"
```

# Assembling for Deployment

```bash
$ sbt assembly

[info] welcome to sbt 1.11.2 (Homebrew Java 21.0.5)
[info] loading settings for project scalabasic1-build from plugins.sbt...
[info] loading project definition from /Users/steve.davis/Development/scala/ScalaBasic1/project
[info] loading settings for project root from build.sbt...
[info] set current project to ScalaBasic1 (in build file:/Users/steve.davis/Development/scala/ScalaBasic1/)
[info] 4 file(s) merged using strategy 'First' (Run the task at debug level to see the details)
[info] 55 file(s) merged using strategy 'Discard' (Run the task at debug level to see the details)
[info] Built: /Users/steve.davis/Development/scala/ScalaBasic1/target/scala-3.3.6/ScalaBasic1-assembly-0.1.0-SNAPSHOT.jar
[info] Jar hash: 4d0827b7286e2446531842b4b5249310bbdbb81e
[success] Total time: 3 s, completed 20 Jun 2025, 11:51:33

$ java -jar target/scala-3.3.6/ScalaBasic1-assembly-0.1.0-SNAPSHOT.jar

Initialising the DB...
Starting the ScalaBasic1 HTTP Server on port 8080 ...
```