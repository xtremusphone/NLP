package nlp;

//This class is to extract data from Brown Corpus
/*
There will be some changes needed to be addressed,
1. for instance for word data, the pos tag list frequency should be changed to hashmap.
2. Any usage of hashmap should be using String as key as lookup in Dictionary can be expensive
3. Another addition needed to be addressed is the n-gram of words and pos-tags should be extracted along the way
4. Optimization should be carry-out as the total run needed for extracting took aroud 1 ~ 1.5 minutes which is too long
5. Serialization of objects is needed to remove the need of running through all the steps which can take a long time
6. This class will be used for another class (MaxEnt Post Tagging).
*/
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class CorpusLoader {
    
    /*
    TODO: serialize obj to make it faster to load initial models.
    */
    
    private final String CORPUS_PATH = "brown";
    private File[] documents;
    
    //Core data needed for POSTagger class later on;
    private ArrayList<String> words;
    //TODO: change Integer to String, to reduce lookup into words list which could be expensive
    private HashMap<String,WordData> word_info;
    private ArrayList<String> listof_postags;
    private HashMap<String,Integer> pos_transition_frequency;
    private HashMap<String,Integer> pos_total_frequency;
    
    public CorpusLoader(){
        words = new ArrayList<>();
        listof_postags = new ArrayList<>();
        word_info = new HashMap<>();
        pos_transition_frequency = new HashMap<>();
        pos_total_frequency = new HashMap<>();
        loadCorpus();
    }
    
    private void loadCorpus(){
        File path = new File(CORPUS_PATH);
        documents = path.listFiles();
        
        System.out.println("Loading Brown Corpus and extracting information...");
        Scanner scn;
        for(File x: documents){
            System.out.println("Extracting from: " + x.getName() + " current total word: " + words.size() + " current bigram size: " + pos_transition_frequency.size());
            String previous = "pad";
            try{
                scn = new Scanner(x);
                while(scn.hasNext()){
                    String composite = scn.next();
                    
                    //Splitting word from it's POS Tag
                    String word = composite.substring(0,composite.indexOf("/"));
                    //splitting string to get the POS Tagging
                    String pos_tag = composite.substring(composite.indexOf("/") + 1,composite.length());
                    
                    if(pos_transition_frequency.containsKey(previous + pos_tag) == true){
                        int val = pos_transition_frequency.get(previous + pos_tag) + 1;
                        pos_transition_frequency.put(previous + pos_tag.toLowerCase(), val);
                    }
                    else{
                        pos_transition_frequency.put(previous + pos_tag.toLowerCase(), 1);
                    }
                    
                    previous = pos_tag.toLowerCase();
                    
                    //check if the word is already added into list of words
                    if(!words.contains(word.toLowerCase())){
                        words.add(word.toLowerCase());
                    }
                    
                    //get the list of tags available in the corpus
                    if(!listof_postags.contains(pos_tag.toLowerCase())){
                        listof_postags.add(pos_tag.toLowerCase());
                    }
                    
                    String index = previous.toLowerCase() + pos_tag.toLowerCase();
                    
                    //check if the word data exist in the list already of not, if not then add new word data
                    //TODO: merge the code with add to new dictionary since it is redundant and can improve performance actually
                    if(word_info.get(word.toLowerCase()) == null){
                        WordData temp = new WordData();
                        temp.total_word_frequency += 1;
                        temp.pos_tag_list.add(pos_tag.toLowerCase());
                        temp.pos_tag_frequency.put(pos_tag.toLowerCase(), 1);
                        word_info.put(word.toLowerCase(), temp);
                    }
                    else{
                        //create new obj of word info and populate the data
                        WordData temp = word_info.get(word.toLowerCase());
                        temp.total_word_frequency += 1;
                        if(!temp.pos_tag_list.contains(pos_tag.toLowerCase())){
                            temp.pos_tag_list.add(pos_tag.toLowerCase());
                            temp.pos_tag_frequency.put(pos_tag.toLowerCase(), 1);
                        }
                        else{
                            int new_frequency = temp.pos_tag_frequency.get(pos_tag.toLowerCase()) + 1;
                            temp.pos_tag_frequency.put(pos_tag.toLowerCase(), new_frequency);
                        }
                    }
                }
            }
            catch(IOException e){
                System.out.println(e);
            }
        }
        //vocabCleanup();
        saveDictionary();
    }
    
    /*
    This method is just to remove unique word which have lower than frequency set
    Not crucial in the main class itself
    */
    private void vocabCleanup(){
        for(int i = 0;i < words.size();i++){
            WordData tmp = word_info.get(i);
            if(tmp.total_word_frequency <= 5){
                System.out.println("Removing : " + words.get(i) + " with frequency of "  + word_info.get(i).total_word_frequency + " with POS tag of " + word_info.get(i).pos_tag_list.toString());
                words.remove(i);
                word_info.remove(i);
            }
        }
        System.out.println("Total word after clean up: " + words.size());
    }
      
    /*
    Save list of words into text file for faster loading later on
    TODO: serialize obj of word info, bigram and also pos list for faster loading.
    */
    public void saveDictionary(){
        try{
            PrintWriter wrt = new PrintWriter(new File("Dictionary.txt"));
            for(String x:words){
                wrt.println(x);
            }
            wrt.close();
        }
        catch(IOException e){
            System.out.println(e);
        }
    }
    
    /*
    Standard getter method for all of the infos.
    */
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
}
