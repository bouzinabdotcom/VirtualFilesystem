package com.bouzinab.scala.oop.commands
import com.bouzinab.scala.oop.files.{Directory, File}
import com.bouzinab.scala.oop.filesystem.State

import scala.annotation.tailrec

class Echo(args: Array[String]) extends Command{
  override def apply(state: State): State = {
    /*
      if no args, state
      else if 1 arg, print to console
      else if multiple args
      {
        operator = next to last arg
        if > echo to file and may create file if not there
        else if >> append to a file
        else
          just echo everything to console
      }


     */



    if(args.isEmpty) state
    else if (args.length == 1) state.setMessage(args(0) + "\n")
    else {
      val operator = args(args.length - 2)
      val filename = args(args.length - 1)
      val contents = createContents(args, args.length-2)

      if(">>".equals(operator))
        doEcho(state, contents, filename, append = true)
      else if (">".equals(operator))
        doEcho(state, contents, filename, append = false)

      else
        state.setMessage(createContents(args,  args.length) + "\n")

    }



  }


  def getRootAfterEcho(currentDirectory: Directory, path: List[String], contents: String, append: Boolean) : Directory = {
    /*
      if path  is empty
        fail
      else if path tail is empty
        find file to create or add content to
        else if file not found
          create file
        else if entry if directory
          fail
        else
          replace or append content to the file
          replace the entry with the filename with  the NEW file
      else
        find the next directory to navigate
        call getRootAfterEcho recursively on that
        if recursive call failed
          fail
        else
          replace dir with the new dir
     */

    if(path.isEmpty) currentDirectory
    else if (path.tail.isEmpty) {
      val dirEntry = currentDirectory.findEntry(path.head)

      if(dirEntry == null)
        currentDirectory.addEntry(new File(currentDirectory.path, path.head, contents))
      else if (dirEntry.isDirectory) currentDirectory
      else
        if(append)
          currentDirectory.replaceEntry(path.head, dirEntry.asFile.appendContents(contents))
        else
          currentDirectory.replaceEntry(path.head, dirEntry.asFile.setContents(contents))
    } else {
      val nextDirectory = currentDirectory.findEntry(path.head).asDirectory
      val newNextDirectory = getRootAfterEcho(nextDirectory, path.tail, contents, append)

      if(newNextDirectory == currentDirectory) currentDirectory
      else currentDirectory.replaceEntry(path.head, newNextDirectory)
    }
  }
  def doEcho(state: State, contents: String, filename: String, append: Boolean) = {
     if(filename.contains(Directory.SEPARATOR))
       state.setMessage("Echo: filename must not contain separators\n")
      else {
       val newRoot : Directory = getRootAfterEcho(state.root, state.wd.getAllFoldersInPath :+ filename, contents, append)
       if(newRoot == state.root)
         state.setMessage(filename + ": no such file\n")
       else
         State(newRoot, newRoot.findDescendant(state.wd.getAllFoldersInPath))
     }
  }


  //topIndex non-inclusive
  def createContents(args: Array[String], topIndex: Int): String = {

    @tailrec
    def createContentHelper(currentIndex: Int = 0, accumulator: String = ""): String = {
      if(currentIndex >= topIndex) accumulator
      else createContentHelper(currentIndex + 1, accumulator + " " + args(currentIndex))
    }

    createContentHelper()
  }
}
