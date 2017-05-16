package qa.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangli on 19/4/2017.
 */
public class ClassUtil {

    public static Map<Integer,String> getClassFieldsName(String className){
        Map<Integer,String> classFields = new HashMap<>();
        int fieldsIndex = 0;
        try {
            Class clazz = Class.forName(className);//根据类名获得其对应的Class对象 写上你想要的类名就是了 注意是全名 如果有包的话要加上 比如java.Lang.String
            Field[] fields = clazz.getDeclaredFields();//根据Class对象获得属性 私有的也可以获得
            for(Field f : fields) {
                classFields.put(fieldsIndex, f.getName());
                fieldsIndex++;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return classFields;
    }

    public static Integer getMapKeyFromValue(Map<Integer, String> map, String value){
        Integer key = null;
        for(Map.Entry entry : map.entrySet()){
            if (entry.getValue().toString().equals(value)){
                key = (Integer) entry.getKey();
            }
        }
        return key;
    }
}
