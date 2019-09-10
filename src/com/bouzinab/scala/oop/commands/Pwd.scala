package com.bouzinab.scala.oop.commands
import com.bouzinab.scala.oop.filesystem.State

class Pwd extends Command{

  override def apply(state: State): State =
    state.setMessage(state.wd.path + "\n")

}
