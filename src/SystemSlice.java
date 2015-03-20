import org.hyperic.sigar.ProcCpu;

public class SystemSlice {

	private double cpuPercent;
	private double memPercent;
	private long time;
	private ProcCpu cpuInfo; //used to calculate next load value
    private double load; // % CPU used by the process

	public SystemSlice(double cpuPercent, double memPercent, double load, ProcCpu cpuInfo, long time)
	{
		this.cpuPercent = cpuPercent;
		this.memPercent = memPercent;
		this.load = load;
		this.cpuInfo = cpuInfo;
		this.time = time;
	}
	
	public String toString(long start)
	{
		return "CPU: " + cpuPercent + " Mem: " + memPercent + " " + (time - start);
	}

	public String toString()
	{
		return "Total CPU%: " + cpuPercent + " Total Mem%: " + memPercent + " Process CPU%: " + load;	
	}
	
	public double getCpu()
	{
		return cpuPercent;
	}

	public double getMem()
	{
		return memPercent;
	}
	
	public long getTime()
	{
		return time; 
	}

	public ProcCpu getCpuInfo()
	{
		return cpuInfo;
	}

	public double getLoad()
	{
		return load;
	}
}
