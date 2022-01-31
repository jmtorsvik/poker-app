package testing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class TestFrame extends JFrame {
	private JLayeredPane lpane = new JLayeredPane();
	private JPanel panel1 = new JPanel();
	private JPanel panel2 = new JPanel();
	private int width = 640;
	private int height = 480;
	
	public TestFrame() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(640, 480));
		setLayout(new BorderLayout());
		//add(lpane, BorderLayout.CENTER);
		lpane.setBounds(0, 0, width, height);
		panel1.setBackground(Color.BLUE);
		panel1.setBounds(0, 0, width, height);
		panel1.setOpaque(true);
		
		panel2.setBackground(Color.RED);
		panel2.setBounds(0, 0, width/2, height/2);
		panel2.setOpaque(true);
		lpane.add(panel1, 1);
		lpane.add(panel2, 0);
		
		add(panel1, 0);
		add(panel2, 0);
		
		pack();
		setVisible(true);
	}

	public static void main(String[] args) {
		new TestFrame();
	}
	
}
