package ru.dm4x.dummy_tldr_bot.botLogic

import cats.effect.{ConcurrentEffect, ContextShift}
import cats.effect.concurrent.Ref
import cats.implicits._
import fs2.Stream
import io.circe.generic.auto._
import org.http4s._
import org.http4s.circe._
import org.http4s.client.blaze.BlazeClientBuilder
import org.typelevel.log4cats.slf4j.Slf4jLogger
import pureconfig.ConfigSource
import pureconfig.generic.auto.exportReader
import ru.dm4x.dummy_tldr_bot.api.Http4SBotAPI
import ru.dm4x.dummy_tldr_bot.api.dao.DbTransactor
import ru.dm4x.dummy_tldr_bot.api.dto.{BotResponse, BotResult}
import ru.dm4x.dummy_tldr_bot.conf.Configuration

import scala.concurrent.ExecutionContext

/**
  * Creates and wires up everything that is needed to launch a [[TldrBot]] and launches it.
  *
  * @param token telegram bot token
  */
class TldrBotProcess[F[_]](token: String)(implicit F: ConcurrentEffect[F], ctx: ContextShift[F]) {

  implicit val decoder: EntityDecoder[F, BotResponse[List[BotResult]]] =
    jsonOf[F, BotResponse[List[BotResult]]]

  def run: Stream[F, Unit] =
    BlazeClientBuilder[F](ExecutionContext.global).stream.flatMap { client =>
      implicit val config: Configuration = ConfigSource.default.load[Configuration].getOrElse("No config error".raiseError)
      val streamF: F[Stream[F, Unit]] = for {
        logger <- Slf4jLogger.create[F]
        dbResource <- F.pure(DbTransactor.make[F])
        storage = new TldrDbStorage(dbResource)
        _ <- Ref.of(List.empty[Item]).map(new InMemoryTldrBotStorage(_))
        botAPI <- F.delay(new Http4SBotAPI(token, client, logger))
        tldrBot <- F.delay(new TldrBot(botAPI, storage, logger))
      } yield tldrBot.launch

      Stream.force(streamF)
    }
}
