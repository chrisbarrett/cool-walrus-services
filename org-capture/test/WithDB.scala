package cool.walrus

import cats.Monad
import doobie._, imports._, h2.imports._
import fs2.interop.cats._
import fs2.Task

// Helper for running tests with a simulated database.

object WithDB {

  // Configure an H2 in-memory database to simulate MySQL.

  val connstr = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;MODE=MySQL"
  val config = Config(connstr, "user", "pass")
  val connect = H2Transactor[Task](config.connectionString, config.username, config.password)

  def insertItem(x: Item) =
    sql""" INSERT INTO `Item` (id, todo, headline, body)
           VALUES (${x.id}, ${x.todo}, ${x.headline}, ${x.body}) """

  // Set up the database.

  val dropTable =
    sql""" DROP TABLE IF EXISTS `Item` """

  val createTable =
    sql"""
        CREATE TABLE `Item` (
          `id`       INTEGER PRIMARY KEY AUTO_INCREMENT,
          `todo`     TEXT,
          `headline` TEXT,
          `body`     TEXT
        ) """

  @SuppressWarnings(Array("org.wartremover.warts.EitherProjectionPartial"))
  def apply[A](action: Transactor[Task] => A): A = {
    val program = for {
      conn <- connect
      _ <- dropTable.update.run.transact(conn)
      _ <- createTable.update.run.transact(conn)
      r <- Monad[Task].pure(action(conn))
    } yield r

    program.unsafeRunSync.right.get
  }
}
