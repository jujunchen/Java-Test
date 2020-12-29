package dahua.lib;

import com.sun.jna.win32.StdCallLibrary;

public interface IDpsdkCore  extends StdCallLibrary {

    /************************************************************************
     ** 接口定义
     ***********************************************************************/
	/** 创建SDK句柄.
	 @param   IN	nType			SDK类型
	 @param   OUT	nDllHandle		返回SDK句柄，后续操作需要以此作为参数
	 @return  函数返回错误类型，参考dpsdk_retval_e
	 @remark									
	*/
	 public   int	DPSDK_Create(int nType, Return_Value_Info_t nDllHandle);
	
	/** 删除SDK句柄.
	 @param   IN	nPDLLHandle		SDK句柄
	 @return  函数返回错误类型，参考dpsdk_retval_e
	 @remark		
	 1、需要和DPSDK_Create成对使用
	*/
	 public   int	DPSDK_Destroy( int nPDLLHandle );
	
	/** 获取错误码信息.
	*/
//	static public native  int	DpsdkGetLastError();
	
	/** 设置日志.
	 @param   IN	szFilename		文件名称
	 @return  函数返回错误类型，参考dpsdk_retval_e
	 @remark									
	*/
	 public  int DPSDK_SetLog(int nPDLLHandle, byte szFilename[]);
	
	/** 打开程序崩溃监听.
	 @param   IN	szFilename		程序崩溃后，自动生成的文件路径和文件名称
	 @return  函数返回错误类型，参考dpsdk_retval_e
	 @remark									
	*/
	 public  int DPSDK_StartMonitor(int nPDLLHandle, byte szFilename[]);

	/** 设置DPSDK状态回调.
	 @param   IN	nPDLLHandle				SDK句柄
	 @param   IN	statusCallback			回调函数
	 @return  函数返回错误类型，参考dpsdk_retval_e
	 @remark
	*/
	 public  int DPSDK_SetDPSDKStatusCallback( int nPDLLHandle, fDPSDKStatusCallback statusCallback);

	/** 设置DPSDK设备变更回调.
	 @param   IN	nPDLLHandle				SDK句柄
	 @param   IN	deviceChangeCallback	回调函数
	 @return  函数返回错误类型，参考dpsdk_retval_e
	 @remark
	*/
	 public  int DPSDK_SetDPSDKDeviceChangeCallback(int nPDLLHandle, fDPSDKDeviceChangeCallback deviceChangeCallback);
	
	/** 设置DPSDK新组织设备变更回调.
	param   IN	nPDLLHandle		SDK句柄
	@param   IN	fun				回调函数
	@return  函数返回错误类型，参考dpsdk_retval_e
	@remark
	*/
	 public  int DPSDK_SetDPSDKOrgDevChangeNewCallback( int nPDLLHandle, fDPSDKOrgDevChangeNewCallback fun);

	/** 根据设备ID获取设备信息
	@param   IN    nPDLLHandle     SDK句柄
	@param   IN    szDevId		   设备ID
	@param   OUT   pDeviceInfoEx   设备信息
	@return  函数返回错误类型，参考dpsdk_retval_e
	@remark 
	*/
	 public  int DPSDK_GetDeviceInfoExById(int nPDLLHanle,  byte[] szDevId, Device_Info_Ex_t deviceInfoEx);
	
	/** 查询NVR通道状态
	@param   IN    nPDLLHandle     SDK句柄
	@param   IN    deviceId	       设备的ID
	@return  函数返回错误类型，参考dpsdk_retval_e
	@remark 
	*/
	 public  int DPSDK_QueryNVRChnlStatus(int nPDLLHandle, byte[] deviceId, int nTimeout);

	/** 设置NVR通道状态回调
	@param   IN	nPDLLHandle		SDK句柄
	@param   IN	fun				回调函数
	@return  函数返回错误类型，参考dpsdk_retval_e
	@remark 通道状态变化的时候会推送
	*/
	 public  int DPSDK_SetDPSDKNVRChnlStatusCallback(int nPDLLHandle, fDPSDKNVRChnlStatusCallback nvrChnlStatusCallback);

	/** 注册平台.
	 @param   IN	nPDLLHandle		SDK句柄
	 @param   IN	loginInfo		用户登录信息
	 @param   IN	nTimeout		超时时长，单位毫秒
	 @return  函数返回错误类型，参考dpsdk_retval_e
	 @remark
	 */
	 public   int  DPSDK_Login( int nPDLLHandle, Login_Info_t loginInfo, int nTimeout);


	/** 设置DPSDK设备状态回调.
	 @param   IN	nPDLLHandle					SDK句柄
	 @param   IN	deviceStatusCallback		回调函数
	 @return  函数返回错误类型，参考dpsdk_retval_e
	 @remark
	 */
	 public  int DPSDK_SetDPSDKDeviceStatusCallback(int nPDLLHandle, fDPSDKDevStatusCallback deviceStatusCallback);


	/** 登出平台.
	 @param   IN	nPDLLHandle		SDK句柄
	 @param   IN	nTimeout		超时时长，单位毫秒
	 @return  函数返回错误类型，参考dpsdk_retval_e
	 @remark
	 */
	 public   int  DPSDK_Logout( int nPDLLHandle, int nTimeout);
}


