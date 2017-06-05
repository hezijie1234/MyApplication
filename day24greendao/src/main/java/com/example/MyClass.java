package com.example;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

public class MyClass {

    public static void main(String[] args) {
        Schema schema = new Schema("wechat",1,"com.example.zte.greendao");
        Entity userEntity = schema.addEntity("Friend");
        userEntity.addStringProperty("userId");
        userEntity.addStringProperty("name");
        userEntity.addStringProperty("portraitUri");
        userEntity.addStringProperty("displayName");
        userEntity.addStringProperty("region");
        userEntity.addStringProperty("phoneNumber");
        userEntity.addStringProperty("status");
        userEntity.addStringProperty("timestamp");
        userEntity.addStringProperty("nameSpelling");
        userEntity.addStringProperty("displayNameSpelling");
        //执行自动生成对象的User类
        try {
            /**
             * 参数1：Schema对象
             * 参数2：自动生成的Java类的具体位置
             */
            new DaoGenerator().generateAll(schema,"../MyApplication/day24_zte_wechat/src/main/java");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
