package nlp;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Con2000Loader implements Serializable {
    
    private final String CORPUS_PATH = "train.txt";
    private ArrayList<String> words;
    
    private HashMap<String,WordData> word_info;
    private ArrayList<String> listof_postags;
    private HashMap<String,Integer> pos_transition_frequency;
    private HashMap<String,Integer> pos_total_frequency;
    private String pos_map = "";
    
    public Con2000Loader(){
        words = new ArrayList<>();
        word_info = new HashMap<>();
        listof_postags = new ArrayList<>();
        pos_transition_frequency = new HashMap<>();
        pos_total_frequency = new HashMap<>();
        loadCorpus();
    }
    
    public void loadCorpus(){
        try {
            Scanner scn = new Scanner(new File(CORPUS_PATH));
            
            String previous_tag = "<START>";
            while(scn.hasNext()){
                String word = scn.next();
                String pos_tag = scn.next();
                String chunk_tag = scn.next();
                
                if(!words.contains(word.toLowerCase())){
                    words.add(word.toLowerCase());
                    WordData tmp = new WordData();
                    tmp.total_word_frequency++;
                    tmp.pos_tag_list.add(pos_tag);
                    tmp.pos_tag_frequency.put(pos_tag, 1);
                    word_info.put(word.toLowerCase(), tmp);
                }
                else{
                    WordData tmp = word_info.get(word.toLowerCase());
                    tmp.total_word_frequency++;
                    if(tmp.pos_tag_list.contains(pos_tag) == false){
                        tmp.pos_tag_list.add(pos_tag);
                        tmp.pos_tag_frequency.put(pos_tag, 1);
                    }
                    else{
                        int new_frequency = tmp.pos_tag_frequency.get(pos_tag) + 1;
                        tmp.pos_tag_frequency.put(pos_tag, new_frequency);
                    }
                }
                
                if(!listof_postags.contains(pos_tag)){
                    listof_postags.add(pos_tag);
                }
                
                if(pos_total_frequency.containsKey(pos_tag) == false){
                    pos_total_frequency.put(previous_tag, 1);
                }
                else{
                    int new_frequency = pos_total_frequency.get(pos_tag) + 1;
                    pos_total_frequency.put(previous_tag, new_frequency);
                }
                
                if(pos_transition_frequency.containsKey(previous_tag + pos_tag) == false){
                    pos_transition_frequency.put(previous_tag + pos_tag, 1);
                }
                else{
                    int new_frequency = pos_transition_frequency.get(previous_tag + pos_tag) + 1;
                    pos_transition_frequency.put(previous_tag + pos_tag, new_frequency);
                }
                
                previous_tag = pos_tag;
                
                //pos_map += pos_tag + " ";
            }
            
            //System.out.println(pos_map);
        } catch(IOException e) {
            System.out.println(e);
        }
    }
    
    public ArrayList<String> getListofPOSTags(){
        return listof_postags;
    }
    
    public ArrayList<String> getListofWords(){
        return words;
    }
    
    public HashMap<String,WordData> getListofWordsData(){
        return word_info;
    }
    
    public HashMap<String,Integer> getPOSTransFreq(){
        return pos_transition_frequency;
    }
    
    public HashMap<String,Integer> getPOSTotalFreq(){
        return pos_total_frequency;
    }
}
