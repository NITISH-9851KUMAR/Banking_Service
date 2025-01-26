package Banking_Service.table;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import Banking_Service.bank.UI;
public class PrnValues extends UI {
    public static void main(String[] args) {

        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }

        try{
            Connection connection=DriverManager.getConnection(url,userName,password);
            Statement statement=connection.createStatement();

            String query;

            String prn1="2202051310";
            for(int i=1;i<100;i++){
                String prn="";
                if(i<10){
                    prn=prn1+"0"+i;
                }
                else prn=prn1+i;
                query=String.format("INSERT INTO prn_details VALUES(%d,%s)",i,prn);
                statement.addBatch(query);
            }
            statement.executeBatch();
            connection.close();

        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
