package lantoli.codejam

import scala.collection.mutable.Set

object BribeThePrisionersFirstVersionNotWorking  {
  def main(args: Array[String]): Unit = {
		new BribeThePrisionersFirstVersionNotWorking().doall();
  }

}

/**
 * bad assumption. Tried to select recursively the last prisioner which needed less money. 
 */
class BribeThePrisionersFirstVersionNotWorking extends CodeJam {

    //def filename = "sample"
    //def filename = "C-small-practice"
    def filename = "C-large-practice"
  	 
    def doit = {
      val cellNum = readInt 
      val prisionerNum = readInt
      
      val occupied = new Array[Boolean](cellNum);
      var prisioners = Set[Int] ()
      
      prisionerNum times {
        val p = readInt - 1
        occupied(p) = true
        prisioners += p
      }
      
      var total = 0
      while (!prisioners.isEmpty) {
        var pCandidate = -1
        var coinsCandidate = cellNum
        for (p <- prisioners) {
          var coins = 0
          var pos = p + 1
          while (pos < cellNum && !occupied(pos)) {
            coins += 1;
            pos += 1;
          }
          pos = p - 1;
          while (pos >= 0 && !occupied(pos) ) {
            coins += 1;
            pos -= 1;
          }
          if (coins < coinsCandidate) {
            coinsCandidate = coins
            pCandidate = p
          }     
        }
        prisioners.remove(pCandidate)
        occupied(pCandidate) = false
        total += coinsCandidate
      }

      "%d".format(total) 
    }
 
   
      
    
}

