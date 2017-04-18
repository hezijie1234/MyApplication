package cc.lotuscard;

public enum LotusCardErrorCode {
	LCEC_OK ,				//����ִ��
	LCED_UNKNOWN,				//δ֪�� 
	LCEC_SEND_FALSE,			//����ʧ��
	LCEC_RECV_TIME_OUT,			//���ճ�ʱ
	LCEC_RECV_ZERO_LEN,			//���ճ���Ϊ0
	LCEC_RECV_CRC_FALSE,		//����У��ʧ��
	LCEC_REQUEST,				//Ѱ��
	LCEC_ANTICOLL,				//����ͻ
	LCEC_SELECT,				//ѡ��
	LCEC_AUTHENTICATION,		//������֤
	LCEC_HALT,					//��ֹ
	LCEC_READ,					//��
	LCEC_WRITE,					//д
	LCEC_INCREMENT,				//��ֵ
	LCEC_DECREMENT,				//��ֵ
	LCEC_LOADKEY,				//װ������
	LCEC_BEEP,					//����
	LCEC_RESTORE,				//�����ݿ鴫�뿨���ڲ��Ĵ���
	LCEC_TRANSFER,				//�ڲ��Ĵ������뿨�Ŀ����ݿ�
	LCEC_SEND_COMMAND			//����14443ָ��
}
