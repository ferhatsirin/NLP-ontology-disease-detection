/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.project;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.ProfileRegistry;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.util.FileManager;

/**
 * Ontoloji uygulaması arama sınıfı.
 * Hastalık belirtisi ve nedenlerinden yola çıkarak hastalık tespit edilir.
 * @author ferhat
 */
public class OntologySearch {

    private final OntModel model;
    private String prefix;

    public OntologySearch(String location, String url) {

        model = ModelFactory.createOntologyModel(ProfileRegistry.OWL_LANG);

        InputStream in = FileManager.get().open(location);
        model.read(in, url);

        prefix = "PREFIX ont: <http://www.semanticweb.org/ferhat/ontologies/>";
        prefix += "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>";
        prefix += "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>";
        prefix += "PREFIX owl:<http://www.w3.org/2002/07/owl#>";
    }

    /**
     * Varlık bilimi üzerinde arama yapılır.
     * @param str aranmak istenilen cümle
     * @return Hastalık listesi
     */
    public List<String> search(String str) {

        List<String> list = searchWords(str);
        Map<String, String> map = findTypes(list);

        List<String> results =new ArrayList<>();
        for (Map.Entry e : map.entrySet()) {
            String query = null;
            if (e.getValue().equals("Nedenler")) {
                query = prefix + "SELECT ?x WHERE {?x ont:kaynaklanır ont:" + e.getKey() + " .}";
            } else if (e.getValue().equals("Belirtiler")) {
                query = prefix + "SELECT ?x WHERE { ?x ont:gösterir ont:" + e.getKey() + " .}";
            } else if (e.getValue().equals("Hastalıklar")) {
                query = prefix + "SELECT ?x WHERE { ?x ont:benzerdir ont:" + e.getKey() + " .}";
            }
            if (query != null) {
                ResultSet set = searchQuery(query);
                while (set.hasNext()) {
                    QuerySolution querySolution = set.next();
                    Resource resource = querySolution.getResource("x");
                    if (resource.getNameSpace().equals("http://www.semanticweb.org/ferhat/ontologies/")) {
                        results.add(resource.getLocalName());
                    }
                }
            }
        }
        return results;
    }

    private ResultSet searchQuery(String query) {

        QueryExecution qe = QueryExecutionFactory.create(query, model);
        ResultSet rs = qe.execSelect();
        return rs;
    }

    private List<String> searchWords(String s) {

        String[] t = s.split(" ");
        List<String> list = new ArrayList<>();
        for (int i = 0; i < t.length; ++i) {
            list.add(t[i].substring(0, 1).toUpperCase() + t[i].substring(1).toLowerCase());
        }
        if (1 < t.length) {
            String res;
            int size = list.size();
            for (int i = 0; i < size; ++i) {
                res = list.get(i);
                for (int j = i + 1; j < size; ++j) {
                    res += list.get(j);
                    list.add(res);
                }
            }
        }

        return list;
    }

    private Map<String, String> findTypes(List<String> list) {

        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < list.size(); ++i) {
            ResultSet set = searchQuery(prefix + "SELECT ?x WHERE { ont:" + list.get(i) + " rdf:type ?x .}");
            while (set.hasNext()) {
                QuerySolution query = set.next();
                Resource resource = query.getResource("x");
                if (resource.getNameSpace().equals("http://www.semanticweb.org/ferhat/ontologies/")) {
                    map.put(list.get(i), resource.getLocalName());
                }
            }
        }
        return map;
    }

}
