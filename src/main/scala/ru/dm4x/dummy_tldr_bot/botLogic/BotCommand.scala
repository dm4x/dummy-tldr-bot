package ru.dm4x.dummy_tldr_bot.botLogic

import ru.dm4x.dummy_tldr_bot.api.ChatId
import ru.dm4x.dummy_tldr_bot.api.dto.BotMessage

sealed trait BotCommand

object BotCommand {

  case class ShowHelp(chatId: ChatId) extends BotCommand
  case class Tldr(chatId: ChatId) extends BotCommand
  case class SilentWatcher(message: BotMessage) extends BotCommand

  def fromRawMessage(message: BotMessage): BotCommand = message match {
    case BotMessage(_, _, chat, _, text, _) => botReaction(text, chat.id)
    case _ => db.save(_)
  }

  def botReaction(message: Option[String], chatId: Long): BotCommand = message match {
    case Some(`help`) | Some("/start") => ShowHelp(chatId)
    case Some(`tldr`) => Tldr(chatId)
  }

  val help = "/help"
  val tldr = "/tldr"
}
