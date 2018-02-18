package nlp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class AutoCorrect {
    
    //Using Norvig Spell Checker Method
    
    /*
    1. Norvig Spell Checker method may not be that great due to limitation on the number of 
    edit distance which people rarely does type within 1 - 2 edit distance.
    2. Changes in algorithm might occur or maybe a hybrid of bruteforce with statistical method
    might be used together.
    */
    
    private String DEFAULT_DICTIONARY_PATH = "Dictionary.txt";
    
    private String wordErr;
    private ArrayList<String> bagOfWord;
    
    public AutoCorrect(String word){
        this.wordErr = word;
        bagOfWord = new ArrayList<>();
        checkDictionaryFile();
        System.out.println(bagOfWord.size());
    }
    
    private boolean checkDictionaryFile(){
        File dict = new File(DEFAULT_DICTIONARY_PATH);
        if(dict.exists()){
            try{
                Scanner scn = new Scanner(dict);
                while(scn.hasNextLine()){
                    bagOfWord.add(scn.nextLine());
                }
            }
            catch(IOException e){
                System.out.println(e);
            }
            return true;
        }
        else
            return false;
    }
    
    
}
