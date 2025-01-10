package Banking_Service.table;

import Banking_Service.bank.UI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Atm extends UI {
    public static void main(String[] args) {
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
        }catch(Exception e){}

        try{
            Connection connection=DriverManager.getConnection(url,userName,password);
            Statement statement=connection.createStatement();
            String query="CREATE TABLE atm_details("+
                    "sr_no NUMBER(3) DEFAULT '17',"+
                    "acc_number VARCHAR(15),"+
                    "mob_number VARCHAR(10),"+
                    "atm_number VARCHAR(16),"+
                    "m_pin VARCHAR(4) DEFAULT'0000',"+
                    "block_info VARCHAR(3) DEFAULT 'no',"+
                    "date_time VARCHAR(20))";

            //account number is foreign key REFERENCES to account_details acc_number
            statement.executeUpdate(query);
        }catch(Exception e){
           e.printStackTrace();
        }
    }
}
