package com.knoldus.book.api

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}

trait BookService extends Service {

  def createBook(isbn: String, name: String, author: String, genre: String): ServiceCall[NotUsed, String]
  def getBookById(isbn: String): ServiceCall[NotUsed, String]
  def getBookByName(name: String): ServiceCall[NotUsed, String]
  def getBookByAuthor(author: String): ServiceCall[NotUsed, String]
  def updateBook(isbn: String, name: String, author: String, genre: String): ServiceCall[NotUsed, String]
  def deleteBook(isbn: String): ServiceCall[NotUsed, String]

  override def descriptor: Descriptor = {
    import Service._

    named("book")
      .withCalls(
        restCall(Method.POST, "/book/create/:isbn/:name/:author/:genre", createBook _),
        restCall(Method.GET, "/book/details/isbn/:isbn", getBookById _),
        restCall(Method.GET, "/book/details/name/:name", getBookByName _),
        restCall(Method.GET, "/book/details/author/:author", getBookByAuthor _),
        restCall(Method.PUT, "/book/update/:isbn/:name/:author/:genre", updateBook _),
        restCall(Method.DELETE, "/book/delete/:isbn", deleteBook _)
      ).withAutoAcl(true)
  }
}