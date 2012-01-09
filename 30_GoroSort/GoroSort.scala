package lantoli.codejam

import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.PrintStream
import java.util.Scanner

/**
 * Done after looking at contest analysis
 */
object Main  {
 
  	def doit = {

  	  val N = in.nextInt 
  	  var total = 0
  	  
  	  for (i <- 1 to N if in.nextInt != i) {
  	    total += 1
  	  }
  	  
  	  total
  	}   
  	
  	System.setIn(new FileInputStream("D-large-practice.in"))
 	System.setOut(new PrintStream(new FileOutputStream("D-large-practice.out")))
  	  
  	
    val in = new Scanner(System.in)
  	  
    def main(args: Array[String]): Unit = {
  	  
		val testNum = in.nextInt
		in.nextLine()
		System.err.println("tests: %d\n".format(testNum))
		for (test <- 1 to testNum) {
			println("Case #%d: %d".format(test, doit))
			System.out.flush();
		}	
    }
	

}
