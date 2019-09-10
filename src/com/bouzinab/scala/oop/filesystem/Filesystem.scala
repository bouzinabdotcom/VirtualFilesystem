package com.bouzinab.scala.oop.filesystem

import java.util.Scanner

import com.bouzinab.scala.oop.files.Directory
import com.bouzinab.scala.oop.commands.Command

object Filesystem extends App{

  val root = Directory.ROOT
  var state = State(root, root)
  val scanner = new Scanner(System.in)
  while(true) {
    state.show
    val input = scanner.nextLine()
    state = Command.from(input).apply(state)
  }
}
