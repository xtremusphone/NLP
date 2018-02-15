package nlp;

import java.util.ArrayList;

public class AutoCorrect {
    
    //Using Norvig Spell Checker Method
    
    private String wordErr;
    private ArrayList<String> bagOfWord;
    
    public AutoCorrect(String word){
        this.wordErr = word;
        bagOfWord = new ArrayList<>();
        
    }
    
}
