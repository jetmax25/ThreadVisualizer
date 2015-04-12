import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class testDriver {
	

	static Lock lock = new ReentrantLock();
	static Lock lock2 = new ReentrantLock();
	static VisualThread x;
	static VisualThread y;
	static VisualThread z;
	static VisualThread u;
	
	public static void main(String[] args) throws InterruptedException {
				
		// TODO Auto-generated method stub
		x = new VisualThread(){
			@Override
			public void run(){
				int i=0;
				while(i<5){
					try{Thread.sleep(VisualGUI.returnRandom()*10);}
					catch(InterruptedException e){}
					int rand = returnRandom();
					if(rand == 1)
						criticalMethod(x);
					else 
						criticalMethod2(x);
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
					int rand = returnRandom();
					if(rand == 1)
						criticalMethod(y);
					else
						criticalMethod2(y);
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
					int rand = returnRandom();
					if(rand == 1)
						criticalMethod(z);
					else
						criticalMethod2(z);
					
					i++;
				}
			}
		};
		try{Thread.sleep(10);}
		catch(InterruptedException e){}
		z.start();
		
//		try{Thread.sleep(4000);}
//		catch(InterruptedException e){}
		
		u = new VisualThread(){
			@Override
			public void run(){
				int i=0;
				while(i<5){
					try{Thread.sleep(VisualGUI.returnRandom()*10);}
					catch(InterruptedException e){}
					int rand = returnRandom();
					if(rand == 1)
						criticalMethod(u);
					else
						criticalMethod2(u);
					
					i++;
				}
			}
		};
		try{Thread.sleep(10);}
		catch(InterruptedException e){}
		u.start();
		
		
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
	
	
	public static void criticalMethod(VisualThread vs){
		lock.lock();
		vs.enterCriticalSection("alpha");
		try{Thread.sleep(500);}
		catch(InterruptedException e){}
		vs.leaveCriticalSection("alpha");
		lock.unlock();
	}
	
	public static void criticalMethod2(VisualThread vs){
		lock2.lock();
		vs.enterCriticalSection("beta");
		try{Thread.sleep(500);}
		catch(InterruptedException e){}
		vs.leaveCriticalSection("beta");
		lock2.unlock();
	}
	
	private static synchronized int returnRandom(){
		Random rn = new Random();
		int answer = rn.nextInt(2) + 1;
		return answer;
	}
	

}
