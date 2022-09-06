package Alkimi
package services
import zio._
import models.domain._

/**
 * SubscriptionServiceLive is an implementation of SubscriptionService with dependency to InMemoryDataSource
 * */

final case class SubscriptionServiceLive(
    repoSub: InMemoryDataSource[String, Subscription]
) extends SubscriptionService:

  /**
   * Subscribes users
   * */
  override def subscribe(
      userId: String,
      subscription: Subscription
  ): Task[Unit] =
    for _ <- repoSub.upsert(userId, subscription)
    yield ()


  /**
   * Unsubscribe users
   * */
  override def unsubscribe(
      userId: String
  ): Task[Unit] =
    for _ <- repoSub.remove(userId)
    yield ()

  /**
   * List all subscriptions
   * */
  override def getAll: Task[List[(String, Subscription)]] =
    repoSub.getAll

object SubscriptionServiceLive:
  val layer: ZLayer[
    InMemoryDataSource[String, Subscription],
    Throwable,
    SubscriptionService
  ] =
    ZLayer.fromFunction(SubscriptionServiceLive.apply(_))
