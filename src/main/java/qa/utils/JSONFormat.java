package qa.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import qa.exception.RunException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by snow.zhang on 2016/9/5.
 */
public class JSONFormat {

    public static Map getMapFromJson(String json){
        Gson gson = new Gson();
        return gson.fromJson(json,Map.class);
    }

    public static List getListFromJson(String json){
        Gson gson = new Gson();
        return gson.fromJson(json,List.class);
    }

    public static String getObjectToJson(Object object){
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        return gson.toJson(object);
    }

    public static <T> T fromJson(String json, Class<T> classOfT){
        Gson gson = new Gson();
        return gson.fromJson(json, classOfT);
    }

    /**
     * 根据表达式解析JSON
     * @param json 被解析的JSON字符串
     * @param jsonExpression 解析JSON的表达式
     * @return Object（Map||List）
     * @throws RunException
     */
    public static Object analyzeJsonExpression(String json,String jsonExpression) throws RunException {
        Object objJson;
        if (RegExp.findCharacters(json, "^\\{.*\\}$")){
            objJson = JSONFormat.getMapFromJson(json);
        }else if(RegExp.findCharacters(json, "^\\[.*\\]$")){
            objJson = JSONFormat.getListFromJson(json);
        }else {
            throw new RunException("JSON格式不正确。");
        }
        Object obj = objJson;
        String[] arr = jsonExpression.split("\\.");
        for(int i=0;i<arr.length;i++){
            String expressionTemp = arr[i];
            // 判断"[数字]"结尾
            if (RegExp.findCharacters(expressionTemp, "\\[\\d+\\]$")) {
                if (RegExp.findCharacters(expressionTemp,"(\\[\\d+\\]){2,}$")){
                    throw new RunException("不支持多维数据结构："+expressionTemp);
                }
                String strTemp = RegExp.filterString(expressionTemp, "^\\[\\d+\\]$");
                if (!StringUtil.isEmpty(strTemp)){
                    obj = getMapValue(obj, strTemp);
//                    obj = ((Map) obj).get(strTemp);
                }
                ArrayList<String> list = RegExp.matcherCharacters(expressionTemp, "(?<=\\[)\\d+(?=\\])");
                if (list.size() > 0) {
                    int index = Integer.parseInt(list.get(0));
                    obj = getListValue(obj, index);
//                    obj = ((List) obj).get(index);
                } else {
                    throw new RunException("表达式异常：" + expressionTemp);
                }
            }else if (RegExp.findCharacters(expressionTemp, "\\[\\]$")){
                // 此情况：获取list中所有map的key的value，存放在list中
                if (i+1 >= arr.length){
                    throw new RunException("表达式不符合规范："+jsonExpression);
                }
                if (RegExp.findCharacters(expressionTemp,"(\\[\\]){2,}$")){
                    throw new RunException("不支持多维数据结构："+expressionTemp);
                }
                expressionTemp = RegExp.filterString(expressionTemp,"^\\[\\]$");
                if (!StringUtil.isEmpty(expressionTemp)){
                    obj = getMapValue(obj, expressionTemp);
//                    obj = ((Map) obj).get(expressionTemp);
                }
                List list = new ArrayList();
                for(Object temp : ((List) obj)){
                    Object value = getMapValue(temp, arr[i+1]);
//                    Object value = ((Map) temp).get(arr[i+1]);
                    list.add(value);
                }
                obj = list;
                break;
            }else {
                obj = getMapValue(obj, expressionTemp);
//                obj = ((Map) obj).get(expressionTemp);
            }
        }
        return obj;
    }

    private static Object getMapValue(Object obj, String key) throws RunException {
        Object object;
        Map map = ((Map) obj);
        if (map.containsKey(key)){
            object = map.get(key);
        }else {
            throw new RunException("没有找到"+key+"字段。");
        }
        return object;
    }

    private static Object getListValue(Object obj, int index){
        Object object = null;
        List list = ((List) obj);
        if (list.size() > index){
            object = list.get(index);
        }
        return object;
    }
}
