package data.entity

import cats.effect.*
import io.circe.generic.auto.*
import org.http4s.EntityEncoder
import org.http4s.circe.*

case class ToDo(id: Int, title: String, completed: Boolean)

given EntityEncoder[IO, List[ToDo]] = jsonEncoderOf[IO, List[ToDo]]
