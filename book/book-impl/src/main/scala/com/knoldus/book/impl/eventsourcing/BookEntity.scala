package com.knoldus.book.impl.eventsourcing

import java.time.LocalDateTime

import akka.Done
import com.knoldus.book.api.Book
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity

class BookEntity extends PersistentEntity {

  override type Command = BookCommand[_]
  override type Event = BookEvent
  override type State = BookState

  override def initialState = BookState(None, LocalDateTime.now().toString)

  override def behavior: (BookState) => Actions = {
    case BookState(_, _) => Actions()
      .onCommand[CreateBookCommand, Done] {
      case (CreateBookCommand(book), ctx, _) ⇒
        ctx.thenPersist(BookCreated(book))(_ ⇒ ctx.reply(Done))
    }
      .onReadOnlyCommand[GetBookCommand, Book] {
      case (GetBookCommand(isbn), ctx, state) =>
        ctx.reply(state.book.getOrElse(Book(isbn, "not found", "not found", "not found")))
    }
      .onEvent {
        case (BookCreated(book), _) ⇒
          BookState(Some(book), LocalDateTime.now().toString)
      }
      .onCommand[UpdateBookCommand, Done] {
      case (UpdateBookCommand(book), ctx, _) =>
        ctx.thenPersist(BookCreated(book))(_ ⇒ ctx.reply(Done))
    }
      .onEvent {
        case (BookUpdated(book), _) ⇒
          BookState(Some(book), LocalDateTime.now().toString)
      }
      .onCommand[DeleteBookCommand, Done] {
      case (DeleteBookCommand(book), ctx, _) =>
        ctx.thenPersist(BookDeleted(book))(_ ⇒ ctx.reply(Done))
    }
      .onEvent {
        case (_, state) ⇒
          state
      }
  }
}