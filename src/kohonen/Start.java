package kohonen;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Start extends JFrame {

    private Kohonen kohonen;
    private int numberOfNeurons = 100;
    private int px = -10;
    private int py = -10;
    private List<Point> examples = new ArrayList<>();
    private Color color= Color.BLACK;
    private JTextField text = new JTextField(10);
    private int interval = 1000;
    private JPanel panel;
    private Panel upPanel;
    private JButton button;
    private JLabel label;
    
    public static void main(String args[]) {
	Start application = new Start();
	application.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
		System.exit(0);
            }
        }); 
    }	
	
    public Start() {
		
        panel = new JPanel();
        upPanel = new Panel();
	label = new JLabel("Iteracje: ");
	button = new JButton("Start");
        
	upPanel.add(label);
	upPanel.add(text);
	upPanel.add(button);
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(upPanel, BorderLayout.NORTH);
 
	button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int iter = 0;
                String iter2 = text.getText();
                if(!(iter2.equals(""))) iter = Integer.parseInt(iter2);

                kohonen = new Kohonen(examples, iter, numberOfNeurons);
                color = Color.BLUE;

                KohonenThread kohonenThread = new KohonenThread();
                kohonenThread.start();
            }
        });
        
	addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged( MouseEvent event ) {
		px = event.getX();
		py = event.getY();
				
		color = Color.BLACK;
		examples.add(new Point(px,py));
		repaint();
            }
	});
                
        pack();
	this.setSize(750, 750);
	setVisible(true);
    }
	
    public class KohonenThread extends Thread {
        @Override
        public void run() {
                
            int iter = 0;
            String iter2 = text.getText();
            System.out.println(iter);
            if(!(iter2.equals(""))) iter = Integer.parseInt(iter2);
                
            for (int t = 0; t < iter; t++) {                 
                kohonen.learning(t);
                    
                if(t % interval == 0) {
                    System.out.println("t " + t);
                    repaint();
                            
                    try {
                        sleep(300);
                    } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                }
            }
        }
    }
	
    @Override
    public void paint(Graphics g) {
        if(color == Color.BLUE) {
            g.clearRect(1, 1, 750, 750);
            for(int i = 0; i < examples.size(); i++) g.fillRoundRect((int) examples.get(i).x, (int)examples.get(i).y, 5, 5, 5, 5);
                     
            int tempx = 0, tempy = 0;
	    for(int i = 0; i < kohonen.getListOfWeights().size(); i++) {
                px = (int) kohonen.getListOfWeights().get(i).getX();
		py = (int) kohonen.getListOfWeights().get(i).getY();	
		
                g.setColor(color);    
		Graphics2D g2 = (Graphics2D) g;
	        g2.setStroke(new BasicStroke(3));
                
		if(i != 0) g.drawLine(tempx + 5, tempy + 5, px + 5, py + 5);
		tempx = px;
		tempy = py;
	    }
            
            for(int i = 0; i < kohonen.getListOfWeights().size(); i++) {
                px = (int) kohonen.getListOfWeights().get(i).getX();
		py = (int) kohonen.getListOfWeights().get(i).getY();
                
                g.setColor(Color.RED);
                g.fillOval(px, py, 10, 10); 
            }
            
	} else g.fillRoundRect(px, py, 5, 5, 5, 5);
    }
}
