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
  
  
	val vowels = Set('a','e','i','o','u','A','E','I','O','U')
  
  	def doit = {
  	  
  	  val country = in.nextLine
 	  val letter = country(country.size-1)  
 
  	  var who = "a king"
  	  if (letter == 'y' || letter == 'Y') who = "nobody"
  	  if (vowels.contains(letter)) who = "a queen"
  	    
      "%s is ruled by %s.".format(country, who) 
    }   
  	
  	
  	System.setIn(new FileInputStream("A-small-practice-2.in"))
  	System.setOut(new PrintStream(new FileOutputStream("A-small-practice-2.out")))
  	  
  	
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
