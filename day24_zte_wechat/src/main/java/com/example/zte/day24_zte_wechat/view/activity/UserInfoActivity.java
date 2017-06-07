package com.example.zte.day24_zte_wechat.view.activity;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zte.day24_zte_wechat.R;
import com.example.zte.day24_zte_wechat.utils.ConstantsUtil;
import com.example.zte.day24_zte_wechat.utils.SharePreferenceUtil;
import com.example.zte.greendao.DBManager;
import com.example.zte.greendao.Friend;
import com.example.zte.greendao.FriendDao;
import com.example.zte.greendao.WechatDaoSession;
import com.squareup.picasso.Picasso;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imlib.model.UserInfo;

public class UserInfoActivity extends BaseActivity {

    private UserInfo userInfo;

    @BindView(R.id.ivHeader)
    ImageView mIvHeader;
    @BindView(R.id.tvName)
    TextView mTvName;
    @BindView(R.id.ivGender)
    ImageView mIvGender;
    @BindView(R.id.tvAccount)
    TextView mTvAccount;
    @BindView(R.id.tvNickName)
    TextView mTvNickName;
    @BindView(R.id.tvArea)
    TextView mTvArea;
    @BindView(R.id.tvSignature)
    TextView mTvSignature;

    @BindView(R.id.llArea)
    LinearLayout mLlArea;
    @BindView(R.id.llSignature)
    LinearLayout mLlSignature;

    @BindView(R.id.btnCheat)
    Button mBtnCheat;
    @BindView(R.id.btnAddToContact)
    Button mBtnAddToContact;
    private Friend mFriend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        userInfo = intent.getParcelableExtra("userInfo");
        initData();
        listener();
    }

    private void listener() {
        //点击跳转到聊天界面
        mBtnCheat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInfoActivity.this,SessionActivity.class);
                intent.putExtra("sessionId",userInfo.getUserId());
                intent.putExtra("sessionType",SessionActivity.SESSION_TYPE_PRIVATE);
                startActivity(intent);
                finish();
            }
        });
        mBtnAddToContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInfoActivity.this,PostFriendsActivity.class);
                intent.putExtra("userId",userInfo.getUserId());
                startActivity(intent);
            }
        });
    }

    private void initData() {
        DBManager wechat = DBManager.getInstance(this, "wechat");
        WechatDaoSession readableDBSession = wechat.getReadableDBSession();
        FriendDao friendDao = readableDBSession.getFriendDao();
//        List<Friend> friends = friendDao.queryRaw("userid = ?", userInfo.getUserId());
        QueryBuilder<Friend> builder = friendDao.queryBuilder();
        builder.where(FriendDao.Properties.UserId.eq(userInfo.getUserId()));
        List<Friend> friends = builder.list();
        if(friends != null && friends.size() > 0){
            mFriend = friends.get(0);
        }
        Picasso.with(this).load(userInfo.getPortraitUri()).error(R.mipmap.default_header).into(mIvHeader);
        mTvAccount.setText("微信号:"+userInfo.getUserId());
        mTvName.setText(userInfo.getName());
//        Log.e(ConstantsUtil.TAG, "登录时缓存的: "+SharePreferenceUtil.getInstance(this).getString("id","") );
//        Log.e(ConstantsUtil.TAG, "跳转时传递过来的: "+userInfo.getUserId() );
//        Log.e(ConstantsUtil.TAG, "是否是陌生人: "+(mFriend == null) );
        if(mFriend == null && !(SharePreferenceUtil.getInstance(this).getString("id","").equals(userInfo.getUserId()))){//陌生人并且不是自己
            mBtnCheat.setVisibility(View.GONE);
            mBtnAddToContact.setVisibility(View.VISIBLE);
            mTvNickName.setVisibility(View.INVISIBLE);
            //我自己
        }else if (SharePreferenceUtil.getInstance(this).getString("id","").equals(userInfo.getUserId())){
            mTvNickName.setVisibility(View.INVISIBLE);
            mLlArea.setVisibility(View.GONE);
            mLlSignature.setVisibility(View.GONE);
            //朋友
        }else {
            String nickName = mFriend.getDisplayName();
            mTvName.setText(nickName);
            if (TextUtils.isEmpty(nickName)) {
                mTvNickName.setVisibility(View.INVISIBLE);
            } else {
                mTvNickName.setText("昵称:" + mFriend.getName());
            }
        }
    }
}
