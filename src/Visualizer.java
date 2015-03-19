import java.util.ArrayList;
import java.util.Hashtable;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Visualizer {

	private static Hashtable<Long,Thread> threadTable = new Hashtable<Long, Thread>();
	private static Hashtable<Long,ArrayList<ActivitySlice>> activityTable = new Hashtable<Long, ArrayList<ActivitySlice>>();
	
	private static ArrayList<SystemSlice> systemList = new ArrayList<SystemSlice>();

	//threads currently active
	private static long activeThreads = 0; 
	//all threads created ever
	private static long totalThreads = 0; 
	//most threads ever active
	private static long maxThreads  = 0; 
	private static long start = System.currentTimeMillis(); 
	
	//rate at which the Visualizer logs CPU/Memory Usage, watched variables, etc.
	private static int tickRate = 500;

	//kicks off our data collection at every tickRate interval
	private static ScheduledExecutorService dataService = Executors.newSingleThreadScheduledExecutor();
	private static boolean serviceStarted = false;
	
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

	public static void addSystemSlice(SystemSlice ss)
	{
		systemList.add(ss);
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
	
	public static void setTickRate(int rateInMS)
	{
		tickRate = rateInMS;
	}
	
	public static void startDataCollection()
	{
		//if already started, shutdown the service and restart (possibly at new rate)
		if(serviceStarted)
		{
			stopDataCollection();
		}

		//dataService.scheduleAtFixedRate(new DataCollectionTask(), 0, tickRate, TimeUnit.MILLISECONDS);

		//final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
    	dataService.scheduleAtFixedRate(new DataCollectionTask()
		      /*{
		        @Override
		        public void run()
		        {
		          System.out.println("sdfglksjdfkljsdfkj");
		        }
		      }*/, 0, tickRate, TimeUnit.MILLISECONDS);

		System.out.println("collection started");
	}

	public static void stopDataCollection()
	{
		dataService.shutdown();
	}

}
