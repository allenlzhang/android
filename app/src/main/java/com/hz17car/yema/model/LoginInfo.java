package com.hz17car.yema.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.hz17car.yema.YemaApplication;
import com.hz17car.yema.data.BaseResponseInfo;

/**
 * Created by Administrator on 2017/10/10 0010.
 */

public class LoginInfo extends BaseResponseInfo {
    /**
     * 用户信息
     */
    private static int id;// 用户主键ID
    private static String user_id;// 用户编号
    private static String user_account;//用户名
    private static String user_name;//员工姓名

    private static int user_agency;//隶属机构类别1=厂商，2=集团，3=4s店
    public final static int AGENCY_FIRM = 1;//厂商
    public final static int AGENCY_GROUP = 2;//集团
    public final static int AGENCY_4S = 3;//4s店

    private static int user_agency_id;//隶属机构ID
    private static int user_agency_id_level;//隶属机构等级1=管理级，2=员工级
    public final static int AGENCY_LEVEL_MANAGE = 1;//管理级
    public final static int AGENCY_LEVEL_STAFF = 2;//员工级

    private static int user_department;//隶属机构内部门ID
    private static int user_department_level;// 隶属机构内部门帐号等级1=主管，2=普通员工
    public final static int AGENCY_TYPE_MANAGER = 1;//管理级
    public final static int AGENCY_TYPE_STAFF = 2;//员工级

    private static int user_office;// 部门职位ID
    private static int user_sex;// 性别0=未知，1=男，2=女
    public final static int SEX_UNKNOW = 0;//未知
    public final static int SEX_MAN = 1;//男
    public final static int SEX_WOMAN = 2;//女

    private static int user_role;// 角色ID
    private static String user_phone = "";// 	用户手机
    private static int user_status;// 用户帐号状态1=启用，2=停用，3=作废
    public final static int USER_STATUS_INUSE = 1;//启用
    public final static int USER_STATUS_STOPUSE = 2;//停用
    public final static int USER_STATUS_DISABLE = 3;//作废

    private static int user_createtime;// 创建时间戳
    private static String user_remark = "";// 	备注
    private static int user_lastlogin;// 用户上一次登录时间戳
    private static int user_create_id;// 创建者ID
    private static int login_time;// 	当前登录时间戳
    private static String office_name = "";//职位名称
    private static int office_department_id;//

    private static String agency_name = "";//机构名称
    private static String wangzhi = "";//机构网址
    private static String tell = "";//机构电话

    private static String token = "";// 当前登录者有效token

    public static final String PREF_USER = "user_pref_erp";

    private static SharedPreferences user_pref = YemaApplication.getInstanse()
            .getSharedPreferences(PREF_USER, Context.MODE_PRIVATE);
    private static SharedPreferences.Editor editor = user_pref.edit();

    public static int getId() {
        id = user_pref.getInt("id", id);
        return id;
    }

    public static void setId(int id) {
        LoginInfo.id = id;
        editor.putInt("id", id);
    }

    public static String getUser_id() {
        user_id = user_pref.getString("user_id", "");
        return user_id;
    }

    public static void setUser_id(String user_id) {
        LoginInfo.user_id = user_id;
        editor.putString("user_id", user_id);
    }

    public static String getUser_account() {
        user_account = user_pref.getString("user_account", "");
        return user_account;
    }

    public static void setUser_account(String user_account) {
        LoginInfo.user_account = user_account;
        editor.putString("user_account", user_account);
    }

    public static String getUser_name() {
        user_name = user_pref.getString("user_name", "");
        return user_name;
    }

    public static void setUser_name(String user_name) {
        LoginInfo.user_name = user_name;
        editor.putString("user_name", user_name);
    }

    public static int getUser_agency() {
        user_agency = user_pref.getInt("user_agency", user_agency);
        return user_agency;
    }

    public static void setUser_agency(int user_agency) {
        LoginInfo.user_agency = user_agency;
        editor.putInt("user_agency", user_agency);
    }

    public static int getUser_agency_id() {
        user_agency_id = user_pref.getInt("user_agency_id", user_agency_id);
        return user_agency_id;
    }

    public static void setUser_agency_id(int user_agency_id) {
        LoginInfo.user_agency_id = user_agency_id;
        editor.putInt("user_agency_id", user_agency_id);
    }

    public static int getUser_agency_id_level() {
        user_agency_id_level = user_pref.getInt("user_agency_id_level", user_agency_id_level);
        return user_agency_id_level;
    }

    public static void setUser_agency_id_level(int user_agency_id_level) {
        LoginInfo.user_agency_id_level = user_agency_id_level;
        editor.putInt("user_agency_id_level", user_agency_id_level);
    }

    public static int getUser_department() {
        user_department = user_pref.getInt("user_department", user_department);
        return user_department;
    }

    public static void setUser_department(int user_department) {
        LoginInfo.user_department = user_department;
        editor.putInt("user_department", user_department);
    }

    public static int getUser_department_level() {
        user_department_level = user_pref.getInt("user_department_level", user_department_level);
        return user_department_level;
    }

    public static void setUser_department_level(int user_department_level) {
        LoginInfo.user_department_level = user_department_level;
        editor.putInt("user_department_level", user_department_level);
    }

    public static int getUser_office() {
        user_office = user_pref.getInt("user_office", user_office);
        return user_office;
    }

    public static void setUser_office(int user_office) {
        LoginInfo.user_office = user_office;
        editor.putInt("user_office", user_office);
    }

    public static int getUser_sex() {
        user_sex = user_pref.getInt("user_sex", user_sex);
        return user_sex;
    }

    public static void setUser_sex(int user_sex) {
        LoginInfo.user_sex = user_sex;
        editor.putInt("user_sex", user_sex);
    }

    public static int getUser_role() {
        user_role = user_pref.getInt("user_role", user_role);
        return user_role;
    }

    public static void setUser_role(int user_role) {
        LoginInfo.user_role = user_role;
        editor.putInt("user_role", user_role);
    }

    public static String getUser_phone() {
        user_phone = user_pref.getString("user_phone", "");
        return user_phone;
    }

    public static void setUser_phone(String user_phone) {
        LoginInfo.user_phone = user_phone;
        editor.putString("user_phone", user_phone);
    }

    public static int getUser_status() {
        user_status = user_pref.getInt("user_status", user_status);
        return user_status;
    }

    public static void setUser_status(int user_status) {
        LoginInfo.user_status = user_status;
        editor.putInt("user_status", user_status);
    }

    public static int getUser_createtime() {
        user_createtime = user_pref.getInt("user_createtime", user_createtime);
        return user_createtime;
    }

    public static void setUser_createtime(int user_createtime) {
        LoginInfo.user_createtime = user_createtime;
        editor.putInt("user_createtime", user_createtime);
    }

    public static String getUser_remark() {
        user_remark = user_pref.getString("user_remark", "");
        return user_remark;
    }

    public static void setUser_remark(String user_remark) {
        LoginInfo.user_remark = user_remark;
        editor.putString("user_remark", user_remark);
    }

    public static int getUser_lastlogin() {
        user_lastlogin = user_pref.getInt("user_lastlogin", user_lastlogin);
        return user_lastlogin;
    }

    public static void setUser_lastlogin(int user_lastlogin) {
        LoginInfo.user_lastlogin = user_lastlogin;
        editor.putInt("user_lastlogin", user_lastlogin);
    }

    public static int getUser_create_id() {
        user_create_id = user_pref.getInt("user_create_id", user_create_id);
        return user_create_id;
    }

    public static void setUser_create_id(int user_create_id) {
        LoginInfo.user_create_id = user_create_id;
        editor.putInt("user_create_id", user_create_id);
    }

    public static int getLogin_time() {
        login_time = user_pref.getInt("login_time", login_time);
        return login_time;
    }

    public static void setLogin_time(int login_time) {
        LoginInfo.login_time = login_time;
        editor.putInt("login_time", login_time);
    }

    public static String getOffice_name() {
        office_name = user_pref.getString("office_name", office_name);
        return office_name;
    }

    public static void setOffice_name(String office_name) {
        LoginInfo.office_name = office_name;
        editor.putString("office_name", office_name);
    }

    public static int getOffice_department_id() {
        office_department_id = user_pref.getInt("office_department_id", office_department_id);
        return office_department_id;
    }

    public static void setOffice_department_id(int office_department_id) {
        LoginInfo.office_department_id = office_department_id;
        editor.putInt("office_department_id", office_department_id);
    }

    public static String getAgency_name() {
        agency_name = user_pref.getString("agency_name", agency_name);
        return agency_name;
    }

    public static void setAgency_name(String agency_name) {
        LoginInfo.agency_name = agency_name;
        editor.putString("agency_name", agency_name);
    }

    public static String getWangzhi() {
        wangzhi = user_pref.getString("wangzhi", wangzhi);
        return wangzhi;
    }

    public static void setWangzhi(String wangzhi) {
        LoginInfo.wangzhi = wangzhi;
        editor.putString("wangzhi", wangzhi);
    }

    public static String getTell() {
        tell = user_pref.getString("tell", tell);
        return tell;
    }

    public static void setTel1(String tell) {
        LoginInfo.tell = tell;
        editor.putString("tell", tell);
    }

    public static String getToken() {
        token = user_pref.getString("token", "");
        return token;
    }

    public static void setToken(String token) {
        LoginInfo.token = token;
        editor.putString("token", token);
        editor.apply();
    }
}
