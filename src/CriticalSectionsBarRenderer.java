import java.awt.Color;
import java.awt.Paint;
import java.util.Random;

import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;



public class CriticalSectionsBarRenderer extends XYBarRenderer {
	
	//Allows for 10 critical sections
	static Color[] colors = {Color.BLUE, Color.CYAN, Color.GREEN, Color.MAGENTA, Color.ORANGE, Color.PINK,
						Color.RED, Color.WHITE, Color.YELLOW, Color.BLACK};
	
	public CriticalSectionsBarRenderer(){
		super();
	}
	
	
	public Paint getItemPaint(int row, int col){
				
		//Paint paint = super.getItemPaint(row, col);
		
		TaskSeries series = VisualGUI.taskSeriesArray.get(row);
		Task task = series.get(col);
		
		for(int i=0; i<10; i++){
			if(task.getDescription().equals(VisualGUI.criticalSectionStrings.get(i)))
				return colors[i];
		}
		
					
		return Color.GREEN;
		//return paint;
	}
	
	
	private static int returnRandom(){
		Random rn = new Random();
		int answer = rn.nextInt(5) + 1;
		return answer;
	}
	
}
