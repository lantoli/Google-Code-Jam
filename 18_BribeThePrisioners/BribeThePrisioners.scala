package lantoli.codejam

import scala.collection.mutable.Set
import scala.collection.mutable.ArrayBuffer

object BribeThePrisioners  {
  def main(args: Array[String]): Unit = {
		new BribeThePrisioners().doall();
  }

}

/*
 * Solved after looking at contest results
 */
class BribeThePrisioners extends CodeJam {

    //def filename = "sample"
    //def filename = "C-small-practice"
    def filename = "C-large-practice"
  	 
    def doit = {
      val cellNum = readInt 
      var prisonerNum = readInt + 2
      val locs = new Array[Int] (prisonerNum)
      locs(0) = 0
      locs(prisonerNum-1) = cellNum + 1
      for (i <- 1 to prisonerNum-2) {
        locs(i) = readInt
      }
      
      val dp = ArrayBuffer.fill(prisonerNum,prisonerNum) (0)
      for (gap <- 2 until prisonerNum) {
        for (a <- 0 until prisonerNum - gap) {
          val b = a + gap
          var value = Int.MaxValue
          for (d <- a+1 until b) {
            value = value min dp(a)(d)+dp(d)(b) + locs(b)-locs(a)-2
          }
          dp(a)(b) = value
        }
      } 
      "%d".format(dp(0)(prisonerNum-1)) 
    }   
}

