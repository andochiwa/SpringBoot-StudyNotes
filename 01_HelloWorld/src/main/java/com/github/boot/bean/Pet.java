package com.github.boot.bean;

/**
 * @author HAN
 * @version 1.0
 * @create 03-04-4:15
 */
public class Pet {

    private String name;

    public Pet() {
    }

    public Pet(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "name='" + name + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
