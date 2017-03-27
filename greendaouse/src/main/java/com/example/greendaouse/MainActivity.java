package com.example.greendaouse;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.greendao.AndroidDaoMaster;
import com.example.greendao.AndroidDaoSession;
import com.example.greendao.MyOpenHelper;
import com.example.greendao.Student;
import com.example.greendao.StudentDao;
import com.example.greendao.user;
import com.example.greendao.userDao;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "111";
    private userDao userDao;
    private StudentDao studentDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //AndroidDaoMaster.DevOpenHelper devOpenHelper = new AndroidDaoMaster.DevOpenHelper(this,"androidtest");
        MyOpenHelper openHelper = new MyOpenHelper(this,"androidtest");
        SQLiteDatabase database = openHelper.getReadableDatabase();
        AndroidDaoMaster androidDaoMaster = new AndroidDaoMaster(database);
        AndroidDaoSession androidDaoSession = androidDaoMaster.newSession();
        userDao = androidDaoSession.getUserDao();
        studentDao = androidDaoSession.getStudentDao();
    }

    public void insert(View view) {
        user user = new user();
        Student student = new Student();
        student.setStudent_age(19);
        student.setStudent_name("何子杰");
        user.setUser_name("张三");
        user.setUser_age(18);
        studentDao.insert(student);
        userDao.insert(user);
    }

    public void query(View view) {
        List<user> users = userDao.loadAll();
        List<Student> students = studentDao.loadAll();
        for (int i = 0; i < users.size(); i++) {
            user user = users.get(i);
            Log.e(TAG, "query: "+user.getId()+"----"+user.getUser_name()+"---"+user.getUser_age() );
        }
        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            Log.e(TAG, "query: "+student.getId()+"----"+student.getStudent_name()+"---"+student.getStudent_age());
        }
    }

    public void update(View view) {
        user user = new user();
        user.setId(1L);
        user.setUser_name("hezijie");
        user.setUser_age(18);
        userDao.update(user);
    }

    public void delete(View view) {
        userDao.deleteByKey(1L);
    }
}
