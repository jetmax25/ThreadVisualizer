import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Timer;


public class Visualizer {

	private static Hashtable<Long,Thread> threadTable = new Hashtable<Long, Thread>();
	private  static Hashtable<Long,ArrayList<ActivitySlice>> activityTable = new Hashtable<Long, ArrayList<ActivitySlice>>();
	
	//accepting thread table
	private static Hashtable<Long,Long> acceptingTable = new Hashtable<Long, Long>();
	//threads currently active
	private static int activeThreads = 0; 
	//all threads created ever
	private static int totalThreads = 0; 
	//most threads ever active
	private static int maxThreads  = 0; 
	private static long start = System.currentTimeMillis(); 
	
	//rate at which the Visualizer logs CPU/Memory Usage, watched variables, etc.
	private static int tickRate = 500;

	//kicks off our data collection at every tickRate interval
	private static Timer collectionTimer = new Timer();
	private static boolean timerStarted = false;
	
	private static enum AcceptState{
		none, all, some
	}
	
	private static AcceptState acceptState = AcceptState.none; 
	
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
	
	public static void setTickRate(int rateInMS)
	{
		tickRate = rateInMS;
	}
	
	public static void startDataCollection()
	{
		//if already started, cancel the tasks and restart (possibly at new rate)
		if(timerStarted)
		{
			stopDataCollection();
		}

		collectionTimer.scheduleAtFixedRate(new DataCollectionTask(), 0, tickRate);
	}

	public static void stopDataCollection()
	{
		collectionTimer.cancel();
		collectionTimer.purge();
	}
	
	public static void startActivityNotifications()
	{
		acceptState = AcceptState.all;
	}
	
	public static void stopActivityNotifications()
	{
		acceptState = AcceptState.none;
		acceptingTable.clear();
	}
	
	public static void startActivityNotifications(long id){
		acceptingTable.put(id, id);
	}
	
	public static void stopActivityNotifications(long id){
		acceptingTable.remove(id);
	}
	
	public static boolean isAcceptingThread(long id)
	{
		if(acceptState.equals(AcceptState.none)) return false;
		if(acceptingTable.get(id) != null) return true; 
		return false; 
	}
	
	public static int getTotalThreads()
	{
		return totalThreads;
	}


	

}
