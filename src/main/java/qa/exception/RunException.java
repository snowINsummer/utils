package qa.exception;

/**
 * Created by snow.zhang on 2015-11-02.
 */
public class RunException extends Exception {
    public RunException(){
        super();
    }
    public RunException(Exception e){
        super(e);
    }
    public RunException(String str){
        super(str);
    }
}
