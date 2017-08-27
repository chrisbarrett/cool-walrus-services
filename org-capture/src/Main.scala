package cool.walrus

import freestyle._, logging._, http.finch._
import io.finch._

object Main extends App {

  @SuppressWarnings(Array("org.wartremover.warts.Any"))
  def postItem[F[_]](item: Item)(implicit S: Service[F]) = {
    import S._
    for {
      _ <- log.info("Creating new item")
      id <- db.insert(item)
      _ <- log.info(s"Successfully inserted item with id=$id")
    } yield id
  }

  @SuppressWarnings(Array("org.wartremover.warts.Any"))
  def getSinceId[F[_]](id: Item.Id)(implicit S: Service[F]) = {
    import S._
    for {
      _ <- log.info(s"Getting items with ID >= $id")
      items <- db.selectAll
      results = items.filter(_.id.filter(_ >= id).nonEmpty)
      _ <- log.info(s"Returning ${results.size} result(s)")
    } yield results
  }
}
