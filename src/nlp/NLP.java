package nlp;

import java.util.List;

public class NLP {
    static String[] conjunctions = {"for","and","nor","but","or","yet","so"};
    static String[] preposition = {"with","at","from","into","during","including","until","against","among","throughout","despite","towards","upon","concerning","of","to","in","for","on","by","about","like","through","over","before","between","after","since","without","under","within",
    "along","following","across","behind","beyond","plus","except","but","up","out","around","down","off","above","near"};
    
    public static void main (String[] args){
        WordTokenizer tokenizer = new WordTokenizer();
        String temp = "My name is F.B.I..";
        List<String> tokens = tokenizer.tokenizer(temp);
        System.out.println(tokens.toString());
        CorpusLoader crp = new CorpusLoader();

    }
}
