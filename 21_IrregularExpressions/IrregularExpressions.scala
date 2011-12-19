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
  
	val vowels = Set('a','e','i','o','u')
 
	var spell = ""
	def isVowel(pos : Int) = vowels.contains(spell(pos))
	def isNotVowel(pos : Int) = !vowels.contains(spell(pos))
	
  	def doit : String = {
  	  
  	  spell = in.nextLine
  	 
  	  for (pos <- 0 until spell.size) if (isVowel(pos)) {
  	    var pos2 = pos + 1
  	    while (pos2 < spell.size && isNotVowel(pos2)) {
  	      pos2 += 1;
  	    }
  	    if (pos2 < spell.size) {
  	      val startWord = spell.substring(pos, pos2+1)
  	      val endPos = spell.lastIndexOf(startWord)
  	      if (endPos > pos2+1) {
  	        for (i <- pos2 until endPos) if (isVowel(i)) return "Spell!"
  	      }
  	    }
  	  }  	  
  	  "Nothing."
    }   
  	
  	
  	System.setIn(new FileInputStream("C-large-practice.in"))
  	System.setOut(new PrintStream(new FileOutputStream("C-large-practice.out")))
  	  
  	
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
