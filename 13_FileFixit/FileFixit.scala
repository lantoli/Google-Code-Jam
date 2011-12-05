package lantoli.codejam

import scala.collection.mutable.Set

object FileFixit  {

  def main(args: Array[String]): Unit = {
		new FileFixit().doall();
  }

}

class FileFixit extends CodeJam {

    //def filename = "sample"
    //def filename = "A-small-practice"
    def filename = "A-large-practice"
  	  
    def doit = {
      val nExist = readInt 
      val mCreate = readIntAndNewline
    
      var existingDirs = Set[String] ()
      for (i <- 1 to nExist) {
        existingDirs += readLine
      }
      
      var count = 0;
      for (i <- 1 to mCreate) {
        var newDir = readLine
        while (!existingDirs(newDir) && !newDir.isEmpty()) {
          count += 1
          existingDirs += newDir
          newDir = newDir.substring(0,newDir.lastIndexOf('/'))
        }
      }
      
      "%d".format(count)
    }
}

