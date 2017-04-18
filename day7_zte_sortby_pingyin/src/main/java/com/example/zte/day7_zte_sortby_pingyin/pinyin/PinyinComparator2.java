package com.example.zte.day7_zte_sortby_pingyin.pinyin;


import com.example.zte.day7_zte_sortby_pingyin.Person;

import java.util.Comparator;

/**
 * 
 * @author xiaanming
 *
 */
public class PinyinComparator2 implements Comparator<Person> {

	public int compare(Person o1, Person o2) {
		if (o1.getSortLetter().equals("☆") || o2.getSortLetter().equals("#")) {
			return -1;
		} else if (o1.getSortLetter().equals("#") || o2.getSortLetter().equals("☆")) {
			return 1;
		} else {
			return o1.getSortLetter().compareTo(o2.getSortLetter());
		}
	}

}
