package cool.walrus

import cats._, implicits._
import cats.derived._, monoid._, legacy._

case class Item(
    id: Option[Item.Id],
    todo: Option[String],
    headline: Option[String],
    body: Option[String]
)

object Item {
  type Id = Long
  def empty = Monoid[Item].empty

  implicit lazy val monoidInstance = Monoid[Item]
}
