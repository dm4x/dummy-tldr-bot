package ru.dm4x.dummy_tldr_bot.botLogic

import cats.Functor
import cats.effect.concurrent.Ref
import cats.implicits._
import ru.dm4x.dummy_tldr_bot.api.ChatId
import ru.dm4x.dummy_tldr_bot.botLogic.TldrDbStorage.jokes

import scala.util.Random

/**
  * Algebra for managing storage of todo-list items
  */
trait TldrBotStorage[F[_]] {
  def randomJoke(): F[Item]
}

/**
  * Simple in-memory implementation of [[TldrBotStorage]] algebra, using [[Ref]].
  * In real world this would go to some database of sort.
  */
class InMemoryTldrBotStorage[F[_] : Functor](private val ref: Ref[F, Seq[Item]]) extends TldrBotStorage[F] {

  def fillStorage: F[Unit] = {
    ref.set(jokes)
  }

  def randomJoke(): F[Item] = {
    ref.get.map(v => v.drop(new Random().nextInt(v.size - 1)).head)
  }


}

