package nlp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ViterbiAlgorithm {
    
    private String user_input = "";
    //private CorpusLoader crp;
    private Con2000Loader crp;
    
    public ViterbiAlgorithm(){
       // crp = new CorpusLoader();
       if(loadCorpus() != null){
            crp = loadCorpus();
        }
        else{
            crp = new Con2000Loader();
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
                else if(convertToRoot(tokenized.get(i),input) != null){
                    mapped.put(tokenized.get(i), convertToRoot(tokenized.get(i),input));
                }
                else{
                    mapped.put(tokenized.get(i), "NN");
                }
                continue;
            }
            
            if(isColour(tokenized.get(i),tokenized)){
                mapped.put(tokenized.get(i), "JJ");
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
        
        return false;
    }
}
