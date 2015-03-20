/*
*	Runs at the tick rate specified by user - default is 500ms
*	Scheduled to run withing Visualizer class
*	Collects system information and reports it back to the visualizer
*/

import org.hyperic.sigar.*;

public class DataCollectionTask implements Runnable {

	@Override
	public void run() {

		Sigar sigar = new Sigar();

		long pid = sigar.getPid();

		ProcCpu ourProcCpu;
		ProcMem ourProcMem;

		ProcCpu prev;

		Mem mem;
		CpuPerc cpu;

		double load;



		try{
			ourProcMem = sigar.getProcMem(pid);
			ourProcCpu = sigar.getProcCpu(pid);

			int cpuCount = sigar.getCpuList().length;

			SystemSlice lastSlice = Visualizer.getLastSystemSlice();

			if(lastSlice==null)
			{
				load = 0;
			}
			else
			{
				prev = lastSlice.getCpuInfo();

				long totalDelta = ourProcCpu.getTotal() - prev.getTotal();
            	long timeDelta = ourProcCpu.getLastTime() - prev.getLastTime();

				load = 100. * totalDelta / timeDelta / cpuCount;
			}

			mem = sigar.getMem();
			cpu = sigar.getCpuPerc();

		} catch(SigarException se) {
			System.out.println("Unable to gather process info");
			return;
		}

		Visualizer.addSystemSlice( new SystemSlice( cpu.getCombined()*100, mem.getUsedPercent(), load, ourProcCpu, System.currentTimeMillis() ) );





	}


}