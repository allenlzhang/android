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
    /**
     * 获取信息中心列表
     * @param callback
     */
    public static void GetInformationCentreInfoListResult(BaseParser.ResultCallback<InformationCategoryInfoList> callback){
        HashMap mHashMap = CreateHashMap.getNullData();
        InformationCentreInfoListParser parser = new InformationCentreInfoListParser(callback);
        parser.setTest(false);
        parser.executePost(URLConfig.getM_SECRETARY_CATEGORY_URL(),mHashMap);
    }


    /**
     * 行车报告日期
     * @param callback
     */
    public static void GetReportdateResult(BaseParser.ResultCallback callback){
        HashMap mHashMap = CreateHashMap.getNullData();
        ReportDateParser parser = new ReportDateParser(callback);
        parser.setTest(false);
        parser.executePost(URLConfig.getM_REPORTDATE_URL(),mHashMap);
    }

    /**
     * 指定用户行车日报
     * @param callback
     * @param date
     */
    public static void GetDayReportResult(BaseParser.ResultCallback<ReportDayInfo> callback, String date){
        HashMap mHashMap = CreateHashMap.getDayReportMap(date);
        ReportDayParser parser = new ReportDayParser(callback);
        parser.setTest(false);
        parser.executePost(URLConfig.getM_REPORTDAY_URL(),mHashMap);
    }

    /**
     * 某一天的行车日志
     * @param callback
     * @param date
     */
    public static void GetDayReportLogResult(BaseParser.ResultCallback<List<ReportDayLogInfo>> callback, String date){
        HashMap mHashMap = CreateHashMap.getDayReportMap(date);
        ReportDayLogParser parser = new ReportDayLogParser(callback);
        parser.setTest(false);
        parser.executePost(URLConfig.getM_REPORTDAYLOG_URL(),mHashMap);
    }

    /**
     * 指定用户行车月报
     * @param callback
     * @param date
     */
    public static void GetMonthReportResult(BaseParser.ResultCallback callback,String date){
        HashMap mHashMap = CreateHashMap.getDayReportMap(date);
        ReportMonthParser parser = new ReportMonthParser(callback);
        parser.setTest(false);
        parser.executePost(URLConfig.getM_MONTHREPORT_URL(),mHashMap);
    }

    /**
     * 获取某年月报统计数据
     * @param callback
     * @param date
     */
    public static void GetMonthReportLogResult(BaseParser.ResultCallback<MonthStatisticChartInfo> callback, String date){
        HashMap mHashMap = CreateHashMap.getDayReportMap(date);
        ReportMonthStatisticParser parser = new ReportMonthStatisticParser(callback);
        parser.setTest(false);
        parser.executePost(URLConfig.getM_MONTHREPORTSTATISTIC_URL(),mHashMap);
    }

    /**
     *车秘书提醒
     * @param callback
     */
    public static void GetInformationMessageResult(BaseParser.ResultCallback<InformationMessageInfoList> callback, int class1){
        HashMap mHashMap = CreateHashMap.getMessageMap(class1);
        InformationMessageListParser parser = new InformationMessageListParser(callback);
        parser.setTest(false);
        parser.executePost(URLConfig.getM_SECRETARY_MESSAGE_URL(),mHashMap);
    }

    /**
     * 获取一年中每月平均得分
     * @param callback
     * @param date
     */
    public static void GetUserMonthPointResult(BaseParser.ResultCallback<List<ReportCalendarMonthInfo>> callback, String date){
        HashMap mHashMap = CreateHashMap.getDayReportMap(date);
        ReportCalendarMonthParser parser = new ReportCalendarMonthParser(callback);
        parser.setTest(false);
        parser.executePost(URLConfig.getM_USER_MONTH_POINT_URL(),mHashMap);
    }

    /**
     * 用户一月中日平均得分
     * @param callback
     * @param date
     */
    public static void GetUserDayPointResult(BaseParser.ResultCallback<List<ReportCalendarMonthInfo>> callback, String date){
        HashMap mHashMap = CreateHashMap.getDayReportMap(date);
        ReportCalendarDayParser parser = new ReportCalendarDayParser(callback);
        parser.setTest(false);
        parser.executePost(URLConfig.getM_USER_DAY_POINT_URL(),mHashMap);
    }

    /**
     * 获取生涯首页数据
     * @param callback
     */
    public static void GetCareerResult(BaseParser.ResultCallback callback){
        HashMap mHashMap = CreateHashMap.getNullData();
        CareerlParser parser = new CareerlParser(callback);
        parser.setTest(false);
        parser.executePost(URLConfig.getM_CAREER_URL(),mHashMap);
    }

    /**
     * 读取里程
     * @param callback
     */
    public static void GetMilesInfoResult(BaseParser.ResultCallback callback){
        HashMap mHashMap = CreateHashMap.getNullData();
        MilesInfoParser parser = new MilesInfoParser(callback);
        parser.setTest(false);
        parser.executePost(URLConfig.getM_MILESINFO_URL(),mHashMap);
    }

    /**
     * GPS行车轨迹
     * @param callback
     * @param gpsStartTime
     * @param gpsStopTime
     * @param runSn
     */
    public static void GetReportGpsResult(BaseParser.ResultCallback<List<ReportGpsInfo>> callback,
                                          String gpsStartTime,String gpsStopTime,String runSn){
        HashMap mHashMap = CreateHashMap.getReportGpsMap(gpsStartTime,gpsStopTime,runSn);
        ReportGpsParser parser = new ReportGpsParser(callback);
        parser.setTest(false);
        parser.executePost(URLConfig.getM_GETCOOR_URL(),mHashMap);
    }

    /**
     * 提醒项 删除
     * @param callback
     * @param class1
     * @param messageid
     */
    public static void GetRemindDeleteResult(BaseParser.ResultCallback callback,int class1,int messageid){
        HashMap mHashMap = CreateHashMap.getRemindDefaultMap(class1,messageid);
        RemindDefaultParser parser = new RemindDefaultParser(callback);
        parser.setTest(false);
        parser.executePost(URLConfig.getM_SECRETARY_DELETE_URL(),mHashMap);
    }


    public static void GetLoginResult(BaseParser.ResultCallback callback){
        HashMap mHashMap = CreateHashMap.getLogin("18700996667","888888");
        LoginInfoParser parser = new LoginInfoParser(callback);
        parser.setTest(false);
        parser.executePost(URLConfig.getM_LOGIN_URL(),mHashMap);
    }


}
