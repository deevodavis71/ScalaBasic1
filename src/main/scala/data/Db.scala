package data

import cats.effect.*
import cats.syntax.all.*
import doobie.*
import doobie.hikari.HikariTransactor
import doobie.implicits.*

object Db {

  // Set up the In-Memory DB resource
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

  // Initialise the DB with some data
  def init(xa: Transactor[IO]): IO[Unit] =
    val create =
      sql"""
        CREATE TABLE todo (
          id        INT PRIMARY KEY,
          title     VARCHAR(255),
          completed BOOLEAN
        )
      """.update.run

    val insert =
      sql"""INSERT INTO todo (id, title, completed) VALUES (1, 'Learn Cats', false), (2, 'Use http4s', true)"""
        .update.run

    (create *> insert).transact(xa).void
}
