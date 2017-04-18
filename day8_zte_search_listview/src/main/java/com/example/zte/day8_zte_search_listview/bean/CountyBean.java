package com.example.zte.day8_zte_search_listview.bean;

/**
 * ÐÐÕþÇø
 * @author Test
 *
 */
public class CountyBean extends BaseBean {

	private String name;
	private String weatherCode;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWeatherCode() {
		return weatherCode;
	}

	public void setWeatherCode(String weatherCode) {
		this.weatherCode = weatherCode;
	}

	@Override
	public String toString() {
		return "CountyBean{" +
				"name='" + name + '\'' +
				", weatherCode='" + weatherCode + '\'' +
				'}';
	}
}
