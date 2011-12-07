package lantoli.codejam

import scala.collection.mutable.Set
import scala.util.control.Breaks._

object PickingupChicks  {

  def main(args: Array[String]): Unit = {
		new PickingupChicks().doall();
  }

}

/**
 * Solved after looking at the contest analysis
 */
class PickingupChicks extends CodeJam {

    //def filename = "sample"
    //def filename = "B-small-practice"
    def filename = "B-large-practice"
  	  
    def doit : String = {
      var nChicks = readInt
      var kAtLeastChicks  = readInt
      var bBarn = readInt
      var tTime = readInt
    
      val x = new Array[Int](nChicks)
      val v = new Array[Int](nChicks)
      
      for (i <- 0 until nChicks) x(i) = readInt
      for (i <- 0 until nChicks) v(i) = readInt      
     
      var jumps = 0
      var fastChickens = 0
      var slowChickens = 0
      
      for (i <- nChicks-1 to 0 by -1) {
        if (x(i) + v(i) * tTime >= bBarn) {
          if (fastChickens < kAtLeastChicks) {
        	fastChickens += 1
            jumps += slowChickens
          }
        } else {
          slowChickens += 1
        }
      }
 
      if (fastChickens == kAtLeastChicks) 
        "%d".format(jumps) 
      else
        "IMPOSSIBLE"
    }
}

