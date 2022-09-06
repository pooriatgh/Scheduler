package Alkimi
import Alkimi.models.domain._
import Alkimi.services._
import zio.*
import zio.test.*

class SubscriptionServiceSpec extends zio.test.junit.JUnitRunnableSpec:

  override def spec: Spec[TestEnvironment, Throwable] = {

    val channelA =
      Channel("A", "id_A", Money(12.0, Currency.POUND), Language.EN)
    val channelB = Channel("B", "id_B", Money(12.0, Currency.EUR), Language.EN)
    val channelC =
      Channel("C", "id_C", Money(12.0, Currency.DOLLAR), Language.FR)

    val simplePackage = Package(Set(channelA, channelB))

    val subscription =
      Subscription(
        "First",
        simplePackage,
        Plan.Annual,
        None,
        Some(channelC)
      )
    val userA = User("userA")
    val userB = User("userB")
    val userC = User("userC")

    suite("Mentioned unit tests on assessment")(
      test("User should be able to subscribe") {
        for {
          _   <- SubscriptionService.subscribe(userA.id, subscription)
          all <- SubscriptionService.getAll
        } yield assertTrue(all == List((userA.id, subscription)))

      },
      test("User should be able to update subscription") {
        val channelF =
          Channel("F", "C_F", Money(2, Currency.DOLLAR), Language.EN)
        val updatedSubscription =
          subscription.copy(onDemandChannel = Some(channelF))
        for {
          _   <- SubscriptionService.subscribe(userA.id, subscription)
          _   <- SubscriptionService.subscribe(userA.id, updatedSubscription)
          all <- SubscriptionService.getAll
        } yield assertTrue(all == List((userA.id, updatedSubscription)))

      },
      test("User should be able to unsubscribe") {
        for {
          _   <- SubscriptionService.subscribe(userB.id, subscription)
          _   <- SubscriptionService.getAll
          _   <- SubscriptionService.unsubscribe(userB.id)
          all <- SubscriptionService.getAll
        } yield assertTrue(all.isEmpty)

      },
      test("List current subscription details") {

        for {
          _   <- SubscriptionService.subscribe(userA.id, subscription)
          _   <- SubscriptionService.subscribe(userB.id, subscription)
          _   <- SubscriptionService.subscribe(userC.id, subscription)
          all <- SubscriptionService.getAll
        } yield assertTrue(
          all == List(
            (userA.id, subscription),
            (userB.id, subscription),
            (userC.id, subscription)
          )
        )

      },
      test("user should be able to add a add-on package") {
        val channelG =
          Channel("G", "id_G", Money(3.0, Currency.POUND), Language.EN)
        val subscriptionWithAddOnePackage =
          subscription.copy(onDemandPackage = Some(Package(Set(channelG))))
        for {
          _ <- SubscriptionService.subscribe(
                 userA.id,
                 subscriptionWithAddOnePackage
               )
          all <- SubscriptionService.getAll
        } yield assertTrue(all == List((userA.id, subscriptionWithAddOnePackage)))

      },
      test("user should be able to remove a add-on package") {
        val channelG =
          Channel("G", "id_G", Money(2.0, Currency.POUND), Language.EN)
        val subscriptionWithoutAnyAddOn =
          subscription.copy(onDemandPackage = None)
        for {
          _   <- SubscriptionService.subscribe(userA.id, subscriptionWithoutAnyAddOn)
          all <- SubscriptionService.getAll
        } yield assertTrue(all == List((userA.id, subscriptionWithoutAnyAddOn)))

      }
    ).provide(
      SubscriptionDataSourceLive.layer,
      SubscriptionServiceLive.layer
    )
  }
