import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class testDriver {

	static Lock lock = new ReentrantLock(true);
	static VisualThread x;
	static VisualThread y;
	
	public static void main(String[] args) throws InterruptedException {
		
		
		// TODO Auto-generated method stub
		x = new VisualThread(new Runnable(){
			public void run(){
				int i=0;
				while(i<5){
					x.enterCriticalSection("alpha");
					criticalMethod();
					x.leaveCriticalSection("alpha");
					i++;
				}
			}
		});
		x.start();
		
		y = new VisualThread(new Runnable(){
			public void run(){
				int i=0;
				while(i<5){
					y.enterCriticalSection("beta");
					criticalMethod();
					y.leaveCriticalSection("beta");
					i++;
				}
			}
		});
		y.start();
		
		for(int i = 0; i < 5; i++)
		{
			Thread.sleep(300);
			Analyzer.printAll();
			System.out.println("*******************************");
		}
		
		
		
		x.interrupt();
		y.interrupt();
		x.join();
		y.join();
		Analyzer.printAll();
		
//		Thread.sleep(5000);
//		VisualThread z = new VisualThread();
//		z.start();
	}
	
	
	public static void criticalMethod(){
		lock.lock();
		try{Thread.sleep(3000);}
		catch(InterruptedException e){}
		lock.unlock();
		
	}
	

}
