/*
*	Runs at the tick rate specified by user - default is 500ms
*	Scheduled to run withing Visualizer class
*	Collects system information and reports it back to the visualizer
*/

import org.hyperic.sigar.*;

public class DataCollectionTask implements Runnable {

	@Override
	public void run() {

		System.out.println("Create Slice");

		Sigar sigar = new Sigar();

		Cpu c;

		try{
			c = sigar.getCpu();
		} catch(SigarException se) {
			System.out.println("Unable to gather CPU info");
			return;
		}

		System.out.println(c.getIdle() + " : " + c.getTotal());




		//Visualizer.addSystemSlice(slice);

		//System.out.println(slice.toString());

	}


}