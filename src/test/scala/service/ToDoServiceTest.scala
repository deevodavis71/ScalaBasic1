package service

import cats.effect.*
import cats.effect.unsafe.implicits.global
import data.entity.ToDo
import data.repository.ToDoRepository
import doobie.util.transactor.Transactor
import dto.ToDoDto
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ToDoServiceTest extends AnyFlatSpec with Matchers {

  val dummyXa: Transactor[IO] =
    Transactor.fromDriverManager[IO](
      "org.h2.Driver",
      "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
      "sa",
      "",
      None
    )

  class FakeRepo(xa: Transactor[IO]) extends ToDoRepository(xa) {
    override def findAll: IO[List[ToDo]] =
      IO.pure(List(ToDo(1, "Test", true), ToDo(2, "Write", false)))

    override def create(todo: ToDo): IO[ToDo] =
      IO.pure(todo.copy(id = 42))
  }

  "getAllToDos" should "map entities to DTOs" in {
    val service = new ToDoService(new FakeRepo(dummyXa))
    val result = service.getAllToDos.unsafeRunSync()
    result should contain inOrderOnly(
      ToDoDto("Test", "done"),
      ToDoDto("Write", "pending")
    )
  }

  "createToDo" should "persist and map correctly" in {
    val service = new ToDoService(new FakeRepo(dummyXa))
    val dto = ToDoDto("New", "done")
    val result = service.createToDo(dto).unsafeRunSync()
    result shouldBe dto
  }
}