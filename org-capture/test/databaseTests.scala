package cool.walrus

import cats.instances.vector._
import cats.syntax._, cartesian._, option._, traverse._
import com.softwaremill.quicklens._
import freestyle._
import fs2.Task
import fs2.interop.cats._
import org.scalatest.{Matchers, WordSpec}

@SuppressWarnings(Array("org.wartremover.warts.EitherProjectionPartial"))
class DatabaseTests extends WordSpec with Matchers {
  def program(items: Vector[Item])(implicit DB: Database[Database.Op]) =
    items.traverse(DB.insert) *> DB.selectAll

  "an inserted item can be retrieved" in WithDB { conn =>
    val input = Item.empty.modify(_.headline).setTo("ohai".some)

    implicit val interpreter = new DatabaseInterpreter(conn)
    val results = program(Vector(input)).interpret[Task].unsafeRunSync.right.get

    val idsErased = results.modify(_.each.id).setTo(None)

    idsErased should contain only input
  }

  "many inserted items can be retrieved" in WithDB { conn =>
    val input = Vector(
      Item.empty.modify(_.todo).setTo("TODO".some),
      Item.empty.modify(_.todo).setTo("DONE".some),
      Item.empty.modify(_.headline).setTo("foo".some),
      Item.empty.modify(_.headline).setTo("bar".some),
      Item.empty.modify(_.body).setTo("baz".some)
    )

    implicit val interpreter = new DatabaseInterpreter(conn)
    val results = program(input).interpret[Task].unsafeRunSync.right.get

    val idsErased = results.modify(_.each.id).setTo(None)

    idsErased should contain theSameElementsInOrderAs input
  }
}
