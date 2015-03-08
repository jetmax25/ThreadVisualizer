import java.util.Hashtable;


public class Visualizer {

	private static Hashtable<Long,Thread> ThreadTable;

	public Visualizer()
	{
		ThreadTable = new Hashtable<Long, Thread>();
	}

	//Threads will call this method to add themselves to the ArrayList
	public void addThread(Thread th)
	{
		ThreadTable.put(th.getId(), th);
	}

	public Thread getThread(long id)
	{
		return ThreadTable.get(id);
	}

}
