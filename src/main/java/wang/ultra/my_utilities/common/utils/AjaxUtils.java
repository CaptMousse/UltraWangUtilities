package wang.ultra.my_utilities.common.utils;

import java.util.HashMap;
import java.util.Map;

public class AjaxUtils {

    private boolean status;
    private String msg;
    private Object obj;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public AjaxUtils(boolean status, String msg, Object obj) {
        this.status = status;
        this.msg = msg;
        this.obj = obj;
    }

    @Override
    public String toString() {
        return "AjaxUtils{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", obj=" + obj +
                '}';
    }

    public static AjaxUtils success() {
        return new AjaxUtils(true, "", "");
    }

    public static AjaxUtils success(String msg) {
        return new AjaxUtils(true, msg, "");
    }

    public static AjaxUtils success(Object obj) {
        return new AjaxUtils(true, "", obj);
    }

    public static AjaxUtils success(String msg, Object obj) {
        return new AjaxUtils(true, msg, obj);
    }

    public static AjaxUtils failed() {
        return new AjaxUtils(false, "", "");
    }

    public static AjaxUtils failed(String msg) {
        return new AjaxUtils(false, msg, "");
    }

    public static AjaxUtils failed(Object obj) {
        return new AjaxUtils(false, "", obj);
    }

    public static AjaxUtils failed(String msg, Object obj) {
        return new AjaxUtils(false, msg, obj);
    }

    public static Map<String, Object> successMap() {
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("status", true);
        return returnMap;
    }

    public static Map<String, Object> successMap(String msg) {
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("status", true);
        returnMap.put("msg", msg);
        return returnMap;
    }

    public static Map<String, Object> successMap(Object obj) {
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("status", true);
        returnMap.put("obj", obj);
        return returnMap;
    }

    public static Map<String, Object> successMap(String msg, Object obj) {
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("status", true);
        returnMap.put("msg", msg);
        returnMap.put("obj", obj);
        return returnMap;
    }

    public static Map<String, Object> failedMap() {
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("status", false);
        return returnMap;
    }

    public static Map<String, Object> failedMap(String msg) {
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("status", false);
        returnMap.put("msg", msg);
        return returnMap;
    }

    public static Map<String, Object> failedMap(Object obj) {
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("status", false);
        returnMap.put("obj", obj);
        return returnMap;
    }

    public static Map<String, Object> failedMap(String msg, Object obj) {
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("status", false);
        returnMap.put("msg", msg);
        returnMap.put("obj", obj);
        return returnMap;
    }


    public static String successJsonString() {
        Map<String, Object> map = new HashMap<>();
        map.put("status", true);
        return JsonConverter.MapToJsonString(map);
    }

    public static String successJsonString(String msg) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", true);
        map.put("msg", msg);
        return JsonConverter.MapToJsonString(map);
    }

    public static String successJsonString(Object obj) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", true);
        map.put("obj", obj);
        return JsonConverter.MapToJsonString(map);
    }

    public static String successJsonString(String msg, Object obj) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", true);
        map.put("msg", msg);
        map.put("obj", obj);
        return JsonConverter.MapToJsonString(map);
    }

    public static String failedJsonString() {
        Map<String, Object> map = new HashMap<>();
        map.put("status", false);
        return JsonConverter.MapToJsonString(map);
    }

    public static String failedJsonString(String msg) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", false);
        map.put("msg", msg);
        return JsonConverter.MapToJsonString(map);
    }

    public static String failedJsonString(Object obj) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", false);
        map.put("obj", obj);
        return JsonConverter.MapToJsonString(map);
    }

    public static String failedJsonString(String msg, Object obj) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", false);
        map.put("msg", msg);
        map.put("obj", obj);
        return JsonConverter.MapToJsonString(map);
    }

}
