package cool

import sbt.{Project,file}

package object walrus {
  implicit class RichProject(inner: Project) {
    def walrus(path: String) =
      inner.in(file(path)).settings(CommonSettings.defaults)
  }
}
