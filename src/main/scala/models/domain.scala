package Alkimi
package models
import zio.json._

object domain:

  /** Recommended to use Strong type as possible so I defined Language Enum and
    * these three field are example
    */

  enum Language(fullName: String):
    case EN extends Language("English")
    case FR extends Language("French")
    case Fa extends Language("Farsi")
  object Language:
    def fromString(string: String): Language =
      string match
        case "EN"  => EN
        case "FR"  => FR
        case "Fa"  => Fa
        case other => throw new RuntimeException(s"$other is not defined as Language")

    implicit val encoder: JsonEncoder[Language] = JsonEncoder[String].contramap(_.toString)
    implicit val decoder: JsonDecoder[Language] = JsonDecoder[String].map(fromString)

  /** Three possible plan
    */

  enum Plan:
    case Monthly
    case BiAnnual
    case Annual
  object Plan:
    def fromString(string: String): Plan =
      string match
        case "Monthly"  => Monthly
        case "BiAnnual" => BiAnnual
        case "Annual"   => Annual
        case other      => throw new RuntimeException(s"$other is not defined as Plan")

    implicit val encoder: JsonEncoder[Plan] = JsonEncoder[String].contramap(_.toString)
    implicit val decoder: JsonDecoder[Plan] = JsonDecoder[String].map(fromString)

  /** It is good to define cost with the currency and these are example of
    * possible Currency in the system
    */

  enum Currency:
    case EUR
    case DOLLAR
    case POUND
  object Currency:

    def fromString(string: String): Currency =
      string match
        case "EUR"    => EUR
        case "DOLLAR" => DOLLAR
        case "POUND"  => POUND
        case other    => throw new RuntimeException(s"$other is not defined as Currency")

    implicit val encoder: JsonEncoder[Currency] = JsonEncoder[String].contramap(_.toString)
    implicit val decoder: JsonDecoder[Currency] = JsonDecoder[String].map(fromString)

  /** Money holds amount of a currency like 2 EUR or 3 Dollar
    */

  case class Money(amount: BigDecimal, currency: Currency)
  object Money:
    implicit val codec: JsonCodec[Money] =
      DeriveJsonCodec.gen[Money]

  /** Based on Email: Channel has name, id, cost, language
    */

  case class Channel(
      name: String,
      id: String,
      cost: Money,
      language: Language
  )
  object Channel:
    implicit val codec: JsonCodec[Channel] =
      DeriveJsonCodec.gen[Channel]

  case class Package(channels: Set[Channel]):
    def updatePrice(f: Money => Money): Set[Channel] =
      channels.map(c => c.copy(cost = f(c.cost)))

  object Package:
    implicit val codec: JsonCodec[Package] =
      DeriveJsonCodec.gen[Package]

  case class Subscription(
      name: String,
      default: Package,
      duration: Plan,
      onDemandPackage: Option[Package],
      onDemandChannel: Option[Channel]
  )
  object Subscription:
    implicit val codec: JsonCodec[Subscription] =
      DeriveJsonCodec.gen[Subscription]

  case class User(id: String)
