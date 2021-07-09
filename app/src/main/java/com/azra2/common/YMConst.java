package com.azra2.common;

import com.azra2.R;

public interface YMConst {

    String KEY_CURRENT_USER = "azra_current_user";
    String KEY_FAVORITE_DEAL    = "key_favorite_deal";
    String KEY_FAVORITE_COUPON  = "key_favorite_coupon";

    /**
     * status			0-offline, 1-online(active), 2-busy(on call)
     * room_type		0-undefined, 1- public, 2-private, 3-private adult
     * call_type		0-undefined, 1-voice, 2- video
     * */
    //status
    int STATUS_OFFLINE = 0;
    int STATUS_ONLINE = 1;
    int STATUS_BUSY = 2;
    //room type
    int RT_UNDEFINED = 0;
    int RT_PUBLIC = 1;
    int RT_PRIVATE = 2;
    int RT_ADULT = 3;
    //call type
    int CT_UNDEFINED = 0;
    int CT_VOICE = 1;
    int CT_VIDEO = 2;

    //
    String PAYMENT_HISTORY = "payment_history";
    String MINUTE_HISTORY = "minute_history";

    //payment method
    int PM_VISA = 0;
    int PM_MASTERCARD = 1;
    int PM_PAYPAL = 2;
    int PM_GOOGLE = 3;

    //balance degree
    int BD_AGENT = 0;
    int BD_CROWN = 1;
    int BD_DIAMOND = 2;
    int BD_COIN = 3;
    int BD_NOTHING = 4;

    //caller , callee
    int ROLE_CALLER = 0;
    int ROLE_CALLEE = 1;

    String CALLING_ROLE = "calling_role";


    String ROOM_TYPE = "room_type";
    String CALL_TYPE = "call_type";

    String CHANNEL_NAME = "channel_name";
    String AGENT_NAME = "agent_name";
    String AG_AGENT_ID = "agent_id";
    String USER_NAME = "user_name";
    String AG_USER_ID = "user_id";

    //color of username in message according to balance degree
    int[] MESSAGE_COLOR_LIST = new int[]{
            R.color.yellow_400, //agent//0
            R.color.yellow_400, //crown//1
            R.color.cyan_400, //diamond//2
            R.color.purple_400, //coin//3
            R.color.blue_400 //normal//4
    };

    //colors for chips(tags)
    int[] CHIP_COLOR_ARRAY = new int[]{
            R.color.pink_400,
            R.color.red_400,
            R.color.blue_400,
            R.color.green_400,
            R.color.purple_400,
            R.color.yellow_400
    };

    //for login and logout
    String LOG_STATE = "log_state";
    String LOGGED_IN = "logged_in";
    String LOGGED_OUT = "logged_out";
    String LOG_PASS = "log_pass";
    String LOG_EMAIL = "log_email";
    String LOG_TOKEN = "log_token";

    //for spicy LEVEL
    int SPICY_ONE = 1;
    int SPICY_TWO = 2;
    int SPICY_THREE = 3;

    //
    String NTF_TYPE = "type"; //notification type
    String NTF_LOGOUT = "logout";
    String NTF_AGENTLOGIN = "agentlogin";

    //server response fail messages
    String MSG_INVALID_TOKEN = "AppToken invalid.";

    //in-app-purchage productId
    String GET_1_COIN = "get_1_coin";
    String GET_2_COINS = "get_2_coins";
    String GET_3_COINS = "get_3_coins";
    String GET_5_COINS = "get_5_coins";
    String GET_10_COINS = "get_10_coins";
    String GET_20_COINS = "get_20_coins";
    String GET_30_COINS = "get_30_coins";
    String GET_50_COINS = "get_50_coins";
    String GET_100_COINS = "get_100_coins";

    //payment type
    String PAYMENT_COIN = "buycoin";
    String PAYMENT_COIN_GIFT = "coingift";
    String PAYMENT_MONEY_GIFT = "money_gift";
    String PAYMENT_COIN_TIP = "cointip";
    String PAYMENT_MONEY_TIP = "money_tip";


}
