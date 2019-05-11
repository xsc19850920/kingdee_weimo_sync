package com.dhsy.async.utils;

import java.util.HashMap;
import java.util.Map;

import com.dhsy.async.entity.UserForKingdee;
import com.dhsy.async.entity.UserForWeimo;

public class ClientConfig {
	
	//part of kingdee
	public static final String KINGDEE_API_URL = "https://api.kingdee.com/auth/user/access_token";
	public static final String KINGDEE_CLIENT_ID = "202208";
	public static final String KINGDEE_CLIENT_SECRET = "9a5ef151341d85e8d41afca45d510b1d";
	public static final String KINGDEE_USERNAME = "13811877513";
	public static final String KINGDEE_PASSWORD = "13811877513guo";
	public static final String KINGDEE_DBID = "7984913393360";
	public static final String KINGDEE_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCBzvlStTMRloiQQ7nBZ/Rhgu4IzEwFngjc4l127f4A57rviFVPGD64Ac0qq2Ljp5CZI4y0GvA3UDpcZzJYDzcJ4oDFTfAu7ZJD1F2sGVY12dcyXO9e8GcZu1xlJoQDO0TdcHvVqHdxq253n/kppLCNelccgPBtYdsLCgeWYTz5RwIDAQAB";
	public static Map<String,UserForKingdee> mapForKingdee = new HashMap<String,UserForKingdee>();
	public static String kingdee_access_token;
	
	//part of weimo
//	public static final String WEIMO_REDIRECT_URI="http://39.107.141.192/getcodefromweimo";
	public static final String WEIMO_REDIRECT_URI="http://localhost/getcodefromweimo";
	public static final String WEIMO_API_URI = "https://dopen.weimob.com/fuwu/b/oauth2/token";
    public static final  String WEIMO_CLIENT_ID="525B0A631FD11A99EAEF6DFEA04CF131";
    public static final  String WEIMO_CLIENT_SECRET="0849D2D0022FE6349D52D125224AB395";
    public static final String WEIMO_GRANT_TYPE_AUTHORIZATION_CODE="authorization_code";
    public static final String WEIMO_GRANT_TYPE_REFRESH_TOKEN="refresh_token";
	public static Map<String,UserForWeimo> mapForWeimo = new HashMap<String,UserForWeimo>();
	
	public  static String weimo_access_token = "eb29bfb8-892b-454b-8e9a-c96f2c101dc8";
    public static  String weimo_refresh_token = "1d2e1ce2-54bc-46fa-b27d-10933e2cf809";
    

}
