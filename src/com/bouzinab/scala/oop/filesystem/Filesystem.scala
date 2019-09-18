package com.bouzinab.scala.oop.filesystem

import java.util.Scanner

import com.bouzinab.scala.oop.files.Directory
import com.bouzinab.scala.oop.commands.Command

object Filesystem extends App{

  val root = Directory.ROOT
//  print("$")
//  io.Source.stdin.getLines().foldLeft(State(root, root))((currentState, newLine) => {
//    currentState.show
//    Command.from(newLine).apply(currentState)
//  })

  var state = State(root, root)
  val scanner = new Scanner(System.in)
  while(true) {
    state.show
    val input = scanner.nextLine()
    state = Command.from(input).apply(state)
  }


}
