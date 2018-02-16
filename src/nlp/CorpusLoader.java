package nlp;

//This class is to specifically extract data from Brown Corpus

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class CorpusLoader {
    
    private final String CORPUS_PATH = "brown";
    private File[] documents;
    private ArrayList<String> words;
    private HashMap<Integer,WordData> word_info;
    private ArrayList<String> listof_postags;
    
    public CorpusLoader(){
        words = new ArrayList<>();
        listof_postags = new ArrayList<>();
        word_info = new HashMap<>();
        loadCorpus();
    }
    
    private void loadCorpus(){
        File path = new File(CORPUS_PATH);
        documents = path.listFiles();
        
        System.out.println("Loading Brown Corpus and extracting information...");
        Scanner scn;
        for(File x: documents){
            System.out.println("Extracting from: " + x.getName() + " current total word: " + words.size());
            try{
                scn = new Scanner(x);
                while(scn.hasNext()){
                    //split pos-tag and word
                    String composite = scn.next();
                    String word = composite.substring(0,composite.indexOf("/"));
                    
                    if(!words.contains(word.toLowerCase())){
                        words.add(word.toLowerCase());
                    }
                    
                    String pos_tag = composite.substring(composite.indexOf("/") + 1,composite.length());
                    
                    if(!listof_postags.contains(pos_tag.toLowerCase())){
                        listof_postags.add(pos_tag.toLowerCase());
                    }

                    if(word_info.get(words.indexOf(word.toLowerCase())) == null){
                        WordData temp = new WordData();
                        temp.total_word_frequency += 1;
                        temp.pos_tag_list.add(pos_tag.toLowerCase());
                        temp.pos_tag_frequency.put(temp.pos_tag_list.indexOf(pos_tag.toLowerCase()), 1);
                        word_info.put(words.indexOf(word.toLowerCase()), temp);
                    }
                    else{
                        WordData temp = word_info.get(words.indexOf(word.toLowerCase()));
                        temp.total_word_frequency += 1;
                        if(!temp.pos_tag_list.contains(pos_tag.toLowerCase())){
                            temp.pos_tag_list.add(pos_tag.toLowerCase());
                            temp.pos_tag_frequency.put(temp.pos_tag_list.indexOf(pos_tag.toLowerCase()), 1);
                        }
                        else{
                            int new_frequency = temp.pos_tag_frequency.get(temp.pos_tag_list.indexOf(pos_tag.toLowerCase())) + 1;
                            temp.pos_tag_frequency.put(temp.pos_tag_list.indexOf(pos_tag.toLowerCase()), new_frequency);
                            ++temp.total_word_frequency;
                        }
                    }
                }
            }
            catch(IOException e){
                System.out.println(e);
            }
        }
        //vocabCleanup();
    }
    
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
    
    public ArrayList<String> getListofPOSTags(){
        return listof_postags;
    }
    
    public ArrayList<String> getListofWords(){
        return words;
    }
    
    public HashMap<Integer,WordData> getListofWordsData(){
        return word_info;
    }
    
}
