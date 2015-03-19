/*
*	Runs at the tick rate specified by user - default is 500ms
*	Scheduled to run withing Visualizer class
*	Collects system information and reports it back to the visualizer
*/

public class DataCollectionTask implements Runnable {

	@Override
	public void run() {

		System.out.println("Create Slice");

		SystemInfo si = new SystemInfo();
			
		//create dummy data

		SystemSlice slice = si.getSlice();

		Visualizer.addSystemSlice(slice);

		System.out.println(slice.toString());

	}


}