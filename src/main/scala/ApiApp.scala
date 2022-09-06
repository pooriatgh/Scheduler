package Alkimi
import Alkimi.server._
import Alkimi.server.routes._
import Alkimi.services.*
import zio.*

object ApiApp extends ZIOAppDefault:
  override def run: ZIO[Any, Any, Any] =
    ZIO
      .serviceWithZIO[SubscriptionServer](_.start(8030, "localhost"))
      .provide(
        SubscriptionRoutes.layer,
        SubscriptionServerLive.layer,
        SubscriptionDataSourceLive.layer,
        SubscriptionServiceLive.layer,
        ZLayer.Debug.mermaid
      )