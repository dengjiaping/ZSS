package cn.koolcloud.pos.service;
import cn.koolcloud.pos.service.SecureInfo;

interface ISecureService {
   	SecureInfo getSecureInfo();
   	void setSecureInfo(in SecureInfo si);
   	String getUserInfo();
   	void setUserInfo(String ui);
}