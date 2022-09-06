package Alkimi
package services
import zio._
import models.domain._

/** InMemoryDataSource manages in memory data stores operations for the Generic
  * K,V type.
  */

trait InMemoryDataSource[K, V]:
  def upsert(key: K, value: V): Task[Unit]
  def remove(key: K): Task[Unit]
  def get(key: K): Task[V]
  def getAll: Task[List[(K, V)]]
  def exist(key: K): Task[Boolean]

/** InMemoryDataSource companion objects hosts accessors functions.
  */

object InMemoryDataSource:

  /** Insert a key,value with type K,V and if key exists will update the value
    */

  def upsert[K: Tag, V: Tag](
      key: K,
      value: V
  ): ZIO[InMemoryDataSource[K, V], Throwable, Unit] =
    ZIO.serviceWithZIO[InMemoryDataSource[K, V]](_.upsert(key, value))

  /** remove the record with the corresponding Key from the map
    */
  def remove[K: Tag, V: Tag](
      key: K
  ): ZIO[InMemoryDataSource[K, V], Throwable, Unit] =
    ZIO.serviceWithZIO[InMemoryDataSource[K, V]](_.remove(key))

  /** get the record with the given key or will throw exception if it could not
    * find it
    */
  def get[K: Tag, V: Tag](key: K): ZIO[InMemoryDataSource[K, V], Throwable, V] =
    ZIO.serviceWithZIO[InMemoryDataSource[K, V]](_.get(key))

  /** Return true if the key exist else will return false
    */
  def exist[K: Tag, V: Tag](
      key: K
  ): ZIO[InMemoryDataSource[K, V], Throwable, Boolean] =
    ZIO.serviceWithZIO[InMemoryDataSource[K, V]](_.exist(key))

  /** Return all records in the data store
    */
  def getAll[K: Tag, V: Tag]: ZIO[InMemoryDataSource[K, V], Throwable, List[(K, V)]] =
    ZIO.serviceWithZIO[InMemoryDataSource[K, V]](_.getAll)
