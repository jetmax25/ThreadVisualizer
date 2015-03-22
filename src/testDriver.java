import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class testDriver {
	private static VisualGUI gui = new VisualGUI();	


	static Lock lock = new ReentrantLock();
	static VisualThread x;
	static VisualThread y;
	
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
		x.start();
		
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
		y.start();
		
		
		x.interrupt();
		System.out.println("interrupt created");
		y.interrupt();
		x.join();
		y.join();
		Analyzer.printAll();
		
	}
	
	
	public synchronized static void criticalMethod(VisualThread vs){
		vs.enterCriticalSection("alpha" + Long.toString(vs.getId()));
		try{Thread.sleep(500);}
		catch(InterruptedException e){}
		vs.leaveCriticalSection("alpha" + Long.toString(vs.getId()));
	}
	

}
