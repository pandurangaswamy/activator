package snap

import akka.actor._
import com.typesafe.sbtchild._
import java.util.concurrent.atomic.AtomicInteger
import activator.properties.ActivatorProperties
import scala.util.control.NonFatal
import java.net.URLEncoder

class App(val config: AppConfig, val system: ActorSystem, val sbtMaker: SbtChildProcessMaker) {
  val actor = system.actorOf(Props(new AppActor(config, sbtMaker)), name = "app-" + URLEncoder.encode(config.id, "UTF-8"))

  // TODO - this method is dangerous, as it hits the file system.
  // Figure out when it should initialize/run.
  val templateID: Option[String] =
    try {
      val props = snap.IO loadProperties (new java.io.File(config.location, "project/build.properties"))
      Option(props.getProperty(ActivatorProperties.TEMPLATE_UUID_PROPERTY_NAME, null))
    } catch {
      case e: java.io.IOException => None // TODO - Log?
    }
}
