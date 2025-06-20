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
                  id INT PRIMARY KEY,
                  title VARCHAR,
                  completed BOOLEAN
               )""".update.run *>
        sql"""INSERT INTO todo (id, title, completed) VALUES
                  (1, 'Learn Cats', false),
                  (2, 'Use http4s', true)
               """.update.run
        ).transact(xa)
    } yield ()
}
