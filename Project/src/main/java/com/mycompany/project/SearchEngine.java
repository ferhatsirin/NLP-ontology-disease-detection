package com.mycompany.project;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

/**
 * Tıbbi Metinler Arama Uygulaması Hastalık belirtilerinden ve nedenlerinden
 * yola çıkarak hastalık tespit edilmeye çalışılır.
 *
 * @author ferhat
 */
public class SearchEngine {

    private static final String PYTHONCODE = "SearchModule.py";
    private static final String OntLocation = "dataset/inferredOnt";
    private static final String OntURL = "http://www.semanticweb.org/ferhat/ontologies/";
    private static final String articleLocation = "dataset/articles/";
    private JFrame frame;
    private JTextField textF;
    private NLPSearch nlpSearch;
    private OntologySearch ontSearch;
    private JPanel frontP;
    private JScrollPane scrollPane;
    private JPanel resultP;
    private JPanel articleP;
    private JPanel searchPanel;
    private JButton textB;
    private JButton searchTextB;
    private JTextField searchTextF;
    private JRadioButton nlpB;
    private JRadioButton ontologyB;
    private ButtonGroup groupB;
    private JButton returnB;
    private Image searchImg;
    private Image returnImg;
    private ArticleInput articleInput;
    private JTextArea searchText;

    public static void main(String[] args) {
        new SearchEngine();
    }

    public SearchEngine() {
        init();
    }

    public void init() {

        nlpSearch = new NLPSearch(PYTHONCODE);
        ontSearch = new OntologySearch(OntLocation, OntURL);

        frame = new JFrame();
        frame.setTitle("Search Engine");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 400);
        frame.setVisible(true);
        frame.addWindowListener(new FrameControl());

        frontP = new JPanel();
        frontP.setBackground(new Color(104, 165, 236));
        GroupLayout layout = new GroupLayout(frontP);
        frontP.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        textF = new JTextField();
        textF.setText("");
        textF.addActionListener(new TextInput());

        textB = new JButton();
        textB.setBackground(new Color(104, 165, 236));
        textB.addActionListener(new TextInput());

        articleInput = new ArticleInput("Epilepsi");
        textB.addActionListener(articleInput);
        textF.addActionListener(articleInput);

        try {
            searchImg = ImageIO.read(getClass().getClassLoader().getResource("img/search.png"));
            searchImg = searchImg.getScaledInstance(40, 30, Image.SCALE_SMOOTH);
            returnImg = ImageIO.read(getClass().getClassLoader().getResource("img/return.png"));
            returnImg = returnImg.getScaledInstance(40, 30, Image.SCALE_SMOOTH);
            textB.setIcon(new ImageIcon(searchImg));
            textB.setBorder(null);
        } catch (IOException ex) {
            Logger.getLogger(SearchEngine.class.getName()).log(Level.SEVERE, null, ex);
        }

        nlpB = new JRadioButton("NLP");
        nlpB.setSelected(true);

        ontologyB = new JRadioButton("Ontology");

        groupB = new ButtonGroup();
        groupB.add(nlpB);
        groupB.add(ontologyB);

        nlpB.setBackground(new Color(104, 165, 236));
        ontologyB.setBackground(new Color(104, 165, 236));

        resultP = new JPanel();
        resultP.setLayout(new GridLayout(0, 2));

        scrollPane = new JScrollPane(resultP);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        layout.setHorizontalGroup(layout.createParallelGroup().addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup().addComponent(textF).addGroup(layout.createSequentialGroup().addComponent(nlpB).addComponent(ontologyB))).addComponent(textB)).addComponent(scrollPane));
        layout.setVerticalGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addGroup(layout.createSequentialGroup().addComponent(true, textF, 0, 30, Short.MAX_VALUE).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(nlpB, GroupLayout.Alignment.CENTER).addComponent(ontologyB))).addComponent(textB)).addComponent(scrollPane));

        frame.add(frontP);

        initSearchPanel();

        frame.revalidate();
        frame.repaint();

    }

    public void initSearchPanel() {

        searchPanel = new JPanel();
        searchPanel.setBackground(new Color(104, 165, 236));
        GroupLayout layout = new GroupLayout(searchPanel);
        searchPanel.setLayout(layout);

        returnB = new JButton();
        returnB.setIcon(new ImageIcon(returnImg));
        returnB.setBorder(null);
        returnB.setBackground(new Color(104, 165, 236));
        returnB.addActionListener(new ReturnButton());

        articleP = new JPanel();
        articleP.setLayout(new GridLayout(0, 1));

        searchTextF = new JTextField();
        searchTextF.setText("");
        searchTextF.addActionListener(new TextInput());

        searchTextB = new JButton();
        searchTextB.setBackground(new Color(104, 165, 236));
        searchTextB.addActionListener(new TextInput());
        searchTextB.setIcon(new ImageIcon(searchImg));
        searchTextB.setBorder(null);

        searchTextB.addActionListener(articleInput);
        searchTextF.addActionListener(articleInput);

        searchText = new JTextArea();
        searchText.setLineWrap(true);
        searchText.setWrapStyleWord(true);
        searchText.setEditable(false);

        TextLineNumber tln = new TextLineNumber(searchText);
        tln.setDigitAlignment(TextLineNumber.CENTER);
        tln.setBorderGap(3);
        tln.setBorder(null);
        tln.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(searchText);
        scrollPane.setRowHeaderView(tln);

        scrollPane.setBackground(Color.WHITE);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        layout.setHorizontalGroup(layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup().addComponent(returnB).addComponent(searchTextF).addComponent(searchTextB))
                .addGroup(layout.createSequentialGroup().addComponent(scrollPane).addComponent(articleP, 100, 100, 100)));
        layout.setVerticalGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup().addComponent(returnB)
                .addComponent(searchTextF, 0, 30, 30).addComponent(searchTextB)).addGroup(layout.createParallelGroup().addComponent(scrollPane).addComponent(articleP)));

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

    }

    public void printArticle(String name, String line) {

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(articleLocation + name));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SearchEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
        String t;
        List<String> data = new ArrayList<>();
        try {
            while ((t = reader.readLine()) != null) {
                data.add(t);
            }
        } catch (IOException ex) {
            Logger.getLogger(SearchEngine.class.getName()).log(Level.SEVERE, null, ex);
        }

        frame.setTitle(name);
        frame.remove(frontP);
        frame.add(searchPanel);

        articleInput.setName(name);
        searchTextF.setText(textF.getText());
        searchText.setText("");
        for (int i = 0; i < data.size(); ++i) {
            searchText.append("\n " + data.get(i));
        }

        printArticleRank(name, line);

        frame.revalidate();
        frame.repaint();
    }

    public String addLineBreaks(String input, int maxLineLength) {
        StringTokenizer tok = new StringTokenizer(input, " ");
        StringBuilder output = new StringBuilder(input.length());
        int lineLen = 0;
        while (tok.hasMoreTokens()) {
            String word = tok.nextToken();

            if (lineLen + word.length() > maxLineLength) {
                output.append("\n");
                lineLen = 0;
            }
            output.append(" " + word);
            lineLen += word.length();
        }
        return output.toString();
    }

    public void printArticleRank(String name, String line) {
        Map<String, Double> result = nlpSearch.searchArticle(name, line);
        articleP.removeAll();
        for (Map.Entry<String, Double> ent : result.entrySet()) {
            JLabel labelK = new JLabel();
            labelK.setText(ent.getKey() + "   " + ent.getValue().toString().substring(0, 9));
            articleP.add(labelK);
        }
        frame.revalidate();
        frame.repaint();
    }

    public static String extractName(String n) {

        String[] temp = n.split("\\s+");
        String name = temp[1];
        for (int i = 2; i < temp.length; ++i) {
            name += " " + temp[i];
        }
        name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        return name;
    }

    public void printNLPResults() {
        String line = textF.getText();
        Map<String, Double> result = nlpSearch.searchDataset(line);
        resultP.removeAll();
        resultP.setLayout(new GridLayout(0, 2));
        for (Map.Entry<String, Double> t : result.entrySet()) {
            JLabel labelK = new JLabel();
            labelK.addMouseListener(new LabelControl(extractName(t.getKey()), line));
            labelK.setText(t.getKey());
            resultP.add(labelK);
            JLabel labelV = new JLabel();
            labelV.setText(t.getValue().toString());
            resultP.add(labelV);

        }
        frame.revalidate();
        frame.repaint();
    }

    public void printOntologyResults() {
        String line = textF.getText();
        List<String> result = ontSearch.search(line);
        resultP.removeAll();
        resultP.setLayout(new GridLayout(0, 1));
        for (String t : result) {
            JLabel label = new JLabel();
            label.addMouseListener(new LabelControl(t, line));
            label.setText(t);
            resultP.add(label);
        }
        frame.revalidate();
        frame.repaint();
    }

    private class TextInput implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!textF.getText().equals("")) {
                if (nlpB.isSelected()) {
                    printNLPResults();
                } else {
                    printOntologyResults();
                }
            }
        }
    }

    private class ArticleInput implements ActionListener {

        private String name;

        public ArticleInput(String n) {
            name = n;
        }

        public void setName(String n) {
            name = n;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!searchTextF.getText().equals("")) {
                printArticleRank(name, searchTextF.getText());
            }
        }
    }

    private class ReturnButton implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            frame.setTitle("Search Engine");
            frame.remove(searchPanel);
            frame.add(frontP);
            frame.revalidate();
            frame.repaint();
        }
    }

    private class FrameControl extends WindowAdapter {

        @Override
        public void windowClosing(WindowEvent we) {
            nlpSearch.close();
        }

    }

    private class LabelControl extends MouseAdapter {

        private String articleName;
        private String line;

        public LabelControl(String articleName, String line) {

            this.articleName = articleName;
            this.line = line;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            printArticle(articleName, line);
        }

    }

}
