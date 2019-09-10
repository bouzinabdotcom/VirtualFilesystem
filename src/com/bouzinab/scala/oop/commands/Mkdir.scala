package com.bouzinab.scala.oop.commands
import com.bouzinab.scala.oop.files.{DirEntry, Directory}
import com.bouzinab.scala.oop.filesystem.State

class Mkdir(name: String) extends CreateEntry(name) {
  override def createSpecificEntry(state: State): DirEntry =
    Directory.empty(state.wd.path, name)
}
