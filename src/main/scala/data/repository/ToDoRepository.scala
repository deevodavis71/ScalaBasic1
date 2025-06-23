package data.repository

import cats.effect.*
import data.entity.ToDo
import doobie.Transactor
import doobie.implicits.*

class ToDoRepository(xa: Transactor[IO]):
  def findAll: IO[List[ToDo]] =
    sql"SELECT id, title, completed FROM todo"
      .query[ToDo]
      .to[List]
      .transact(xa)

  def create(todo: ToDo): IO[ToDo] =
    sql"INSERT INTO todo (title, completed) VALUES (${todo.title}, ${todo.completed})"
      .update
      .withGeneratedKeys[Int]("id")
      .map(id => todo.copy(id = id))
      .compile
      .lastOrError
      .transact(xa)