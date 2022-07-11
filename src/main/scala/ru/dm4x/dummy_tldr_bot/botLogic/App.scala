package ru.dm4x.dummy_tldr_bot.botLogic

import cats.effect.{ExitCode, IO, IOApp}
import fs2.Stream

object App extends IOApp {

  def stream: Stream[IO, ExitCode] =
    for {
      token <- Stream.eval(IO(System.getenv("BOT_TOKEN")))
      exitCode <-
        new TldrBotProcess[IO](token).run.last.map(_ => ExitCode.Success)
    } yield exitCode

  override def run(args: List[String]): IO[ExitCode] =
    stream.compile.drain.as(ExitCode.Success)

}
