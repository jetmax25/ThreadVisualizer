import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class testDriver {


	static Lock lock = new ReentrantLock();
	static VisualThread x;
	static VisualThread y;
	static VisualThread z;
	
	public static void main(String[] args) throws InterruptedException {
				
		// TODO Auto-generated method stub
		x = new VisualThread(){
			@Override
			public void run(){
				int i=0;
				while(i<5){
					try{Thread.sleep(VisualGUI.returnRandom()*10);}
					catch(InterruptedException e){}
					criticalMethod(x);
					i++;
				}
			}
		};
		try{Thread.sleep(10);}
		catch(InterruptedException e){}
		x.start();
		
		try{Thread.sleep(10);}
		catch(InterruptedException e){}
		
		y = new VisualThread(){
			@Override
			public void run(){
				int i=0;
				while(i<5){
					try{Thread.sleep(VisualGUI.returnRandom()*10);}
					catch(InterruptedException e){}
					criticalMethod(y);
					i++;
				}
			}
		};
		try{Thread.sleep(10);}
		catch(InterruptedException e){}
		y.start();
		
		try{Thread.sleep(10);}
		catch(InterruptedException e){}
		
		
		z = new VisualThread(){
			@Override
			public void run(){
				int i=0;
				while(i<5){
					try{Thread.sleep(VisualGUI.returnRandom()*10);}
					catch(InterruptedException e){}
					criticalMethod(z);
					i++;
				}
			}
		};
		try{Thread.sleep(10);}
		catch(InterruptedException e){}
		z.start();
		
		
		
		
		x.interrupt();
		try{Thread.sleep(10);}
		catch(InterruptedException e){}
		y.interrupt();
		try{Thread.sleep(10);}
		catch(InterruptedException e){}
		x.join();
		y.join();
		//Analyzer.printAll();
		
//		try{Thread.sleep(VisualGUI.returnRandom()*10);}
//		catch(InterruptedException e){}
//		
//		y.destroy();
//		
//		try{Thread.sleep(VisualGUI.returnRandom()*10);}
//		catch(InterruptedException e){}
//		
//		z.destroy();
//		
//		try{Thread.sleep(VisualGUI.returnRandom()*10);}
//		catch(InterruptedException e){}
//		
//		x.destroy();
		
	}
	
	
	public synchronized static void criticalMethod(VisualThread vs){
		vs.enterCriticalSection("alpha" + Long.toString(vs.getId()));
		try{Thread.sleep(500);}
		catch(InterruptedException e){}
		vs.leaveCriticalSection("alpha" + Long.toString(vs.getId()));
	}
	

}
