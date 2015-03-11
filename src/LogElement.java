/*
* This is a struct-like class that makes storing log elements easy
*/
import java.sql.Timestamp;

public class LogElement {
	
	public Long threadID;
	public String message;
	public Timestamp time;

	public LogElement(Long threadID, String message, Timestamp time)
	{
		this.threadID = threadID;
		this.message = message;
		this.time = time;
	}
}