package com.knoldus.book.impl.eventsourcing

import akka.Done
import com.knoldus.book.api.Book
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import play.api.libs.json.{Format, Json}

trait BookCommand[R] extends ReplyType[R]

case class CreateBookCommand(book: Book) extends BookCommand[Done]

object CreateBookCommand{
  implicit val format: Format[CreateBookCommand] = Json.format[CreateBookCommand]
}

case class UpdateBookCommand(book: Book) extends BookCommand[Done]

object UpdateBookCommand{
  implicit val format: Format[UpdateBookCommand] = Json.format[UpdateBookCommand]
}

case class GetBookCommand(isbn: String) extends BookCommand[Book]

object GetBookCommand{
  implicit val format: Format[GetBookCommand] = Json.format[GetBookCommand]
}

case class DeleteBookCommand(isbn: String) extends BookCommand[Done]

object DeleteBookCommand{
  implicit val format: Format[DeleteBookCommand] = Json.format[DeleteBookCommand]
}