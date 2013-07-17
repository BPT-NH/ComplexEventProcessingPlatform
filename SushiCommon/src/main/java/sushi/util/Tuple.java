package sushi.util;

/**
 * generic tuple class 
 */
public class Tuple<X, Y> { 
	  public final X x; 
	  public final Y y; 
	  
	   public Tuple(X x, Y y) {
		   this.x = x;
		   this.y = y; 
	  }
	   
	   public Tuple() {
		this.x = null;
		this.y = null;
	}

	@Override
	   public String toString(){
		return "Tuple:" + x.toString() + "," + y.toString();
	   }
} 