package qa.utils;

import qa.exception.DBException;

import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by zhangli on 25/4/2017.
 */
public class DataBaseUtil {

    private Connection conn = null;
    private Statement statement = null;
    private ResultSet resultSet = null;
    private String dbUrl = "";
    private String dbDriver = "";
    private String user = "";
    private String password = "";
    private String dbConnectInfo = "";
    private LinkedList<Map<String, String>> sqlData = null;
    private String sqlDataToJSON = "";

    public DataBaseUtil(String dbUrl, String user, String password) throws DBException {
        this.dbUrl = dbUrl;
        this.user = user;
        this.password = password;
        setDbDriver();
    }

    /**
     * 执行select
     * 读取可用中文，若读取的数据库内容需要修改，如显示中存在中文字符时采用
     *
     * @param sql
     * @return LinkedList<HashMap<String, String>>
     * @throws DBException
     */
    public void executeQueryOracle(String sql) throws DBException{
        LinkedList<Map<String, String>> sqlData = new LinkedList<>();
        try {
            String sql1 = new String(sql.getBytes("GBK"), "iso-8859-1");
            Statement sta = conn.createStatement();
            ResultSet resultSet = sta.executeQuery(sql1);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            if (columnCount != 0) {
                Map<String, String> colData;
                while (resultSet.next()) {
                    colData = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        String cValue = resultSet.getString(i);
                        if (!StringUtil.isEmpty(cValue)) {
                            colData.put(metaData.getColumnLabel(i), new String(resultSet.getString(i).getBytes("iso-8859-1"), "GBK"));
                        } else {
                            colData.put(metaData.getColumnLabel(i), cValue);
                        }
                    }
                    sqlData.add(colData);
                }
            }
        } catch (SQLException e) {
            throw new DBException(e.getErrorCode(), e.getMessage() + "\r\n SQL :" + sql);
        } catch (Exception e) {
            throw new DBException(e);
        }

        setSqlData(sqlData);
//        setSqlDataToJSON(new Gson().toJson(sqlData));
    }

    public void conn() throws DBException {
        try {
            if (null == conn) {
                if (dbUrl.isEmpty() || user.isEmpty() || password.isEmpty())
                    throw new DBException("数据库URL或用户名或密码 为空。" + getDbConnectInfo());
                Class.forName(dbDriver);
                conn = DriverManager.getConnection(dbUrl, user, password);
            }
        } catch (ClassNotFoundException e) {
            throw new DBException(e);
        } catch (SQLException e) {
            throw new DBException("创建数据库连接失败：" + e.getMessage());
        }

    }

    public void disConn() {
        if (null != conn){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void setDbDriver() throws DBException {
        if (dbDriver.isEmpty()){
            if (dbUrl.isEmpty()) throw new DBException("dbUrl=" + dbUrl);
            if (dbUrl.contains("mysql")){
                setDbDriver("com.mysql.jdbc.Driver");
            }else if(dbUrl.contains("oracle")){
                setDbDriver("oracle.jdbc.driver.OracleDriver");
            }else if(dbUrl.contains("sqlserver")){
                setDbDriver("com.microsoft.jdbc.sqlserver.SQLServerDriver");
            }
        }
    }

    private void setDbConnectInfo() {
        this.dbConnectInfo = "DBURL="+dbUrl + ",USER="+user+",PASSWORD="+password+";";
    }

    private void setDbDriver(String dbDriver) {
        this.dbDriver = dbDriver;
    }

    public String getDbConnectInfo() {
        return this.dbConnectInfo;
    }

    public void setSqlData(LinkedList<Map<String,String>> sqlData) {
        this.sqlData = sqlData;
    }

    public void setSqlDataToJSON(String sqlDataToJSON) {
        this.sqlDataToJSON = sqlDataToJSON;
    }

    public LinkedList<Map<String, String>> getSqlData() {
        return sqlData;
    }

    public Map<String,String> getFirstData(){
        return sqlData.getFirst();
    }

    public Map<String,String> getSqlData(int index){
        return sqlData.get(index);
    }

    public String getSqlDataToJSON() {
        return sqlDataToJSON;
    }

/*
    public static void main(String args[]) throws DBException, UnsupportedEncodingException {
        DataBaseUtil dataBaseUtil = new DataBaseUtil("jdbc:oracle:thin:@192.168.38.207:1521:ORADEV","xxd_v6_test","xxd_v6_test");
        dataBaseUtil.conn();
        dataBaseUtil.executeQueryOracle("select * from RECEIPT_SZR_APPLICATIONFORM");
        LinkedList<Map<String, String>> data = dataBaseUtil.getSqlData();
    }
    */
}
