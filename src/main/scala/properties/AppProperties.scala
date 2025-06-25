package properties

import pureconfig.{ConfigReader, ConfigSource}

case class AppConfig(
                      name: String,
                      port: Int,
                      makeRequestToDoContent: MakeRequestToDoContentConfig
                    )derives ConfigReader

case class MakeRequestToDoContentConfig(
                                         title: String,
                                         status: String
                                       )derives ConfigReader

class AppProperties:
  def load(): AppConfig =
    val result: ConfigReader.Result[AppConfig] = ConfigSource.default.load[AppConfig]

    result match
      case Right(config) =>
        config
      case Left(err) =>
        throw new RuntimeException(s"Error loading config: $err")
