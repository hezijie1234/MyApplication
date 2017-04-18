package com.example.zte.day7_zte_sortby_pingyin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zte.day7_zte_sortby_pingyin.pinyin.CharacterParser;
import com.example.zte.day7_zte_sortby_pingyin.pinyin.PinyinComparator2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private List<Person> list = new ArrayList<>();
    private List<Person> sortList = new ArrayList<>();
    private ListView listView;
    private TextView textView;
    private SlideBar slideBar;
    private CharacterParser characterParser;
    private PinyinComparator2 pinyinComparator2;
    private ContactAdapter contactAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
        listener();
    }

    private void listener() {

    }

    private void initView() {
        listView = (ListView) findViewById(R.id.listview);
        textView = (TextView) findViewById(R.id.textview);
        slideBar = (SlideBar) findViewById(R.id.slidebar);
        slideBar.setmTextDialog(textView);
        contactAdapter = new ContactAdapter(this,sortList);
        listView.setAdapter(contactAdapter);
    }

    private void initData() {
        list.add(new Person("张三"));
        list.add(new Person("李四"));
        list.add(new Person("王五"));
        list.add(new Person("赵六"));
        list.add(new Person("何子杰"));
        list.add(new Person("周"));
        sortList = getSortContacts(list);
    }

    /**汉字转拼音，取首字母。
     * @param name
     * @return
     */
    private String getSortLetter(String name) {
        String letter = "#";
        if (name == null) {
            return letter;
        }
        // 汉字转换成拼音
        String pinyin = characterParser.getSelling(name);
//        String pinyin = PinYin.getPinYin(name);
        Log.i("main", "pinyin:" + pinyin);
        String sortString = pinyin.substring(0, 1).toUpperCase(Locale.CHINESE);

        // 正则表达式，判断首字母是否是英文字母
        if (sortString.matches("[a-zA-Z]")) {
            letter = sortString.toUpperCase(Locale.CHINESE);
        }
        return letter;
    }

    /**对数据按照拼音顺序排序。
     * @param contactList
     * @return
     */
    public List<Person> getSortContacts(List<Person> contactList){
        if(contactList == null){
            return null;
        }
        List<Person> sort = new ArrayList<>();
        for (int i = 0; i < contactList.size(); i++) {
            Person person = contactList.get(i);
            if(characterParser == null){
                characterParser = new CharacterParser();
            }
            String pinyin;
            if(TextUtils.isEmpty(person.getName())){
                pinyin = getSortLetter(person.getName());
                person.setNickNmae(pinyin);
            }else{
                pinyin = "";
                person.setNickNmae(pinyin);
            }
            if(TextUtils.isEmpty(pinyin)){
                person.setSortLetter("#");
            }else{
                String sortString = pinyin.substring(0,1).toUpperCase();
                if(sortString.matches("[A-Z]")){
                    person.setSortLetter(sortString);
                }else{
                    person.setSortLetter("#");
                }
            }
            sort.add(person);
        }
        if(pinyinComparator2 == null){
            pinyinComparator2 = new PinyinComparator2();
        }
        Collections.sort(sort,pinyinComparator2);
        return sort;
    }
}
