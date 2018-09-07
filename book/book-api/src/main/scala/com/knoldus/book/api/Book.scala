package com.knoldus.book.api

import play.api.libs.json.{Format, Json}

case class Book(isbn: String, name: String, author: String, genre: String)

object Book {
  implicit val format: Format[Book] = Json.format
}
