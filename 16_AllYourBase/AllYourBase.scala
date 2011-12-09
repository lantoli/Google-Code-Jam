package lantoli.codejam

import scala.collection.mutable.Set
import scala.util.control.Breaks._
import scala.collection.mutable.ArrayBuffer

import scala.collection.mutable.Map

object AllYourBase  {

  def main(args: Array[String]): Unit = {
		new AllYourBase().doall();
  }

}

class AllYourBase extends CodeJam {

    //def filename = "sample"
    //def filename = "A-small-practice"
    def filename = "A-large-practice"
  	  
    def doit = {
      var msg = readLine

      var symbols = Map(msg(0) -> 1)
      msg.foreach { letter => 
      	if (!symbols.contains(letter)) {
      	  val digit = if (symbols.size == 1) 0 else symbols.size
      	  symbols += letter -> digit
        }
      }
      
      val base = 2 max symbols.size
      var factor = 1L
      var count = 0L
      for (i <- msg.length - 1 to 0 by -1) {
        count += symbols(msg(i)) * factor
        factor *= base
      }
      
      "%d".format(count) 
    }
 
   
      
    
}

