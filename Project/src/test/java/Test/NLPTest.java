/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import com.mycompany.project.NLPSearch;
import java.util.List;
import java.util.Map;

/**
 * Doğal dil işleme test algoritması
 * @author ferhat
 */
public class NLPTest {

    public static void main(String[] args) {

        List<Hastalık> list = ExcelReader.readHastalık("/home/ferhat/Desktop/dataset/test.xlsx");
        double count = 0;
        NLPSearch nlpSearch = new NLPSearch( "/home/ferhat/PycharmProjects/tensor/SearchModule.py");
        for (int i = 0; i < list.size(); ++i) {
            Hastalık h = list.get(i);
            if (h.getBelirti1() != null) {
                Map<String, Double> result = nlpSearch.searchDataset(h.getBelirti1());
                count += checkResult(result, h);
            }
            if (h.getBelirti2() != null) {
                Map<String, Double> result = nlpSearch.searchDataset(h.getBelirti2());
                count += checkResult(result, h);
            }
            if (h.getNeden1() != null) {
                Map<String, Double> result = nlpSearch.searchDataset(h.getNeden1());
                count += checkResult(result, h);
            }
            if (h.getNeden2() != null) {
                Map<String, Double> result = nlpSearch.searchDataset(h.getNeden2());
                count += checkResult(result, h);
            }
        }

        System.out.println("Count is : " + count);
        nlpSearch.close();

    }

    public static double checkResult(Map<String, Double> result, Hastalık h) {

        int k = 0;
        for (Map.Entry<String, Double> t : result.entrySet()) {
            if (extractName(t.getKey()).equals(h.getName())) {
                return 0.25;
            }
            if (k == 3) {
                break;
            }
            ++k;
        }
        return 0.0;
    }

    public static String extractName(String n) {

        String[] temp = n.split("\\s+");
        String name = temp[1];
        for (int i = 2; i < temp.length; ++i) {
            name += " " + temp[i];
        }

        return name.toLowerCase();
    }

}
