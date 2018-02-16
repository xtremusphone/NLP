package nlp;

import java.util.ArrayList;
import java.util.HashMap;

public class WordData {
    
    public int total_word_frequency = 0;
    public int word_document_frequency = 0;
    public ArrayList<String> pos_tag_list;
    public HashMap<Integer,Integer> pos_tag_frequency;
    
    public WordData(){
        pos_tag_list = new ArrayList<>();
        pos_tag_frequency = new HashMap<>();
    }
}
