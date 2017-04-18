package cc.lotuscard;

public class TwoIdInfoParam {
	TwoIdInfoParam()
	{
		arrTwoIdName = new byte[30];
		arrTwoIdSex = new byte[2];
		arrTwoIdNation = new byte[4];
		arrTwoIdBirthday = new byte[16];
		arrTwoIdAddress = new byte[70];
		arrTwoIdNo = new byte[36];
		arrTwoIdSignedDepartment = new byte[30];
		arrTwoIdValidityPeriodBegin = new byte[16];
		arrTwoIdValidityPeriodEnd = new byte[16];
		arrTwoIdNewAddress = new byte[70];
		arrTwoIdPhoto = new byte[1024];
		arrTwoIdFingerprint = new byte[1024];
		arrTwoIdPhotoJpeg = new byte[4096];
	}
	
	/**
	 * ���� UNICODE
	 */
	public byte[] arrTwoIdName;
	/**
	 * �Ա� UNICODE
	 */
	public byte[] arrTwoIdSex;
	/**
	 * ���� UNICODE
	 */
	public byte[] arrTwoIdNation;
	/**
	 * �������� UNICODE YYYYMMDD
	 */
	public byte[] arrTwoIdBirthday;
	/**
	 * סַ UNICODE
	 */
	public byte[] arrTwoIdAddress;
	/**
	 * ���֤���� UNICODE
	 */
	public byte[] arrTwoIdNo;
	/**
	 * ǩ������ UNICODE
	 */
	public byte[] arrTwoIdSignedDepartment;
	/**
	 * ��Ч����ʼ���� UNICODE YYYYMMDD
	 */
	public byte[] arrTwoIdValidityPeriodBegin;
	/**
	 * ��Ч�ڽ�ֹ���� UNICODE YYYYMMDD ��Ч��Ϊ����ʱ�洢�����ڡ�
	 */
	public byte[] arrTwoIdValidityPeriodEnd;
	/**
	 * ����סַ UNICODE
	 */
	public byte[] arrTwoIdNewAddress;
	/**
	 * ��Ƭ��Ϣ
	 */
	public byte[] arrTwoIdPhoto;
	/**
	 * ָ����Ϣ
	 */
	public byte[] arrTwoIdFingerprint;
	/**
	 * ��Ƭ��Ϣ JPEG ��ʽ
	 */
	public byte[] arrTwoIdPhotoJpeg;
	/**
	 * ��Ƭ��Ϣ���� JPEG��ʽ
	 */
	public int unTwoIdPhotoJpegLength;


}
