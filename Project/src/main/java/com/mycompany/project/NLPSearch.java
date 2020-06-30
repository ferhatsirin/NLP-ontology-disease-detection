/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Doğal dil işleme uygulaması
 * Hastalığın belirtileri ve nedenlerinden yola çıkarak hastalık tespit edilmeye çalışılır.
 * Doğal dil işleme algoritması olarak Paragraph2Vec ve Word2Vec algoritmaları kullanıldı.
 * @author ferhat
 */
public class NLPSearch {

    private Process process;
    private static final String STOP = "STOP";
    private static final String DATASET = "DATASET";
    private static final String ARTICLE = "ARTICLE";
    private final String location;

    public NLPSearch(String codeLocation) {
        location =codeLocation;
        createProcess();
    }

    public void close() {
        OutputStream outS = process.getOutputStream();
        PrintStream st = new PrintStream(outS);
        st.println(STOP);
        st.flush();
    }

    private void createProcess() {
        ProcessBuilder builder = new ProcessBuilder("python3",location);
        try {
            process = builder.start();
        } catch (IOException ex) {
            System.err.println("Exception raised while creating python process");
        }
    }

    /**
     * Veri kümesi içinde, doğal dil işleme algoritmasına uygun arama yapar
     * @param line aranmak istenilen cümle
     * @return bulunan makaleler
     */
    public Map<String, Double> searchDataset(String line) {
        OutputStream outS = process.getOutputStream();
        PrintStream stream = new PrintStream(outS);
        stream.println(DATASET);
        stream.println(line);
        stream.flush();

        Map articles = readArticles();

        return articles;
    }

    /**
     * Makale üzerinde, doğal dil işleme algoritmasına göre arama yapar.
     * @param article makale adı
     * @param line aranmak istenilen cümle
     * @return Makaledeki paragraf numarası ile benzerlik oranı 
     */
    public Map<String, Double> searchArticle(String article, String line) {

        OutputStream outS = process.getOutputStream();
        PrintStream stream = new PrintStream(outS);
        stream.println(ARTICLE);
        stream.println(article);
        stream.println(line);
        stream.flush();

        return readArticles();
    }

    private Map<String, Double> readArticles() {
        Map<String, Double> list = new LinkedHashMap<>();
        InputStream inS = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inS));
        try {
            int count = Integer.parseInt(reader.readLine());
            for (int i = 0; i < count; ++i) {
                String article = reader.readLine();
                Double similarity = Double.parseDouble(reader.readLine());
                list.put(article, similarity);
            }
        } catch (IOException ex) {
            Logger.getLogger(SearchEngine.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

}
