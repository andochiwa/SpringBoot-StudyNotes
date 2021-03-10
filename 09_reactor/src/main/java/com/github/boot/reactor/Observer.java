package com.github.boot.reactor;

import java.util.Observable;

/**
 * java8的观察者模式-->响应式编程
 * @author HAN
 * @version 1.0
 * @create 03-11-3:54
 */
public class Observer extends Observable {

    public static void main(String[] args) {
        Observer observer = new Observer();
        // 添加观察者
        observer.addObserver((o, arg) -> {
            System.out.println("发生变化" + o + ":" + arg);
        });
        observer.addObserver((o, arg) -> {
            System.out.println("手动被观察者通知 准备发生改变" + o + ":" + arg);
        });

        // 数据变化
        observer.setChanged();
        // 通知
        observer.notifyObservers();
    }

}
