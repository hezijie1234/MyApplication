package com.example.zte.day8_zte_search_listview.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 城市
 * @author Test
 *
 */
public class CityBean extends BaseBean {

	private String name;
	private char firstLetter;
	private List<CountyBean> countyList;
	
	public CityBean(){
		countyList = new ArrayList<CountyBean>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "CityBean{" +
				"name='" + name + '\'' +
				", firstLetter=" + firstLetter +
				", countyList=" + countyList +
				'}';
	}

	/**
	 * 获取首字母，是大写
	 * @return
	 */
	public char getFirstLetter() {
		return firstLetter;
	}

	public void setFirstLetter(char firstLetter) {
		this.firstLetter = firstLetter;
	}

	public List<CountyBean> getCountyList() {
		return countyList;
	}

	public void setCountyList(List<CountyBean> countyList) {
		this.countyList = countyList;
	}

}
