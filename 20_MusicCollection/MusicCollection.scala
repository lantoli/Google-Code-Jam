package lantoli.codejam

import java.io.FileInputStream
import java.util.Scanner
import scala.collection.immutable.TreeMap
import java.io.PrintStream
import java.io.FileOutputStream

object Main  {
  
  	def doit = {
  	  val songCount = in.nextInt
  	  in.nextLine
  	  var maxLen = 0
  	  val songs = new Array[String](songCount)
  	  for (i <- 0 until songCount) {
  	    songs(i) = in.nextLine.toUpperCase
  	    maxLen = maxLen max songs(i).length
  	  }
 
  	  val res = new Array[String](songCount)
  	  for (len <- 0 to maxLen) {
  	    var map = TreeMap[String,Int]()
  	    for (i <- 0 until songCount) {
  	      for (pos <- 0 to songs(i).size - len ) {
  	        val key = songs(i).substring(pos,pos+len)
  	        if (map.contains(key)) {
  	          if (map(key) != i) map += key -> -1  
  	        } else{
  	          map += key -> i
  	        }
  	      }
  	    }
  	
  	    for ((key,value) <- map) {
  	      if (value != -1 && res(value) == null) {
  	         res(value) = key
  	      }
  	    }
  	  }
  	  
  	  for (i <- 0 until songCount) { 
  	    if (res(i) == null) {
  	      println(":(")
  	    } else {
  	      println("\"%s\"".format(res(i)))
  	    }
  	  }
  	}   
  	
  	
  	System.setIn(new FileInputStream("B-small-practice-2.in"))
  	System.setOut(new PrintStream(new FileOutputStream("B-small-practice-2.out")))
  	  
  	
    val in = new Scanner(System.in)
  	  
    def main(args: Array[String]): Unit = {
  	  
		val testNum = in.nextInt()
		in.nextLine()
		System.err.println("tests: %d\n".format(testNum))
		for (test <- 1 to testNum) {
			val res = "Case #%d:".format(test)
			println(res)
			doit
			System.out.flush();
		}	
    }
	
  
}
