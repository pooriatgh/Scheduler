package Alkimi
package services

import Alkimi.models.domain.Subscription
import zio.*

/**
 * Subscription service which is responsible for subscribe/unsubscribe/list users subscriptions
 * */

trait SubscriptionService:
  def subscribe(userId: String, subscription: Subscription): Task[Unit]
  def unsubscribe(userId: String): Task[Unit]
  def getAll: Task[List[(String, Subscription)]]


/**
 * Companion object which holds accessors
 * */
object SubscriptionService:
  def subscribe(
      userId: String,
      subscription: Subscription
  ): ZIO[SubscriptionService, Throwable, Unit] =
    ZIO.serviceWithZIO[SubscriptionService](_.subscribe(userId, subscription))

  def unsubscribe(userId: String): ZIO[SubscriptionService, Throwable, Unit] =
    ZIO.serviceWithZIO[SubscriptionService](_.unsubscribe(userId))

  def getAll: ZIO[SubscriptionService, Throwable, List[(String, Subscription)]] =
    ZIO.serviceWithZIO[SubscriptionService](_.getAll)
