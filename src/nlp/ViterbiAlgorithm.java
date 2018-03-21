package nlp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ViterbiAlgorithm {
    
    private String user_input = "";
    //private CorpusLoader crp;
    private Con2000Loader crp;
    private ArrayList<String> verb_list;
    
    public ViterbiAlgorithm(){
       // crp = new CorpusLoader();
       if(loadCorpus() != null){
            crp = loadCorpus();
        }
        else{
            crp = new Con2000Loader();
        }
        
        if(loadVerb() != null){
            verb_list = loadVerb();
        }
 
    }
    
    public ViterbiAlgorithm(String input){
        user_input = input;
        //crp = new CorpusLoader();
        //crp = new Con2000Loader();
        if(loadCorpus() != null){
            crp = loadCorpus();
        }
        else{
            crp = new Con2000Loader();
        }
    }
    
    public ArrayList<String> loadVerb(){
        ArrayList<String> temp = new ArrayList<>();
        try{
            Scanner scn = new Scanner(new File("verb.txt"));
            while(scn.hasNext()){
                temp.add(scn.next());
            }
        }
        catch(IOException e){
            System.out.println(e);
            return null;
        }
        return temp;
    }
    
    public Con2000Loader loadCorpus(){
        try{
            ObjectInputStream stream = new ObjectInputStream(new FileInputStream("Model.slz"));
                try {
                    return (Con2000Loader) stream.readObject();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ViterbiAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
        catch(IOException e){
            System.out.println(e);
        }
        return null;
    }
    
    public HashMap<String,String> getPOSTagging(String input){
        HashMap<String,String> mapped = new HashMap<>();
        
        WordTokenizer tokens = new WordTokenizer();
        List<String> tokenized = tokens.tokenizer(input);
        for(int i = 0; i < tokenized.size();i++){
            if(!crp.getListofWords().contains(tokenized.get(i).toLowerCase())){
                if(!tokenized.get(i).equals(tokenized.get(i).toLowerCase())){
                    mapped.put(tokenized.get(i), "NNP");
                }
                else if(isVerb(tokenized.get(i)) != null){
                    mapped.put(tokenized.get(i), isVerb(tokenized.get(i)));
                }
                else{
                    mapped.put(tokenized.get(i), "NN");
                }
                continue;
            }
            
            String previous = "";
            if(i == 0){
                previous = "<START>";
            }
            else{
                previous = mapped.get(tokenized.get(i - 1));
            }
            
            int total_transition_frequency = 0;
            for(int val:crp.getPOSTransFreq().values()){
                total_transition_frequency += val;
            }
            
            int total_word_frequency = 0;
                
            double pos_max_prob = 0;
            String pos_max = "";
            
            if(i == 0){
                for(String pos:crp.getListofWordsData().get(tokenized.get(i).toLowerCase()).pos_tag_list){
                    double pos_probability = (double)crp.getListofWordsData().get(tokenized.get(i).toLowerCase()).pos_tag_frequency.get(pos) / (double)crp.getListofWordsData().get(tokenized.get(i).toLowerCase()).total_word_frequency;
                    if(pos_probability > pos_max_prob){
                        pos_max_prob = pos_probability;
                        pos_max = pos;
                    }
                }
            }
            else{
                for(String pos:crp.getListofWordsData().get(tokenized.get(i).toLowerCase()).pos_tag_list){
                    
                    double pos_probability = (double)crp.getListofWordsData().get(tokenized.get(i).toLowerCase()).pos_tag_frequency.get(pos) / (double)crp.getListofWordsData().get(tokenized.get(i).toLowerCase()).total_word_frequency;
                    double transition_probability;
                    
                    if(crp.getPOSTransFreq().get(previous + pos) == null)
                        transition_probability = 0;
                    else{
                        transition_probability = (double)crp.getPOSTransFreq().get(previous + pos) / (double)total_transition_frequency;
                                //(double)crp.getPOSTotalFreq().get(previous);
                    }
                        
                    double final_pos_probability = pos_probability * transition_probability;
                 
                    if(final_pos_probability > pos_max_prob){
                        pos_max_prob = final_pos_probability;
                        pos_max = pos;
                    }
                }
                if(pos_max.equals("")){
                    for(String pos:crp.getListofWordsData().get(tokenized.get(i).toLowerCase()).pos_tag_list){
                        double pos_probability = (double)crp.getListofWordsData().get(tokenized.get(i).toLowerCase()).pos_tag_frequency.get(pos) / (double)crp.getListofWordsData().get(tokenized.get(i).toLowerCase()).total_word_frequency;

                        if(pos_probability > pos_max_prob){
                            pos_max_prob = pos_probability;
                            pos_max = pos;
                        }
                    }
                }
            }
            mapped.put(tokenized.get(i), pos_max);
        }
        
        return mapped;
    }
    
    public String getHighestTransition(String previous_tag){
        int max_score = 0;
        String max_tag = "";
        for(String tag:crp.getListofPOSTags()){
            if(crp.getPOSTransFreq().keySet().contains(previous_tag + tag) && crp.getPOSTransFreq().get(previous_tag + tag) > max_score){
                max_score = crp.getPOSTransFreq().get(previous_tag + tag);
                max_tag = tag;
            }
        }
        return max_tag;
    }
    
    public String isVerb(String word){
        if(word.endsWith("ies")){
            if(word.equalsIgnoreCase("dies") || word.equalsIgnoreCase("tries")){
                return "VBG";
            }
            else{
                for(String verbs:verb_list){
                    if(verbs.contains(word.substring(0, word.indexOf("ies")) + "y")){
                        return "VBG";
                    }
                }
            }
        }
        
        if(word.endsWith("us")){
            for(String verbs:verb_list){
                if(verbs.contains(word))
                    return "VBG";
            }
        }
        
        if(word.endsWith("es")){
            int index = word.indexOf("es");
            if(word.length() > 4 && !isVowel(word.charAt(index - 1)) && isVowel(word.charAt(index - 2))){
                return "VBG";
            }
        }
        
        if(word.equalsIgnoreCase("bit"))
            return "VBD";
        
        if(word.equalsIgnoreCase("thought") || word.equalsIgnoreCase("fought") || word.equalsIgnoreCase("sought") || word.equalsIgnoreCase("bought") || word.equalsIgnoreCase("brought"))
            return "VBD";
        
        if(word.endsWith("ang")){
            for(String verbs:verb_list){
                if(verbs.equalsIgnoreCase(word.replace("ang", "ing")))
                    return "VBG";
            }
        }
        
        if(word.equalsIgnoreCase("caught") || word.equalsIgnoreCase("taught"))
            return "VBG";
        
        if(word.endsWith("wn")){
            if(word.equalsIgnoreCase("drown") || word.equalsIgnoreCase("frown") || word.equalsIgnoreCase("disown"))
                return "VBD";
            else if(word.equalsIgnoreCase("clown") || word.endsWith("crown"))
                return "NN";
            char list[] = {'r','l','h','n','s'};
            ArrayList<Character> lst = new ArrayList<>(Arrays.asList());
            if(word.length() >= 4 && isVowel(word.charAt(word.indexOf("wn") - 1)) && lst.contains(word.charAt(word.indexOf("wn") - 2))){
                return "VBN";
            }
        }
        
        if(word.equalsIgnoreCase("blew") || word.equalsIgnoreCase("flew") || word.equalsIgnoreCase("drew")){
            return "VBD";
        }
        
        if(word.equalsIgnoreCase("accept")){
            return "VB";
        }
        else if(word.endsWith("ept")){
            for(String verbs:verb_list){
                if(verbs.equalsIgnoreCase(word.replace("ept", "eep")))
                    return "VBD";
            }
        }
        
        
        return null;
    }
    
    private boolean isVowel(char character){
        String vowel = "aeiou";
        for(char ch:vowel.toCharArray()){
            if(character == ch)
                return true;
        }
        return false;
    }
}
