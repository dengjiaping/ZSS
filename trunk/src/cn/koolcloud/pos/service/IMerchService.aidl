package cn.koolcloud.pos.service;
import cn.koolcloud.pos.service.MerchInfo;
import cn.koolcloud.pos.service.IMerchCallBack;

interface IMerchService {
   	MerchInfo getMerchInfo();
   	void setMerchInfo(in MerchInfo mi); 	
   	void setLoginStatus(String ls);  
   	void endCallPayEx();
   	
   	void registerCallback(IMerchCallBack cb);     
    void unregisterCallback(IMerchCallBack cb);  	
}