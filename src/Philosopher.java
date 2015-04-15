
public class Philosopher extends VisualThread{

	private int eatTime;
	private int thinkTime;
	private int id; 
	private Chopstick chopstick1, chopstick2;
	
	public Philosopher(Chopstick one, Chopstick two, int id, int think, int eat) {
		eatTime = eat;
		thinkTime = think;
		this.id = id;
		chopstick1 = one;
		chopstick2 = two; 
	}
	
	@SuppressWarnings("static-access")
	public void run(){

		while(true)
		{
			try {
				this.sleep(thinkTime);
				System.out.println(id + " is now hungry");
				while(!eat()){ }
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				return;
			}
	
		}
		
	}
	
	@SuppressWarnings("static-access")
	private boolean eat() throws InterruptedException{
		if(chopstick1.setFlag(id)){
			if(chopstick2.setFlag(id)){
				
				synchronized(chopstick1){
					synchronized(chopstick2)
					{
						this.enterCriticalSection("Eating");
						chopstick1.pickUp(id);
						chopstick2.pickUp(id);
						System.out.println(id + " is now eating");
						
						this.sleep(eatTime);
						
						
						chopstick1.putDown(id);
						chopstick2.putDown(id);
						System.out.println(id + " is now thinking");
						chopstick2.resetFlag();
						}
						chopstick1.resetFlag();
						this.leaveCriticalSection("Eating");

				}
			}
			else{
				chopstick1.resetFlag();
				return false;
			}
		}
		else{
			return false;
		}
		return true;
	}
}