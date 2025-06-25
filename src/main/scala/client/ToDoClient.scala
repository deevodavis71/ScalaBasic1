package client

import cats.effect.*
import dto.ToDoDto
import io.circe.generic.auto.*
import io.circe.syntax.*
import org.http4s.*
import org.http4s.Method.POST
import org.http4s.blaze.client.BlazeClientBuilder
import org.http4s.circe.*


class ToDoClient(port: Int):
  def sendRequest(): IO[Unit] = {
    BlazeClientBuilder[IO].resource.use { client =>
      val jsonPayload = ToDoDto("Sent Via Http Client", "pending")

      val req = Request[IO](method = POST, uri = Uri.unsafeFromString(s"http://localhost:${port}/todos"))
        .withEntity(jsonPayload.asJson)

      client.expect[String](req).flatMap(IO.println)
    }
  }
