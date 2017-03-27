package com.example;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

public class MyClass {

    public static void main(String[] args) {
        Schema schema = new Schema("android",4,"com.example.greendao");
        Entity user = schema.addEntity("user");
        user.addIdProperty().primaryKey().autoincrement();
        user.addStringProperty("user_name");
        user.addIntProperty("user_age");
        Entity student = schema.addEntity("Student");
        student.addIdProperty().primaryKey().autoincrement();
        student.addStringProperty("student_name");
        student.addIntProperty("student_age");
        try {
            new DaoGenerator().generateAll(schema,"../MyApplication/greendaouse/src/main/java");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
