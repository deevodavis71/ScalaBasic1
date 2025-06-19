import cats.effect.*
import data.Db
import doobie.Transactor
import doobie.implicits.*
import dto.{ToDo, given_EntityEncoder_IO_List}
import org.http4s.*
import org.http4s.Method.GET
import org.http4s.Uri.Path.Root
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.dsl.io.{Ok, *}
import org.http4s.implicits.*

object MainApp extends IOApp.Simple {
  def run: IO[Unit] = {

    // Set up the HTTP routes
    val routes = (xa: Transactor[IO]) =>
      HttpRoutes.of[IO] {
        case GET -> Root / "hello" / name =>
          for {
            _ <- IO.println(s"'/hello' request received")
            res <- Ok(s"Hello, $name")
          } yield res
        case GET -> Root / "todos" =>
          for {
            _ <- IO.println(s"'/todos' request received")
            res <- sql"SELECT id, title FROM todo"
              .query[ToDo]
              .to[List]
              .transact(xa)
              .flatMap(Ok(_))
          } yield res
      }.orNotFound

    // Set up the HTTP server with DB access
    Db.transactor.use { xa =>
      for {
        _ <- IO.println("Starting the HTTP Server on port 8080 ...")
        _ <- Db.init(xa)
        _ <- BlazeServerBuilder[IO]
          .bindHttp(8080, "0.0.0.0")
          .withHttpApp(routes(xa))
          .resource
          .useForever
      } yield ()
    }

  }
}

