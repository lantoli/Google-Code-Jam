package lantoli.codejam
import java.util.Scanner
import java.io.File
import java.io.PrintWriter

abstract class CodeJam() {

  	def filename:String
  	private def inFilename = filename + ".in"
  	private def outFilename = filename + ".out"
  	
  	private val sc = new Scanner(new File(inFilename))
  	private val writer = new PrintWriter(outFilename)		

  	def readInt = sc.nextInt
  
  	def readIntAndNewline = {
  	  val ret = readInt
  	  sc.nextLine();
  	  ret
  	}
  	
  	def readLine = sc.nextLine()
  	
	def doall() {
		val testNum = readIntAndNewline
		println("tests: %d\n".format(testNum))
		init
		for (test <- 1 to testNum) {
			val res = "Case #%d: %s".format(test, doit)
			println(res)
			writer.println(res)
			writer.flush()
		}		
		writer.close()
  	}
	
  	def doit : String

  	def init = {}
}