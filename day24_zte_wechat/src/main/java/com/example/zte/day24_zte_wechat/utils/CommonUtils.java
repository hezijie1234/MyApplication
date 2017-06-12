package com.example.zte.day24_zte_wechat.utils;

import com.example.zte.day24_zte_wechat.view.MyApplication;
import com.example.zte.greendao.Friend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017-05-26.
 */

public class CommonUtils {
    /**
     * dip-->px
     */
    public static int dip2Px(int dip) {
        // px/dip = density;
        // density = dpi/160
        // 320*480 density = 1 1px = 1dp
        // 1280*720 density = 2 2px = 1dp

        float density = MyApplication.getContext().getResources().getDisplayMetrics().density;
        int px = (int) (dip * density + 0.5f);
        return px;
    }
    /**正则表达式：https://juejin.im/post/59144e1ca0bb9f005fca5be7
     * @param num
     * @return判断是否是电话号码
     */
    public static boolean isPhoneNum(String num){
        Pattern pattern = Pattern.compile("^((13[0-9])|(15[0-9])|(18[0-9])|(147)|(17[0|6|7|8]))\\d{8}$");
        Matcher matcher = pattern.matcher(num);
        return matcher.matches();
    }

    /**
     * @param email
     * @return判断是不是邮箱地址
     */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**根据拼音顺序进行排序
     * @param list
     */
    public static void sortContacts(List<Friend> list) {
        Collections.sort(list);//排序后由于#号排在上面，故得把#号的部分集合移动到集合的最下面
        List<Friend> specialList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            //将属于#号的集合分离开来
            if (list.get(i).getNameSpelling().equalsIgnoreCase("#")) {
                specialList.add(list.get(i));
            }
        }

        if (specialList.size() != 0) {
            list.removeAll(specialList);//先移出掉顶部的#号部分
            list.addAll(list.size(), specialList);//将#号的集合添加到集合底部
        }

    }
}
