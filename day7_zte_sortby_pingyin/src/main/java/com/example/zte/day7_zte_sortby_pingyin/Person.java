package com.example.zte.day7_zte_sortby_pingyin;

/**
 * Created by Administrator on 2017-04-14.
 */

public class Person {
    private String name;
    private String nickNmae;
    private String sortLetter;

    public void setSortLetter(String sortLetter) {
        this.sortLetter = sortLetter;
    }

    public String getSortLetter() {

        return sortLetter;
    }

    public void setNickNmae(String nickNmae) {
        this.nickNmae = nickNmae;
    }

    public String getNickNmae() {

        return nickNmae;
    }

    public Person() {
    }

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
