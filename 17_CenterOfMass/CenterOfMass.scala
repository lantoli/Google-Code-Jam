package lantoli.codejam

import scala.collection.mutable.Set
import scala.util.control.Breaks._
import scala.collection.mutable.ArrayBuffer

import scala.collection.mutable.Map

object CenterOfMass  {
  def main(args: Array[String]): Unit = {
		new CenterOfMass().doall();
  }

}

class CenterOfMass extends CodeJam {

    //def filename = "sample"
    //def filename = "B-small-practice"
    def filename = "B-large-practice"
  	  
    def doit = {
      val N = readInt

      var x,y,z,vx,vy,vz = 0L
      N times {
    	  x += readInt
    	  y += readInt
    	  z += readInt
    	  vx += readInt
    	  vy += readInt
    	  vz += readInt
      }
     
      val posVel = x*vx + y*vy + z*vz
      val velVel = vx*vx + vy*vy + vz*vz
      val posPos = x*x + y*y + z*z
      
      var t = -1.0 * posVel / velVel max 0 
      if (t.isNaN()) t=0
      val d = Math.sqrt(posPos + t*t*velVel + 2*t*posVel) / N
      
      "%.8f %.8f".format(d,t) 
    }
 
   
      
    
}

