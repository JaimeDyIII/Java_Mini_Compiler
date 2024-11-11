import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GUI extends IOException{
    private File file;
    private Lexer lexer;
    private List<Token> tokens;

    public GUI() {
        appFrame();
    }

    public void appFrame(){
        JFrame frame = new JFrame();

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1));

        frame.add(panel, BorderLayout.PAGE_START);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Java Mini Compiler");
        frame.pack();
        frame.setSize(960, 540);

        JButton openFileButton = new JButton("Open File");
        openFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("java", "txt"));
                fileChooser.setDialogTitle("Select File to Open");

                int result = fileChooser.showOpenDialog(frame);
                
                if (result == JFileChooser.APPROVE_OPTION) {
                    file = fileChooser.getSelectedFile();
                    System.out.println("File selected!");
                } else {
                    System.out.println("No file selected.");
                }
            }
        });

        JButton lexicalAnalyisisButton = new JButton("Lexical Analysis");
        lexicalAnalyisisButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                try {
                    lexer = new Lexer(file);
                    tokens = lexer.getTokens();

                    System.out.println("Lexical Analysis Completed!");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        
        panel.add(openFileButton);
        panel.add(lexicalAnalyisisButton);
        frame.setVisible(true);
    }
}