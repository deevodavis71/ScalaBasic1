package data

import cats.effect.*
import cats.syntax.all.*
import doobie.*
import doobie.hikari.HikariTransactor
import doobie.implicits.*

object Db {

  val transactor: Resource[IO, Transactor[IO]] =
    for {
      ce <- ExecutionContexts.fixedThreadPool(2)
      xa <- HikariTransactor.newHikariTransactor[IO](
        driverClassName = "org.h2.Driver",
        url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
        user = "sa",
        pass = "",
        connectEC = ce
      )
    } yield xa

  def init(xa: Transactor[IO]): IO[Unit] =
    for {
      _ <- IO.println("Initialising the DB...")
      _ <- (
        sql"""CREATE TABLE IF NOT EXISTS todo (
                  id INT AUTO_INCREMENT PRIMARY KEY,
                  title VARCHAR(255),
                  completed BOOLEAN
               )""".update.run *>
        sql"""INSERT INTO todo (title, completed) VALUES
                  ('Learn Cats', false),
                  ('Use http4s', true)
               """.update.run
        ).transact(xa)
    } yield ()
}
