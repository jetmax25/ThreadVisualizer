
public class ActivitySlice {

	private String description;
	private long threadNum;
	private long time;
	
	
	public ActivitySlice(String descript, long id, long time)
	{
		description = descript;
		threadNum = id;
		this.time = time; 
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
}
