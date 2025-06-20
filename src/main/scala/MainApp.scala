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
      for {
        _ <- Db.init(xa)
        repo = ToDoRepository(xa)
        service = ToDoService(repo)
        routes = ToDoRoutes(service).httpRoutes.orNotFound
        _ <- IO.println("Starting the HTTP Server on port 8080 ...")
        _ <- BlazeServerBuilder[IO]
          .bindHttp(8080, "0.0.0.0")
          .withHttpApp(routes)
          .resource
          .useForever
      } yield ()
    }

  }
}

