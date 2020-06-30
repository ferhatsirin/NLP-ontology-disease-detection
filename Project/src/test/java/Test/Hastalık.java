/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

/**
 *
 * @author ferhat
 */
public class Hastalık {
    
    private String name;
    private String belirti1;
    private String belirti2;
    private String neden1;
    private String neden2;
    
    public Hastalık(String name,String b1,String b2,String n1, String n2){
    
        this.name =name;
        belirti1 =b1;
        belirti2 =b2;
        neden1 =n1;
        neden2 =n2;
    }

    public String getName() {
        return name;
    }

    public String getBelirti1() {
        return belirti1;
    }

    public String getBelirti2() {
        return belirti2;
    }

    public String getNeden1() {
        return neden1;
    }

    public String getNeden2() {
        return neden2;
    }
    
    
}
