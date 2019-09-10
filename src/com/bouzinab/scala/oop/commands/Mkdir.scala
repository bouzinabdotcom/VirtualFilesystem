package com.bouzinab.scala.oop.commands
import com.bouzinab.scala.oop.files.{DirEntry, Directory}
import com.bouzinab.scala.oop.filesystem.State

class Mkdir(name: String) extends Command {
  override def apply(state: State): State = {
    val wd = state.wd
    if(wd.hasEntry(name)) {
      state.setMessage(s"Entry $name already exists!\n")
    }
    else if (name.contains(Directory.SEPARATOR)){
      state.setMessage(s"$name must not contain separators!\n")
    }
    else if (checkIllegal(name)) {
      state.setMessage((s"$name: illegal entry name!\n"))
    }
    else {
      doMkdir(state, name)
    }
  }

  def checkIllegal(name: String): Boolean = {
    name.contains(".")
  }


  def doMkdir(state: State, name: String): State = {

    def updateStructure(currentDirectory: Directory, path: List[String], newEntry: DirEntry): Directory = {
      if(path.isEmpty) currentDirectory.addEntry(newEntry)
      else {
        val oldEntry = currentDirectory.findEntry(path.head).asDirectory
        currentDirectory.replaceEntry(oldEntry.name, updateStructure(oldEntry, path.tail, newEntry))
      }
    }

    val wd = state.wd
    // all the directories in the full path

    val allDirsInPath = wd.getAllFoldersInPath
    // create new directory entry in wd
    val newDir = Directory.empty(wd.path, name)
    // update the whole directory structure from the root

    val newRoot = updateStructure(state.root, allDirsInPath, newDir)
    //find new working directory instance diven wd full path in the new directory structure
    val newWd = newRoot.findDescendant(allDirsInPath )

    State(newRoot, newWd)
  }
}
