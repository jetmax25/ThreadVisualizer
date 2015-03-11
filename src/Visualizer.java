/*
*	Gathers and stores system information (CPU%, Memory use, etc) at a specified tickRate
*	Stores all of the information repoted by threads via the logging function
*	Makes all of this information available to the UI system
*/


import java.util.Hashtable;
import java.util.ArrayList;
import java.sql.Timestamp;
import java.util.Timer;


public final class Visualizer {

	private static Hashtable<Long,Thread> threadTable;

	//stores all of the events called by threads
	private static ArrayList<LogElement> logList;

	//rate at which the Visualizer logs CPU/Memory Usage, watched variables, etc.
	private static int tickRate;

	//kicks off our data collection at every tickRate interval
	private static Timer collectionTimer;
	private static boolean timerStarted;

	private Visualizer()
	{
		threadTable = new Hashtable<Long, Thread>();
		logList = new ArrayList<LogElement>();

		tickRate = 500;

		collectionTimer = new Timer();
		timerStarted = false;
	}

	//Threads will call this method to add themselves to the threadTable
	public static void addThread(Thread th)
	{
		threadTable.put(th.getId(), th);
	}

	public static Thread getThread(long id)
	{
		return threadTable.get(id);
	}

	public static void setTickRate(int rateInMS)
	{
		tickRate = rateInMS;
	}

	public static void logEvent(Long threadID, String message, Timestamp time)
	{
		logList.add(new LogElement(threadID, message, time));
	}

	public static void printLog()
	{
		for(LogElement entry : logList)
		{
			System.out.println(entry.time.toString() + " - "
				+ entry.threadID + " - " + entry.message);
		}
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

}
