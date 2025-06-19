# Scala Functional HTTP API Example

This project demonstrates how to build a small functional HTTP API in Scala using modern libraries including http4s,
Cats Effect, Circe, and Doobie, with an in-memory H2 database.

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

# Running the app

```bash
$ sbt run
```

# Interacting with the app

```bash
$ curl localhost:8080/hello/world
Hello, world

$ curl localhost:8080/todos
[{"id":1,"title":"Learn Cats"},{"id":2,"title":"Use http4s"}]
```