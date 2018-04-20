package worldwark;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

public class WorldWarK extends JPanel {

    public static void main(String[] args) {
        
        JFrame frame = new JFrame("World War K");
        JPanel panel = new JPanel();
        JLabel label = new JLabel("YEEEEEEEEEEEEEEEEEEEEEEEEEEET");
        JMenuBar menu = new JMenuBar();
        
        panel.setSize(3000, 3000);
        panel.add(label);
        panel.add(menu);
        
        frame.add(panel);
        frame.setPreferredSize(new Dimension(500, 500));
        frame.setContentPane(panel);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

    }
}
