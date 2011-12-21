package lantoli.codejam

import scala.collection.mutable.Set
import scala.collection.mutable.ArrayBuffer
import java.io.PrintWriter
import java.util.Scanner
import java.io.File
import java.io.InputStream
import java.io.FileInputStream
import java.io.PrintStream
import java.io.FileOutputStream
import java.util.Scanner

object Main  {
 
  	def doit = {

  	  val split = in.nextLine().split(' ')
  	  val (src_number, src_table, tar_table) = (split(0), split(1), split(2))
  	 
  	  var dec_number = 0
  	  var base = 1
  	  for (i <- (src_number.length - 1) to 0 by -1) {
  	    dec_number += base * src_table.indexOf(src_number(i))
  	    base *= src_table.length
  	  }
  	  
  	  var tar_number = ""
  	  while (dec_number != 0) {
   		  tar_number = tar_table( dec_number % tar_table.length  ) + tar_number
 		  dec_number /= tar_table.length
  	  }
  	  
  	  tar_number
  	}   
  	
  	
  	System.setIn(new FileInputStream("A-large-practice.in"))
  	System.setOut(new PrintStream(new FileOutputStream("A-large-practice.out")))
  	  
  	
    val in = new Scanner(System.in)
  	  
    def main(args: Array[String]): Unit = {
  	  
		val testNum = in.nextInt()
		in.nextLine()
		System.err.println("tests: %d\n".format(testNum))
		for (test <- 1 to testNum) {
			val res = "Case #%d: %s".format(test, doit)
			println(res)
			System.out.flush();
		}	
    }
	
  
}
