
public class ActivitySlice {

	private String description;
	private long threadNum;
	private long time;
	private long objectSize;
	
	
	public ActivitySlice(String descript, long id, long time, long objectSize)
	{
		description = descript;
		threadNum = id;
		this.time = time; 
		this.objectSize = objectSize; 
	}
	
	public String toString(long start)
	{
		return threadNum + " " + description + " " + (time - start);
	}
	
	public long getThread()
	{
		return threadNum;
	}
	
	public long getTime(){
		return time; 
	}
	
	public String getDescription(){
		return description;
	}
}
