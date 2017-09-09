package com.infra.cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tokenizer {
    
    private String             line;
    private List<String>       tokens = new ArrayList<String>();
    private int                cursor = 0;
    
    
    public Tokenizer(String line) {
        this.line = line;    
        tokens.addAll(Arrays.asList(line.split("\\s+")));
    }

    public String nextToken() {
        return tokens.get(cursor++);
    }
    
    public boolean hasNext() {
        if (tokens.size() == cursor) {
            return false;
        }
        return true;
    }

    public String getLine() {
        return line;
    }
    
    public void pushBack(int nback) {
        cursor -= nback;
    }

}