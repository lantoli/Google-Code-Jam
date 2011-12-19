package lantoli.codejam

import java.io.FileInputStream
import java.util.Scanner
import scala.collection.mutable.Set
import java.io.PrintStream
import java.io.FileOutputStream

object Main  {
  
  	def doit = {
  	  val monksCount = in.nextInt
  	  
  	  val followers = new Array[List[Int]](monksCount)
  	  for (i <- 0 until monksCount) followers(i) = Nil
  	  
  	  for (i <- 0 until monksCount) followers(in.nextInt - 1) ::= i
  	  
  	  for (i <- 0 until monksCount) {
  	    var visited = Set[Int]()
  	    var list = List(i)
  	    while (!list.isEmpty) {
  	      val monk = list.head
  	      list = list.tail
  	      if (!visited.contains(monk)) {
  	        visited += monk
  	        list :::= followers(monk) 
  	      }
  	    } 
  	    println("%d".format(visited.size))
  	  }
  	}   
  	
  	
  	System.setIn(new FileInputStream("D-large-practice.in"))
  	System.setOut(new PrintStream(new FileOutputStream("D-large-practice.out")))
  	  
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
