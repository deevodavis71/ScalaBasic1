package dto

import cats.effect.*
import io.circe.generic.auto.*
import org.http4s.EntityEncoder
import org.http4s.circe.*

case class ToDoDto(title: String, status: String)

given EntityEncoder[IO, List[ToDoDto]] = jsonEncoderOf[IO, List[ToDoDto]]