package com.knoldus.book.impl.eventsourcing


import com.knoldus.book.api.Book
import play.api.libs.json.{Format, Json}

case class BookState(book: Option[Book], timeStamp: String)

object BookState {
  implicit val format: Format[BookState] = Json.format
}

