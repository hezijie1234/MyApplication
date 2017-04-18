package cc.lotuscard;

import java.io.IOException;

import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.nfc.tech.NfcB;
import android.util.Log;

public class LotusCardDriver {

	// Ѱ���������� _eRequestType
	public final static int RT_ALL = 0x52; // /< ����14443A��Ƭ
	public final static int RT_NOT_HALT = 0x26; // /< δ��������״̬�Ŀ�
	// ��Կ��֤ģʽ _eAuthMode
	public final static int AM_A = 0x60; // /< ��֤A����
	public final static int AM_B = 0x61; // /< ��֤B����

	/*********************** ���¶���Ҫ�ⲿҪ��ֵ **************************************/
	public static UsbDeviceConnection m_UsbDeviceConnection = null;
	public static UsbEndpoint m_InEndpoint = null;
	public static UsbEndpoint m_OutEndpoint = null;
	public static LotusCardDemoActivity m_lotusDemoActivity = null;

	/*************************************************************/
	public LotusCardDriver() {
	}

	static {
		System.loadLibrary("LotusCardDriver");
	}

	/**
	 * ���豸
	 * 
	 * @param strDeviceName
	 *            �����豸����
	 * @param nVID
	 *            USB�豸VID
	 * @param nPID
	 *            USB�豸PID
	 * @param nUsbDeviceIndex
	 *            USB�豸����
	 * @param unRecvTimeOut
	 *            ���ճ�ʱ
	 * @param bUseExendReadWrite
	 *            �Ƿ�ʹ���ⲿ��дͨ�� ���û���豸дȨ��ʱ������ʹ���ⲿUSB�򴮿ڽ���ͨѶ��
	 *            ��Ҫ����callBackProcess����ش�����ɶ�д���� Ŀǰ�����ṩUSB����
	 * @return �豸���
	 */
	public native long OpenDevice(String strDeviceName, int nVID, int nPID,
			int nUsbDeviceIndex, int unRecvTimeOut, boolean bUseExendReadWrite);

	/**
	 * �ر��豸
	 * 
	 * @param nDeviceHandle
	 *            �豸���
	 */
	public native void CloseDevice(long nDeviceHandle);

	/**
	 * Ѱ��
	 * 
	 * @param nDeviceHandle
	 *            �豸���
	 * @param nRequestType
	 *            ��������
	 * @param tLotusCardParam
	 *            ���ֵ ������Ŀ�Ƭ����
	 * @return true = �ɹ�
	 */
	public native boolean Request(long nDeviceHandle, int nRequestType,
			LotusCardParam tLotusCardParam);

	/**
	 * ����ͻ
	 * 
	 * @param nDeviceHandle
	 *            �豸���
	 * @param tLotusCardParam
	 *            ���ֵ ������Ŀ���
	 * @return true = �ɹ�
	 */
	public native boolean Anticoll(long nDeviceHandle,
			LotusCardParam tLotusCardParam);

	/**
	 * ѡ��
	 * 
	 * @param nDeviceHandle
	 *            �豸���
	 * @param tLotusCardParam
	 *            ����(ʹ������Ŀ���)����ֵ(ʹ������Ŀ�������С)
	 * @return true = �ɹ�
	 */
	public native boolean Select(long nDeviceHandle,
			LotusCardParam tLotusCardParam);

	/**
	 * ��Կ��֤
	 * 
	 * @param nDeviceHandle
	 *            �豸���
	 * @param nAuthMode
	 *            ��֤ģʽ
	 * @param nSectionIndex
	 *            ��������
	 * @param tLotusCardParam
	 *            ����(ʹ������Ŀ���)
	 * @return true = �ɹ�
	 */
	public native boolean Authentication(long nDeviceHandle, int nAuthMode,
			int nSectionIndex, LotusCardParam tLotusCardParam);

	/**
	 * ��Ƭ��ֹ��Ӧ
	 * 
	 * @param nDeviceHandle
	 *            �豸���
	 * @return true = �ɹ�
	 */
	public native boolean Halt(long nDeviceHandle);

	/**
	 * ��ָ����ַ����
	 * 
	 * @param nDeviceHandle
	 *            �豸���
	 * @param nAddress
	 *            ���ַ
	 * @param tLotusCardParam
	 *            ���ֵ����д���壩
	 * @return true = �ɹ�
	 */
	public native boolean Read(long nDeviceHandle, int nAddress,
			LotusCardParam tLotusCardParam);

	/**
	 * дָ����ַ����
	 * 
	 * @param nDeviceHandle
	 *            �豸���
	 * @param nAddress
	 *            ���ַ
	 * @param tLotusCardParam
	 *            ��������д���壩
	 * @return true = �ɹ�
	 */
	public native boolean Write(long nDeviceHandle, int nAddress,
			LotusCardParam tLotusCardParam);

	/**
	 * ��ֵ
	 * 
	 * @param nDeviceHandle
	 *            �豸���
	 * @param nAddress
	 *            ���ַ
	 * @param nValue
	 *            ֵ
	 * @return true = �ɹ�
	 */
	public native boolean Increment(long nDeviceHandle, int nAddress, int nValue);

	/**
	 * ��ֵ
	 * 
	 * @param nDeviceHandle
	 *            �豸���
	 * @param nAddress
	 *            ���ַ
	 * @param nValue
	 *            ֵ
	 * @return true = �ɹ�
	 */
	public native boolean Decreament(long nDeviceHandle, int nAddress,
			int nValue);

	/**
	 * �����ݿ鴫�뿨���ڲ��Ĵ���
	 * 
	 * @param nDeviceHandle
	 *            �豸���
	 * @param nAddress
	 *            ���ַ
	 * @return true = �ɹ�
	 */
	public native boolean Restore(long nDeviceHandle, int nAddress);

	/**
	 * �ڲ��Ĵ������뿨�Ŀ����ݿ�
	 * 
	 * @param nDeviceHandle
	 *            �豸���
	 * @param nAddress
	 *            ���ַ
	 * @return true = �ɹ�
	 */
	public native boolean Transfer(long nDeviceHandle, int nAddress);

	/**
	 * װ����Կ
	 * 
	 * @param nDeviceHandle
	 *            �豸���
	 * @param nAuthMode
	 *            ��֤ģʽ
	 * @param nSectionIndex
	 *            ��������
	 * @param tLotusCardParam
	 *            ��������Կ��
	 * @return true = �ɹ�
	 */
	public native boolean LoadKey(long nDeviceHandle, int nAuthMode,
			int nSectionIndex, LotusCardParam tLotusCardParam);

	/**
	 * ����
	 * 
	 * @param nDeviceHandle
	 *            �豸���
	 * @param nBeepLen
	 *            �������� ����Ϊ��λ
	 * @return true = �ɹ�
	 */
	public native boolean Beep(long nDeviceHandle, int nBeepLen);

	/**
	 * ����ָ�� ����CPU��
	 * 
	 * @param nDeviceHandle
	 *            �豸���
	 * @param nTimeOut
	 *            ��ʱ����
	 * @param tLotusCardParam
	 *            ������ָ���,���ؽ����
	 * @return true = �ɹ�
	 */
	public native boolean SendCpuCommand(long nDeviceHandle, int nTimeOut,
			LotusCardParam tLotusCardParam);

	/******************************** ���º�����������������Ϊ�˼򻯵��������ò��� ***************************/
	/**
	 * ��ȡ���� ��λ����
	 * 
	 * @param nDeviceHandle
	 *            �豸���
	 * @param nRequestType
	 *            ��������
	 * @param tLotusCardParam
	 *            ���ֵ
	 * @return true = �ɹ�
	 */
	public native boolean GetCardNo(long nDeviceHandle, int nRequestType,
			LotusCardParam tLotusCardParam);

	/**
	 * ��ȡ���� MCU��
	 * 
	 * @param nDeviceHandle
	 *            �豸���
	 * @param nRequestType
	 *            ��������
	 * @param ucBeepLen
	 *            �������� �255����
	 * @param ucUseHalt
	 *            ʹ����ֹ 1=������ֹ���� 0=������
	 * @param ucUseLoop
	 *            ʹ��ѭ�� 1=�������ڲ�ѭ����ȡ���� ��ȡ�������ٷ��� ��λ�����ճ�ʱ�� Ӧ�����ٴζ�ȡ 0=�������ڲ�ֻ����һ�� *
	 * @param tLotusCardParam
	 *            ���ֵ
	 * @return true = �ɹ�
	 */
	public native boolean GetCardNoEx(long nDeviceHandle, int nRequestType,
			byte ucBeepLen, byte ucUseHalt, byte ucUseLoop,
			LotusCardParam tLotusCardParam);

	/**
	 * ��ʼֵ
	 * 
	 * @param nDeviceHandle
	 *            �豸���
	 * @param nAddress
	 *            ���ַ
	 * @param nValue
	 *            ֵ
	 * @return true = �ɹ�
	 */
	public native boolean InitValue(long nDeviceHandle, int nAddress, int nValue);

	/**
	 * �޸�����AB
	 * 
	 * @param nDeviceHandle
	 *            �豸���
	 * @param pPasswordA
	 *            ����A
	 * @param pPasswordB
	 *            ����B
	 * @return true = �ɹ�
	 */
	public native boolean ChangePassword(long nDeviceHandle, int nSectionIndex,
			String strPasswordA, String strPasswordB);

	/**
	 * ��λCPU��
	 * 
	 * @param nDeviceHandle
	 *            �豸���
	 * @param tLotusCardParam
	 *            ���ֵ
	 * @return true = �ɹ�
	 */
	public native boolean ResetCpuCard(long nDeviceHandle,
			LotusCardParam tLotusCardParam);

	/**
	 * ��λCPU�� �˷�������û�е���GetCardNo
	 * 
	 * @param nDeviceHandle
	 *            �豸���
	 * @param tLotusCardParam
	 *            ���ֵ
	 * @return true = �ɹ�
	 */
	public native boolean ResetCpuCardNoGetCardNo(long nDeviceHandle,
			LotusCardParam tLotusCardParam);

	/**
	 * ȡ������CPU�� �������Է���WUPA
	 * 
	 * @param nDeviceHandle
	 *            �豸���
	 * @param tLotusCardParam
	 *            ���ֵ
	 * @return true = �ɹ�
	 */
	public native boolean DeselectCpuCard(long nDeviceHandle,
			LotusCardParam tLotusCardParam);

	/**
	 * ����ָ�� ����CPU�� ��װLotusCardSendCpuCommand
	 * 
	 * @param nDeviceHandle
	 *            �豸���
	 * @param tLotusCardParam
	 *            ������ָ���,���ؽ����
	 * @return true = �ɹ�
	 */
	public native boolean SendCOSCommand(long nDeviceHandle,
			LotusCardParam tLotusCardParam);

	/**
	 * ��ȡ���п�����
	 * 
	 * @param nDeviceHandle
	 * @param pBankCardNo
	 *            ���п��� ͬӡˢ����
	 * @param unBankCardNoLength
	 *            ���п��ų���
	 * @return true = �ɹ�
	 */

	/**
	 * ��ȡ���п�����
	 * 
	 * @param nDeviceHandle
	 * @return ���п�����
	 */
	public native String GetBankCardNo(long nDeviceHandle);

	/**
	 * ��ָ����ַ���� һ��ָ���������ж���
	 * 
	 * @param nDeviceHandle
	 *            �豸���
	 * @param nRequestType
	 *            ��������
	 * @param nAddress
	 *            ���ַ
	 * @param ucUsePassWord
	 *            ʹ������ 1=ʹ�ò������� 0 =ʹ���豸�ڲ�����
	 * @param ucBeepLen
	 *            �������� �255����
	 * @param ucUseHalt
	 *            ʹ����ֹ
	 * @param tLotusCardParam
	 *            ���ֵ����д���壩
	 * @return true = �ɹ�
	 */
	public native boolean ReadData(long nDeviceHandle, int nRequestType,
			int nAddress, byte ucUseParameterPassWord, byte ucBeepLen,
			byte ucUseHalt, LotusCardParam tLotusCardParam);

	/**
	 * дָ����ַ����
	 * 
	 * @param nDeviceHandle
	 *            �豸���
	 * @param nAddress
	 *            ���ַ
	 * @param ucBeepLen
	 *            �������� �255����
	 * @param ucUseHalt
	 *            ʹ����ֹ
	 * @param tLotusCardParam
	 *            ��������д���壩
	 * @return true = �ɹ�
	 */
	public native boolean WriteData(long nDeviceHandle, int nAddress,
			byte ucBeepLen, byte ucUseHalt, LotusCardParam tLotusCardParam);

	/****************************** ���´���ΪIPͨ����ش��� **********************************************/
	/**
	 * ��ָ����ַ�ı�
	 * 
	 * @param nSectionIndex
	 *            ��������
	 * @return ��ȡ�����ִ�
	 */
	public native String ReadText(long nDeviceHandle, int nSectionIndex);

	/**
	 * дָ����ַ�ı�
	 * 
	 * @param nSectionIndex
	 *            ��������
	 * @param strTextInfo
	 *            д���ı��ִ�
	 * @return true = �ɹ�
	 */
	public native boolean WriteText(long nDeviceHandle, int nSectionIndex,
			String strTextInfo);

	/**
	 * ���Ӳ���
	 * 
	 * @param strServerIp
	 *            ������IP��ַ
	 * @param nConnectTimeOut
	 *            ��ʱusΪ��λ
	 * @return true = �ɹ�
	 */
	public native boolean ConnectTest(String strServerIp, int nConnectTimeOut);

	/**
	 * ��ȡ�������
	 * 
	 * @param nDeviceHandle
	 *            �豸���
	 * @return ������� ���ö��ֵ����
	 */
	public native int GetErrorCode(long nDeviceHandle);

	/**
	 * ��ȡ����֤�����������
	 * 
	 * @param nDeviceHandle
	 *            �豸���
	 * @return ������� ���ö��ֵ����
	 */
	public native int GetTwoIdErrorCode(long nDeviceHandle);

	/**
	 * ��ȡ������Ϣ
	 * 
	 * @param nDeviceHandle
	 *            �豸���
	 * @param errCode
	 *            �������
	 * @param pszErrorInfo
	 *            ������Ϣ��ַ
	 * @param unErrorInfoLength
	 *            ������Ϣ����
	 */
	public native String GetErrorInfo(long nDeviceHandle, int errCode);

	/**
	 * ��ȡ����֤������Ϣ
	 * 
	 * @param nDeviceHandle
	 *            �豸���
	 * @param errCode
	 *            �������
	 * @param pszErrorInfo
	 *            ������Ϣ��ַ
	 * @param unErrorInfoLength
	 *            ������Ϣ����
	 */
	public native String GetTwoIdErrorInfo(long nDeviceHandle, int errCode);

	/**
	 * ���ÿ�Ƭ����
	 * 
	 * @param nDeviceHandle
	 *            �豸���
	 * @param cCardType
	 *            ��Ƭ���� A='A'/'a' B='B'/'b' F='F'/'f' C='C'/'c'
	 * @return true = �ɹ�
	 */
	public native boolean SetCardType(long nDeviceHandle, char cCardType);

	/**
	 * FelicaѰ��
	 * 
	 * @param nDeviceHandle
	 *            �豸���
	 * @param unTimerSlot
	 *            timer slot
	 * @param tLotusCardParam
	 *            ��������д���壩
	 * @return true = �ɹ�
	 */
	public native boolean FelicaPolling(long nDeviceHandle, int unTimerSlot,
			LotusCardParam tLotusCardParam);

	/**
	 * ��ȡNFC����
	 * 
	 * @param nDeviceHandle
	 *            �豸���
	 * @return �����ִ�
	 */
	public native String ReadNfcBuffer(long nDeviceHandle);

	/**
	 * д��NFC����
	 * 
	 * @param nDeviceHandle
	 *            �豸���
	 * @param pszNfcBuffer
	 *            �����ַ
	 * @return true = �ɹ�
	 */
	public native boolean WriteNfcBuffer(long nDeviceHandle, String strNfcBuffer);

	/******************************** ���º���Ϊtype b�������� ***************************/
	/**
	 * Ѱ��
	 * 
	 * @param nDeviceHandle
	 *            �豸���
	 * @param nRequestType
	 *            ��������
	 * @param tLotusCardParam
	 *            ���ֵ ������Ŀ�Ƭ����
	 * @return true = �ɹ�
	 */
	public native int RequestB(long nDeviceHandle, int nRequestType,
			LotusCardParam tLotusCardParam);

	/**
	 * ѡ��
	 * 
	 * @param nDeviceHandle
	 *            �豸���
	 * @param tLotusCardParam
	 *            ����(ʹ������Ŀ���)����ֵ(ʹ������Ŀ�������С)
	 * @return true = �ɹ�
	 */
	public native int SelectB(long nDeviceHandle, LotusCardParam tLotusCardParam);

	/**
	 * ��Ƭ��ֹ��Ӧ
	 * 
	 * @param nDeviceHandle
	 *            �豸���
	 * @return true = �ɹ�
	 */
	public native int HaltB(long nDeviceHandle);

	/**
	 * ��ȡ�豸��
	 * 
	 * @param nDeviceHandle
	 *            �豸���
	 * @return ��ȡ�����ִ�
	 */
	public native String GetDeviceNo(long nDeviceHandle);

	/**
	 * ��ȡ�������֤��ƬID
	 * 
	 * @param nDeviceHandle
	 *            �豸���
	 * @return ��ȡ�����ִ�
	 */
	public native String GetTwoGenerationIDCardNo(long nDeviceHandle);

	/**
	 * ������Ƶ����
	 * 
	 * @param nDeviceHandle
	 *            �豸���
	 * @param ucRfOnOff
	 *            1=����Ƶ�ź� 0= �ر���Ƶ�ź�
	 * @return true = �ɹ�
	 */
	public native boolean SetRfOnOff(long nDeviceHandle, byte ucRfOnOff);

	/**
	 * ��ȡ����֤��Ϣ
	 * 
	 * @param nDeviceHandle
	 *            �豸���
	 * @param pTwoIdInfo
	 *            ���� ����֤��Ϣ�ṹ���ַ
	 * @return true = �ɹ�
	 */
	public native boolean GetTwoIdInfo(long nDeviceHandle,
			TwoIdInfoParam tTwoIdInfo);

	/**
	 * ͨ�������ȡ����֤��Ϣ
	 * 
	 * @param nDeviceHandle
	 *            �豸���
	 * @param pszServerIp
	 *            ���� ��ȫģ�����ڷ�����IP
	 * @param pTwoIdInfo
	 *            ���� ����֤��Ϣ�ṹ���ַ
	 * @return true = �ɹ�
	 */
	public native boolean GetTwoIdInfoByServer(long nDeviceHandle,
			String strServerIp, TwoIdInfoParam tTwoIdInfo);

	/**
	 * ͨ�������ȡ����֤��Ϣ
	 * 
	 * @param nDeviceHandle
	 *            �豸���
	 * @param pszServerIp
	 *            ���� ��ȫģ�����ڷ�����IP
	 * @param unServerPort
	 *            ���� �������˿�
	 * @param pTwoIdInfo
	 *            ���� ����֤��Ϣ�ṹ���ַ
	 * @param unRecvTimeOut
	 *            ���� ���ճ�ʱ Ĭ��10��
	 * @return true = �ɹ�
	 */
	public native boolean GetTwoIdInfoByServerEx(long nDeviceHandle,
			String strServerIp, long unServerPort, TwoIdInfoParam tTwoIdInfo,
			long unRecvTimeOut);

	/**
	 * ͨ�������ȡ����֤��Ϣ
	 * 
	 * @param nDeviceHandle
	 *            �豸���
	 * @param strDispatchServerIp
	 *            ���� ���ȷ�����IP
	 * @param strUserName
	 *            ���� ��¼���ȷ����� �û���
	 * @param strPassWord
	 *            ���� ��¼���ȷ����� ����
	 * @param pTwoIdInfo
	 *            ���� ����֤��Ϣ�ṹ���ַ
	 * @param unRecvTimeOut
	 *            ���� ���ճ�ʱ Ĭ��10��
	 * @return true = �ɹ�
	 */
	public native boolean GetTwoIdInfoByMcuServer(Object objUserObject,
			long nDeviceHandle, String strDispatchServerIp, String strUserName,
			String strPassWord, TwoIdInfoParam tTwoIdInfo, long unRecvTimeOut);

	/**
	 * ͨ�������ȡ����֤��Ƭ��Ϣ WL��ʽ����
	 * 
	 * @param nDeviceHandle
	 *            �豸���
	 * @param pszServerIp
	 *            ���� ��ȫģ�����ڷ�����IP
	 * @param pTwoIdInfo
	 *            ���� ����֤��Ϣ�ṹ���ַ
	 * @return true = �ɹ�
	 */
	public native boolean WlDecodeByServer(long nDeviceHandle,
			String strServerIp, TwoIdInfoParam tTwoIdInfo);

	/**
	 * ͨ�������ȡ����֤��Ϣ ���API�������绷���Ƚ����ĵط� �ڲ������Զ���
	 * 
	 * @param nDeviceHandle
	 *            �豸���
	 * @param pszServerIp
	 *            ���� ��ȫģ�����ڷ�����IP
	 * @param unServerPort
	 *            ���� �������˿�
	 * @param pTwoIdInfo
	 *            ���� ����֤��Ϣ�ṹ���ַ
	 * @param unRecvTimeOut
	 *            ���� ���ճ�ʱ Ĭ��10��
	 * @return true = �ɹ�
	 */
	public native boolean GetTwoIdInfoByWireless(long nDeviceHandle,
			String strServerIp, long unServePort, TwoIdInfoParam tTwoIdInfo,
			long unRecvTimeOut);

	/**
	 * ��ȡ����֤�������
	 * 
	 * @param arrRandomBuffer
	 *            ���� ���������
	 */
	public native void GetIdRandom(byte[] arrRandomBuffer);

	/**
	 * ������־
	 * 
	 * @param strLogFile
	 *            ���� ��־�ļ�
	 * @param strLog
	 *            ���� ��־����
	 */
	public native void SaveLog(String strLogFile, String strLog);

	/**
	 * ��ȡUSB�豸����
	 * 
	 * @param nVID
	 *            ���� �豸VID
	 * @param nPID
	 *            ���� �豸PID
	 */
	public native int GetUsbDeviceCount(int nVID, int nPID);

	/**
	 * ��ȡMCU����
	 * 
	 * @param nDeviceHandle
	 *            �豸���
	 * @param arrMcuConfig
	 *            ���� MCU�������ݻ���
	 * @return true = �ɹ�
	 */
	public native boolean GetMcuConfig(long nDeviceHandle, byte[] arrMcuConfig);

	/**
	 * ��ȡISPѡ��
	 * 
	 * @param nDeviceHandle
	 *            �豸���
	 * @param arrIspOption
	 *            ���� ѡ�����ݻ���
	 * @return true = �ɹ�
	 */
	public native boolean GetIspOption(long nDeviceHandle, byte[] arrIspOption);

	/**
	 * ����ISPѡ��
	 * 
	 * @param nDeviceHandle
	 *            �豸���
	 * @param arrIspOption
	 *            ���� ѡ�����ݵ�ַ 0=��ֹ ��0=����
	 * @return true = �ɹ�
	 */
	public native boolean SetIspOption(long nDeviceHandle, byte[] arrIspOption);

	/**
	 * ��ȡntag�汾 21x֧��
	 * 
	 * @param nDeviceHandle
	 *            �豸���
	 * @param arrNtagVersionBuffer
	 *            ���� �汾��Ϣ����
	 * @return true = �ɹ�
	 */
	public native boolean NtagGetVersion(long nDeviceHandle,
			byte[] arrNtagVersionBuffer);

	/**
	 * ntag������֤ 21x֧��
	 * 
	 * @param nDeviceHandle
	 *            �豸���
	 * @param arrNtagPasswordBuffer
	 *            ���� ���뻺�� ���� ���뻺�峤�� Ĭ��4�ֽ�
	 * @return true = �ɹ�
	 */
	public native boolean NtagPwdAuth(long nDeviceHandle,
			byte[] arrNtagPasswordBuffer);

	/**
	 * CPU����������ѡ���ļ���Ŀ¼
	 * 
	 * @param nDeviceHandle
	 *            �豸���
	 * @param pszDirOrFileName
	 *            �ļ���Ŀ¼��
	 * @return ���ؽ��BUFFER
	 */
	public native byte[] CpuCardSelectByName(long nDeviceHandle,
			String strDirOrFileName);

	/**
	 * CPU����ȡ�������ļ�
	 * 
	 * @param nDeviceHandle
	 *            �豸���
	 * @param nSfi
	 *            �ļ���ʶ
	 * @return ���ؽ��BUFFER
	 */
	public native byte[] CpuCardReadBinary(long nDeviceHandle, int nSfi);

	/**
	 * ͨ���ص��������ݶ�д
	 * 
	 * @param nDeviceHandle
	 *            �豸���
	 * @param bRead
	 *            �Ƿ������
	 * @param arrBuffer
	 *            ����
	 * @return true = �����ɹ�
	 */
	public static boolean callBackProcess(long nDeviceHandle, boolean bRead,
			byte[] arrBuffer) {
		int nResult = 0;
		boolean bResult = false;
		int nBufferLength = arrBuffer.length;
		int nWaitCount = 0;
		if (null == m_UsbDeviceConnection)
			return false;
		if (null == m_OutEndpoint)
			return false;
		if (null == m_InEndpoint)
			return false;
		if (nBufferLength != 64)
			return false;
		if (true == bRead) {
			arrBuffer[0] = 0;
			while (true) {
				nResult = m_UsbDeviceConnection.bulkTransfer(m_InEndpoint,
						arrBuffer, nBufferLength, 3000);
				if (nResult <= 0)
					break;
				if (arrBuffer[0] != 0)
					break;
				nWaitCount++;
				if (nWaitCount > 1000)
					break;
			}
			if (nResult == nBufferLength) {
				bResult = true;
			} else {
				bResult = false;
			}
		} else {
			nResult = m_UsbDeviceConnection.bulkTransfer(m_OutEndpoint,
					arrBuffer, nBufferLength, 3000);
			if (nResult == nBufferLength) {
				bResult = true;
			} else {
				bResult = false;
			}
		}
		return bResult;
	}

	public static boolean isZero(byte[] a) {
		for (int i = 0; i < a.length; i++) {
			if (a[i] != 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * ͨ���ص���ɵ������豸��������֤
	 * 
	 * @param objUser
	 *            ���� �û����� �ڵ���GetTwoIdInfoByMcuServer�Ǵ���
	 * 
	 * @param arrBuffer
	 *            ���� �� ����������� ���ݻص����Ĳ�����������֤���ѽ���������� ��һ�ֽ��ǳ��� ����������
	 * @return true = �����ɹ�
	 */
	public static boolean callBackExtendIdDeviceProcess(Object objUser,
			byte[] arrBuffer) {
		boolean bResult = false;
		NfcB nfcbId = (NfcB) objUser;
		if (null == nfcbId)
			return false;
		byte[] arrCommnad = new byte[arrBuffer[0]];
		System.arraycopy(arrBuffer, 1, arrCommnad, 0, arrBuffer[0]);

		try {
			byte[] arrResult = nfcbId.transceive(arrCommnad);
			if (isZero(arrResult)) {
				m_lotusDemoActivity.AddLog("��ȡ��Ƭ����ȫ��Ϊ0");

			} else {
				arrBuffer[0] = (byte) (arrResult.length - 1);
				System.arraycopy(arrResult, 0, arrBuffer, 1, arrBuffer[0]);
				bResult = true;
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			m_lotusDemoActivity.AddLog(e.getMessage());
		}

		return bResult;
	}
}
