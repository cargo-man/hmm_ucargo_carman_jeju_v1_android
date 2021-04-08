package kr.bgsoft.ucargo.jeju.data.model

data class UserInfo(
        var catid: String = ""           //카드결제 아이디
        , var vAccount: String = ""      //가상계좌
        , var bzNum: String = ""         //사업자번호
        , var mShipJoin: String = ""     //가맹점 가입동의
        , var lvType: String = ""        //차종
        , var name: String = ""          //성명
        , var lv00: String = ""          //회원구분
        , var lv01: String = ""          //회원구분
        , var id: String = ""            //아이디
        , var lvArea: String = ""        //검색
        , var point: String = ""         //가상포인트
        , var lv03: String = ""          //회원가입경로
        , var sComZip: String = ""       //업체우편번호
        , var sCartonName: String = ""   //차량톤수
        , var sCartonCode: String = ""   //차량톤코드
        , var sCeoName: String = ""      //대표자명
        , var sCarNum: String = ""       //차량번호
        , var sUptap: String = ""        //업태
        , var sUpjang: String = ""       //업종
        , var sComName: String = ""      //회사명
        , var sComAddr: String = ""      //회사주소
        , var sEmail: String = ""        //이메일
        , var sHP: String = ""           //휴대전화정보
        , var sGPSAddress: String = ""   //gps 정보
        , var sGPSTime: String = ""      //gps 시간정보
        , var sTRSNum: String = ""       //TRS정보
        , var sCartypeName: String = ""  //차량구분
        , var sCartypeCode: String = ""  //차량구분코드
        , var sBankUserName: String = "" //가상계좌 예금
        , var sBankName: String = ""     //가상계좌 은행명
        , var sCarName: String = ""      //차량명
        , var sCmsBankCode: String = ""  //은행코드
        , var nCmsAccount: String = ""   //은행계좌
        , var sCmsName: String = ""      //은행예금주
        , var sCmsBank: String = ""      //은행명
        , var sMemberidx: String = ""    //회원idx
        , var sAddress: String = ""      //차고지
        , var sPW: String = ""           //비밀번호
        , var sBzType: String = ""       //사업자구분
)