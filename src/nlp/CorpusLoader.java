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
        listof_postags = new ArrayList<>();
        loadCorpus();
    }
    
    private void loadCorpus(){
        File path = new File(CORPUS_PATH);
        documents = path.listFiles();
        
        Scanner scn;
        for(File x: documents){
            System.out.println(x.getName());
            try{
                scn = new Scanner(x);
                while(scn.hasNext()){
                    //split pos-tag and word
                    String composite = scn.next();
                    String word = composite.substring(0,composite.indexOf("/"));
                    
                    System.out.println(words.size());
                    
                    if(!words.contains(word.toLowerCase())){
                        words.add(word.toLowerCase());
                    }
                    
                    String pos_tag = composite.substring(composite.indexOf("/") + 1,composite.length());
                    //System.out.println(pos_tag);
                    
                    if(!listof_postags.contains(pos_tag.toLowerCase())){
                        listof_postags.add(pos_tag.toLowerCase());
                    }
                    //get arraylist of word index
                    ArrayList<String> tmp;
                    if(pos_tags.get(words.indexOf(word.toLowerCase())) == null){
                        System.out.println("Not yet added");
                        pos_tags.add(words.indexOf(word.toLowerCase()),new ArrayList<>());
                        tmp = pos_tags.get(words.indexOf(word.toLowerCase()));
                        tmp.add(pos_tag);
                    }
                    else{
                        tmp = pos_tags.get(words.indexOf(word.toLowerCase()));
                        tmp.add(pos_tag);
                    }
                }
            }
            catch(IOException e){
                System.out.println(e);
            }
        }
    }
    
}
