package service

import cats.effect.*
import data.repository.ToDoRepository
import dto.ToDoDto

class ToDoService(repo: ToDoRepository):
  def getAllDtos: IO[List[ToDoDto]] =
    repo.findAll.map(_.map(todo => ToDoDto(todo.title, if todo.completed then "done" else "pending")))