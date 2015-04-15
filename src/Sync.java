
public class Sync {

	  boolean flag = true;
	 public synchronized void go() {
	        
		 if (!flag) {
	            try {
	                wait();
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }

	     
	        flag = false;
	        notify();
	    }
	    

	    public synchronized void hold() {
	     
	    	if (flag) {
	            try {
	                wait();
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
	        flag = true;
	        notify();
	    }
}
