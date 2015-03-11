import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;


public class VisualGUI {
	
	public VisualGUI(){
		// create a dataset...
					DefaultCategoryDataset data = new DefaultCategoryDataset();			

					// create a chart...
					JFreeChart chart = ChartFactory.createLineChart("CPU Usage", "Time", "Hz", data, PlotOrientation.VERTICAL, true, true, false);
					// create and display a frame...
					ChartPanel chartPanel = new ChartPanel(chart);
					chartPanel.setVisible(true);
						
					
					//creating JButtons for JPanel 1
					JButton button1 = new JButton("CPU Visualizer");
					JButton button2 = new JButton("Memory Visualizer");
					JButton button3 = new JButton("Thread1 Visualizer");
					JButton button4 = new JButton("Thread2 Visualizer");
					JButton button5 = new JButton("Thread3 Visualizer");
					JButton button6 = new JButton("Thread4 Visualizer");
					JButton button7 = new JButton("Thread5 Visualizer");
								
					button1.setMaximumSize(new Dimension(200, 200));
					button2.setMaximumSize(new Dimension(200, 200));
					button3.setMaximumSize(new Dimension(200, 200));
					button4.setMaximumSize(new Dimension(200, 200));
					button5.setMaximumSize(new Dimension(200, 200));
					button6.setMaximumSize(new Dimension(200, 200));
					button7.setMaximumSize(new Dimension(200, 200));
					
					
					
					//creating JPanels
					JPanel jpanel1 = new JPanel();
					jpanel1.setLayout(new BoxLayout(jpanel1, BoxLayout.Y_AXIS));
					
					JPanel jpanel2 = new JPanel();
					JPanel jpanel3 = new JPanel();			
					
					//adding buttons to JPanel1
					jpanel1.add(button1);
					jpanel1.add(button2);
					jpanel1.add(button3);
					jpanel1.add(button4);
					jpanel1.add(button5);
					jpanel1.add(button6);
					jpanel1.add(button7);
					
					jpanel1.setVisible(true);
					
					
					//adding JFreeChart to JPanel2
					jpanel2.add(chartPanel);
					jpanel2.setVisible(true);
					
					//adding label to jpanel3
					jpanel3.add(new JLabel("Lots of Data goes here"));
					jpanel3.setVisible(true);
					
					
					
					//Adding components to JFrame using GridBagLayout manager
					JFrame jframe = new JFrame("Concurrent Visualizer");
					jframe.setLayout(new GridBagLayout());
					jframe.setResizable(false);
					jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					
					GridBagConstraints gc = new GridBagConstraints();
					gc.gridx = 0;
					gc.gridy = 0;
					gc.gridwidth = 1;
					gc.gridheight = 1;
					gc.weightx = 10;
					gc.weighty = 10;
					gc.insets = new Insets(0, 0, 0, 0);
					gc.anchor = GridBagConstraints.FIRST_LINE_START;
					gc.fill = GridBagConstraints.BOTH;
					
					
					gc.gridheight = 3;
					gc.gridwidth = 1;
					jframe.add(jpanel1, gc);
					
					
					gc.gridx = 1;
					gc.gridy = 0;
					gc.gridheight = 2;
					gc.gridwidth = 2;
					jframe.add(jpanel2, gc);
					
					gc.gridx = 1;
					gc.gridy = 2;
					gc.gridheight = 1;
					gc.gridwidth = 2;
					jframe.add(jpanel3, gc);
					
					
					jframe.setSize(900, 600);
					jframe.setVisible(true);
					
					
					
					
					
					int counter = 0;
					while(counter < 100){
						data.addValue(returnRandom(), "CPU Usage", Integer.toString(counter));
						counter++;
						try{Thread.sleep(1000);}
						catch(InterruptedException e){

						}
					}
	}
	
	public static int returnRandom(){
		Random rn = new Random();
		int answer = rn.nextInt(20) + 1;
		return answer;
	}
	
}
