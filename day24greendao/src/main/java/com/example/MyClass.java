package com.example;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

public class MyClass {

    public static void main(String[] args) {
        Schema schema = new Schema("wechat",3,"com.example.zte.greendao");
        Entity userEntity = schema.addEntity("Friend");
        userEntity.addIdProperty().primaryKey().autoincrement();
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

        //添加新的表s
        Entity groups = schema.addEntity("Groups");
        groups.addIdProperty().primaryKey().autoincrement();
        groups.addStringProperty("groupId");
        groups.addStringProperty("name");
        groups.addStringProperty("portraitUri");
        groups.addStringProperty("displayName");
        groups.addStringProperty("role");
        groups.addStringProperty("bulletin");
        groups.addStringProperty("timestamp");
        groups.addStringProperty("nameSpelling");

        Entity groupMember = schema.addEntity("GroupMember");
        groupMember.addIdProperty().primaryKey().autoincrement();
        groupMember.addStringProperty("userId");
        groupMember.addStringProperty("name");
        groupMember.addStringProperty("portraitUri");
        groupMember.addStringProperty("groupId");
        groupMember.addStringProperty("displayName");
        groupMember.addStringProperty("nameSpelling");
        groupMember.addStringProperty("displayNameSpelling");
        groupMember.addStringProperty("groupName");
        groupMember.addStringProperty("groupNameSpelling");
        groupMember.addStringProperty("groupPortrait");



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
