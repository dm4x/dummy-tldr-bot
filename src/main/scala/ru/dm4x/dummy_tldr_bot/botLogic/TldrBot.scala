package ru.dm4x.dummy_tldr_bot.botLogic

import cats.effect.Sync
import cats.implicits._
import fs2._
import org.typelevel.log4cats._
import ru.dm4x.dummy_tldr_bot.api._
import ru.dm4x.dummy_tldr_bot.api.dto.BotMessage
import ru.dm4x.dummy_tldr_bot.botLogic.BotCommand._

/**
  * Tldr telegram bot
  * When launched, send funny messages tldrBot storage algebra.
  *
  * @param api     telegram bot api
  * @param storage storage algebra for tldr items
  * @param logger  logger algebra
  */
class TldrBot[F[_]](
  api: StreamingBotAPI[F],
  storage: TldrDbStorage[F],
  logger: Logger[F])(
  implicit F: Sync[F]) {

  /**
    * Launches the bot process
    */
  def launch: Stream[F, Unit] = pollCommands.evalMap(handleCommand)

  private def pollCommands: Stream[F, BotCommand] = for {
    update <- api.pollUpdates(0)
    botMessage <- Stream.emits(update.message.flatMap(a => BotMessage(a.message_id, a.from, a.chat, a.date, a.text).some).toSeq)
  } yield BotCommand.fromRawMessage(botMessage)

  private def handleCommand(command: BotCommand): F[Unit] = command match {
    case c: Tldr => showRandomJoke(c.chatId)
    case c: ShowHelp => api.sendMessage(c.chatId, List(
      "Я весьма туп. Умею только обобщить о чем писали выше, да и это то делаю посредственно.\nНо ты можешь попробовать эти команды:",
      s"`$help` - покажет этот хелп",
      s"`$tldr` - постараюсь рассказать о чем там выше писали",
    ).mkString("\n"))
    case c: SilentWatcher => logger.info(s"raw message from chat: ${c.message}")
  }

  private def showRandomJoke(chatId: ChatId): F[Unit] = for {
    item <- storage.randomJoke(chatId)
    _ <- logger.info(s"tldr queried for chat $chatId") *> api.sendMessage(chatId,
      if (item.isEmpty) "Чет я хз"
      else item.mkString)
  } yield ()

}
