package com.test.testmaksat.data;


import java.io.Serializable;
import java.util.List;

public class DialogData implements Serializable{
    private List<String> inList;
    private List<String> outList;
    private int userId;


    public DialogData(List<String> inList, List<String> outList, int userId) {
        this.inList = inList;
        this.outList = outList;
        this.userId = userId;
    }

    public List<String> getInList() {
        return inList;
    }

    public List<String> getOutList() {
        return outList;
    }

    public int getUserId() {
        return userId;
    }
}
