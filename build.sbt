import cool.walrus._

lazy val root = project
  .walrus(".")
  .aggregate(`org-capture`)

lazy val `org-capture` = project.walrus("org-capture")
