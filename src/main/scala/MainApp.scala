import cats.effect.*
import data.Db
import data.repository.ToDoRepository
import http.ToDoRoutes
import org.http4s.*
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.implicits.*
import properties.AppProperties
import service.ToDoService


object MainApp extends IOApp.Simple {
  def run: IO[Unit] = {

    val appConfig = AppProperties.config

    Db.transactor.use { xa =>
      for {
        _ <- Db.init(xa)
        repo = ToDoRepository(xa)
        service = ToDoService(repo)
        routes = ToDoRoutes(service).httpRoutes.orNotFound
        _ <- IO.println(s"Starting the ${appConfig.name} HTTP Server on port ${appConfig.port} ...")
        _ <- BlazeServerBuilder[IO]
          .bindHttp(appConfig.port, "0.0.0.0")
          .withHttpApp(routes)
          .resource
          .useForever
      } yield ()
    }

  }
}

