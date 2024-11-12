import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;

public class GUI extends JFrame {
    private File file;
    private Lexer lexer;
    private List<Token> tokens;

    public GUI() {
        appFrame();
    }

    public void appFrame(){
        setTitle("Java Mini Compiler");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(960, 540);
        setLocationRelativeTo(null);


        JTextField codeTextField = new JTextField();
        JTextArea codeTextArea = new JTextArea();
        codeTextField.setEditable(false);
        codeTextArea.setEditable(false);

        codeTextArea.setBorder(new CompoundBorder(
            new LineBorder(Color.GRAY, 1),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        codeTextField.setPreferredSize(new Dimension(400, 30));

        JButton openFileButton = new JButton("Open File");
        openFileButton.addActionListener((ActionEvent e) -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("java", "txt"));
            fileChooser.setDialogTitle("Select File to Open");
            
            int result = fileChooser.showOpenDialog(GUI.this);
            
            if (result == JFileChooser.APPROVE_OPTION) {
                file = fileChooser.getSelectedFile();
                
                try {
                    String codeText = new String(Files.readAllBytes(file.toPath()));
                    codeTextArea.setText(codeText);
                    codeTextField.setText("File Selected!");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            } else {
                codeTextField.setText("No file selected.");
            }
        });

        JButton lexicalAnalysisButton = new JButton("Lexical Analysis");
        lexicalAnalysisButton.addActionListener((ActionEvent e) -> {
            try {
                lexer = new Lexer(file);
                tokens = lexer.getTokens();
                
                codeTextField.setText("Lexical Analysis Completed!");
                
                for(Token token: tokens){
                    System.out.println(token.toString());
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        JButton syntaxAnalysisButton = new JButton("Syntax Analysis");
        syntaxAnalysisButton.addActionListener((ActionEvent e) -> {
        });

        JButton semanticAnalysisButton = new JButton("Semantic Analysis");
        semanticAnalysisButton.addActionListener((ActionEvent e) -> {
        });

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener((ActionEvent e) -> {
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 1));
        buttonPanel.add(openFileButton);
        buttonPanel.add(lexicalAnalysisButton);
        buttonPanel.add(syntaxAnalysisButton);
        buttonPanel.add(semanticAnalysisButton);
        buttonPanel.add(clearButton);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(buttonPanel, BorderLayout.WEST);
        mainPanel.add(codeTextField, BorderLayout.NORTH);
        mainPanel.add(codeTextArea, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }
}