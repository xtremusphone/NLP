package nlp;

//This class is to specifically extract data from Brown Corpus

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class CorpusLoader {
    
    private final String CORPUS_PATH = "brown";
    private File[] documents;
    public ArrayList<String> words;
    public ArrayList<Integer> word_frequency;
    public ArrayList<Integer> word_document_frequency;
    public ArrayList<ArrayList<String>> pos_tags;
    public ArrayList<String> listof_postags;
    
    public CorpusLoader(){
        words = new ArrayList<>();
        word_frequency = new ArrayList<>();
        word_document_frequency = new ArrayList<>();
        pos_tags = new ArrayList<>();
        loadCorpus();
    }
    
    private void loadCorpus(){
        File path = new File(CORPUS_PATH);
        documents = path.listFiles();
        System.out.println(path.exists());
        
        Scanner scn;
        for(File x: documents){
            System.out.println(x.getName());
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
                        
                    }
                    
                    //get arraylist of word index
                    ArrayList<String> tmp = pos_tags.get(words.indexOf(word));
                    tmp.add(pos_tag);
                }
            }
            catch(IOException e){
                System.out.println(e);
            }
        }
    }
    
}
