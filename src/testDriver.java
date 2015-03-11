
public class testDriver {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		VisualThread x = new VisualThread();
		x.start();
		
		VisualThread y = new VisualThread();
		y.start();
		
		for(int i = 0; i < 5; i++)
		{
			Thread.sleep(300);
			Visualizer.printAll();
			System.out.println("*******************************");
		}
		
		x.interrupt();
		y.interrupt();
		x.join();
		y.join();
		Visualizer.printAll();
	}

}
