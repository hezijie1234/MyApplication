package cc.lotuscard;

public class LotusCardParam {
	public LotusCardParam()
	{
		arrCardNo = new byte[8];
		arrBuffer = new byte[64];
		arrKeys = new byte[64];
		arrCosResultBuffer =  new byte[256];
		arrCosSendBuffer = new byte[256];
	}
	/**
	 * ��Ƭ����
	 */
	public int nCardType;
	/**
	 * 8�ֽڿ���
	 */
	public byte[] arrCardNo;
	
	/**
	 * ��Ƭ������С
	 */
	public int nCardSize;
	
	/**
	 * ��д����
	 */
	public byte[] arrBuffer;
	
	/**
	 * �����С
	 */
	public int nBufferSize;
	/**
	 * ��Կ
	 */
	public byte[] arrKeys;
	
	/**
	 * KEYs��С
	 *
	 */
	public int nKeysSize;
	/**
	 * pCosResultBuffer COSִ�н������
	 */
	public byte[] arrCosResultBuffer;
	/**
	 * unCosReultBufferLength COSִ�н�����峤�� 
	 */
	public int unCosReultBufferLength;	
	
	/**
	 * pCosSendBuffer COSָ��ͻ���
	 */
	public byte[] arrCosSendBuffer;
	/**
	 * unCosSendBufferLength COSָ��ͻ��峤�� 
	 */
	public int unCosSendBufferLength;	
}
