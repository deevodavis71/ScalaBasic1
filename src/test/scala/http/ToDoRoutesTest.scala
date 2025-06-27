package http

import cats.effect.*
import cats.effect.unsafe.implicits.global
import dto.ToDoDto
import io.circe.generic.auto.*
import io.circe.syntax.*
import org.http4s.*
import org.http4s.circe.*
import org.http4s.implicits.*
import org.scalamock.scalatest.MockFactory
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import service.ToDoService


class ToDoRoutesTest extends AnyFlatSpec with Matchers with MockFactory {

  "GET /todos" should "return all todos as JSON" in {
    given EntityDecoder[IO, List[ToDoDto]] = jsonOf[IO, List[ToDoDto]]

    val mockService = mock[ToDoService]
    ((() => mockService.getAllToDos))
      .expects()
      .returning(IO.pure(List(ToDoDto("Test", "done"), ToDoDto("Write", "pending"))))

    val routes = ToDoRoutes(mockService).httpRoutes
    val request = Request[IO](Method.GET, uri"/todos")

    val response = routes.orNotFound.run(request).unsafeRunSync()

    response.status shouldBe Status.Ok

    val json = response.as[List[ToDoDto]].unsafeRunSync()
    json should contain theSameElementsAs List(ToDoDto("Test", "done"), ToDoDto("Write", "pending"))
  }

  "POST /todos" should "create and return a todo" in {

    val input = ToDoDto("New", "done")
    val mockService = mock[ToDoService]
    (mockService.createToDo _).expects(input).returning(IO.pure(input))

    val routes = ToDoRoutes(mockService).httpRoutes
    val request = Request[IO](Method.POST, uri"/todos").withEntity(input.asJson)

    val response = routes.orNotFound.run(request).unsafeRunSync()

    response.status shouldBe Status.Created
    val result = response.as[ToDoDto].unsafeRunSync()
    result shouldBe input
  }

  "GET /todos-request" should "trigger service call and respond" in {
    val mockService = mock[ToDoService]
    (mockService.sendToDoRequest _).expects().returning(IO.unit)

    val routes = ToDoRoutes(mockService).httpRoutes
    val request = Request[IO](Method.GET, uri"/todos-request")

    val response = routes.orNotFound.run(request).unsafeRunSync()

    response.status shouldBe Status.Ok
    val body = response.as[String].unsafeRunSync()
    body should include("Sent request")
  }

  "GET /hello/:name" should "respond with greeting" in {
    val mockService = mock[ToDoService]
    val routes = ToDoRoutes(mockService).httpRoutes
    val request = Request[IO](Method.GET, uri"/hello/Scala")

    val response = routes.orNotFound.run(request).unsafeRunSync()

    response.status shouldBe Status.Ok
    val body = response.as[String].unsafeRunSync()
    body should include("Hello, Scala")
  }
}