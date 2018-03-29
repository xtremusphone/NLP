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
<<<<<<< HEAD
                else if(convertToRoot(tokenized.get(i),input) != null){
                    mapped.put(tokenized.get(i), convertToRoot(tokenized.get(i),input));
=======
                else if(isVerb(tokenized.get(i)) != null){
                    mapped.put(tokenized.get(i), isVerb(tokenized.get(i)));
>>>>>>> 0915d0853b8816df40be8a2a1d911ab1d529abe6
                }
                else{
                    mapped.put(tokenized.get(i), "NN");
                }
<<<<<<< HEAD
                continue;
            }
            
            if(isColour(tokenized.get(i),tokenized)){
                mapped.put(tokenized.get(i), "JJ");
=======
>>>>>>> 0915d0853b8816df40be8a2a1d911ab1d529abe6
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
    
    public String convertToRoot(String word,String sentence){
        //words that ends with ies
        if(word.endsWith("ies") && !word.equalsIgnoreCase("dies") && !word.equalsIgnoreCase("ties")){
            if(crp.getListofWords().contains(word.substring(0,word.indexOf("ies")))){
                if(crp.getListofWordsData().get(word.substring(0,word.indexOf("ies"))).pos_tag_list.contains("VB")){
                    return "VBG";
                }
            }
        }
        else{
            if(word.equalsIgnoreCase("dies") || word.equalsIgnoreCase("ties")){
                return "VBG";
            }
        }
        
        if(word.endsWith("it") && crp.getListofWordsData().containsKey(word.substring(0,word.indexOf("it")) + "e")){
            return "VBD";
        }
        
        if(word.equalsIgnoreCase("thought") || word.equalsIgnoreCase("sought") || word.equalsIgnoreCase("bought") || word.equalsIgnoreCase("brought") || word.equalsIgnoreCase("fought")){
            return "VBD";
        }

        if(word.equalsIgnoreCase("think") || word.equalsIgnoreCase("seek") || word.equalsIgnoreCase("buy") || word.equalsIgnoreCase("bring") || word.equalsIgnoreCase("fight")){
            return "VB";
        }
        
        if(word.endsWith("ang") || word.endsWith("rang") || word.endsWith("tang") || word.endsWith("wang") && isConsonant(word,word.indexOf("ang") - 1)){
            return "VBD";
        }
        
        if(word.equalsIgnoreCase("caught") || word.equalsIgnoreCase("taught")){
            return "VBD";
        }
        
        
        
        return null;
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
        
        String consonant = "aeiou";
        
        return max_tag;
    }
    
<<<<<<< HEAD
    public boolean isConsonant(String word,int index){
        String vowels = "aeiou";
        for(char x:vowels.toCharArray()){
            if((word.charAt(index) + "").equalsIgnoreCase(x + "")){
                return false;
            }
        }
        return true;
    }
    
    public boolean isColour(String word,List<String> sentence){
        String[] colours = {"red","blue","green","yellow","orange","purple","black","gray","white","rose","gold"};
        if(sentence.contains("Colour") || sentence.contains("colour") || sentence.contains("color") || 
                sentence.contains("color") || sentence.contains("colored") || sentence.contains("Colored") || sentence.contains("coloured") || sentence.contains("Coloured")){
            for(String cl:colours){
                if(word.equalsIgnoreCase(cl))
                    return true;
            }
        }
        
=======
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
        
        if(word.endsWith("iting") || word.endsWith("ating") || word.endsWith("outing") || word.endsWith("uoting")){
            return "VBG";
        }
        
        if(word.endsWith("eating") && !word.equalsIgnoreCase("eating"))
            return "VBG";
        
        if(word.endsWith("ting")){
            return "VBG";
        }
        
        if(word.endsWith("nning") || word.endsWith("uning") || word.endsWith("oning") || word.endsWith("ining") || word.endsWith("caning")){
            return "VBG";
        }
        
        if(!word.endsWith("ening") && word.endsWith("ning")){
            return "VBG";
        }
        
        if(word.endsWith("aking") || word.endsWith("iving") || word.endsWith("dging") || word.endsWith("gling") || word.endsWith("tling") || word.endsWith("ching") || word.endsWith("nging") || word.endsWith("bling") || word.endsWith("kling")){
            return "VBD";
        }
        
        if(word.endsWith("ing"))
            return "VBD";
        
        if(!word.endsWith("dd") || !word.endsWith("rd") || !word.endsWith("ld") || !word.endsWith("nd") || !word.endsWith("ad") || !word.endsWith("ed") || !word.endsWith("id") || !word.endsWith("od") ||!word.endsWith("ud") && word.endsWith("d")){
            return "VBD";
        }
        
        if(word.endsWith("gned") || word.endsWith("yed") || word.endsWith("ned") || word.endsWith("hed") || word.endsWith("led") || word.endsWith("bed") || word.endsWith("cked") || word.endsWith("rked") || word.endsWith("ssed") || word.endsWith("rreded") || word.endsWith("med")
                || word.endsWith("ured") || word.endsWith("ied") || word.endsWith("red") || word.endsWith("tted") || word.endsWith("dded") || word.endsWith("gned"))
            return "VBD";
        
        return null; 
    }
    
    private boolean isVowel(char character){
        String vowel = "aeiou";
        for(char ch:vowel.toCharArray()){
            if(character == ch)
                return true;
        }
>>>>>>> 0915d0853b8816df40be8a2a1d911ab1d529abe6
        return false;
    }
}
