package cool.walrus

import doobie.imports._
import fs2.Task
import fs2.interop.cats._

class DatabaseInterpreter(tx: Transactor[Task]) extends Database.Handler[Task] {
  def insert(x: Item): Task[Int] =
    sql"""
       INSERT INTO `Item` (todo, headline, body)
       VALUES (${x.todo}, ${x.headline}, ${x.body})
       """.update.run.transact(tx)

  def selectAll: Task[Vector[Item]] =
    sql"""
       SELECT * FROM `Item`
       """.query[Item].vector.transact(tx)
}
