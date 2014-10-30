package com.koolyun.mis.core.user;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.koolyun.mis.R;
import com.koolyun.mis.core.BaseInfoManager;
import com.koolyun.mis.core.DealModel;
import com.koolyun.mis.core.ManagerBase;
import com.koolyun.mis.util.Common;
import com.koolyun.mis.util.MyLog;

@SuppressLint("DefaultLocale")
public class AccountManager extends ManagerBase {	    
	private Account mCurrentAccount = new Account();
	private BaseInfoManager mBaseInfo = new BaseInfoManager();
	private static AccountManager instance = null;
	
	private AccountManager() {
	}
	
	public enum ManageLevel{
		Merchant(1, Common.getString(R.string.administrator)),
		Manager(2, Common.getString(R.string.shop_manager)),
		Clerk(3, Common.getString(R.string.shop_assistant));
		private int code; //状态码值
		private String name;
		ManageLevel(int code,String name) { //
	        this.code=code;
	        this.name = name;
	    }
		
		public int toInt() {
			return code;
		}
		
		public String getCode() {
			return String.valueOf(code);
		}
		
	    public String toString() {
	        return name;
	    }
	    
	    public static String valueOf(int code) {
	    	switch(code) {
	    	case 1:
	    		return Merchant.toString();
	    	case 2:
	    		return Manager.toString();
	    	case 3:
	    		return Clerk.toString();
	    	default:
	    		return "";
	    	}
	    }
	};
	
	public static synchronized AccountManager getInstance() {
		if (instance==null) {
			instance=new AccountManager();
		}
		return instance;
	}
	
	/**
	 * @param privilege 根据登陆账号的等级展示本账号及下级账号
	 * @return
	 */
	public List<Account> getAccountListByPrivilege(int privilege) {
		String sql = "SELECT * FROM `Account` WHERE accountPrivilegeID > " + privilege +
				" OR accountID="+getCurrentAccount().getAccountID()+" ORDER BY accountID ASC";
		MyLog.e("sql:"+sql);
		
		Cursor result = msqlitedb.rawQuery(sql, null);
		List<Account> aclist = new LinkedList<Account>();

		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
			int accountID = result.getInt(0);
			int accountPrivilege = result.getInt(1);
			String accountName = result.getString(2);
			String account = result.getString(3);
			String password = result.getString(4);
			String operatorID = result.getString(5);
			String accountPhoto = result.getString(6);
			String lastName = result.getString(7);
			int sync = result.getInt(8);
			int phoneID = result.getInt(9);

			Account ac = new Account(accountID, accountPrivilege, accountName, account,
					password, operatorID, accountPhoto, lastName,sync,phoneID);
			aclist.add(ac);
		}

		result.close();
		return aclist;
	}
	
	public List<Account> getAccountList(boolean edit) {
		String sql = "SELECT * FROM `Account`  ORDER BY accountID ASC";
		
		Cursor result = msqlitedb.rawQuery(sql, null);
		List<Account> aclist = new LinkedList<Account>();

		for (result.moveToFirst(); !result.isAfterLast(); result.moveToNext()) {
			int accountID = result.getInt(0);
			int accountPrivilege = result.getInt(1);
			String firstName = result.getString(2);
			String account = result.getString(3);
			String password = result.getString(4);
			String operatorID = result.getString(5);
			String accountPhoto = result.getString(6);
			String lastName = result.getString(7);
			int sync = result.getInt(8);
			int phoneID = result.getInt(9);

			Account ac = new Account(accountID, accountPrivilege, firstName, account,
					password, operatorID, accountPhoto, lastName, sync, phoneID);
			aclist.add(ac);
		}

		result.close();
		return aclist;
	}
	
	public Account getAccountByName(String mAccount) {
		String sql = "SELECT * FROM `Account` where account = \'"+Common.getSqlString(mAccount)+"\'";
		Cursor result = msqlitedb.rawQuery(sql, null);
		boolean retval = result.moveToFirst();
		if(retval == false)
		{
			result.close();
			return null;
		}
		int accountID = result.getInt(0);
		int accountPrivilege = result.getInt(1);
		String accountName = result.getString(2);
		String account = result.getString(3);
		String password = result.getString(4);
		String operatorID = result.getString(5);
		String accountPhoto = result.getString(6);
		String lastName = result.getString(7);
		int sync = result.getInt(8);
		int phoneID = result.getInt(9);
		
		Account ac = new Account(accountID, accountPrivilege, accountName, account, password,
				operatorID, accountPhoto, lastName, sync, phoneID);
		result.close();
		return ac;
	}
	
	public Account getAccountById(int mAccountId) {
		String sql = "SELECT * FROM `Account` where accountID = "+mAccountId;
		Cursor result = msqlitedb.rawQuery(sql, null);
		boolean retval = result.moveToFirst();
		if(retval == false) {
			result.close();
			return null;
		}
		int accountID = result.getInt(0);
		int accountPrivilege = result.getInt(1);
		String accountName = result.getString(2);
		String account = result.getString(3);
		String password = result.getString(4);  ;
		String operatorID = result.getString(5);
		String accountPhoto = result.getString(6);
		String lastName = result.getString(7);
		int sync = result.getInt(8);
		int phoneID = result.getInt(9);
		
		Account ac = new Account(accountID, accountPrivilege, accountName, account, password,
				operatorID, accountPhoto, lastName, sync, phoneID);
		result.close();
		return ac;
	}
	
	public void deleteAccount(int mAccountId) {
		String sql = "delete from Account where accountID="+mAccountId+" and accountPrivilegeID>1";
		msqlitedb.execSQL(sql);
	}
	
	public int saveAccount(Account mAccount) {
		if(mAccount == null) {
			return -1;
		}
		if(checkHasSame(mAccount)) {//修改
			try {
				String passWord = Common.SHA1.toSHA1(mAccount.getPass()).toUpperCase();
				String sql = "UPDATE `Account` SET accountPrivilegeID="+mAccount.getAccountPrivilege()+
						",firstName='"+Common.getSqlString(mAccount.getFirstName())+"',lastName='"+Common.getSqlString(mAccount.getLastName())+
						"',account='"+mAccount.getAccount()+"',pass='"+passWord+"'"+
						" where accountID="+mAccount.getAccountID()+" and accountPrivilegeID>=1";
				msqlitedb.execSQL(sql);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return 0;
		} else {//增加
			if(checkAlreadyIn(mAccount)){
				
				return -2;
			}
			ContentValues values = new ContentValues();
		    values.put("accountPrivilegeID", mAccount.getAccountPrivilege());
		    values.put("firstName",mAccount.getFirstName());
		    values.put("lastName",mAccount.getLastName());
		    values.put("account", mAccount.getAccount());
		    values.put("phoneID", mAccount.getPhoneID());
		    int length = mAccount.getPass().length();
		  //  int length = mAccount.getAccount().length();
		    MyLog.i("priority==="+mAccount.getAccountID()+"---"+length);
		    if(mAccount.getAccountID() < 0 && length >=6) {
		    	String sha1pass = null;
				try {
					sha1pass = Common.SHA1.toSHA1(mAccount.getPass().trim()).
							toUpperCase();
					values.put("pass",sha1pass);
				} catch (Exception e) {
					e.printStackTrace();
					return -1;
				}
		    } else {
		    	return -3;
		    }
		    
		    msqlitedb.insertWithOnConflict("Account", null, values, SQLiteDatabase.CONFLICT_REPLACE);
		    return 0;
		}
	}
	
	
	private boolean checkAlreadyIn(Account mAccount) {
		if(mAccount == null)
			return false;
		String sql = "SELECT COUNT(*) from `Account` WHERE account='"+mAccount.getAccount()+"'";
		Cursor result = msqlitedb.rawQuery(sql, null);
		boolean retval = result.moveToFirst();
		if(retval == false) {
			result.close();
			return false;
		}
		if(result.getInt(0) > 0) {
			result.close();
			return true;
		} else {
			result.close();
			return false;
		}
	}
	
	private boolean checkHasSame(Account mAccount) {
		if(mAccount == null)
			return false;
		String sql = "SELECT COUNT(*) from `Account` WHERE accountID="+mAccount.getAccountID();
		Cursor result = msqlitedb.rawQuery(sql, null);
		boolean retval = result.moveToFirst();
		if(retval == false) {
			result.close();
			return false;
		}
		
		if(result.getInt(0) > 0) {
			result.close();
			return true;
		} else {
			result.close();
			return false;
		}
	}
	
	public boolean checkCurrentPassword(String pass) {
		String password = "";
		try {
			password = Common.SHA1.toSHA1(pass).toUpperCase(Locale.ENGLISH);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(mCurrentAccount != null) {
			return mCurrentAccount.getPass().equals(password);
		} else {
			return false;
		}
	}
	
	public Account getCurrentAccount() {
		return mCurrentAccount;
	}

	public ManageLevel getManageLevel() {
		return null;
	}
	
	@SuppressLint("DefaultLocale")
	public int checkPassword(String username,String password) {
		Account account = null;
		try {
			account = new Account(username,Common.SHA1.toSHA1(password).toUpperCase());
		} catch (Exception e) {
			e.printStackTrace();
		}
		int result =  (Integer) account.login();
		if(result == 2)//成功
			mCurrentAccount.InitWith(account);
		return result;
	}
	
	public BaseInfoManager getmBaseInfo() {
		return mBaseInfo;
	}
	
	public void logout() {
		mBaseInfo.clearAllInfos();
		//TODO: is a error here
		// please check it! @Imbak
		mCurrentAccount = new Account();
		DealModel.getInstance().removeAll();		
	}
}
