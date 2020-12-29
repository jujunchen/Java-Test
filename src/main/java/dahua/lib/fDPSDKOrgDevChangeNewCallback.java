package dahua.lib;

/** DPSDK 新组织设备变更回调.
 @param   IN									nPDLLHandle				SDK句柄
 @param   IN									nOrgChangeType			参考dpsdk_org_change_type_e
 @param   IN									szOrgCode				组织ID，多个ID以“|”分隔
 @remark									
*/
public interface fDPSDKOrgDevChangeNewCallback {
	public void invoke(int nPDLLHandle, int nChangeType, byte[] szOrgCode);
}