package client

import cats.effect.*
import dto.ToDoDto
import io.circe.generic.auto.*
import io.circe.syntax.*
import org.http4s.*
import org.http4s.Method.POST
import org.http4s.blaze.client.BlazeClientBuilder
import org.http4s.circe.*
import properties.AppProperties


class ToDoClient(port: Int):
  def sendRequest(): IO[Unit] = {
    val appConfig = AppProperties.config

    BlazeClientBuilder[IO].resource.use { client =>
      val jsonPayload = ToDoDto(appConfig.makeRequestToDoContent.title, appConfig.makeRequestToDoContent.status)

      val req = Request[IO](method = POST, uri = Uri.unsafeFromString(s"http://localhost:${port}/todos"))
        .withEntity(jsonPayload.asJson)

      client.expect[String](req).flatMap(IO.println)
    }
  }
