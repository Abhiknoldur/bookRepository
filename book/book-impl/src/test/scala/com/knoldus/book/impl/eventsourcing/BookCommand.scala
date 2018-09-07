package com.knoldus.book.impl.eventsourcing

import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType

trait BookCommand[R] extends ReplyType[R]
