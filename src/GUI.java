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
    private Parser parser;
    private List<Token> tokens;
    private JTextArea console, codeTextArea;
    private JButton openFileButton, lexicalAnalysisButton, syntaxAnalysisButton, semanticAnalysisButton, clearButton;

    public GUI() {
        appFrame();
    }
    
    public void appFrame(){
        setTitle("Java Mini Compiler");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(960, 540);
        setLocationRelativeTo(null);
        
        codeTextArea = new JTextArea(100, 30);
        console = new JTextArea(50, 30);
        
        codeTextArea.setEditable(false);
        codeTextArea.setBorder(new CompoundBorder(
            new LineBorder(Color.GRAY, 1),
            new EmptyBorder(10, 10, 10, 10)
            ));
            
            console.setLineWrap(true);
            console.setEditable(false);
            
            JScrollPane consoleScrollPane = new JScrollPane(console);
            consoleScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            
            openFileButton = new JButton("Open File");
            lexicalAnalysisButton = new JButton("Lexical Analysis");
            syntaxAnalysisButton = new JButton("Syntax Analysis");
            semanticAnalysisButton = new JButton("Semantic Analysis");
            clearButton = new JButton("Clear");
            
            lexicalAnalysisButton.addActionListener(this::handleLexicalAnalysis);
            syntaxAnalysisButton.addActionListener(this::handleSyntaxAnalysis);
            semanticAnalysisButton.addActionListener(this::handleSemanticAnalysis);
            clearButton.addActionListener(this::handleClear);
            openFileButton.addActionListener(this::handleOpenFile);
            
            
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(5, 1));
            buttonPanel.add(openFileButton);
            buttonPanel.add(lexicalAnalysisButton);
            buttonPanel.add(syntaxAnalysisButton);
            buttonPanel.add(semanticAnalysisButton);
            buttonPanel.add(clearButton);
            
            JPanel textPanel = new JPanel();
            textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
            textPanel.add(codeTextArea);
            textPanel.add(consoleScrollPane);

            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.add(buttonPanel, BorderLayout.WEST);
            mainPanel.add(textPanel, BorderLayout.CENTER);
            
            if(file == null){
                lexicalAnalysisButton.setEnabled(false);
                syntaxAnalysisButton.setEnabled(false);
                semanticAnalysisButton.setEnabled(false);
            }
            
            add(mainPanel);
            setVisible(true);
        }
        
    private void handleOpenFile(ActionEvent e){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("java", "txt"));
        fileChooser.setDialogTitle("Select File to Open");
        
        int result = fileChooser.showOpenDialog(GUI.this);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
            
            try {
                if(file != null){
                    lexicalAnalysisButton.setEnabled(true);
                }
                String codeText = new String(Files.readAllBytes(file.toPath()));
                codeTextArea.setText(codeText);
                console.append("File Selected!\n");
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        } else {
            console.append("No file selected.\n");
        }
    }

    private void handleLexicalAnalysis(ActionEvent e) {
        try {
            lexer = new Lexer(file, this);
            tokens = lexer.getTokens();
            
            syntaxAnalysisButton.setEnabled(true);
            lexicalAnalysisButton.setEnabled(false);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void handleSyntaxAnalysis(ActionEvent e) {
        parser = new Parser(tokens, this);
        parser.runSyntaxAnalysis();
        semanticAnalysisButton.setEnabled(true);
        syntaxAnalysisButton.setEnabled(false);
    }

    private void handleSemanticAnalysis(ActionEvent e){
        parser.runSemanticAnalysis();
        semanticAnalysisButton.setEnabled(false);
    }

    private void handleClear(ActionEvent e){
        file = null;
        console.append("Cleared file!");
        codeTextArea.setText(null);
        lexicalAnalysisButton.setEnabled(false);
        syntaxAnalysisButton.setEnabled(false);
        semanticAnalysisButton.setEnabled(false);
    }

    public void update(String message){
        console.append(message + "\n");
    }

}