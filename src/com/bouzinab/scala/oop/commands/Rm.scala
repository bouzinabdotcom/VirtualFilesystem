package com.bouzinab.scala.oop.commands
import com.bouzinab.scala.oop.files.Directory
import com.bouzinab.scala.oop.filesystem.State

class Rm(name: String) extends Command {

  override def apply(state: State): State = {
    //1- get working directory
    val wd = state.wd
    //2- get the absolute path
    val absolutePath =
      if (name.startsWith(Directory.SEPARATOR)) name
      else if(wd.isRoot) wd.path + name
      else wd.path + Directory.SEPARATOR + name
    //3- do some checks

    if(Directory.ROOT_PATH.equals(absolutePath))
      state.setMessage("Nuclear war not supported  yet.\n")
    else
      doRm(state, absolutePath)

  }

  def doRm(state: State, path: String): State = {

    def rmHelper(currentDirectory: Directory, path: List[String]): Directory = {
      if(path.isEmpty) currentDirectory
      else if (path.tail.isEmpty) currentDirectory.removeEntry(path.head)
      else {
        val nextDirectory = currentDirectory.findEntry(path.head)

        if(!nextDirectory.isDirectory) currentDirectory
        else {
          val newNextDirectory = rmHelper(nextDirectory.asDirectory, path.tail)
          if(newNextDirectory == nextDirectory) currentDirectory
          else currentDirectory.replaceEntry(path.head, newNextDirectory)
        }
      }
    }
    //4- find the entry to remove
    //5- update the structure

    val tokens = path.substring(1).split(Directory.SEPARATOR).toList.tail
    val newRoot : Directory = rmHelper(state.root, tokens)

    if(newRoot == state.root)
      state.setMessage(path + ": no such file or directory\n")
    else
      State(newRoot, newRoot.findDescendant(state.wd.path.substring(1)))
  }

}
