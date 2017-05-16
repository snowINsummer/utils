package qa.exception;

/**
 * Created by snow.zhang on 2015/9/7.
 */
public class DBException extends Exception {

    public DBException(Exception e){
        super(e);
    }

    public DBException(String message){
        super(message);
    }

    public DBException(int errorCode, String message){
        super(errorCode+":"+message);
    }
}
