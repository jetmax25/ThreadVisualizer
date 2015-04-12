import java.awt.Color;
import java.awt.Paint;
import java.util.Random;

import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;



public class CriticalSectionsBarRenderer extends XYBarRenderer {
	
	public CriticalSectionsBarRenderer(){
		super();
	}
	
	
	public Paint getItemPaint(int row, int col){
				
		//Paint paint = super.getItemPaint(row, col);
		
		TaskSeries series = VisualGUI.taskSeriesArray.get(row);
		Task task = series.get(col);
		
		
		
		if(task.getDescription().equals("alpha"))
			return Color.RED;
		
		else if(task.getDescription().equals("beta"))
			return Color.BLUE;
					
		return Color.GREEN;
		//return paint;
	}
	
	
	private static int returnRandom(){
		Random rn = new Random();
		int answer = rn.nextInt(5) + 1;
		return answer;
	}
	
}
