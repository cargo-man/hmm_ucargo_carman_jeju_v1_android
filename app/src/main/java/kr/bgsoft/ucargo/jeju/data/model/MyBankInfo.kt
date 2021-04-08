package kr.bgsoft.ucargo.jeju.data.model

import java.io.Serializable

data class MyBankInfo(
        var sBankAccountCertCode: String = ""
        , var sVirtualAccount: String = ""
        , var sBankAccount: String = ""
        , var sBankAccountCertMsg: String = ""
        , var sBankUserName: String = ""
        , var sBankName: String = ""
        , var nUserPoint: Int = 0
        , var nMaxPay: Int = 0
        , var nUsePay: Int = 0
) : Serializable