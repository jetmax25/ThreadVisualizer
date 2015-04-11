import java.awt.Color;
import java.awt.Paint;
import java.util.Random;

import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.gantt.XYTaskDataset;


public class CriticalSectionsBarRenderer extends XYBarRenderer {
	
	public CriticalSectionsBarRenderer(){
		super();
	}
	
	
	public Paint getItemPaint(int row, int col){
				
		Paint paint = super.getItemPaint(row, col);
		
		//This is where we deque the wait free queue
		
		return paint;
	}
	
	
	private static int returnRandom(){
		Random rn = new Random();
		int answer = rn.nextInt(5) + 1;
		return answer;
	}
	
}
