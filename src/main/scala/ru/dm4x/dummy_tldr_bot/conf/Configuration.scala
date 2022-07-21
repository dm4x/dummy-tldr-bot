package ru.dm4x.dummy_tldr_bot.conf

import pureconfig._
import pureconfig.generic.auto._

case class Configuration(
    dbDriver: String,
    dbUrl: String,
    dbName: String,
    dbUser: String,
    dbPassword: String
)
