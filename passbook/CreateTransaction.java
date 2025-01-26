package Banking_Service.passbook;

import Banking_Service.bank.UI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class CreateTransaction extends UI {
    public CreateTransaction(String prn,String name,double bal,String date,String details){
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
        }catch(Exception e){
            e.getMessage();
        }

        try{
            Connection connection=DriverManager.getConnection(url,userName,password);
            Statement statement=connection.createStatement();

            //create table of passbook Details
            //Table Name =name and last five digit of prn number

            //last five digit of prn number
            StringBuilder lastFiveDigitPrn=new StringBuilder();
            for(int i=7;i<12;i++){
                lastFiveDigitPrn.append(prn.charAt(i));
            }
            //Table Name =name and last five digit of prn number
            String tableName=name+lastFiveDigitPrn;//Final Table name Create Update passbook
            String query;
            //Query for create table
            query=String.format("CREATE TABLE %s(" +
                    "sr_no NUMBER(3) DEFAULT 1," +
                    "date_time VARCHAR(20),"+
                    "details VARCHAR(4),"+
                    "debit FLOAT(10) DEFAULT 0.0,"+
                    "credit FLOAT(10) DEFAULT 0.0,"+
                    "balance FLOAT(10),"+
                    "fine_charge FLOAT(10) DEFAULT 0.0 )",tableName);
            statement.executeUpdate(query);

            query=String.format("INSERT INTO %s(date_time,details,balance) VALUES('%s','%s',%f)",tableName,date,details,bal);
            statement.executeUpdate(query);
            connection.close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

