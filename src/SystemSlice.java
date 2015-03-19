public class SystemSlice {

	private int cpuPercent;
	private int memUse;
	private long time;

	public SystemSlice(int cpuPercent, int memUse, long time)
	{
		this.cpuPercent = cpuPercent;
		this.memUse = memUse;
		this.time = time;
	}
	
	public String toString(long start)
	{
		return "CPU: " + cpuPercent + " Mem: " + memUse + " " + (time - start);
	}

	public String toString()
	{
		return "CPU: " + cpuPercent + " Mem: " + memUse + " " + time;	
	}
	
	public int getCpu()
	{
		return cpuPercent;
	}

	public int getMem()
	{
		return memUse;
	}
	
	public long getTime()
	{
		return time; 
	}
}
