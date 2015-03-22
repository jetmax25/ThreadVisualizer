import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Analyzer {

	private static Hashtable<Long,Thread> threadTable = new Hashtable<Long, Thread>();
	private  static Hashtable<Long,ArrayList<ActivitySlice>> activityTable = new Hashtable<Long, ArrayList<ActivitySlice>>();
	private static Hashtable<String, Hashtable<Long, long[]>> criticalSection = new Hashtable<String, Hashtable<Long, long[]>>();

	private static ArrayList<SystemSlice> systemList = new ArrayList<SystemSlice>(); 

	private static ActivitySlice lastSlice = null; 
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
	private static ScheduledExecutorService dataService = Executors.newSingleThreadScheduledExecutor();
	private static boolean serviceStarted = false;

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
		//System.out.println("step3");
		
		//get prev info
		long num = as.getThread();
		ArrayList<ActivitySlice> temp = activityTable.get(num);
		
		temp.add(as);
		//save it
		activityTable.put(num, temp);
		
		lastSlice = as;
		
		VisualGUI.addActivitySlice(as);
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

    	dataService.scheduleAtFixedRate(new DataCollectionTask(), 0, tickRate, TimeUnit.MILLISECONDS);

		System.out.println("collection started");
	}

	public static void stopDataCollection()
	{
		dataService.shutdown();
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
		if(acceptState.equals(AcceptState.all)) return true;
		return false; 
	}
	
	public static int getTotalThreads()
	{
		return totalThreads;
	}

	public static ActivitySlice getActivity()
	{
		return lastSlice; 
	}
	
	public static ActivitySlice getActivity(long id)
	{
		if(!activityTable.containsKey(id)) return null; 
		
		ArrayList<ActivitySlice> temp = activityTable.get(id);
		return temp.get(temp.size() - 1);
		
	}

	//used by DataCollectionTask to calculate the current load
	public static synchronized SystemSlice getLastSystemSlice()
	{
		if(systemList.isEmpty())
			return null;
		else
			return systemList.get( systemList.size()-1 );
	}

	public static ArrayList<SystemSlice> getAllSystemSlices()
	{
		return systemList;
	}
	
	public static void enteringCriticalSection(long id, String section, long time)
	{
		//new critical sectiomn
		if(!criticalSection.containsKey(section)){
			criticalSection.put(section, new Hashtable<Long, long[]>());
			//VisualGUI.addCriticalSection(section);
		}
		long[] temp = {time, -1};
		criticalSection.get(section).put(id, temp );
		VisualGUI.enteringCS(section, id, time);
	}
	
	public static void leavingCriticalSection(long id, String section, long time)
	{
		long[] temp = {criticalSection.get(section).get(id)[0], time}; 
		criticalSection.get(section).put(id, temp );
		
		//tells the gui that it left
		VisualGUI.leaveCS(section, id, temp[0], temp[1]);
		
		VisualGUI.addCriticalSection(section, id, temp[0], temp[1]);
	}
	
	
}
