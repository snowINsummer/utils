package qa.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

/**
 * Created by zhangli on 10/1/17.
 */
public class RuntimeUtil {

    public static String execute(String str) throws IOException {
        String restr = "";
        try {
            Process p = Runtime.getRuntime().exec(str);  //调用Linux的相关命令
//            String cmds[] = {"curl",str.replace("curl ","")};
//            Process p = Runtime.getRuntime().exec(cmds);
            InputStreamReader ir = new InputStreamReader(p.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);      //创建IO管道，准备输出命令执行后的显示内容
            String line;
            while ((line = input.readLine ()) != null){     //按行打印输出内容
                restr += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return restr;
    }

}
