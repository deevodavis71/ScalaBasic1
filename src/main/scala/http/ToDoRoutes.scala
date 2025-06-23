package http

import cats.effect.*
import dto.{ToDoDto, given_EntityEncoder_IO_List}
import io.circe.generic.auto.*
import org.http4s.*
import org.http4s.Method.GET
import org.http4s.Uri.Path.Root
import org.http4s.circe.*
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.dsl.io.{Ok, *}
import service.ToDoService

given EntityDecoder[IO, ToDoDto] = jsonOf[IO, ToDoDto]

class ToDoRoutes(toDoService: ToDoService):
  val httpRoutes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "hello" / name =>
      for {
        _ <- IO.println(s"'/hello' request received")
        res <- Ok(s"Hello, $name")
      } yield res

    case GET -> Root / "todos" =>
      for {
        _ <- IO.println(s"'/todos' GET request received")
        dtos <- toDoService.getAllToDos
        res <- Ok(dtos)
      } yield res

    case req@POST -> Root / "todos" =>
      for {
        _ <- IO.println(s"'/todos' POST request received")
        dto <- req.as[ToDoDto]
        todo <- toDoService.createToDo(dto)
        res <- Created(todo)
      } yield res
  }