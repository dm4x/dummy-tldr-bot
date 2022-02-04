package ru.dm4x.dummy_tldr_bot.botLogic

import cats.Functor
import fs2.async.Ref
import cats.implicits._
import ru.dm4x.dummy_tldr_bot.api.ChatId

import scala.language.higherKinds
import scala.util.Random

/**
  * Algebra for managing storage of todo-list items
  */
trait TldrBotStorage[F[_]] {
  def randomJoke(chatId: ChatId): F[Item]
}

/**
  * Simple in-memory implementation of [[TldrBotStorage]] algebra, using [[Ref]].
  * In real world this would go to some database of sort.
  */
class InMemoryTodoListStorage[F[_] : Functor](
  private val ref: Ref[F, List[Item]]) extends TldrBotStorage[F] {

  def fillStorage: F[Unit] = {
    val jokes = List(
      "Суть вкратце: ебала жаба гадюку",
      "Нихуя не понятно, но очень интересно",
      "Кожаные ублюдки опять развели срач на пустом месте",
      "Шо то хуйня, шо это хуйня",
      "Издают какие то звуки, периодически работают",
      "Ты обращаешься ко мне, но делаешь это без уважения",
      "Кто не понял, тот поймет... я пока не понял",
      "В 2000г музыка Моцарта вошла в \"Реквием по мечте\". В 2008 - в фильм \"Сумерки\". А что они там понаписали я не ебу",
      "Скорее всего, там какой то заговор",
      "Я вышел с твоим вопросом в интернет",
      "Настоящий пацан не думает что было до него, он делает так чтобы после него ничего не было",
      "А че так можно было чтоли? и нахуя я все это читал",
      "Нельзя просто взять и прочитать все непрочитанные",
      "Падажжи, я сам еще читаю",
      "Полегче ковбой",
      "Мне что, все заново перечитывать? ну уж нет!",
      "Вот ты доебался с этим своим tldr",
      "Какой интерес спрашивать у меня то, что ты итак знаешь?",
      "Хватит болтать, а то весь класс пересажу на первую парту",
      "Там же ясно написано русским по белому!",
      "tldr даю только тем, у кого большое желание на лице",
      "Выводы будут долгими и мучительными...",
      "Ты мне уже третий раз надоел!",
      "Если будешь плохо работать, то всю жизнь будешь этот чат читать, как я...",
      "У меня слов нет смотреть на это!",
      "Я видал такую хуету, по сравнению с которой эта хуета - толковый словарь!",
      "Даже в критические дни Анна Ахматова продолжала писать стихи. Тут все походу с нее пример решили взять...",
      "Беру секундомер и замеряю расстояние...",
      "Перечитываю по слогам, особенно знаки препинания",
      "Ясно одно: пишут на нормальном, понятном и даже русском языке!",
      "У меня лучшая в мире работа - в чате косить под бота!",
      "Утомил ты меня. Пойду чай себе сделаю. Никуда не уходи, я скоро вернусь",
      "Пока тебя в чате не было, тут такое случилось!",
      "Слово — великое оружие жизни, но вот конкретно они тут лучше б его держали в кобуре",
      "Я не обязан все знать",
      "Почему ты задался этим вопросом?",
      "Я не знаю, что тебе от меня надо, но я точно знаю, что ты озорник"
    )
    ref.setAsync(jokes)
  }

  def randomJoke(chatId: ChatId): F[Item] = {
    ref.get.map(v => v.drop(new Random().nextInt(v.size - 1)).head)
  }


}

