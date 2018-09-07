package com.knoldus.book.impl.eventsourcing

import akka.Done
import com.datastax.driver.core.{BoundStatement, PreparedStatement}
import com.knoldus.book.api.Book
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraSession

import scala.concurrent.{ExecutionContext, Future}

class BookRepository(session: CassandraSession)(implicit ec: ExecutionContext) {

  private var storeBook: PreparedStatement = _

  private var removeBook: PreparedStatement = _

  private var updateBook: PreparedStatement = _


  def createTable(): Future[Done] = {
    session.executeCreateTable(
      """
        |CREATE TABLE IF NOT EXISTS booktable(
        |isbn text PRIMARY KEY,
        |name text,
        |author text,
        |genre text
        |);
      """.stripMargin)

    session.executeCreateTable(
      """
        |CREATE INDEX IF NOT EXISTS
        |name_index ON booktable (name);
      """.stripMargin)
  }

  def createPreparedStatements: Future[Done] = {
    session.prepare("INSERT INTO booktable(isbn, name, author, genre) VALUES (?, ?, ?, ?)")
      .map { ps =>
        storeBook = ps
        Done
      }.map(_ => session.prepare("DELETE FROM booktable where isbn = ?").map(ps => {
      removeBook = ps
      Done
    })).map(_ => session.prepare("UPDATE booktable SET name=?,author=?,genre=? where isbn =?").map(ps => {
      updateBook = ps
      Done
    })).flatten
  }

  def storeBook(book: Book): Future[List[BoundStatement]] = {
    val bookCreateBook = storeBook.bind()
    bookCreateBook.setString("isbn", book.isbn)
    bookCreateBook.setString("name", book.name)
    bookCreateBook.setString("author", book.author)
    bookCreateBook.setString("genre", book.genre)
    Future.successful(List(bookCreateBook))
  }

  def updateBook(book: Book): Future[List[BoundStatement]] = {
    val bindUpdateBook = updateBook.bind()
    bindUpdateBook.setString("isbn", book.isbn)
    bindUpdateBook.setString("name", book.name)
    bindUpdateBook.setString("author", book.author)
    bindUpdateBook.setString("genre", book.genre)
    Future.successful(List(bindUpdateBook))
  }

  def removeBook(isbn: String): Future[List[BoundStatement]] = {
    val bindDeleteBook: BoundStatement = removeBook.bind()
    bindDeleteBook.setString("isbn", isbn)
    Future.successful(List(bindDeleteBook))
  }

  def getBookByName(name: String): Future[Option[Book]] =
    session.selectOne(s"SELECT * FROM booktable WHERE name = '$name' ALLOW FILTERING").map{optRow =>
      optRow.map{row =>
        val isbn = row.getString("isbn")
        val name = row.getString("name")
        val author = row.getString("author")
        val genre = row.getString("genre")
        Book(isbn, name, author, genre )
      }
    }

  def getBookByAuthor(author: String): Future[Option[Book]] =
    session.selectOne(s"SELECT * FROM booktable WHERE author = '$author'").map{optRow =>
      optRow.map{row =>
        val isbn = row.getString("isbn")
        val name = row.getString("name")
        val author = row.getString("author")
        val genre = row.getString("genre")
        Book(isbn, name, author, genre )
      }
    }

}