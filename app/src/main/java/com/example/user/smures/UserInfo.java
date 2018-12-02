package com.example.user.smures;

public class UserInfo {
    private static String uid="";
    private static String url = "http://35.243.113.55/4c/";
    private static String name="";
    private static String department = "";
    private static String phoneNum = "";
    private static String type ="";
    //교수 : Id, 전화번호, 이름
    //대학 : Id(대학명), 전화번호
    //시설 : Id
    //학생 : Id, name, 학과, 전화번호


    public static String getUid() { return uid; }
    public static void setUid(String uid) { UserInfo.uid = uid; }

    public static String getUrl() { return url; }
    public static void setUrl(String url) { UserInfo.url = url; }

    public static String getName() { return name; }
    public static void setName(String name) { UserInfo.name = name; }

    public static String getDepartment() { return department; }
    public static void setDepartment(String department) { UserInfo.department = department; }

    public static String getPhoneNum() { return phoneNum; }
    public static void setPhoneNum(String phoneNum) { UserInfo.phoneNum = phoneNum; }

    public static String getType() { return type; }
    public static void setType(String type) { UserInfo.type = type; }
}
