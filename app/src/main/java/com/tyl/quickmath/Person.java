package com.tyl.quickmath;

public class Person implements Comparable<Person> {
    private String name;
    private int score;

    public Person(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public int compareTo(Person other) {
        if(this.getScore() < other.getScore())
            return 1;
        else if (this.getScore() == other.getScore())
            return 0 ;
        return -1 ;
    }

}
