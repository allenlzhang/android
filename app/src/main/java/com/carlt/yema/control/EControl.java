package com.carlt.yema.control;


import com.carlt.yema.data.ReportGpsInfo;
import com.carlt.yema.data.home.InformationCategoryInfoList;
import com.carlt.yema.data.home.InformationMessageInfoList;
import com.carlt.yema.data.home.MonthStatisticChartInfo;
import com.carlt.yema.data.home.ReportCalendarMonthInfo;
import com.carlt.yema.data.home.ReportDayInfo;
import com.carlt.yema.data.home.ReportDayLogInfo;
import com.carlt.yema.protocolparser.BaseParser;
import com.carlt.yema.protocolparser.LoginInfoParser;
import com.carlt.yema.protocolparser.home.CareerlParser;
import com.carlt.yema.protocolparser.home.InformationCentreInfoListParser;
import com.carlt.yema.protocolparser.home.InformationMessageListParser;
import com.carlt.yema.protocolparser.home.MilesInfoParser;
import com.carlt.yema.protocolparser.home.RemindDefaultParser;
import com.carlt.yema.protocolparser.home.ReportCalendarDayParser;
import com.carlt.yema.protocolparser.home.ReportCalendarMonthParser;
import com.carlt.yema.protocolparser.home.ReportDateParser;
import com.carlt.yema.protocolparser.home.ReportDayLogParser;
import com.carlt.yema.protocolparser.home.ReportDayParser;
import com.carlt.yema.protocolparser.home.ReportGpsParser;
import com.carlt.yema.protocolparser.home.ReportMonthParser;
import com.carlt.yema.protocolparser.home.ReportMonthStatisticParser;
import com.carlt.yema.systemconfig.URLConfig;
import com.carlt.yema.utils.CreateHashMap;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Marlon on 2018/3/21.
 */

public class EControl {



    public static void GetLoginResult(BaseParser.ResultCallback callback){
        HashMap mHashMap = CreateHashMap.getLogin("18700996667","888888");
        LoginInfoParser parser = new LoginInfoParser(callback);
        parser.setTest(false);
        parser.executePost(URLConfig.getM_LOGIN_URL(),mHashMap);
    }


}
