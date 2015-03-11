import java.util.ArrayList;
import java.util.Hashtable;


public class Visualizer {

	private static Hashtable<Long,Thread> threadTable = new Hashtable<Long, Thread>();
	private  static Hashtable<Long,ArrayList<ActivitySlice>> activityTable = new Hashtable<Long, ArrayList<ActivitySlice>>();
	
	//threads currently active
	private static long activeThreads = 0; 
	//all threads created ever
	private static long totalThreads = 0; 
	//most threads ever active
	private static long maxThreads  = 0; 
	private static long start = System.currentTimeMillis(); 
	
	//Threads will call this method to add themselves to the ArrayList
	public static void addThread(Thread th)
	{
		threadTable.put(th.getId(), th);
		activityTable.put(th.getId(), new ArrayList<ActivitySlice>());
		activeThreads++;
		
		if(activeThreads > maxThreads) maxThreads++; 
		totalThreads++;
	}
	
	public static void removeThreads(Thread th)
	{
		threadTable.remove(th.getId());
		activeThreads--;
	}

	public static Thread getThread(long id)
	{
		return threadTable.get(id);
	}
	
	public static void addSlice(ActivitySlice as)
	{
		//get prev info
		long num = as.getThread();
		ArrayList<ActivitySlice> temp = activityTable.get(num);
		
		temp.add(as);
		//save it
		activityTable.put(num, temp);
	}
	
	public synchronized static void printAll()
	{
		for(ArrayList<ActivitySlice> list : activityTable.values())
		{
			for(ActivitySlice as : list) System.out.println(as.toString(start));
		}
	}
	
	public static long threadCount()
	{
		return activeThreads;
	}

}
