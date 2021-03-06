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
	public static final String KINGDEE_PASSWORD = "13811877513jdy";
	public static final String KINGDEE_DBID = "7984913393360";
	public static final String KINGDEE_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCBzvlStTMRloiQQ7nBZ/Rhgu4IzEwFngjc4l127f4A57rviFVPGD64Ac0qq2Ljp5CZI4y0GvA3UDpcZzJYDzcJ4oDFTfAu7ZJD1F2sGVY12dcyXO9e8GcZu1xlJoQDO0TdcHvVqHdxq253n/kppLCNelccgPBtYdsLCgeWYTz5RwIDAQAB";
	public static Map<String,UserForKingdee> mapForKingdee = new HashMap<String,UserForKingdee>();
	public static String kingdee_access_token = "1564113605accbf23f92f0fb5c2cac2a";
	
	//part of weimo
	public static final String WEIMO_API_URI = "https://dopen.weimob.com/fuwu/b/oauth2/token";
    public static final  String WEIMO_CLIENT_ID="525B0A631FD11A99EAEF6DFEA04CF131";
    public static final  String WEIMO_CLIENT_SECRET="0849D2D0022FE6349D52D125224AB395";
    public static final String WEIMO_GRANT_TYPE_REFRESH_TOKEN="refresh_token";
	public static Map<String,UserForWeimo> mapForWeimo = new HashMap<String,UserForWeimo>();
	public  static String weimo_access_token = "81ca98ae-760a-4207-8c90-0935c25cb946";
    public static  String weimo_refresh_token = "5ced7f84-725d-4b58-a7bf-03f2f436dc1d";
    

}
