package com.bouzinab.scala.oop.commands
import com.bouzinab.scala.oop.filesystem.State

class UnknownCommand extends Command{
  override def apply(state: State): State =
    state.setMessage("Command not found!\n")

}
