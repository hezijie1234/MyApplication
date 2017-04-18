package com.example.zte.day8_zte_search_listview;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Xml;
import android.widget.EditText;
import android.widget.ListView;

import com.example.zte.day8_zte_search_listview.bean.CityBean;
import com.example.zte.day8_zte_search_listview.bean.CountyBean;
import com.example.zte.day8_zte_search_listview.bean.ProvinceBean;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "111";
    private EditText editText;
    private ListView listView;
    private SlideBar slideBar;

    private List<CityBean> mCityList;
    private CityListAdapter mCityAdapter;
    private List<CityBean> mSortList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(TAG, "onCreate: "+"测试" );
        initView();
        initDatas();
        listener();
    }

    private void listener() {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(mCityAdapter != null){
                    if(s.length() == 0 ){
                        mCityAdapter.resetData();
                    }else{
                        mCityAdapter.queryData(s);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        slideBar.setOnSelectListener(new SlideBar.OnSelectListener() {
            @Override
            public void onSelect(String s) {
                if("#".equals(s)){
                    listView.setSelection(0);
                }else{
                    int positionForSection = mCityAdapter.getPositionForSection(s.charAt(0) - 'A');
                    listView.setSelection(positionForSection);
                }

            }

            @Override
            public void onMoveUp(String s) {

            }
        });
    }

    private void initDatas() {
        mCityList = new ArrayList<>();
        new ParseXmlTask().execute();
        mCityAdapter = new CityListAdapter(R.layout.main_list_item,mCityList,this);
        listView.setAdapter(mCityAdapter);
    }

    private void initView() {
        editText = (EditText) findViewById(R.id.main_edit);
        listView = (ListView) findViewById(R.id.main_listview);
        slideBar = (SlideBar) findViewById(R.id.main_slide);
    }

    class ParseXmlTask extends AsyncTask<Void,Void,List<CityBean>>{
        private XmlPullParser xmlPullParser;
        private List<ProvinceBean> mProvinceList;
        private ProvinceBean provinceBean;
        private CityBean cityBean;
        private CountyBean countyBean;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProvinceList = new ArrayList<>();
            xmlPullParser = Xml.newPullParser();
            InputStream inputStream = getResources().openRawResource(R.raw.cities);
            try {
                xmlPullParser.setInput(inputStream,"UTF-8");
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected List<CityBean> doInBackground(Void... params) {
            int event = 1;
            try {
                event = xmlPullParser.getEventType();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            while(event != XmlPullParser.END_DOCUMENT){
                switch (event){
                    case XmlPullParser.START_TAG:
                        String name = xmlPullParser.getName();
                        //头部标记的解析。
                        startTagParser(name);
                        break;
                    case XmlPullParser.END_TAG:
                        String endName = xmlPullParser.getName();
                        //尾部标记的解析。
                        endTagParser(endName);
                        break;
                }
                try {
                    event = xmlPullParser.next();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(mProvinceList != null){
                //循环遍历所有的数据，将城市放进容器中。
                for(ProvinceBean bean:mProvinceList){
                    mCityList.addAll(bean.getCityList());
                }
            }
            //对数据进行排序。
            Collections.sort(mCityList, new Comparator<CityBean>() {
                @Override
                public int compare(CityBean o1, CityBean o2) {
                    return String.valueOf(o1.getFirstLetter()).compareTo(String.valueOf(o2.getFirstLetter()));
                }
            });
            return mCityList;
        }

        private void endTagParser(String endName) {
            switch (endName){
                case "province":
                    mProvinceList.add(provinceBean);
                    break;
                case "city":
                    provinceBean.getCityList().add(cityBean);
                    break;
                case "county":
                    cityBean.getCountyList().add(countyBean);
                    break;
            }
        }

        private void startTagParser(String name) {
            switch(name){
                case "province" :
                    provinceBean = new ProvinceBean();
                    provinceBean.setName(xmlPullParser.getAttributeValue(null,"name"));
                    provinceBean.setId(xmlPullParser.getAttributeValue(null,"id"));
                    break;
                case "city":
                    cityBean = new CityBean();
                    cityBean.setName(xmlPullParser.getAttributeValue(null,"name"));
                    cityBean.setId(xmlPullParser.getAttributeValue(null,"id"));
                    String cityName = cityBean.getName();
                    String firstLetter = CharacterParser.getInstance().getPinYinSpelling(cityName).substring(0, 1);
                    cityBean.setFirstLetter((char)(firstLetter.charAt(0) - 32));
                    break;
                case "county":
                    countyBean = new CountyBean();
                    countyBean.setName(xmlPullParser.getAttributeValue(null,"name"));
                    countyBean.setId(xmlPullParser.getAttributeValue(null,"id"));
                    countyBean.setWeatherCode(xmlPullParser.getAttributeValue(null,"weatherCode"));
                    break;
            }
        }

        @Override
        protected void onPostExecute(List<CityBean> cityBeen) {
            super.onPostExecute(cityBeen);
            mCityAdapter.notifyDataSetChanged();
        }
    }
}
