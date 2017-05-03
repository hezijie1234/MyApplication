package com.example.zte.day_zte_notification_push;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import java.net.URI;
import java.net.URISyntaxException;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ServerHandshake;

/** This example demonstrates how to create a websocket connection to a server. Only the most important callbacks are overloaded. */
public class ExampleClient extends WebSocketClient {

	public ExampleClient( URI serverUri , Draft draft ) {
		super( serverUri, draft );
	}

	public ExampleClient( URI serverURI ) {
		super( serverURI );
	}

	@Override
	public void onOpen( ServerHandshake handshakedata ) {
		System.out.println("opened connection");
		// if you plan to refuse connection based on ip or httpfields overload: onWebsocketHandshakeReceivedAsClient
	}

	@Override
	public void onMessage( String message ) {

		System.out.println( "received: " + message );

		showNotifictionIcon(MyApplication.getContext(),message);
	}

	public void onFragment( Framedata fragment ) {
		System.out.println( "received fragment: " + new String( fragment.getPayloadData().array() ) );
	}

	@Override
	public void onClose( int code, String reason, boolean remote ) {
		// The codecodes are documented in class org.java_websocket.framing.CloseFrame
		System.out.println( "Connection closed by " + ( remote ? "remote peer" : "us" ) );
	}

	@Override
	public void onError( Exception ex ) {
		ex.printStackTrace();
		// if the error is fatal then onClose will be called additionally
	}

	public static void main( String[] args ) throws URISyntaxException, InterruptedException {
		ExampleClient c = new ExampleClient( new URI( "ws://127.0.0.1:8887" ), new Draft_10() ); // more about drafts here: http://github.com/TooTallNate/Java-WebSocket/wiki/Drafts
		c.connectBlocking();
		c.send("_user_login_admin");
	}

	public static void showNotifictionIcon(Context context,String msg) {
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
		Intent intent = new Intent(context, MainActivity1.class);//将要跳转的界面
		builder.setAutoCancel(true);//点击后消失
		builder.setSmallIcon(R.mipmap.ic_launcher);//设置通知栏消息标题的头像
		builder.setDefaults(NotificationCompat.DEFAULT_SOUND);//设置通知铃声
		builder.setTicker("你好");
		builder.setContentText(msg);//通知内容
		builder.setContentTitle("来自服务端的提醒");
		//利用PendingIntent来包装我们的intent对象,使其延迟跳转
		PendingIntent intentPend = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		builder.setContentIntent(intentPend);
		NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
		manager.notify(0, builder.build());
	}

}