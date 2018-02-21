package nlp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class WordData implements Serializable{
    
    public int total_word_frequency = 0;
    public int word_document_frequency = 0;
    public ArrayList<String> pos_tag_list;
    public HashMap<String,Integer> pos_tag_frequency;
    
    /*
    TODO: Need to change pos_tag_frequency to cater for TF-IDF implementation later on.
    Cons: larger list will be made, but who da heck cares man.
    */
    public WordData(){
        pos_tag_list = new ArrayList<>();
        pos_tag_frequency = new HashMap<>();
    }
}
