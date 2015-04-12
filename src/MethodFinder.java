import java.util.ArrayList;


public class MethodFinder extends Thread {

	private static ArrayList<VisualThread> threads;
	private static ArrayList<Integer> stackSize;
	private static int threadNum; 
	
	private MethodFinder()
	{
		threads = new ArrayList<VisualThread>();
		stackSize = new ArrayList<Integer>();
		threadNum = 0; 
	}
	
	public static void addThread(VisualThread thread)
	{
		threads.add(thread);
		stackSize.add(thread.getStackTrace().length);
	}
	
	public static void removeThread(VisualThread thread)
	{
		int remove = threads.indexOf(thread);
		threads.remove(remove);
		stackSize.remove(remove);
	}
	
	public void run()
	{
		while(true)
		{
			checkMethods(threads.get(threadNum));
			threadNum %= threads.size();
		}
	}
	
	private static void checkMethods(VisualThread thread)
	{
		if(thread.getStackTrace().length > stackSize.get(threadNum))
		{
			System.out.println(thread.getStackTrace()[1]);
		}
		
		else {
			stackSize.set(threadNum, thread.getStackTrace().length);  
		}
	}
	
	
}
