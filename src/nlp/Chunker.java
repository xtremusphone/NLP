/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author User
 */
public class Chunker {
    
    public Chunker(){
        
    }
    
    public List<String> getNouns(HashMap<String,String> tagged,ArrayList<String> sentence){
        ArrayList<String> nouns = new ArrayList<>();
        String chunking = "";
        for(String x:sentence){
            if(tagged.get(x).equals("NNP")){
                chunking += x + " ";
                //continue;
            }
            else if(!chunking.equals("")){
                nouns.add(chunking.substring(0,chunking.length() - 1));
                chunking = "";
            }
        }
        if(!chunking.equals(""))
            nouns.add(chunking.substring(0,chunking.length() - 1));
        return nouns;
    }
    
    public List<String> getNounPhrase(HashMap<String,String> tagged,ArrayList<String> sentence){
        ArrayList<String> nounphrase = new ArrayList<>();
        String chunking = "";
        for(String word:sentence){
            if(chunking.equals("")){
                if(tagged.get(word).equals("DT")){
                    chunking += "the";
                    continue;
                }
            }
            if(tagged.get(word).equals("JJ") || tagged.get(word).equals("NN")){
                chunking += word + " ";
            }
            else{
                nounphrase.add(chunking.substring(0, chunking.length() - 1));
                chunking = "";
            }
        }
        if(!chunking.equals("")){
            nounphrase.add(chunking.substring(0,chunking.length() - 1));
        }
        return nounphrase;
    }
}
