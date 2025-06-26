package service

import cats.effect.*
import client.ToDoClient
import data.entity.ToDo
import data.repository.ToDoRepository
import dto.ToDoDto
import properties.{AppConfig, AppProperties}

class ToDoService(repo: ToDoRepository):
  val appConfig: AppConfig = AppProperties.config

  def getAllToDos: IO[List[ToDoDto]] =
    repo.findAll.map(
      _.map(todo => ToDoDto(todo.title, if todo.completed then "done" else "pending"))
    ).handleErrorWith { err =>
      IO.println(s"[ERROR] Failed to get todos: ${err.getMessage}") *> IO.raiseError(err)
    }

  def createToDo(dto: ToDoDto): IO[ToDoDto] =
    val todo = ToDo(0, dto.title, dto.status == "done")
    repo.create(todo).flatTap(created => IO.println(s"Created todo: $created")).map(
      created => ToDoDto(created.title, if created.completed then "done" else "pending")
    ).handleErrorWith { err =>
      IO.println(s"[ERROR] Failed to create todo: ${err.getMessage}") *> IO.raiseError(err)
    }

  def sendToDoRequest(): IO[Unit] =
    ToDoClient(appConfig.port).sendRequest().flatMap(_ => IO.println("Sent"))