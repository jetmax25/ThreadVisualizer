import java.util.ArrayList;
import java.util.Collection;

import com.sun.xml.internal.xsom.impl.scd.Iterators.Map;


public class MethodFinder extends Thread {

	private static ArrayList<VisualThread> threads;
	private static ArrayList<Integer> stackSize;
	private static int threadNum; 
	
	public MethodFinder()
	{
		threads = new ArrayList<VisualThread>();
		stackSize = new ArrayList<Integer>();
	}
	
	public void addThread(VisualThread thread)
	{
		//if(threadNum < 0) threadNum++;
		//threads.add(thread);
		//stackSize.add(thread.stackTraceSize());
		java.util.Map<Thread, StackTraceElement[]> map =  Thread.getAllStackTraces();
		Collection<StackTraceElement[]> p = map.values();

		
		System.out.println(p.iterator());
	}
	
	public void removeThread(VisualThread thread)
	{
		int remove = threads.indexOf(thread);
		threads.remove(remove);
		
	}
	
	public void run()
	{
		
		while(true)
		{
			//System.out.println("running");
			if(threadNum < 0) continue;
			
		//	System.out.println("running");
		//	checkMethods(threads.get(threadNum));
		//	threadNum++;
		//	threadNum %= threads.size();
		}
	}
	
	private void checkMethods(VisualThread thread)
	{
		//if(thread.getStackTrace().length > stackSize.get(threadNum))
		//{
			System.out.println("asdf" + thread.getStackTrace());
		//}
		
		//else {
			stackSize.set(threadNum, thread.getStackTrace().length);  
		//}
	}
	
	
}
