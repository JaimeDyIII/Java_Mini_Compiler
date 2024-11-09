import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GUI{
    public GUI() {
        appFrame();
    }

    public void appFrame(){
        JFrame frame = new JFrame();

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1));

        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Java Mini Compiler");
        frame.pack();
        frame.setVisible(true);
        frame.setSize(960, 540);
    }
    public static void main(String[] args) {
        new GUI();
    }
}