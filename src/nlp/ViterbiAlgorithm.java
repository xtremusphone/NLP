package nlp;

import java.util.HashMap;
import java.util.List;

public class ViterbiAlgorithm {
    
    private String user_input = "";
    private CorpusLoader crp;
    
    public ViterbiAlgorithm(){
        crp = new CorpusLoader();
    }
    
    public ViterbiAlgorithm(String input){
        user_input = input;
        crp = new CorpusLoader();
    }
    
    public HashMap<String,String> getPOSTagging(String input){
        HashMap<String,String> mapped = new HashMap<>();
        
        WordTokenizer tokens = new WordTokenizer();
        List<String> tokenized = tokens.tokenizer(input);
        for(int i = 0; i < tokenized.size();i++){
            if(!crp.getListofWords().contains(tokenized.get(i).toLowerCase())){
                mapped.put(tokenized.get(i), "unk");
            }
            String previous = "";
            if(i == 0){
                previous = "pad";
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
            for(int val:crp.getListofWordsData().get(tokenized.get(i).toLowerCase()).pos_tag_frequency.values()){
                total_word_frequency += val;
            }
            
            if(i == 0){
                for(String pos:crp.getListofWordsData().get(tokenized.get(i).toLowerCase()).pos_tag_list){
                    double pos_probability = (double)crp.getListofWordsData().get(tokenized.get(i).toLowerCase()).pos_tag_frequency.get(pos) / (double)total_word_frequency;
     
                    if(pos_probability > pos_max_prob){
                        pos_max_prob = pos_probability;
                        pos_max = pos;
                    }
                }
            }
            else{
                for(String pos:crp.getListofWordsData().get(tokenized.get(i).toLowerCase()).pos_tag_list){
                    double pos_probability = (double)crp.getListofWordsData().get(tokenized.get(i).toLowerCase()).pos_tag_frequency.get(pos) / (double)total_word_frequency;
                    double transition_probability;
                    if(crp.getPOSTransFreq().get(previous + pos) == null)
                        transition_probability = 0;
                    else{
                        transition_probability = (double)crp.getPOSTransFreq().get(previous + pos) / (double)total_transition_frequency;
                    }
                        
                    double final_pos_probability = pos_probability * transition_probability;
                 
                    if(final_pos_probability > pos_max_prob){
                        pos_max_prob = final_pos_probability;
                        pos_max = pos;
                    }
                }
            }
            mapped.put(tokenized.get(i), pos_max);
        }
        handleUNK(mapped);
        return mapped;
    }
    
    private HashMap<String,String> handleUNK(HashMap<String,String> pos){
        //not yet done.
        return null;
    }
}
