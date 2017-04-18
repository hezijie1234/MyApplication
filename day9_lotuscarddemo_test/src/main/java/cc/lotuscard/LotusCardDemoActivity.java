package cc.lotuscard;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zte.day9_lotuscarddemo_test.R;

public class LotusCardDemoActivity extends Activity {
	private NfcAdapter m_NfcAdpater;
	private PendingIntent pendingIntent;
	private IntentFilter[] mFilters;
	private String[][] mTechLists;

	private LotusCardDriver mLotusCardDriver;

//	private UsbManager m_UsbManager = null;
//	private UsbDevice m_LotusCardDevice = null;
//	private UsbInterface m_LotusCardInterface = null;
//	private UsbDeviceConnection m_LotusCardDeviceConnection = null;
	private final int m_nVID = 1306;
	private final int m_nPID = 20763;
	private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";

//	private Boolean m_bUseUsbHostApi = true;
//	private Boolean m_bCanUseUsbHostApi = true;
	private String m_strDeviceNode;

	private long m_nDeviceHandle = -1;
	private Handler m_Handler = null;
	private CardOperateThread m_CardOperateThread;
	private Boolean m_bCardOperateThreadRunning = false;
	/*********************************** UI *********************************/
	private Button m_btnTest;
	private Button m_btnTestNtag203;
	private Button m_btnTestId;
	private Button m_btnAutoTest;
	private Button m_btnTestBeijingCard;
	private CheckBox m_chkUseUsbHostApi;
	private EditText m_edtLog;
	private TextView m_tvDeviceNode;
	private TextView m_tvMessage;
	private ImageView m_imgIdPhoto;
	private byte m_bytTest;

	private final static char[] HEX = { '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	private final static int SFI_EXTRA_LOG = 4;
	private final static int SFI_EXTRA_CNT = 5;

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if ((m_nDeviceHandle != -1) && (null != m_CardOperateThread)) {
			if (true == m_bCardOperateThreadRunning) {
				m_CardOperateThread.cancel();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			m_bCardOperateThreadRunning = !m_bCardOperateThreadRunning;

		}
		if (-1 != m_nDeviceHandle)
			mLotusCardDriver.CloseDevice(m_nDeviceHandle);
		super.onDestroy();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		m_NfcAdpater = NfcAdapter.getDefaultAdapter(this);
		if (m_NfcAdpater == null) {
			Toast.makeText(this, "Not Found NfcAdapter!", Toast.LENGTH_SHORT)
					.show();
			// finish();
			 return;
		} else if (!m_NfcAdpater.isEnabled()) {
			Toast.makeText(this, "Please Enabled NfcAdapter",
					Toast.LENGTH_SHORT).show();
			// finish();
			// return;
		}


		m_imgIdPhoto = (ImageView) findViewById(R.id.imgIdPhoto);
		m_btnTestId = (Button) findViewById(R.id.btnTestId);
		m_btnTestNtag203 = (Button) findViewById(R.id.btnTestNtag203);
		m_btnTest = (Button) findViewById(R.id.btnTest);
		m_btnAutoTest = (Button) findViewById(R.id.btnAutoTest);
		m_btnTestBeijingCard = (Button) findViewById(R.id.btnTestBeijingCard);
		m_edtLog = (EditText) findViewById(R.id.edtLog);
		m_tvDeviceNode = (TextView) findViewById(R.id.tvDeviceNode);
		m_tvMessage = (TextView) findViewById(R.id.tvMessage);
		m_edtLog.setText("");
		m_imgIdPhoto.setBackgroundColor(0);
		//m_chkUseUsbHostApi = (CheckBox) findViewById(R.id.chkUseUsbHostApi);

		pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
		ndef.addCategory("*/*");
		mFilters = new IntentFilter[] { ndef };// 过滤器
		mTechLists = new String[][] {
				new String[] { MifareClassic.class.getName() },
				new String[] { NfcB.class.getName() },
				new String[] { IsoDep.class.getName() },
				new String[] { NfcA.class.getName() } };// 允许扫描的标签类型

		// 设置USB读写回调 串口可以不用此操作
//		m_bCanUseUsbHostApi = SetUsbCallBack();
//		if (m_bCanUseUsbHostApi) {
//			AddLog("Find L1-U LotusSmart IC Reader!");
//			m_tvDeviceNode.setText("Device Node:" + m_strDeviceNode);
//		} else {
//			AddLog("Not Find L1-U LotusSmart IC Reader!");
//		}
//		m_chkUseUsbHostApi.setChecked(m_bCanUseUsbHostApi);

		// LotusCardParam tLotusCardParam = new LotusCardParam();
		mLotusCardDriver = new LotusCardDriver();
		// int nDeviceHandle = mLotusCardDriver.OpenDevice("/dev/ttyTCC1", 0,
		// 0);
		// int nDeviceHandle = mLotusCardDriver.OpenDevice("/dev/ttyS3", 0, 0);
		mLotusCardDriver.m_lotusDemoActivity = this;
		m_Handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				AddLog(msg.obj.toString());
				super.handleMessage(msg);
			}
		};

	}

	private String convertArray2String(byte arrBuffer[]) {
		String strResult = "";
		String strTmp = "";
		for (int i = 0; i < arrBuffer.length; i++) {

			strTmp = "00" + Integer.toHexString(arrBuffer[i] & 0xff);
			strTmp = strTmp.substring(strTmp.length() - 2, strTmp.length());
			strResult += strTmp;
		}
		return strResult;
	}

	@SuppressLint("NewApi")
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(m_NfcAdpater != null){
			m_NfcAdpater.enableForegroundDispatch(this, pendingIntent, mFilters,
					mTechLists);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		boolean bResult = false;
		boolean bWlDecodeResult = false;
		String temp;
		byte[] arrCardId = { (byte) 0x00, (byte) 0x36, (byte) 0x00,
				(byte) 0x00, (byte) 0x08 };
		byte[] arrFirstFile = { (byte) 0x00, (byte) 0xA4, (byte) 0x00,
				(byte) 0x00, (byte) 0x02, (byte) 0x60, (byte) 0x02 };
		byte[] arrReadFirstFile = { (byte) 0x80, (byte) 0xB0, (byte) 0x00,
				(byte) 0x00, (byte) 0x20 };
		// m_NfcIntent = intent;
		String strWriteText = "春眠不觉晓";
		if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
			Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			NfcB nfcbId = NfcB.get(tagFromIntent);
			Ndef ndef = Ndef.get(tagFromIntent);
			IsoDep iso = IsoDep.get(tagFromIntent);
			if (nfcbId != null) {

				try {
					nfcbId.connect();
					if (nfcbId.isConnected())
						AddLog("connect");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (nfcbId.isConnected()) {
					// try {
					// byte[] arrResult =nfcbId.getApplicationData();
					// byte[] arrResult =nfcbId.getProtocolInfo();
					// byte[] arrResult = nfcbId.transceive(arrCardId);
					// AddLog("Read Id:" + arrResult.length + " " +
					// convertArray2String(arrResult));
					// arrResult = nfcbId.transceive(arrFirstFile);
					// AddLog("Select FirstFile:" + arrResult.length + " " +
					// convertArray2String(arrResult));
					//
					// arrResult = nfcbId.transceive(arrReadFirstFile);
					// AddLog("Read FirstFile:" + arrResult.length + " " +
					// convertArray2String(arrResult));
					TwoIdInfoParam tTwoIdInfo = new TwoIdInfoParam();
					if (m_nDeviceHandle == -1) {
						m_nDeviceHandle = mLotusCardDriver.OpenDevice("", 0, 0,
								0, 0,// 使用内部默认超时设置
								true);

					}
					bResult = mLotusCardDriver.GetTwoIdInfoByMcuServer(nfcbId,
							m_nDeviceHandle, "120.24.249.49", "username",
							"password", tTwoIdInfo,2);
//					bResult = mLotusCardDriver.GetTwoIdInfoByMcuServer(nfcbId,
//							m_nDeviceHandle, "192.168.1.52", "username",
//							"password", tTwoIdInfo, 2);
					if (!bResult) {
						AddLog("Call GetTwoIdInfoByMcuServer Error! ErrorCode:" + mLotusCardDriver.GetTwoIdErrorCode(m_nDeviceHandle));
						return;
					}
					AddLog("Call GetTwoIdInfoByMcuServer Ok!");
					//处理照片
					if(0x00 == tTwoIdInfo.unTwoIdPhotoJpegLength)
					{
						bWlDecodeResult = mLotusCardDriver.WlDecodeByServer(m_nDeviceHandle, "120.24.249.49", tTwoIdInfo);
						if (!bWlDecodeResult) {
							AddLog("Call WlDecodeByServer Error! ");
						}
						else
						{
							AddLog("Call WlDecodeByServer Ok!");
						}

					}
					if (true == bResult) {
						// 姓名
						try {
							temp = new String(tTwoIdInfo.arrTwoIdName, 0, 30,
									"UTF-16LE").trim();
							if (temp.equals("")) {
								AddLog("数据为空");
								return;
							}
							AddLog("姓名:" + temp);

							// 性别
							temp = new String(tTwoIdInfo.arrTwoIdSex, 0, 2,
									"UTF-16LE").trim();
							if (temp.equals("1"))
								temp = "男";
							else
								temp = "女";
							AddLog("性别:" + temp);
							// 民族
							temp = new String(tTwoIdInfo.arrTwoIdNation, 0, 4,
									"UTF-16LE").trim();
							try {
								int code = Integer.parseInt(temp.toString());
								temp = decodeNation(code);
							} catch (Exception e) {
								temp = "";
							}
							AddLog("民族:" + temp);
							// 出生日期
							temp = new String(tTwoIdInfo.arrTwoIdBirthday, 0,
									16, "UTF-16LE").trim();
							AddLog("出生日期:" + temp);
							// 住址
							temp = new String(tTwoIdInfo.arrTwoIdAddress, 0,
									70, "UTF-16LE").trim();
							AddLog("住址:" + temp);
							// 身份证号码
							temp = new String(tTwoIdInfo.arrTwoIdNo, 0, 36,
									"UTF-16LE").trim();
							AddLog("身份证号码:" + temp);
							// 签发机关
							temp = new String(
									tTwoIdInfo.arrTwoIdSignedDepartment, 0, 30,
									"UTF-16LE").trim();
							AddLog("签发机关:" + temp);
							// 有效期起始日期
							temp = new String(
									tTwoIdInfo.arrTwoIdValidityPeriodBegin, 0,
									16, "UTF-16LE").trim();
							AddLog("有效期起始日期:" + temp);
							// 有效期截止日期 UNICODE YYYYMMDD 有效期为长期时存储“长期”
							temp = new String(
									tTwoIdInfo.arrTwoIdValidityPeriodEnd, 0,
									16, "UTF-16LE").trim();
							AddLog("有效期截止日期:" + temp);
							if (tTwoIdInfo.unTwoIdPhotoJpegLength > 0) {
								Bitmap photo = BitmapFactory.decodeByteArray(
										tTwoIdInfo.arrTwoIdPhotoJpeg, 0,
										tTwoIdInfo.unTwoIdPhotoJpegLength);
								m_imgIdPhoto.setBackgroundDrawable(new BitmapDrawable(photo));
							}

						} catch (UnsupportedEncodingException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

					} else {
						AddLog("GetTwoIdInfoByMcuServer执行失败");
					}

					// } catch (IOException e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// AddLog(e.getMessage());
					// }
				}
			}
		}
	}



	public void OnTestListener(View arg0) {
		if (null == mLotusCardDriver)
			return;
		m_btnAutoTest.setClickable(false);
		if ((m_nDeviceHandle != -1) && (null != m_CardOperateThread)) {
			if (true == m_bCardOperateThreadRunning) {
				m_CardOperateThread.cancel();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			m_bCardOperateThreadRunning = !m_bCardOperateThreadRunning;
			m_tvMessage.setText("Message:");
			m_btnAutoTest.setText("AutoTest");
		}
		if (m_nDeviceHandle == -1) {
			// m_nDeviceHandle = mLotusCardDriver.OpenDevice("192.168.4.1", 0,
			// 0,0,0,
			// m_chkUseUsbHostApi.isChecked());
			// m_nDeviceHandle = mLotusCardDriver.OpenDevice("192.168.4.1", 0,
			// 0,0,0,
			// false);
			// m_nDeviceHandle = mLotusCardDriver.OpenDevice("192.168.1.107", 0,
			// 0,0,0,//使用内部默认超时设置
			// false);
			// m_nDeviceHandle = mLotusCardDriver.OpenDevice("192.168.1.115", 0,
			// 0,0,100,//把超时设置为100秒
			// false);
			m_nDeviceHandle = mLotusCardDriver.OpenDevice("", 0, 0, 0, 0,// 使用内部默认超时设置
					true);

		}
		if (m_nDeviceHandle != -1) {
			// AddLog("Open Device Success!");
			// testIcCardReader(m_nDeviceHandle);
			// testEasyApi(m_nDeviceHandle);
			// testGetCardNoEx(m_nDeviceHandle);
			// mLotusCardDriver.CloseDevice(m_nDeviceHandle);
			// testTextReadWrite(m_nDeviceHandle);
			// testNfcCardMode(m_nDeviceHandle);
			testTwoGenerationIDCardNo(m_nDeviceHandle);
		} else {
			AddLog("Open Device False!");
		}
		m_btnAutoTest.setClickable(true);

	}

	public void OnClearLogListener(View arg0) {
		m_imgIdPhoto.setBackgroundColor(0);
		if (null == m_edtLog)
			return;
		m_edtLog.setText("");

	}

	public void OnTestBeijingCardListener(View arg0) {

		if (null == mLotusCardDriver)
			return;
		m_btnTestBeijingCard.setClickable(false);
		if (m_nDeviceHandle == -1) {
			m_nDeviceHandle = mLotusCardDriver.OpenDevice("", 0, 0, 0, 0,// 使用内部默认超时设置
					true);

		}
		if (m_nDeviceHandle != -1) {
			testBeijingCard(m_nDeviceHandle);

		} else {
			AddLog("Open Device False!");
		}
		m_btnTestBeijingCard.setClickable(true);
	}

	public void OnTestIdListener(View arg0) {

		if (null == mLotusCardDriver)
			return;
		m_btnTestBeijingCard.setClickable(false);
		if (m_nDeviceHandle == -1) {
			m_nDeviceHandle = mLotusCardDriver.OpenDevice("", 0, 0, 0, 0,// 使用内部默认超时设置
					true);

		}
		if (m_nDeviceHandle != -1) {
			try {
				testId("120.24.253.33", m_nDeviceHandle);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			AddLog("Open Device False!");
		}
		m_btnTestBeijingCard.setClickable(true);
	}

	public void OnTestNtag203Listener(View arg0) {

		if (null == mLotusCardDriver)
			return;
		m_btnTestNtag203.setClickable(false);
		if (m_nDeviceHandle == -1) {
			// m_nDeviceHandle = mLotusCardDriver.OpenDevice("192.168.4.1", 0,
			// 0,0,0,
			// m_chkUseUsbHostApi.isChecked());
			// m_nDeviceHandle = mLotusCardDriver.OpenDevice("192.168.4.1", 0,
			// 0,0,0,
			// false);
			// m_nDeviceHandle = mLotusCardDriver.OpenDevice("192.168.1.107", 0,
			// 0,0,0,//使用内部默认超时设置
			// false);
			// m_nDeviceHandle = mLotusCardDriver.OpenDevice("192.168.1.115", 0,
			// 0,0,100,//把超时设置为100秒
			// false);
			m_nDeviceHandle = mLotusCardDriver.OpenDevice("", 0, 0, 0, 0,// 使用内部默认超时设置
					true);

		}
		if (m_nDeviceHandle != -1) {
			// AddLog("Open Device Success!");
			// testIcCardReader(m_nDeviceHandle);
			// testEasyApi(m_nDeviceHandle);
			// testGetCardNoEx(m_nDeviceHandle);
			// mLotusCardDriver.CloseDevice(m_nDeviceHandle);
			// testTextReadWrite(m_nDeviceHandle);
			testNtag203(m_nDeviceHandle);

		} else {
			AddLog("Open Device False!");
		}
		m_btnTestNtag203.setClickable(true);
	}

	public void OnAutoTestListener(View arg0) {
		if (-1 == m_nDeviceHandle) {
			m_nDeviceHandle = mLotusCardDriver.OpenDevice("", 0, 0, 0, 0,
					m_chkUseUsbHostApi.isChecked());
		}
		if (null == m_CardOperateThread) {
			m_CardOperateThread = new CardOperateThread();
		}
		if ((m_nDeviceHandle != -1) && (null != m_CardOperateThread)) {
			if (false == m_bCardOperateThreadRunning) {
				m_CardOperateThread.start();
				m_btnAutoTest.setText("AutoTesting");
				m_tvMessage.setText("Message:Please Put the IC Card to Reader");
			} else {
				m_CardOperateThread.cancel();
				m_btnAutoTest.setText("AutoTest");
				m_tvMessage.setText("Message:");
			}
			m_bCardOperateThreadRunning = !m_bCardOperateThreadRunning;

		}
	}

	private void testGetCardNoEx(long nDeviceHandle) {
		boolean bResult = false;
		int nRequestType;
		long lCardNo = 0;
		int nErrorCode = 0;
		nRequestType = LotusCardDriver.RT_NOT_HALT;
		LotusCardParam tLotusCardParam = new LotusCardParam();
		bResult = mLotusCardDriver.GetCardNoEx(nDeviceHandle, nRequestType,
				(byte) 10, (byte) 1, (byte) 1, tLotusCardParam);
		if (!bResult) {
			AddLog("Call GetCardNoEx Error!");
			nErrorCode = mLotusCardDriver.GetErrorCode(nDeviceHandle);
			AddLog(GetErrorInfo(nErrorCode));
			// //超时错误 关闭重连
			// if(LotusCardErrorCode.LCEC_RECV_TIME_OUT ==
			// LotusCardErrorCode.values()[nErrorCode])
			// {
			// mLotusCardDriver.CloseDevice(m_nDeviceHandle);
			// m_nDeviceHandle = -1;
			//
			// }

			return;
		}
		lCardNo = bytes2long(tLotusCardParam.arrCardNo);
		AddLog("Call GetCardNoEx Ok!");
		AddLog("CardNo(DEC):" + lCardNo);
		AddLog("CardNo(HEX):"
				+ leftString(
						"00"
								+ Integer
										.toHexString(tLotusCardParam.arrCardNo[3]),
						2).toUpperCase()
				+ leftString(
						"00"
								+ Integer
										.toHexString(tLotusCardParam.arrCardNo[2]),
						2).toUpperCase()
				+ leftString(
						"00"
								+ Integer
										.toHexString(tLotusCardParam.arrCardNo[1]),
						2).toUpperCase()
				+ leftString(Integer.toHexString(tLotusCardParam.arrCardNo[0]),
						2).toUpperCase());
	}

	private void testTwoGenerationIDCardNo(long nDeviceHandle) {
		boolean bResult = false;
		String strTwoGenerationIDCardNo = "";
		bResult = mLotusCardDriver.SetCardType(nDeviceHandle, 'B');
		if (!bResult)
			return;
		strTwoGenerationIDCardNo = mLotusCardDriver
				.GetTwoGenerationIDCardNo(nDeviceHandle);
		// AddLog(strTwoGenerationIDCardNo);
	}

	private void testNfcCardMode(long nDeviceHandle) {
		boolean bResult = false;
		String strNfcWriteBuffer = "处处闻啼鸟";
		String strNfcReadBuffer = "";
		bResult = mLotusCardDriver.SetCardType(nDeviceHandle, 'C');
		if (!bResult)
			return;
		strNfcReadBuffer = mLotusCardDriver.ReadNfcBuffer(nDeviceHandle);
		// AddLog(strNfcReadBuffer);
		bResult = mLotusCardDriver.WriteNfcBuffer(nDeviceHandle,
				strNfcWriteBuffer);
		if (!bResult)
			return;
	}

	private void testEasyApi(long nDeviceHandle) {
		boolean bResult = false;
		int nRequestType;
		long lCardNo = 0;
		int nErrorCode = 0;
		nRequestType = LotusCardDriver.RT_NOT_HALT;
		LotusCardParam tLotusCardParam = new LotusCardParam();
		tLotusCardParam.arrKeys[0] = (byte) 0xff;
		tLotusCardParam.arrKeys[1] = (byte) 0xff;
		tLotusCardParam.arrKeys[2] = (byte) 0xff;
		tLotusCardParam.arrKeys[3] = (byte) 0xff;
		tLotusCardParam.arrKeys[4] = (byte) 0xff;
		tLotusCardParam.arrKeys[5] = (byte) 0xff;
		tLotusCardParam.nKeysSize = 6;
		bResult = mLotusCardDriver.ReadData(nDeviceHandle, nRequestType, 5,
				(byte) 1, (byte) 0, (byte) 0, tLotusCardParam);
		if (!bResult) {
			AddLog("Call ReadData Error!");
			nErrorCode = mLotusCardDriver.GetErrorCode(nDeviceHandle);
			AddLog(GetErrorInfo(nErrorCode));
			// 超时错误 关闭重连
			if (LotusCardErrorCode.LCEC_RECV_TIME_OUT == LotusCardErrorCode
					.values()[nErrorCode]) {
				mLotusCardDriver.CloseDevice(m_nDeviceHandle);
				m_nDeviceHandle = -1;

			}

			return;
		}
		lCardNo = bytes2long(tLotusCardParam.arrCardNo);
		AddLog("Call ReadData Ok!");
		AddLog("CardNo(DEC):" + lCardNo);
		AddLog("CardNo(HEX):"
				+ leftString(
						"00"
								+ Integer
										.toHexString(tLotusCardParam.arrCardNo[3]),
						2).toUpperCase()
				+ leftString(
						"00"
								+ Integer
										.toHexString(tLotusCardParam.arrCardNo[2]),
						2).toUpperCase()
				+ leftString(
						"00"
								+ Integer
										.toHexString(tLotusCardParam.arrCardNo[1]),
						2).toUpperCase()
				+ leftString(Integer.toHexString(tLotusCardParam.arrCardNo[0]),
						2).toUpperCase());

		AddLog("Buffer(HEX):"
				+ leftString(
						"00"
								+ Integer
										.toHexString(tLotusCardParam.arrBuffer[0]),
						2).toUpperCase()
				+ leftString(
						"00"
								+ Integer
										.toHexString(tLotusCardParam.arrBuffer[1]),
						2).toUpperCase()
				+ leftString(
						"00"
								+ Integer
										.toHexString(tLotusCardParam.arrBuffer[2]),
						2).toUpperCase()
				+ leftString(
						"00"
								+ Integer
										.toHexString(tLotusCardParam.arrBuffer[3]),
						2).toUpperCase()
				+ leftString(
						"00"
								+ Integer
										.toHexString(tLotusCardParam.arrBuffer[4]),
						2).toUpperCase()
				+ leftString(
						"00"
								+ Integer
										.toHexString(tLotusCardParam.arrBuffer[5]),
						2).toUpperCase()
				+ leftString(
						"00"
								+ Integer
										.toHexString(tLotusCardParam.arrBuffer[6]),
						2).toUpperCase()
				+ leftString(
						"00"
								+ Integer
										.toHexString(tLotusCardParam.arrBuffer[7]),
						2).toUpperCase()
				+ leftString(
						"00"
								+ Integer
										.toHexString(tLotusCardParam.arrBuffer[8]),
						2).toUpperCase()
				+ leftString(
						"00"
								+ Integer
										.toHexString(tLotusCardParam.arrBuffer[9]),
						2).toUpperCase()
				+ leftString(
						"00"
								+ Integer
										.toHexString(tLotusCardParam.arrBuffer[0xa]),
						2).toUpperCase()
				+ leftString(
						"00"
								+ Integer
										.toHexString(tLotusCardParam.arrBuffer[0xb]),
						2).toUpperCase()
				+ leftString(
						"00"
								+ Integer
										.toHexString(tLotusCardParam.arrBuffer[0xc]),
						2).toUpperCase()
				+ leftString(
						"00"
								+ Integer
										.toHexString(tLotusCardParam.arrBuffer[0xd]),
						2).toUpperCase()
				+ leftString(
						"00"
								+ Integer
										.toHexString(tLotusCardParam.arrBuffer[0xe]),
						2).toUpperCase()
				+ leftString(
						"00"
								+ Integer
										.toHexString(tLotusCardParam.arrBuffer[0xf]),
						2).toUpperCase());
		m_bytTest++;
		tLotusCardParam.arrBuffer[0] = (byte) m_bytTest;
		tLotusCardParam.arrBuffer[1] = (byte) 0x01;
		tLotusCardParam.arrBuffer[2] = (byte) 0x02;
		tLotusCardParam.arrBuffer[3] = (byte) 0x03;
		tLotusCardParam.arrBuffer[4] = (byte) 0x04;
		tLotusCardParam.arrBuffer[5] = (byte) 0x05;
		tLotusCardParam.arrBuffer[6] = (byte) 0x06;
		tLotusCardParam.arrBuffer[7] = (byte) 0x07;
		tLotusCardParam.arrBuffer[8] = (byte) 0x08;
		tLotusCardParam.arrBuffer[9] = (byte) 0x09;
		tLotusCardParam.arrBuffer[10] = (byte) 0x0a;
		tLotusCardParam.arrBuffer[11] = (byte) 0x0b;
		tLotusCardParam.arrBuffer[12] = (byte) 0x0c;
		tLotusCardParam.arrBuffer[13] = (byte) 0x0d;
		tLotusCardParam.arrBuffer[14] = (byte) 0x0e;
		tLotusCardParam.arrBuffer[15] = (byte) 0x0f;
		tLotusCardParam.nBufferSize = 16;
		bResult = mLotusCardDriver.WriteData(nDeviceHandle, 5, (byte) 10,
				(byte) 0, tLotusCardParam);
		if (!bResult) {
			AddLog("Call Write Error!");
			nErrorCode = mLotusCardDriver.GetErrorCode(nDeviceHandle);
			AddLog(GetErrorInfo(nErrorCode));
			// 超时错误 关闭重连
			if (LotusCardErrorCode.LCEC_RECV_TIME_OUT == LotusCardErrorCode
					.values()[nErrorCode]) {
				mLotusCardDriver.CloseDevice(m_nDeviceHandle);
				m_nDeviceHandle = -1;

			}
			return;
		}
		AddLog("Call Write Ok!");
	}

	private void testIcCardReader(long nDeviceHandle) {
		boolean bResult = false;
		int nRequestType;
		long lCardNo = 0;
		LotusCardParam tLotusCardParam1 = new LotusCardParam();
		bResult = mLotusCardDriver.Beep(nDeviceHandle, 10);
		// bResult = mLotusCardDriver.Beep(nDeviceHandle, 10);
		if (!bResult) {
			AddLog("Call Beep Error!");
			return;
		}
		AddLog("Call Beep Ok!");
		nRequestType = LotusCardDriver.RT_NOT_HALT;
		// 以下3个函数可以用GetCardNo替代
		// bResult = mLotusCardDriver.Request(nDeviceHandle, nRequestType,
		// tLotusCardParam1);
		// if (!bResult)
		// return;
		// bResult = mLotusCardDriver.Anticoll(nDeviceHandle, tLotusCardParam1);
		// if (!bResult)
		// return;
		// bResult = mLotusCardDriver.Select(nDeviceHandle, tLotusCardParam1);
		// if (!bResult)
		// return;
		bResult = mLotusCardDriver.GetCardNo(nDeviceHandle, nRequestType,
				tLotusCardParam1);
		if (!bResult) {
			AddLog("Call GetCardNo Error!");
			return;
		}
		lCardNo = bytes2long(tLotusCardParam1.arrCardNo);
		AddLog("Call GetCardNo Ok!");
		AddLog("CardNo(DEC):" + lCardNo);
		AddLog("CardNo(HEX):"
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrCardNo[3]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrCardNo[2]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrCardNo[1]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrCardNo[0]), 2)
						.toUpperCase());
		tLotusCardParam1.arrKeys[0] = (byte) 0xff;
		tLotusCardParam1.arrKeys[1] = (byte) 0xff;
		tLotusCardParam1.arrKeys[2] = (byte) 0xff;
		tLotusCardParam1.arrKeys[3] = (byte) 0xff;
		tLotusCardParam1.arrKeys[4] = (byte) 0xff;
		tLotusCardParam1.arrKeys[5] = (byte) 0xff;
		tLotusCardParam1.nKeysSize = 6;
		bResult = mLotusCardDriver.LoadKey(nDeviceHandle, LotusCardDriver.AM_A,
				0, tLotusCardParam1);
		if (!bResult) {
			AddLog("Call LoadKey Error!");
			return;
		}
		AddLog("Call LoadKey Ok!");
		bResult = mLotusCardDriver.Authentication(nDeviceHandle,
				LotusCardDriver.AM_A, 0, tLotusCardParam1);
		if (!bResult) {
			AddLog("Call Authentication(A) Error!");
			return;
		}
		AddLog("Call Authentication(A) Ok!");
		bResult = mLotusCardDriver.Read(nDeviceHandle, 1, tLotusCardParam1);
		if (!bResult) {
			AddLog("Call Read Error!");
			return;
		}
		AddLog("Call Read Ok!");
		AddLog("Buffer(HEX):"
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrBuffer[0]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrBuffer[1]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrBuffer[2]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrBuffer[3]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrBuffer[4]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrBuffer[5]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrBuffer[6]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrBuffer[7]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrBuffer[8]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrBuffer[9]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrBuffer[0xa]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrBuffer[0xb]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrBuffer[0xc]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrBuffer[0xd]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrBuffer[0xe]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrBuffer[0xf]), 2)
						.toUpperCase());
		tLotusCardParam1.arrBuffer[0] = (byte) 0x10;
		tLotusCardParam1.arrBuffer[1] = (byte) 0x01;
		tLotusCardParam1.arrBuffer[2] = (byte) 0x02;
		tLotusCardParam1.arrBuffer[3] = (byte) 0x03;
		tLotusCardParam1.arrBuffer[4] = (byte) 0x04;
		tLotusCardParam1.arrBuffer[5] = (byte) 0x05;
		tLotusCardParam1.arrBuffer[6] = (byte) 0x06;
		tLotusCardParam1.arrBuffer[7] = (byte) 0x07;
		tLotusCardParam1.arrBuffer[8] = (byte) 0x08;
		tLotusCardParam1.arrBuffer[9] = (byte) 0x09;
		tLotusCardParam1.arrBuffer[10] = (byte) 0x0a;
		tLotusCardParam1.arrBuffer[11] = (byte) 0x0b;
		tLotusCardParam1.arrBuffer[12] = (byte) 0x0c;
		tLotusCardParam1.arrBuffer[13] = (byte) 0x0d;
		tLotusCardParam1.arrBuffer[14] = (byte) 0x0e;
		tLotusCardParam1.arrBuffer[15] = (byte) 0x0f;
		tLotusCardParam1.nBufferSize = 16;
		bResult = mLotusCardDriver.Write(nDeviceHandle, 1, tLotusCardParam1);
		if (!bResult) {
			AddLog("Call Write Error!");
			return;
		}
		AddLog("Call Write Ok!");
	}

	private void testBeijingCard(long nDeviceHandle) {
		boolean bResult = false;
		int nRequestType;
		byte[] arrResult;
		String strResult;
		LotusCardParam tLotusCardParam1 = new LotusCardParam();
		bResult = mLotusCardDriver.Beep(nDeviceHandle, 10);
		if (!bResult)
			return;
		bResult = mLotusCardDriver
				.ResetCpuCard(nDeviceHandle, tLotusCardParam1);
		if (!bResult)
			return;

		// 选择1PAY.SYS.DDF01
		arrResult = mLotusCardDriver.CpuCardSelectByName(nDeviceHandle,
				"1PAY.SYS.DDF01");
		AddLog(toHexString(arrResult, 0, arrResult.length));
		// 读取二进制数据
		arrResult = mLotusCardDriver.CpuCardReadBinary(nDeviceHandle,
				SFI_EXTRA_LOG);
		strResult = toHexString(arrResult, 0, arrResult.length);
		AddLog(strResult);
		AddLog("卡片序号" + strResult.substring(0, 16));

	}

	private void testId(String strServerIp, long nDeviceHandle)
			throws UnsupportedEncodingException {
		boolean bResult = false;
		int nRequestType;
		long lCardNo = 0;
		String temp = null;
		TwoIdInfoParam tTwoIdInfo = new TwoIdInfoParam();
		bResult = mLotusCardDriver.Beep(nDeviceHandle, 10);
		if (!bResult) {
			AddLog("Call Beep Error!");
			return;
		}
		AddLog("Call Beep Ok!");
		bResult = mLotusCardDriver.SetCardType(nDeviceHandle, 'B');
		if (!bResult) {
			AddLog("Call SetCardType Error!");
			return;
		}
		AddLog("Call SetCardType Ok!");

//		bResult = mLotusCardDriver.GetTwoIdInfoByServer(nDeviceHandle,
//				strServerIp, tTwoIdInfo);
//		if (!bResult) {
//			AddLog("Call GetTwoIdInfoByServer Error! ErrorCode:"
//					+ mLotusCardDriver.GetTwoIdErrorCode(nDeviceHandle));
//			return;
//		}
//		AddLog("Call GetTwoIdInfoByServer Ok!");

		//直接操作 内部有重试机制
		bResult = mLotusCardDriver.GetTwoIdInfoByWireless(nDeviceHandle, "58.253.96.229", 10000,tTwoIdInfo, 2);
		if (!bResult) {
			AddLog("Call GetTwoIdInfoByWireless Error! ErrorCode:" + mLotusCardDriver.GetTwoIdErrorCode(nDeviceHandle));
			return;
		}
		AddLog("Call GetTwoIdInfoByWireless Ok!");
		//处理照片
		if(0x00 == tTwoIdInfo.unTwoIdPhotoJpegLength)
		{
			bResult = mLotusCardDriver.WlDecodeByServer(nDeviceHandle, "120.24.249.49", tTwoIdInfo);
			if (!bResult) {
				AddLog("Call WlDecodeByServer Error! ");
			}
			else
			{
				AddLog("Call WlDecodeByServer Ok!");
			}
			
		}
		
		// 姓名
		temp = new String(tTwoIdInfo.arrTwoIdName, 0, 30, "UTF-16LE").trim();

		AddLog("姓名:" + temp);
		// 性别
		temp = new String(tTwoIdInfo.arrTwoIdSex, 0, 2, "UTF-16LE").trim();
		if (temp.equals("1"))
			temp = "男";
		else
			temp = "女";
		AddLog("性别:" + temp);
		// 民族
		temp = new String(tTwoIdInfo.arrTwoIdNation, 0, 4, "UTF-16LE").trim();
		try {
			int code = Integer.parseInt(temp.toString());
			temp = decodeNation(code);
		} catch (Exception e) {
			temp = "";
		}
		AddLog("民族:" + temp);
		// 出生日期
		temp = new String(tTwoIdInfo.arrTwoIdBirthday, 0, 16, "UTF-16LE")
				.trim();
		AddLog("出生日期:" + temp);
		// 住址
		temp = new String(tTwoIdInfo.arrTwoIdAddress, 0, 70, "UTF-16LE").trim();
		AddLog("住址:" + temp);
		// 身份证号码
		temp = new String(tTwoIdInfo.arrTwoIdNo, 0, 36, "UTF-16LE").trim();
		AddLog("身份证号码:" + temp);
		// 签发机关
		temp = new String(tTwoIdInfo.arrTwoIdSignedDepartment, 0, 30,
				"UTF-16LE").trim();
		AddLog("签发机关:" + temp);
		// 有效期起始日期
		temp = new String(tTwoIdInfo.arrTwoIdValidityPeriodBegin, 0, 16,
				"UTF-16LE").trim();
		AddLog("有效期起始日期:" + temp);
		// 有效期截止日期 UNICODE YYYYMMDD 有效期为长期时存储“长期”
		temp = new String(tTwoIdInfo.arrTwoIdValidityPeriodEnd, 0, 16,
				"UTF-16LE").trim();
		AddLog("有效期截止日期:" + temp);
		if (tTwoIdInfo.unTwoIdPhotoJpegLength > 0) {
			Bitmap photo = BitmapFactory.decodeByteArray(
					tTwoIdInfo.arrTwoIdPhotoJpeg, 0,
					tTwoIdInfo.unTwoIdPhotoJpegLength);
			m_imgIdPhoto.setBackgroundDrawable(new BitmapDrawable(photo));
		}

	}

	private void testNtag203(long nDeviceHandle) {
		boolean bResult = false;
		int nRequestType;
		long lCardNo = 0;
		LotusCardParam tLotusCardParam1 = new LotusCardParam();
		bResult = mLotusCardDriver.Beep(nDeviceHandle, 10);
		if (!bResult) {
			AddLog("Call Beep Error!");
			return;
		}
		AddLog("Call Beep Ok!");
		nRequestType = LotusCardDriver.RT_NOT_HALT;
		bResult = mLotusCardDriver.Request(nDeviceHandle, nRequestType,
				tLotusCardParam1);
		if (!bResult)
			return;
		AddLog("CardType:" + tLotusCardParam1.nCardType);

		bResult = mLotusCardDriver.Anticoll(nDeviceHandle, tLotusCardParam1);
		if (!bResult) {
			AddLog("Call GetCardNo Error!");
			return;
		}
		// lCardNo = bytes2long(tLotusCardParam1.arrCardNo);
		AddLog("Call GetCardNo Ok!");
		// AddLog("CardNo(DEC):" + lCardNo);
		AddLog("CardNo(HEX):"
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrCardNo[6]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrCardNo[5]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrCardNo[4]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrCardNo[3]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrCardNo[2]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrCardNo[1]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrCardNo[0]), 2)
						.toUpperCase());

		bResult = mLotusCardDriver.Read(nDeviceHandle, 8, tLotusCardParam1);
		if (!bResult) {
			AddLog("Call Read Error!");
			return;
		}
		AddLog("Call Read Ok!");
		AddLog("Buffer(HEX):"
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrBuffer[0]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrBuffer[1]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrBuffer[2]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrBuffer[3]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrBuffer[4]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrBuffer[5]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrBuffer[6]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrBuffer[7]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrBuffer[8]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrBuffer[9]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrBuffer[0xa]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrBuffer[0xb]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrBuffer[0xc]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrBuffer[0xd]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrBuffer[0xe]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrBuffer[0xf]), 2)
						.toUpperCase());
		tLotusCardParam1.arrBuffer[0] = (byte) 0x10;
		tLotusCardParam1.arrBuffer[1] = (byte) 0x01;
		tLotusCardParam1.arrBuffer[2] = (byte) 0x02;
		tLotusCardParam1.arrBuffer[3] = (byte) 0x03;
		tLotusCardParam1.nBufferSize = 16;
		bResult = mLotusCardDriver.Write(nDeviceHandle, 8, tLotusCardParam1);
		if (!bResult) {
			AddLog("Call Write Error!");
			return;
		}
		AddLog("Call Write Ok!");
	}

	private void lockNtag21x(long nDeviceHandle) {
		boolean bResult = false;
		int nRequestType;
		long lCardNo = 0;
		LotusCardParam tLotusCardParam1 = new LotusCardParam();
		byte[] arrNtagVersionBuffer = new byte[8];
		byte[] arrPassword = new byte[4];
		int nPwdPageIndex = 43;
		int nAuth0PageIndex = 41;

		bResult = mLotusCardDriver.Beep(nDeviceHandle, 10);
		if (!bResult) {
			AddLog("Call Beep Error!");
			return;
		}
		AddLog("Call Beep Ok!");
		nRequestType = LotusCardDriver.RT_NOT_HALT;
		bResult = mLotusCardDriver.Request(nDeviceHandle, nRequestType,
				tLotusCardParam1);
		if (!bResult)
			return;
		AddLog("CardType:" + tLotusCardParam1.nCardType);

		bResult = mLotusCardDriver.Anticoll(nDeviceHandle, tLotusCardParam1);
		if (!bResult) {
			AddLog("Call GetCardNo Error!");
			return;
		}
		// lCardNo = bytes2long(tLotusCardParam1.arrCardNo);
		AddLog("Call GetCardNo Ok!");
		// AddLog("CardNo(DEC):" + lCardNo);
		AddLog("CardNo(HEX):"
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrCardNo[6]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrCardNo[5]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrCardNo[4]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrCardNo[3]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrCardNo[2]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrCardNo[1]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrCardNo[0]), 2)
						.toUpperCase());

		bResult = mLotusCardDriver.Read(nDeviceHandle, 8, tLotusCardParam1);
		if (!bResult) {
			AddLog("Call Read Error!");
			return;
		}
		AddLog("Call Read Ok!");
		AddLog("Buffer(HEX):"
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrBuffer[0]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrBuffer[1]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrBuffer[2]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrBuffer[3]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrBuffer[4]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrBuffer[5]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrBuffer[6]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrBuffer[7]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrBuffer[8]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrBuffer[9]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrBuffer[0xa]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrBuffer[0xb]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrBuffer[0xc]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrBuffer[0xd]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrBuffer[0xe]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrBuffer[0xf]), 2)
						.toUpperCase());

		bResult = mLotusCardDriver.NtagGetVersion(nDeviceHandle,
				arrNtagVersionBuffer);
		if (!bResult) {
			AddLog("NtagGetVersion Error!");
			return;
		}
		AddLog("NtagGetVersion Ok!");
		AddLog("Version Buffer(HEX):"
				+ leftString(Integer.toHexString(arrNtagVersionBuffer[0]), 2)
						.toUpperCase()
				+ leftString(Integer.toHexString(arrNtagVersionBuffer[1]), 2)
						.toUpperCase()
				+ leftString(Integer.toHexString(arrNtagVersionBuffer[2]), 2)
						.toUpperCase()
				+ leftString(Integer.toHexString(arrNtagVersionBuffer[3]), 2)
						.toUpperCase()
				+ leftString(Integer.toHexString(arrNtagVersionBuffer[4]), 2)
						.toUpperCase()
				+ leftString(Integer.toHexString(arrNtagVersionBuffer[5]), 2)
						.toUpperCase()
				+ leftString(Integer.toHexString(arrNtagVersionBuffer[6]), 2)
						.toUpperCase()
				+ leftString(Integer.toHexString(arrNtagVersionBuffer[7]), 2)
						.toUpperCase());
		switch (arrNtagVersionBuffer[6]) {
		case 0x0f:
			AddLog("卡片型号：NTAG213");
			nPwdPageIndex = 43;
			nAuth0PageIndex = 41;
			break;
		case 0x11:
			AddLog("卡片型号：NTAG215");
			nPwdPageIndex = 133;
			nAuth0PageIndex = 131;
			break;
		case 0x13:
			AddLog("卡片型号：NTAG216");
			nPwdPageIndex = 229;
			nAuth0PageIndex = 227;

			break;
		default:
			AddLog("卡片型号：未知");
			return;
		}
		// 设置密码
		tLotusCardParam1.arrBuffer[0] = (byte) 0x31;
		tLotusCardParam1.arrBuffer[1] = (byte) 0x32;
		tLotusCardParam1.arrBuffer[2] = (byte) 0x33;
		tLotusCardParam1.arrBuffer[3] = (byte) 0x34;
		tLotusCardParam1.nBufferSize = 16;
		bResult = mLotusCardDriver.Write(nDeviceHandle, nPwdPageIndex,
				tLotusCardParam1);
		if (!bResult) {
			AddLog("Call Write Error!");
			return;
		}
		AddLog("Call Write Ok!");
		// 设置密码生效位
		tLotusCardParam1.arrBuffer[0] = (byte) 0x04;
		tLotusCardParam1.arrBuffer[1] = (byte) 0x00;
		tLotusCardParam1.arrBuffer[2] = (byte) 0x00;
		tLotusCardParam1.arrBuffer[3] = (byte) 0x04;// 加密生效
		tLotusCardParam1.nBufferSize = 16;
		bResult = mLotusCardDriver.Write(nDeviceHandle, nAuth0PageIndex,
				tLotusCardParam1);
		if (!bResult) {
			AddLog("Call Write Error!");
			return;
		}
		AddLog("Call Write Ok!");

	}

	private void unlockNtag21x(long nDeviceHandle) {
		boolean bResult = false;
		int nRequestType;
		long lCardNo = 0;
		byte[] arrNtagVersionBuffer = new byte[8];
		byte[] arrPassword = new byte[4];
		int nPwdPageIndex = 43;
		int nAuth0PageIndex = 41;

		LotusCardParam tLotusCardParam1 = new LotusCardParam();
		bResult = mLotusCardDriver.Beep(nDeviceHandle, 10);
		if (!bResult) {
			AddLog("Call Beep Error!");
			return;
		}
		AddLog("Call Beep Ok!");
		nRequestType = LotusCardDriver.RT_NOT_HALT;
		bResult = mLotusCardDriver.Request(nDeviceHandle, nRequestType,
				tLotusCardParam1);
		if (!bResult)
			return;
		AddLog("CardType:" + tLotusCardParam1.nCardType);

		bResult = mLotusCardDriver.Anticoll(nDeviceHandle, tLotusCardParam1);
		if (!bResult) {
			AddLog("Call GetCardNo Error!");
			return;
		}
		// lCardNo = bytes2long(tLotusCardParam1.arrCardNo);
		AddLog("Call GetCardNo Ok!");
		// AddLog("CardNo(DEC):" + lCardNo);
		AddLog("CardNo(HEX):"
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrCardNo[6]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrCardNo[5]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrCardNo[4]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrCardNo[3]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrCardNo[2]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrCardNo[1]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrCardNo[0]), 2)
						.toUpperCase());
		bResult = mLotusCardDriver.NtagGetVersion(nDeviceHandle,
				arrNtagVersionBuffer);
		if (!bResult) {
			AddLog("NtagGetVersion Error!");
			return;
		}
		AddLog("NtagGetVersion Ok!");
		AddLog("Version Buffer(HEX):"
				+ leftString(Integer.toHexString(arrNtagVersionBuffer[0]), 2)
						.toUpperCase()
				+ leftString(Integer.toHexString(arrNtagVersionBuffer[1]), 2)
						.toUpperCase()
				+ leftString(Integer.toHexString(arrNtagVersionBuffer[2]), 2)
						.toUpperCase()
				+ leftString(Integer.toHexString(arrNtagVersionBuffer[3]), 2)
						.toUpperCase()
				+ leftString(Integer.toHexString(arrNtagVersionBuffer[4]), 2)
						.toUpperCase()
				+ leftString(Integer.toHexString(arrNtagVersionBuffer[5]), 2)
						.toUpperCase()
				+ leftString(Integer.toHexString(arrNtagVersionBuffer[6]), 2)
						.toUpperCase()
				+ leftString(Integer.toHexString(arrNtagVersionBuffer[7]), 2)
						.toUpperCase());
		switch (arrNtagVersionBuffer[6]) {
		case 0x0f:
			AddLog("卡片型号：NTAG213");
			nPwdPageIndex = 43;
			nAuth0PageIndex = 41;
			break;
		case 0x11:
			AddLog("卡片型号：NTAG215");
			nPwdPageIndex = 133;
			nAuth0PageIndex = 131;
			break;
		case 0x13:
			AddLog("卡片型号：NTAG216");
			nPwdPageIndex = 229;
			nAuth0PageIndex = 227;

			break;
		default:
			AddLog("卡片型号：未知");
			return;
		}
		// 认证密码
		arrPassword[0] = 0x31;
		arrPassword[1] = 0x32;
		arrPassword[2] = 0x33;
		arrPassword[3] = 0x34;
		bResult = mLotusCardDriver.NtagPwdAuth(nDeviceHandle, arrPassword);
		if (!bResult) {
			AddLog("调用LotusCardNtagPwdAuth失败!");
			return;
		}
		AddLog("调用LotusCardNtagPwdAuth成功!");
		// 设置密码生效位
		tLotusCardParam1.arrBuffer[0] = (byte) 0x04;
		tLotusCardParam1.arrBuffer[1] = (byte) 0x00;
		tLotusCardParam1.arrBuffer[2] = (byte) 0x00;
		tLotusCardParam1.arrBuffer[3] = (byte) 0xFF;// 取消加密
		tLotusCardParam1.nBufferSize = 16;
		bResult = mLotusCardDriver.Write(nDeviceHandle, nAuth0PageIndex,
				tLotusCardParam1);
		if (!bResult) {
			AddLog("Call Write Error!");
			return;
		}
		AddLog("Call Write Ok!");

	}

	private void testTextReadWrite(long nDeviceHandle) {
		boolean bResult = false;
		int nRequestType;
		long lCardNo = 0;
		LotusCardParam tLotusCardParam1 = new LotusCardParam();
		bResult = mLotusCardDriver.Beep(nDeviceHandle, 10);
		// bResult = mLotusCardDriver.Beep(nDeviceHandle, 10);
		if (!bResult) {
			AddLog("Call Beep Error!");
			return;
		}
		AddLog("Call Beep Ok!");
		nRequestType = LotusCardDriver.RT_NOT_HALT;

		bResult = mLotusCardDriver.GetCardNo(nDeviceHandle, nRequestType,
				tLotusCardParam1);
		if (!bResult) {
			AddLog("Call GetCardNo Error!");
			return;
		}
		lCardNo = bytes2long(tLotusCardParam1.arrCardNo);
		AddLog("Call GetCardNo Ok!");
		AddLog("CardNo(DEC):" + lCardNo);
		AddLog("CardNo(HEX):"
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrCardNo[3]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrCardNo[2]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrCardNo[1]), 2)
						.toUpperCase()
				+ leftString(
						Integer.toHexString(tLotusCardParam1.arrCardNo[0]), 2)
						.toUpperCase());
		tLotusCardParam1.arrKeys[0] = (byte) 0xff;
		tLotusCardParam1.arrKeys[1] = (byte) 0xff;
		tLotusCardParam1.arrKeys[2] = (byte) 0xff;
		tLotusCardParam1.arrKeys[3] = (byte) 0xff;
		tLotusCardParam1.arrKeys[4] = (byte) 0xff;
		tLotusCardParam1.arrKeys[5] = (byte) 0xff;
		tLotusCardParam1.nKeysSize = 6;
		bResult = mLotusCardDriver.LoadKey(nDeviceHandle, LotusCardDriver.AM_A,
				10, tLotusCardParam1);
		if (!bResult) {
			AddLog("Call LoadKey Error!");
			return;
		}
		AddLog("Call LoadKey Ok!");
		bResult = mLotusCardDriver.Authentication(nDeviceHandle,
				LotusCardDriver.AM_A, 10, tLotusCardParam1);
		if (!bResult) {
			AddLog("Call Authentication(A) Error!");
			return;
		}
		AddLog("Call Authentication(A) Ok!");

		String strTextInfo = mLotusCardDriver.ReadText(nDeviceHandle, 10);
		AddLog("ReadText:" + strTextInfo);
		strTextInfo = "Chong Qing Lotus Smart LTD";
		bResult = mLotusCardDriver.WriteText(nDeviceHandle, 10, strTextInfo);
		if (!bResult) {
			AddLog("Call WriteText Error!");
			return;
		}
		AddLog("Call WriteText Ok!");

	}

	private String GetErrorInfo(int nErrorCode) {
		String strResult = "未知的错误";
		if (LotusCardErrorCode.LCEC_SEND_COMMAND.ordinal() < nErrorCode)
			return strResult;
		switch (LotusCardErrorCode.values()[nErrorCode]) {
		case LCEC_OK:
			strResult = "正常执行";
			break;
		case LCED_UNKNOWN: // 未知的
			strResult = "未知的错误";
			break;
		case LCEC_SEND_FALSE: // 发送失败
			strResult = "发送失败";
			break;
		case LCEC_RECV_TIME_OUT: // 接收超时
			strResult = "接收超时";
			break;
		case LCEC_RECV_ZERO_LEN: // 接收长度为0
			strResult = "接收长度为0";
			break;
		case LCEC_RECV_CRC_FALSE: // 接收校验失败
			strResult = "接收校验失败";
			break;
		case LCEC_REQUEST: // 寻卡
			strResult = "寻卡错误";
			break;
		case LCEC_ANTICOLL: // 防冲突
			strResult = "防冲突错误";
			break;
		case LCEC_SELECT: // 选卡
			strResult = "选卡错误";
			break;
		case LCEC_AUTHENTICATION: // 三次验证
			strResult = "三次验证错误";
			break;
		case LCEC_HALT: // 中止
			strResult = "中止错误";
			break;
		case LCEC_READ: // 读
			strResult = "读错误";
			break;
		case LCEC_WRITE: // 写
			strResult = "写错误";
			break;
		case LCEC_INCREMENT: // 加值
			strResult = "加值错误";
			break;
		case LCEC_DECREMENT: // 减值
			strResult = "减值错误";
			break;
		case LCEC_LOADKEY: // 装载密码
			strResult = "装载密码错误";
			break;
		case LCEC_BEEP: // 蜂鸣
			strResult = "蜂鸣错误";
			break;
		case LCEC_RESTORE: // 卡数据块传入卡的内部寄存器
			strResult = "数据块传入卡的内部寄存器错误";
			break;
		case LCEC_TRANSFER: // 内部寄存器传入卡的卡数据块
			strResult = "内部寄存器传入卡的卡数据块错误";
			break;
		case LCEC_SEND_COMMAND: // 发送14443指令
			strResult = "发送14443指令错误";
			break;
		default:
			strResult = "未知的错误编码";
			break;

		}

		return strResult;
	}

//	private Boolean SetUsbCallBack() {
//		Boolean bResult = false;
//		PendingIntent pendingIntent;
//		pendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(
//				ACTION_USB_PERMISSION), 0);
//		m_UsbManager = (UsbManager) getSystemService(USB_SERVICE);
//		if (null == m_UsbManager)
//			return bResult;
//
//		HashMap<String, UsbDevice> deviceList = m_UsbManager.getDeviceList();
//		if (!deviceList.isEmpty()) {
//			for (UsbDevice device : deviceList.values()) {
//				if ((m_nVID == device.getVendorId())
//						&& (m_nPID == device.getProductId())) {
//					m_LotusCardDevice = device;
//					m_strDeviceNode = m_LotusCardDevice.getDeviceName();
//					break;
//				}
//			}
//		}
//		if (null == m_LotusCardDevice)
//			return bResult;
//		m_LotusCardInterface = m_LotusCardDevice.getInterface(0);
//		if (null == m_LotusCardInterface)
//			return bResult;
//		if (false == m_UsbManager.hasPermission(m_LotusCardDevice)) {
//			m_UsbManager.requestPermission(m_LotusCardDevice, pendingIntent);
//		}
//		UsbDeviceConnection conn = null;
//		if (m_UsbManager.hasPermission(m_LotusCardDevice)) {
//			conn = m_UsbManager.openDevice(m_LotusCardDevice);
//		}
//
//		if (null == conn)
//			return bResult;
//
//		if (conn.claimInterface(m_LotusCardInterface, true)) {
//			m_LotusCardDeviceConnection = conn;
//		} else {
//			conn.close();
//		}
//		if (null == m_LotusCardDeviceConnection)
//			return bResult;
//		// 把上面获取的对性设置到接口中用于回调操作
//		LotusCardDriver.m_UsbDeviceConnection = m_LotusCardDeviceConnection;
//		if (m_LotusCardInterface.getEndpoint(1) != null) {
//			LotusCardDriver.m_OutEndpoint = m_LotusCardInterface.getEndpoint(1);
//		}
//		if (m_LotusCardInterface.getEndpoint(0) != null) {
//			LotusCardDriver.m_InEndpoint = m_LotusCardInterface.getEndpoint(0);
//		}
//		bResult = true;
//		return bResult;
//	}

	public void AddLog(String strLog) {
		//SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS"); 
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String strDate = formatter.format(curDate);
		if (null == m_edtLog)
			return;
		String strLogs = m_edtLog.getText().toString().trim();
		if (strLogs.equals("")) {
			strLogs = strDate + " " + strLog;
		} else {
			strLogs += "\r\n" + strDate + " " + strLog;
		}
		m_edtLog.setText(strLogs);
	}

	public long bytes2long(byte[] byteNum) {
		long num = 0;
		for (int ix = 3; ix >= 0; --ix) {
			num <<= 8;
			if (byteNum[ix] < 0) {
				num |= (256 + (byteNum[ix]) & 0xff);
			} else {
				num |= (byteNum[ix] & 0xff);
			}
		}
		return num;
	}

	public String leftString(String strText, int nLeftLength) {
		if (1 == strText.length())
			strText = "0" + strText;
		if (strText.length() <= nLeftLength)
			return strText;

		return strText.substring(strText.length() - nLeftLength,
				strText.length());
	}

	private String decodeNation(int code) {
		String nation;
		switch (code) {
		case 1:
			nation = "汉";
			break;
		case 2:
			nation = "蒙古";
			break;
		case 3:
			nation = "回";
			break;
		case 4:
			nation = "藏";
			break;
		case 5:
			nation = "维吾尔";
			break;
		case 6:
			nation = "苗";
			break;
		case 7:
			nation = "彝";
			break;
		case 8:
			nation = "壮";
			break;
		case 9:
			nation = "布依";
			break;
		case 10:
			nation = "朝鲜";
			break;
		case 11:
			nation = "满";
			break;
		case 12:
			nation = "侗";
			break;
		case 13:
			nation = "瑶";
			break;
		case 14:
			nation = "白";
			break;
		case 15:
			nation = "土家";
			break;
		case 16:
			nation = "哈尼";
			break;
		case 17:
			nation = "哈萨克";
			break;
		case 18:
			nation = "傣";
			break;
		case 19:
			nation = "黎";
			break;
		case 20:
			nation = "傈僳";
			break;
		case 21:
			nation = "佤";
			break;
		case 22:
			nation = "畲";
			break;
		case 23:
			nation = "高山";
			break;
		case 24:
			nation = "拉祜";
			break;
		case 25:
			nation = "水";
			break;
		case 26:
			nation = "东乡";
			break;
		case 27:
			nation = "纳西";
			break;
		case 28:
			nation = "景颇";
			break;
		case 29:
			nation = "柯尔克孜";
			break;
		case 30:
			nation = "土";
			break;
		case 31:
			nation = "达斡尔";
			break;
		case 32:
			nation = "仫佬";
			break;
		case 33:
			nation = "羌";
			break;
		case 34:
			nation = "布朗";
			break;
		case 35:
			nation = "撒拉";
			break;
		case 36:
			nation = "毛南";
			break;
		case 37:
			nation = "仡佬";
			break;
		case 38:
			nation = "锡伯";
			break;
		case 39:
			nation = "阿昌";
			break;
		case 40:
			nation = "普米";
			break;
		case 41:
			nation = "塔吉克";
			break;
		case 42:
			nation = "怒";
			break;
		case 43:
			nation = "乌孜别克";
			break;
		case 44:
			nation = "俄罗斯";
			break;
		case 45:
			nation = "鄂温克";
			break;
		case 46:
			nation = "德昂";
			break;
		case 47:
			nation = "保安";
			break;
		case 48:
			nation = "裕固";
			break;
		case 49:
			nation = "京";
			break;
		case 50:
			nation = "塔塔尔";
			break;
		case 51:
			nation = "独龙";
			break;
		case 52:
			nation = "鄂伦春";
			break;
		case 53:
			nation = "赫哲";
			break;
		case 54:
			nation = "门巴";
			break;
		case 55:
			nation = "珞巴";
			break;
		case 56:
			nation = "基诺";
			break;
		case 97:
			nation = "其他";
			break;
		case 98:
			nation = "外国血统中国籍人士";
			break;
		default:
			nation = "";
		}

		return nation;
	}

	public String toHexString(byte[] d, int s, int n) {
		final char[] ret = new char[n * 2];
		final int e = s + n;

		int x = 0;
		for (int i = s; i < e; ++i) {
			final byte v = d[i];
			ret[x++] = HEX[0x0F & (v >> 4)];
			ret[x++] = HEX[0x0F & v];
		}
		return new String(ret);
	}

	public String toHexStringR(byte[] d, int s, int n) {
		final char[] ret = new char[n * 2];

		int x = 0;
		for (int i = s + n - 1; i >= s; --i) {
			final byte v = d[i];
			ret[x++] = HEX[0x0F & (v >> 4)];
			ret[x++] = HEX[0x0F & v];
		}
		return new String(ret);
	}

	public class CardOperateThread extends Thread {
		volatile boolean m_bStop = false;

		public void cancel() {
			Thread.currentThread().interrupt();
			m_bStop = true;
		}

		public void run() {
			boolean bResult = false;
			int nRequestType;
			int nCount = 0;
			long lCardNo = 0;
			LotusCardParam tLotusCardParam1 = new LotusCardParam();

			while (!Thread.currentThread().isInterrupted()) {
				if (m_bStop)
					break;
				try {

					// nRequestType = LotusCardDriver.RT_NOT_HALT;
					// bResult = mLotusCardDriver.GetCardNo(m_nDeviceHandle,
					// nRequestType, tLotusCardParam1);
					//
					// if (!bResult) {
					// Thread.sleep(200);
					// continue;
					// }
					// Message msg = new Message();
					// lCardNo = bytes2long(tLotusCardParam1.arrCardNo);
					// msg.obj = "CardNo(DEC):" + lCardNo;
					// m_Handler.sendMessage(msg);
					// Message msg1 = new Message();
					// msg1.obj = "CardNo(HEX):"
					// + leftString(
					// Integer.toHexString(tLotusCardParam1.arrCardNo[3]),
					// 2).toUpperCase()
					// + leftString(
					// Integer.toHexString(tLotusCardParam1.arrCardNo[2]),
					// 2).toUpperCase()
					// + leftString(
					// Integer.toHexString(tLotusCardParam1.arrCardNo[1]),
					// 2).toUpperCase()
					// + leftString(
					// Integer.toHexString(tLotusCardParam1.arrCardNo[0]),
					// 2).toUpperCase();
					// m_Handler.sendMessage(msg1);
					// mLotusCardDriver.Beep(m_nDeviceHandle, 10);
					// mLotusCardDriver.Halt(m_nDeviceHandle);

					nRequestType = LotusCardDriver.RT_ALL;
					bResult = mLotusCardDriver.GetCardNo(m_nDeviceHandle,
							nRequestType, tLotusCardParam1);
					Message msg = new Message();
					if (!bResult) {
						msg.obj = "GetCardNo false";
						m_Handler.sendMessage(msg);
						Thread.sleep(200);
						continue;
					}
					bResult = mLotusCardDriver.ResetCpuCardNoGetCardNo(
							m_nDeviceHandle, tLotusCardParam1);
					if (!bResult) {
						msg.obj = "ResetCpuCardNoGetCardNo false";
						m_Handler.sendMessage(msg);
						Thread.sleep(200);
						continue;
					}
					bResult = mLotusCardDriver.DeselectCpuCard(m_nDeviceHandle,
							tLotusCardParam1);
					if (!bResult) {
						msg.obj = "DeselectCpuCard false";
						m_Handler.sendMessage(msg);
						Thread.sleep(200);
						continue;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}