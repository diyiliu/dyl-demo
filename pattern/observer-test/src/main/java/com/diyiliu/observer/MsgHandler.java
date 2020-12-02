package com.diyiliu.observer;

import java.util.Observable;
import java.util.Observer;

/**
 * Description: MsgHandler
 * Author: DIYILIU
 * Update: 2020-12-02 10:42
 */
public class MsgHandler implements Observer {

    @Override
    public void update(Observable o, Object arg) {
//        System.out.println(o);
        System.out.println(arg);
    }
}
