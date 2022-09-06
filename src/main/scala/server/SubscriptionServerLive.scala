package Alkimi
package server
import Alkimi.server.routes.SubscriptionRoutes
import zhttp._
import zhttp.http.{HttpApp, Request}
import zhttp.service.Server
import zio._
import zio.json._

final case class SubscriptionServerLive(subscriptionRoutes: SubscriptionRoutes) extends SubscriptionServer:
  override def start(port: Int, host: String): Task[Unit] =
    for {
      - <- Server.start(port = port, http = allRoutes)
    } yield ()
  
  val allRoutes: HttpApp[Any, Throwable] =
    subscriptionRoutes.routes

object SubscriptionServerLive:
  val layer: ZLayer[SubscriptionRoutes, Throwable, SubscriptionServer] =
    ZLayer.fromFunction(SubscriptionServerLive.apply(_))
