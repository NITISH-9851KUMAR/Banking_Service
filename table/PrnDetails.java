package Banking_Service.table;

import java.sql.*;
import Banking_Service.bank.UI;
public class PrnDetails extends UI{
    public static void main(String[] args) {
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
        }catch(Exception e){}

        try{
            Connection connection=DriverManager.getConnection(url,userName,password);
            Statement statement=connection.createStatement();

            String query= "CREATE TABLE prn_details("+
                    "sr_no NUMBER(3) PRIMARY KEY,"+
                    "prn VARCHAR(12) UNIQUE )";
            statement.executeUpdate(query);
            connection.close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
