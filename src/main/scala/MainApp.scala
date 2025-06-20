import cats.effect.*
import data.Db
import data.repository.ToDoRepository
import http.ToDoRoutes
import org.http4s.*
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.implicits.*
import service.ToDoService

object MainApp extends IOApp.Simple {
  def run: IO[Unit] = {

    Db.transactor.use { xa =>
      val repo = ToDoRepository(xa)
      val service = ToDoService(repo)
      val routes = ToDoRoutes(service).httpRoutes.orNotFound

      for {
        _ <- IO.println("Starting the HTTP Server on port 8080 ...")
        _ <- Db.init(xa)
        _ <- BlazeServerBuilder[IO]
          .bindHttp(8080, "0.0.0.0")
          .withHttpApp(routes)
          .resource
          .useForever
      } yield ()
    }

  }
}

