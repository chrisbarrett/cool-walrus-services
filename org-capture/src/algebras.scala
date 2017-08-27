package cool.walrus

import freestyle._
import freestyle.logging.LoggingM

@free trait Database {
  def insert(x: Item): FS[Int]
  def selectAll: FS[Vector[Item]]
}

@module trait Service {
  val db: Database
  val log: LoggingM
}
