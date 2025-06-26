package service

import cats.effect.*
import cats.effect.unsafe.implicits.global
import data.entity.ToDo
import data.repository.ToDoRepository
import doobie.util.transactor.Transactor
import dto.ToDoDto
import hedgehog.*
import hedgehog.runner.*

object ToDoServiceTest extends Properties:

  val dummyXa: Transactor[IO] =
    Transactor.fromDriverManager[IO](
      "org.h2.Driver",
      "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
      "sa",
      "",
      None
    )

  def tests: List[Test] =
    List(
      example("getAllToDos maps entities to DTOs", testGetAllToDos),
      example("createToDo persists and maps correctly", testCreateToDo)
    )

  def testGetAllToDos: Result =
    val service = ToDoService(FakeRepo(dummyXa))
    val result = service.getAllToDos.unsafeRunSync()
    Result.all(
      List(
        Result.assert(result.head == ToDoDto("Test", "done")),
        Result.assert(result(1) == ToDoDto("Write", "pending"))
      )
    )

  def testCreateToDo: Result =
    val service = ToDoService(FakeRepo(dummyXa))
    val dto = ToDoDto("New", "done")
    val result = service.createToDo(dto).unsafeRunSync()
    Result.assert(result == dto)

  class FakeRepo(xa: Transactor[IO]) extends ToDoRepository(xa: Transactor[IO]):
    override def findAll: IO[List[ToDo]] =
      IO.pure(List(ToDo(1, "Test", true), ToDo(2, "Write", false)))

    override def create(todo: ToDo): IO[ToDo] =
      IO.pure(todo.copy(id = 42))

