package Alkimi
package services
import models.domain.Subscription

import zio._

/** SubscriptionDataSourceLive is an implementation of InMemoryDataSource to
  * store Map[String, Subscription]
  */

final case class SubscriptionDataSourceLive(map: Ref[Map[String, Subscription]])
    extends InMemoryDataSource[String, Subscription]:

  /** Update current map in an atomic way and add a key,value: If the key
    * exists, will update the value
    */
  override def upsert(key: String, value: Subscription): Task[Unit] =
    map.update(_.updated(key, value))

  /** Remove key from the map based on userId as a key
    */
  override def remove(key: String): Task[Unit] =
    map.update(_.removed(key))

  /** return user's subscription
    */
  override def get(
      key: String
  ): Task[Subscription] =
    map.get
      .map(_.get(key))
      .someOrFail(throw new RuntimeException(s"$key not found"))

  /** Return true if user has subscribed any else false
    */
  override def exist(key: String): Task[Boolean] =
    map.get.map(_.keys.exists(k => k == key))

  /** Return all tuples aas UserId,Subscription
    */
  override def getAll: Task[List[
    (String, Subscription)
  ]] = map.get.map(_.toList)

  /** Define layer for this service and create atomic reference on Map
    */

object SubscriptionDataSourceLive:
  val layer: ZLayer[Any, Throwable, InMemoryDataSource[String, Subscription]] =
    ZLayer {
      Ref
        .make(Map[String, Subscription]())
        .map(SubscriptionDataSourceLive.apply)
    }
