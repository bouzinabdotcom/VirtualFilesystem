package com.bouzinab.scala.oop.commands
import com.bouzinab.scala.oop.files.{DirEntry, File}
import com.bouzinab.scala.oop.filesystem.State

class Touch(name: String) extends CreateEntry(name) {
  override def createSpecificEntry(state: State): DirEntry =
    File.empty(state.wd.path, name)
}
