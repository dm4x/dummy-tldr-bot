package ru.dm4x.dummy_tldr_bot.api.dto

import ru.dm4x.dummy_tldr_bot.api.{ChatId, Date, FromId}

case class BotResponse[T](ok: Boolean, result: T)

case class BotResult(update_id: Long, message: Option[BotMessage])

case class From (id: FromId, is_bot: Boolean, first_name: String, username: String)
case class Chat(id: ChatId, first_name: String, username: String, `type`: String)
case class Entities(offset: Long, length: Long, `type`: String)
case class File(file_id: String, file_unique_id: String, file_size: Long, width: Long, height: Long)
case class Animation(file_name: String, mime_type: String, duration: Long, width: Long, height: Long, thumb: File)
case class Document(file_name: String, mime_type: String, thumb: File, file_id: String, file_unique_id: String, file_size: Long)

case class BotMessage(message_id: Long, from: From, chat: Chat, date: Date, text: Option[String], entities: Entities)
case class TextMessage(message_id: Long, from: From, chat: Chat, date: Date, text: Option[String])
case class ReplyMessage(message_id: Long, from: From, chat: Chat, date: Date, reply_to_message: Option[TextMessage], text: String)
case class PhotoMessage(message_id: Long, from: From, chat: Chat, date: Date, photo: Seq[File], caption: String)
case class AnimationMessage(message_id: Long, from: From, chat: Chat, date: Date, animation: Animation, document: Document, caption: String)
