# NLP tool
This was done in part of my assignment for numerical methods which was to create chatbot.
The task requires to hand-code everything from scratch with the exception of using the built in classes and functions.

This tool contains:
1. Tokenizer (can properly split braces, comma, full stop) //Though a lot more tweaks should be done
2. Auto-correct (using norvig spell checker which only works for English language for now)
3. Chunker (which tries to get the noun phrase and also verb phrase)
4. POS Tagger (uses Viterbi algorithm which was trained on CoNLL 2000 data set. Final trained model serialized as "Model.slz")

The final chatbot can be seen here:
https://github.com/xtremusphone/Chatbots
