package properties

import com.typesafe.config.ConfigFactory
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import pureconfig.*


class AppPropertiesTest extends AnyFlatSpec with Matchers {

  "AppConfig" should "load valid config" in {
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

    result match {
      case Right(conf) =>
        conf.name shouldBe "my-app"
        conf.port shouldBe 8080
        conf.makeRequestToDoContent.title shouldBe "Hello"
        conf.makeRequestToDoContent.status shouldBe "active"

      case Left(err) =>
        fail(s"Config failed to load: $err")
    }
  }
}