package kr.bgsoft.ucargo.jeju.controller.conf

object Config {
    val SET_TEST = false
    val SET_LOG = true

    val SET_AES         = "935D8275B1EB4892"
    val SET_HASH        = "1d5d30a1bfae249c"
    val SET_NMAPKEY     = "dOEP4CHqQW1r9QLUjB_F"

    val HDL_POINT       = 0

    val VAL_SSID        = "asdfasd7asdfjlksad"
    val VAL_SSVER       = "zcxkjaklsdjflkjsss"
    val VAL_SSINFO      = "eaw87dsjhx87csdhfs"
    val VAL_SSCONFIG    = "akdjhflukajraasdfk"
    val VAL_NICEID      = "adujfosdajfjaskjdd"
    val VAL_SEARCH      = "zalkadfjssasasdfff"
    val VAL_FCMID       = "a98cuvysqjsfudawea"
    val VAL_QRSEND      = "aiicjvxjewqajdvasa"

    val DB_FCM          = "kr_bgsoft_ucargo_jeju_fcm.db"
    val DB_FCM_ver      = 1
    val DB_Notice       = "kr_bgsoft_ucargo_jeju_notice.db"
    val DB_Notice_ver   = 1
    val DB_Infos        = "kr_bgsoft_ucargo_jeju_infos.db"
    val DB_Infos_ver    = 1
    val DB_Reads        = "kr_bgsoft_ucargo_jeju_reads.db"
    val DB_Reads_ver    = 1

    val EXTRA_IDX       = "cxziuoaewjtkldf"
    val EXTRA_TYPE      = "xzxzioasdklqakl"
    val EXTRA_DATA      = "datalkdjflc8ivj"
    val EXTRA_ORDER     = "a98dasdfjkljcxv"

    val SMATRO_PKG      = "com.smartro.secapps.freepay"
    val SMATRO_SEND     = "card"
    val SMATRO_CANCLE   = "card_cancel"

    val SMATRO_ANSWER_TRANCODE      = "trancode"
    val SMATRO_ANSWER_APPNO         = "approvalno"
    val SMATRO_ANSWER_APPDATE       = "approvaldate"
    val SMATRO_ANSWER_CARDNO        = "cardno"
    val SMATRO_ANSWER_TERMINALID    = "catid"
    val SMATRO_ANSWER_TRANNO        = "tranno"
    val SMATRO_ANSWER_RECEIPTNO     = "receiptno"
    val SMATRO_ANSWER_TOTALAMOUNT   = "totalamount"
    val SMATRO_ANSWER_SURTAX        = "surtax"
    val SMATRO_ANSWER_SEVICETIP     = "servicetip"
    val SMATRO_ANSWER_OUTPUTMSG     = "outmessage"
    val SMATRO_ANSWER_ISSUERNAME    = "issuername"
    val SMATRO_ANSWER_AUTHEDNO      = "authedno"
    val SMATRO_ANSWER_AUTHEDDATE    = "autheddate"
    val SMATRO_ANSWER_SUCCESS       = 0
    val SMATRO_ANSWER_CALL_INST     = "installment"

    val RESULT_SPLASH   = 1
    val RESULT_BANK     = 2
    val RESULT_SMS      = 3
    val RESULT_LOGIN    = 4
    val RESULT_JOIN     = 5
    val RESULT_SMARTRO  = 66

    val RTNINT_SUCESS   = 1
    val RTNINT_CANCLE   = 2
    val RTNINT_LOGIN    = 3
    val RTNINT_SMSCHECK = 4
    val RTNINT_FINISH   = 5
    val RTNINT_JOIN     = 6
    val RTNINT_ERROR    = 9

    val READ_CARDRULE   = 993

    val RULE1           = "terms/agreement/1"
    val RULE2           = "terms/agreement/2"
    val RULE3           = "terms/agreement/3"
    val RULE4           = "terms/agreement/4"

    val FCM_NORMAL  = 0
    val FCM_ORDER   = 1
    val FCM_NBOARD  = 2
    val FCM_JBOARD  = 3
    val FCM_BANK    = 4
    val FCM_MIN     = 5
    val FCM_HBOARD  = 6
    val FCM_QRSEND  = 7
    val FCM_HNORMAL = 8
    val FCM_LOGOUT  = 99


    val BOARD_NOTICE    = 0
    val BOARD_JIBU      = 1
    val BOARD_HNOTICE   = 2
    val BOARD_NEWS      = 3
    val BOARD_FAQ       = 4

    val QR_SCANNER          = "scanner"
    val QR_IN               = "in"
    val QR_OUT              = "out"
    val QR_STATUS_PROCESS   = "process"
    val QR_STATUS_OK        = "ok"
    val QR_STATUS_CANCEL    = "cancel"
}