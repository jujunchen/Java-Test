package dahua;

import com.sun.jna.Native;
import dahua.lib.Device_Info_Ex_t;
import dahua.lib.IDpsdkCore;
import dahua.lib.Login_Info_t;
import dahua.lib.Return_Value_Info_t;
import dahua.lib.dpsdk_dev_type_e;
import dahua.lib.dpsdk_protocol_version_e;
import dahua.lib.dpsdk_retval_e;
import dahua.lib.dpsdk_sdk_type_e;
import dahua.lib.fDPSDKDevStatusCallback;
import dahua.lib.fDPSDKNVRChnlStatusCallback;

/**
 * @author chenjujun
 * @date 12/29/20
 */
public class DaHuaSDKTest {

    public static IDpsdkCore dpsdk = Native.loadLibrary("DPSDK_Java",IDpsdkCore.class);;
    public static int 	    m_nDLLHandle    = -1;
    public String   m_strAlarmCamareID = "1000000";
    public String   m_strRealCamareID = "1000000$1$0$0";
    public String   m_strPTZCamareID = "1000000$1$0$0";
    public String   m_strNetAlarmHostCamareID = "1000000$1$0$0";
    public String   m_strNetAlarmHostDeviceID = "1000000";

    public static String 	m_strIp 		= "10.31.87.17";
    public static int    	m_nPort 		= 9000;
    public static String 	m_strUser 		= "system";
    public static String 	m_strPassword 	= "admin123";


    public static void main(String[] args) {
        Return_Value_Info_t res = new Return_Value_Info_t();
        int nRet = dpsdk.DPSDK_Create(dpsdk_sdk_type_e.DPSDK_CORE_SDK_SERVER,res);
        m_nDLLHandle = res.nReturnValue;
        System.out.print("DPSDK_Create, m_nDLLHandle = ");
        System.out.println(m_nDLLHandle);
        if(m_nDLLHandle > 0)
        {
            //login
            Login_Info_t loginInfo = new Login_Info_t();
            loginInfo.szIp = m_strIp.getBytes();
            loginInfo.nPort = m_nPort;
            loginInfo.szUsername = m_strUser.getBytes();
            loginInfo.szPassword = m_strPassword.getBytes();
            loginInfo.nProtocol = dpsdk_protocol_version_e.DPSDK_PROTOCOL_VERSION_II;
            loginInfo.iType = 1;

            nRet = dpsdk.DPSDK_Login(m_nDLLHandle,loginInfo,10000);
            if(nRet == dpsdk_retval_e.DPSDK_RET_SUCCESS)
            {
                System.out.printf("login success! nRet = %d", nRet);
            }else
            {
                System.out.printf("login failed! nRet = %d", nRet);
            }
            System.out.println();

            //设置设备状态上报监听函数
            nRet = dpsdk.DPSDK_SetDPSDKDeviceStatusCallback(m_nDLLHandle, fDeviceStatus);

            //设置NVR通道状态上报监听函数
            nRet =dpsdk.DPSDK_SetDPSDKNVRChnlStatusCallback(m_nDLLHandle, fNVRChnlStatus);


            //logout DSS
            nRet = dpsdk.DPSDK_Logout(m_nDLLHandle, 10000);
            if(nRet == dpsdk_retval_e.DPSDK_RET_SUCCESS)
            {
                System.out.printf("logout success! nRet = %d", nRet);
            }else
            {
                System.out.printf("logout failed! nRet = %d", nRet);
            }
            System.out.println();

            // destory SDK
            nRet = dpsdk.DPSDK_Destroy(m_nDLLHandle);
            if(nRet == dpsdk_retval_e.DPSDK_RET_SUCCESS)
            {
                System.out.printf("Destroy success! nRet = %d", nRet);
            }else
            {
                System.out.printf("Destroy failed! nRet = %d", nRet);
            }
            System.out.println();
        }
    }


    public static fDPSDKDevStatusCallback fDeviceStatus = new fDPSDKDevStatusCallback() {
        @Override
        public void invoke(int nPDLLHandle, byte[] szDeviceId, int nStatus) {
            String status = "online";
            if(nStatus == 1)
            {
                status = "offline";
                Device_Info_Ex_t deviceInfo = new Device_Info_Ex_t();
                int nRet = dpsdk.DPSDK_GetDeviceInfoExById(m_nDLLHandle, szDeviceId,deviceInfo);
                if(deviceInfo.nDevType == dpsdk_dev_type_e.DEV_TYPE_NVR || deviceInfo.nDevType == dpsdk_dev_type_e.DEV_TYPE_EVS || deviceInfo.nDevType == dpsdk_dev_type_e.DEV_TYPE_SMART_NVR || deviceInfo.nDevType == dpsdk_dev_type_e.DEV_MATRIX_NVR6000)
                {
                    nRet = dpsdk.DPSDK_QueryNVRChnlStatus(m_nDLLHandle, szDeviceId , 10*1000);

                    if(nRet == dpsdk_retval_e.DPSDK_RET_SUCCESS)
                    {
                        System.out.printf("查询NVR通道状态成功，nRet = %d", nRet);
                    }else
                    {
                        System.out.printf("查询NVR通道状态失败，nRet = %d", nRet);
                    }
                    System.out.println();
                }
            }
            System.out.printf("Device Status Report!, szDeviceId = %s, nStatus = %s", new String(szDeviceId),status);
            System.out.println();
        }
    };

    public static fDPSDKNVRChnlStatusCallback fNVRChnlStatus = new fDPSDKNVRChnlStatusCallback() {
        @Override
        public void invoke(int nPDLLHandle, byte[] szCameraId, int nStatus) {
            String status = "online";
            if(nStatus == 1)
            {
                status = "offline";
            }
            System.out.printf("NVR Channel Status Report!, szCameraId = %s, nStatus = %s", new String(szCameraId),status);
            System.out.println();
        }
    };


}


