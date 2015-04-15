
public class Chopstick {

	private int num;
	private boolean flag = false;
	private int personWaiting = -1;
	private int anotherPersonWaiting = -1;
	
	public Chopstick(int num)
	{
		this.num = num;
	}
	
	public void pickUp(int id){

	//System.out.println("Philospher " + id + " has chopstick " + num);
	}
	
	public void putDown(int id)
	{
		//System.out.println("Philospher " + id + " put down the chopstick " + num);
	}
	
	public synchronized boolean setFlag(int id){
		if(personWaiting >= 0)
		{
			if(id != personWaiting) {
				anotherPersonWaiting = id;
				return false;
			}
		}
		
		if(!flag){
			flag = true;
			return true;
		}
		else{
			personWaiting = id;
			anotherPersonWaiting = -1;
			return false;
		}
	}
	
	public void resetFlag(){
		flag = false;
		personWaiting = anotherPersonWaiting;
		anotherPersonWaiting = -1;
	}
}
