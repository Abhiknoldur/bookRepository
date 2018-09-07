package com.knoldus.book.impl.service


import com.knoldus.book.api.BookService
import com.knoldus.book.impl.eventsourcing.{BookEntity, BookProcessor, BookRepository}
import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server._
import com.softwaremill.macwire._
import play.api.libs.ws.ahc.AhcWSComponents

class BookServiceLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new BookServiceApplication(context) {
      override def serviceLocator: ServiceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new BookServiceApplication(context) with LagomDevModeComponents
}

abstract class BookServiceApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with CassandraPersistenceComponents
    with AhcWSComponents {

  // Bind the service that this server provides
  override lazy val lagomServer: LagomServer = serverFor[BookService](wire[BookServiceImpl])

  //Register the JSON serializer registry
  override lazy val jsonSerializerRegistry: BookSerializerRegistry.type = BookSerializerRegistry

  // Register the lagom persistent entity
  persistentEntityRegistry.register(wire[BookEntity])

  lazy val repository: BookRepository = wire[BookRepository]

  // Register the lagom persistent read side processor persistent entity
  readSide.register(wire[BookProcessor])
}
