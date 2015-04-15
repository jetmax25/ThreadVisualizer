import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.concurrent.Semaphore;


public class PrimeFinder extends VisualThread {
	
	
	public static boolean flag = true;
	private static int prime;
	private static Sync syncer;
	private int thread;
	
	private  static final Semaphore available = new Semaphore(1, true);
	private  static final Semaphore mutex = new Semaphore(1, true);
	private static int count = 0; 
	public static boolean done = false;
	
	PrimeFinder(int thread)
	{
		this.thread = thread; 
		super.start();
		
	}
	
	public void run()
	{
		while(flag)
		{
			try {
				
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				
		//		System.out.println(" interupting thread " + thread);
			
					if(!done)
						try {
							seive();
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					else return;
					
			
			}
		}
		
		
	}
	
	private void seive() throws InterruptedException{
		int j = prime * 2;
		int k = prime * prime;
		
		//waits for all threads to finish marking non primes before continuing
		available.acquire();
		count++;
		if(count == 6){
			syncer.go();
			count = 0;
		}
		available.release();
		this.enterCriticalSection("findingPrime");
		//each thread sieves 1/8 of total numbers
		int start = k + (max * thread / 5);
		int stop = start + (max / 5);
		
		//each thread must start on a number where start % prime == 0
		start = (start / k) * k;
		
		//last thread goes to the last number (truncation problems could cause a few numbers short otherwise)
		if (stop > max) stop = max;
		
		//mark all sieved numbers
		for(int i = start; i < stop; i += j)
		{
			numbers.set(i);
		}
		
		this.leaveCriticalSection("findingPrime");

	}
	
	
	static int max;
	 static BitSet numbers;
	 static int maximum;
	
	public static void main(String[] args) throws InterruptedException
	{
		
		max = 100000000;
		numbers = new BitSet(max);
		maximum = (int) Math.sqrt(max);
		int sum = 0;
		int topPrimes[] = new int[10];
		int counter = 1;
		
		Sync syncer = new Sync();
		PrimeFinder[] threads = new PrimeFinder[6];
		
		
		PrimeFinder.numbers = new BitSet(max);
		int temp = (int)Math.sqrt(max);
		
		PrimeFinder.maximum = (int) Math.sqrt(max);
		for(int i = 0; i < 6; i++)
		{
			threads[i] = new PrimeFinder(i);
		}

		PrimeFinder.syncer = syncer;

		
		long startTime = System.nanoTime();
		
		int n = 3;
		for(; n <= temp; n+=2)
		{
			if(!numbers.get(n))
			{
				PrimeFinder.prime = n;
				for(Thread t : threads) t.interrupt();
				sum += n;
				counter++;
				syncer.hold();
			}
			
			
		}
		
		PrimeFinder.done = true;
		for(Thread t : threads) t.interrupt();
		
		//for(Thread t : threads) t.join();
		for(; n < max; n+=2)
		{
			if(!numbers.get(n))
			{
				
				sum += n;
				counter ++;
				topPrimes[counter % 10] = n;
			}
		}

		long endTime = System.nanoTime();
		
		long runTime = endTime - startTime;
		
		
		System.out.println("Execution Time: " + runTime + " nanoseconds");
		System.out.println("Total Primes Found " + counter);
		System.out.println("Sum of Primes Found " + sum);
		
		Arrays.sort(topPrimes);
		
		for(int i : topPrimes)
		{
			System.out.print(i + ", ");
		}
		
		PrimeFinder.flag = false;
		for(Thread t : threads) t.interrupt();
		
		
		
		/*Single Threaded Algorithm 
		 * 
		 * 
		 */
		
		
		long startTime2 = System.nanoTime();
		BitSet numbers2 = new BitSet(max);
		counter = 1; 
		sum = 0;
		
		for(int i = 3; i < max; i += 2)
		{
			if(!numbers2.get(i))
			{
				if(i <= Math.sqrt(max)){
					for(int j = i * i; j < max; j += i)
					{
						numbers2.set(j);
					}
				}
				counter++;
				sum+= i;
				topPrimes[counter % 10] = i;
			}
		}
		long endTime2 = System.nanoTime();
		long runTime2 = endTime2 - startTime2;
		System.out.println("\n" + runTime2 +  " ratio " + ((float)runTime2 / (float)runTime) + " counter " + counter);
	}
	

	public void getPrime()
	{
		
	}
}



