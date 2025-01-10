package Banking_Service.table;

import Banking_Service.bank.UI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Account extends UI {
    public static void main(String[] args) {
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
        }catch(Exception e){}

        try{
            Connection connection=DriverManager.getConnection(url,userName,password);
            Statement statement=connection.createStatement();
            String query="CREATE TABLE account_details("+
                    "sr_no NUMBER(3),"+
                    "first_name VARCHAR(20),"+
                    "last_name VARCHAR(20),"+
                    "acc_balance FLOAT(10),"+
                    "mob_number VARCHAR(10),"+
                    "prn_number VARCHAR(12) UNIQUE,"+
                    "acc_number VARCHAR(15) UNIQUE,"+
                    "ifsc_code VARCHAR(11) DEFAULT 'PRSH0009851',"+
                    "bank_name VARCHAR(12) DEFAULT 'NITIYA BANK',"+
                    "freeze_details VARCHAR(3) DEFAULT 'no',"+
                    "date_time VARCHAR(20))";

            statement.executeUpdate(query);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
