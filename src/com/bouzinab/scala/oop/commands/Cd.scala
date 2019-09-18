package com.bouzinab.scala.oop.commands
import com.bouzinab.scala.oop.files.{DirEntry, Directory}
import com.bouzinab.scala.oop.filesystem.State

import scala.annotation.tailrec

class Cd(dir: String) extends Command {

  override def apply(state: State): State = {
    //1- find the root

    val root = state.root
    val wd = state.wd
    //2- find the absolute  path  of the directory  I want  to cd to

    val separatorIfNecessary =
      if(Directory.ROOT_PATH.equals(wd.path)) ""
      else Directory.SEPARATOR

    val absolutePath =
      if(dir.startsWith(Directory.SEPARATOR)) dir
      else if(wd.isRoot) wd.path + dir
      else wd.path + separatorIfNecessary + dir
    //3- find the directory to cd to, given the path

    val destinationDirectory = doFindEntry(root, absolutePath)
    //4- change the state given the new directory

    if(destinationDirectory == null || !destinationDirectory.isDirectory)
      state.setMessage(dir + ": no such directory\n")

    else State(root, destinationDirectory.asDirectory)
  }


  def doFindEntry(root: Directory, path: String): DirEntry = {
    @tailrec
    def findEntryHelper(currentDirectory: Directory, path: List[String]): DirEntry = {


      if(path.isEmpty || path.head.isEmpty) currentDirectory
      else if(path.tail.isEmpty) currentDirectory.findEntry(path.head)
      else {
        val nextDir = currentDirectory.findEntry(path.head)
        if(nextDir == null || !nextDir.isDirectory) null
        else findEntryHelper(nextDir.asDirectory, path.tail)
      }
    }

    //1- tokens
    val tokens: List[String] = path.substring(1).split(Directory.SEPARATOR).toList

    //1.5- eliminate/collapse relative tokens

    @tailrec
    def collapseRelativeTokens(path: List[String],acc: List[String] = List()): List[String] = {
      if(path.isEmpty) acc
      else if(".".equals(path.head)) collapseRelativeTokens(path.tail, acc)
      else if("..".equals(path.head)) {
        if(acc.isEmpty) null
        else collapseRelativeTokens(path.tail, acc.init)
      } else collapseRelativeTokens(path.tail, acc :+ path.head)
    }
    //2- navigate to the correct entry

    val newTokens = collapseRelativeTokens(tokens)

    findEntryHelper(root, newTokens)

  }



}
