package ru.dm4x.dummy_tldr_bot.api.dao

import cats.effect.{Async, Blocker, ContextShift, Resource}
import doobie.Transactor
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts
import ru.dm4x.dummy_tldr_bot.conf.Configuration

object DbTransactor {
  def make[F[_]: ContextShift: Async](implicit conf: Configuration): Resource[F, Transactor[F]] =
    Blocker[F].map { be =>
      Transactor.fromDriverManager[F](
        driver = conf.dbDriver,
        url = conf.dbUrl,
        user = conf.dbUser,
        pass = conf.dbPassword,
        blocker = be,
      )
    }

  /** `transactor` backed by connection pool
    * It uses 3 execution contexts:
    *  1 - for handling queue of connection requests
    *  2 - for handling blocking result retrieval
    *  3 - CPU-bound provided by `ContextShift` (usually `global` from `IOApp`)
    */
  def pooled[F[_]: ContextShift: Async](implicit conf: Configuration): Resource[F, Transactor[F]] =
    for {
      ce <- ExecutionContexts.fixedThreadPool[F](10)
      be <- Blocker[F]
      xa <- HikariTransactor.newHikariTransactor[F](
        driverClassName = conf.dbDriver,
        url = conf.dbUrl,
        user = conf.dbUser,
        pass = conf.dbPassword,
        connectEC = ce, // await connection on this EC
        blocker = be, // execute JDBC operations on this EC
      )
    } yield xa

}
