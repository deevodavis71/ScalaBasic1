package properties

import com.typesafe.config.ConfigFactory
import hedgehog.*
import hedgehog.runner.*
import pureconfig.*

object AppPropertiesTest extends Properties:

  def tests: List[Test] =
    List(
      example("load valid config", testValidConfig)
    )

  def testValidConfig: Result =
    val configStr =
      """
        |name = "my-app"
        |port = 8080
        |make-request-to-do-content {
        |  title = "Hello"
        |  status = "active"
        |}
        |""".stripMargin

    val configSource = ConfigSource.fromConfig(ConfigFactory.parseString(configStr))
    val result = configSource.load[AppConfig]

    result match
      case Right(conf) =>
        Result.all(
          List(
            Result.assert(conf.name == "my-app"),
            Result.assert(conf.port == 8080),
            Result.assert(conf.makeRequestToDoContent.title == "Hello"),
            Result.assert(conf.makeRequestToDoContent.status == "active")
          )
        )
      case Left(err) =>
        Result.failure.log(s"Config failed to load: $err")