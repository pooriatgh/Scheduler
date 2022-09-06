package Alkimi
package server
import zio._

trait SubscriptionServer {
  def start(port:Int, host:String):Task[Unit]
}
