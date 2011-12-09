package lantoli.codejam

import scala.collection.mutable.Set
import scala.util.control.Breaks._
import scala.collection.mutable.ArrayBuffer

object YourRankIsPure  {

  def main(args: Array[String]): Unit = {
		new YourRankIsPure().doall();
  }

}

/**
 * Solved after looking at some contestant's code
 */
class YourRankIsPure extends CodeJam {

    //def filename = "sample"
    //def filename = "C-small-practice"
    def filename = "C-large-practice"
  	  
    def doit = {
      var n = readInt
      
      var ret = 0L
      for (k <- 1 to n-1) {
        ret += ans(n)(k)
        ret %= mod
      }
      
      "%d".format(ret) 
    }

    
    val mod = 100003;
    val choose = ArrayBuffer.fill(500,500)(0L)

    val ans = ArrayBuffer.fill(501,500)(0L)

    override def init = {   
      choose(0)(0) = 1;
      for (n <- 1 until 500) {
        choose(n)(0) = 1
        for (k <- 1 to n) {
          choose(n)(k) = (choose(n-1)(k-1) + choose(n-1)(k)) % mod
        }
      }
      for (n <- 2 to 500) {
        ans(n)(1) = 1
        for (k <- 2 to n-1) {
          for (k2 <- 1 until k) { 
	         ans(n)(k) += ans(k)(k2) * choose(n-k-1)(k-k2-1)
	         ans(n)(k) %= mod
          }
        }
      }
    }
    
    
}

