package com.eth.api;

public enum WhisperMethod {
	

	SHH_VERSION("shh_version"),
	SHH_INFO("shh_info"),
	SHH_SETMAXMESSAGESIZE("shh_setMaxMessageSize"),
	SHH_SETMINPOW("shh_setMinPoW"),
	SHH_MARKTRUSTEDPEER("shh_markTrustedPeer"),
	SHH_NEWKEYPAIR("shh_newKeyPair"),
	SHH_ADDPRIVATEKEY("shh_addPrivateKey"),
	SHH_DELETEKEYPAIR("shh_deleteKeyPair"),
	SHH_HASKEYPAIR("shh_hasKeyPair"),
	SHH_GETPUBLICKEY("shh_getPublicKey"),
	SHH_GETPRIVATEKEY("shh_getPrivateKey"),
	SHH_NEWSYMKEY("shh_newSymKey"),
	SHH_ADDSYMKEY("shh_addSymKey"),
	SHH_GENERATE_SYM_KEY_FROM_PASSWORD("shh_generateSymKeyFromPassword"),
	SHH_HASSYMKEY("shh_hasSymKey"),
	SHH_GETSYMKEY("shh_getSymKey"),
	SHH_DELETESYMKEY("shh_deleteSymKey"),
	SHH_SUBSCRIBE("shh_subscribe"),
	SHH_UNSUBSCRIBE("SHH_UNSUBSCRIBE"),
	SHH_NEW_MESSAGE_FILTER("shh_newMessageFilter"),
	SHH_DELETEMESSAGEFILTER("shh_deleteMessageFilter"),
	SHH_GET_FILTER_MESSAGES("shh_getFilterMessages"),
	SHH_POST("shh_post");
    
    String methodName;
    
    WhisperMethod(String methodName)
    {
		this.methodName = methodName;
    }

    
    public String getMethodName()
    {
    	return methodName;
    }
}
