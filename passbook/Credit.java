package Banking_Service.passbook;

import Banking_Service.bank.UI;
import Banking_Service.function.Date;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Credit extends UI {
    public Credit(String acc_number, String details, double creditBal, double totalBal, double fineCharge){
        try{
            Class.forName("com.mysql.jc.jdbc.Driver");
        }catch(Exception e) {
            e.getMessage();
        };

        try{
            Connection connection=DriverManager.getConnection(url,userName,password);
            Statement statement=connection.createStatement();
            String query;
            ResultSet resultSet;

            String prn="";
            String name="";
            query=String.format("SELECT * FROM account_details WHERE acc_number='%s'",acc_number);
            resultSet=statement.executeQuery(query);
            while(resultSet.next()){
                prn=resultSet.getString("prn_number");
                name=resultSet.getString("first_name");
            }
            //show the name and prn of  Account Holder

            //Account Holder Transaction Table
            StringBuilder lastFiveDigitPrn=new StringBuilder();
            for(int i=7;i<12;i++){//Store last five digit of prn number in fiveDigitPrn
                lastFiveDigitPrn.append(prn.charAt(i));
            }
            String tableName=name+lastFiveDigitPrn;

            String date= Date.date();
            //Query for insert Credit balance in Transaction Table
            query=String.format("INSERT INTO %s(date_time,details,credit,balance,fine_charge) VALUES('%s','%s',%f,%f,%f)",tableName,date,details,creditBal,totalBal,fineCharge);
            statement.executeUpdate(query);
            connection.close();
        }catch(Exception e){e.getMessage();}
    }
}
