package com.taxerts.scratch;

public class TaskList {
    private String desctext;
    private int icons;
    private String text;

    public TaskList(int i, String str, String str2) {
        this.icons = i;
        this.text = str;
        this.desctext = str2;
    }

    public int getIcons() {
        return this.icons;
    }

    public void setIcons(int i) {
        this.icons = i;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String str) {
        this.text = str;
    }

    public String getDesctext() {
        return this.desctext;
    }

    public void setDesctext(String str) {
        this.desctext = str;
    }
}
