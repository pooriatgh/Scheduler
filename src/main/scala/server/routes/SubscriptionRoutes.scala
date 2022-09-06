package Alkimi
package server.routes
import Alkimi.models.domain.*
import Alkimi.services.SubscriptionService
import io.netty.handler.codec.http.HttpResponse
import zio.*
import zhttp.http.*
import zio.json.*

final case class SubscriptionRoutes(subscriptionService: SubscriptionService):

  def parseBody[A: JsonDecoder](request: Request): IO[Throwable, A] =
    for {
      body   <- request.bodyAsString.orElseFail(throw new RuntimeException("fail to get as string")).debug
      parsed <- ZIO.from(body.fromJson[A]).debug.mapError(e => throw new RuntimeException(s"fail to parse as Json ${e}"))
    } yield parsed

  val routes: Http[Any, Nothing, Request, Response] = Http.collectZIO[Request] {

    case req @ Method.POST -> !! / "subscribe" / id =>
      val result: ZIO[Any, Throwable, Response] = for {
        subscribe <- parseBody[Subscription](req)
        _         <- subscriptionService.subscribe(id, subscribe)
      } yield Response.ok
      result.orElse(ZIO.succeed(Response.status(Status.BadRequest)))

    case Method.DELETE -> !! /   "unsubscribe" / id =>
      for{
        _ <- subscriptionService.unsubscribe(id).orElse(ZIO.succeed(Response.status(Status.BadRequest)))
      } yield Response.ok

    case Method.GET -> !! / "all"  =>
      (for{
        list <-subscriptionService.getAll
      } yield Response.json(list.toJson)).orElse(ZIO.succeed(Response.status(Status.BadRequest)))

  }

object SubscriptionRoutes:
  val layer: ZLayer[SubscriptionService, Nothing, SubscriptionRoutes] = ZLayer.fromFunction(SubscriptionRoutes.apply _)
