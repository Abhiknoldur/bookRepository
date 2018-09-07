package com.knoldus.book.impl.service

import com.knoldus.book.api.Book
import com.knoldus.book.impl.eventsourcing._
import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}

import scala.collection.immutable

object BookSerializerRegistry extends JsonSerializerRegistry {
  override def serializers: immutable.Seq[JsonSerializer[_]] = immutable.Seq(
    JsonSerializer[Book],
    JsonSerializer[CreateBookCommand],
    JsonSerializer[GetBookCommand],
    JsonSerializer[BookCreated],
    JsonSerializer[BookState],
    JsonSerializer[BookUpdated],
    JsonSerializer[BookDeleted]
  )
}
