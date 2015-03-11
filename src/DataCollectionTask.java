/*
*	Runs at the tick rate specified by user - default is 500ms
*	Scheduled to run withing Visualizer class
*	Collects system information and reports it back to the visualizer
*/


import java.util.TimerTask;
import java.util.Random;

public class DataCollectionTask extends TimerTask {

	@Override
	public void run() {

		Random rng = new Random();
			
		//create dummy data
		Visualizer.addSystemSlice(new SystemSlice(rng.nextInt(100), rng.nextInt(1024), System.currentTimeMillis()));

	}


}