package com.diyiliu.observer;


import java.util.Observable;
import java.util.Observer;

/**
 * Description: Subject
 * Author: DIYILIU
 * Update: 2020-12-02 10:39
 */
public class Subject extends Observable {

    private static Subject subject = new Subject();

    private Subject() {

    }

    public static void add(Observer obs) {
        subject.addObserver(obs);
    }


    public static void notify(Object obj) {
        subject.setChanged();
        subject.notifyObservers(obj);
    }
}
