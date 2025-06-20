package http

import cats.effect.*
import dto.given_EntityEncoder_IO_List
import org.http4s.*
import org.http4s.Method.GET
import org.http4s.Uri.Path.Root
import org.http4s.dsl.io.{Ok, *}
import service.ToDoService


class ToDoRoutes(toDoService: ToDoService):
  val httpRoutes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "hello" / name =>
      for {
        _ <- IO.println(s"'/hello' request received")
        res <- Ok(s"Hello, $name")
      } yield res

    case GET -> Root / "todos" =>
      for {
        _ <- IO.println(s"'/todos' request received")
        dtos <- toDoService.getAllDtos
        res <- Ok(dtos)
      } yield res
  }