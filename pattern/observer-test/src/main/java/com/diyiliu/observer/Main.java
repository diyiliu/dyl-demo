package com.diyiliu.observer;



/**
 * Description: Main
 * Author: DIYILIU
 * Update: 2020-12-02 10:43
 */
public class Main {

    public static void main(String[] args){
        MsgHandler handler = new MsgHandler();
        Subject.add(handler);
        Subject.notify("handler");
    }
}
