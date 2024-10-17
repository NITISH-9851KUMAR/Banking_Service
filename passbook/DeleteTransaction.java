package Banking_Service.passbook;

import Banking_Service.bank.UI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DeleteTransaction extends UI {
    public DeleteTransaction(String acc_number) {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        }catch(Exception e) {}

        try {
            Connection connection = DriverManager.getConnection(url, userName, password);
            Statement statement = connection.createStatement();

            String query;
            ResultSet resultSet;
            query=String.format("SELECT * FROM account_details WHERE acc_number='%s'",acc_number);
            resultSet=statement.executeQuery(query);

            String prn="",name="";
            while (resultSet.next()) {
                prn=resultSet.getString("prn_number");
                name=resultSet.getString("first_name");
            }

            StringBuilder lastFiveDigitPrn=new StringBuilder();
            for(int i=7;i<12;i++){//Store last five digit of prn number in fiveDigitPrn
                lastFiveDigitPrn.append(prn.charAt(i));
            }
            String tableName=name+lastFiveDigitPrn;


            //Query for delete Transaction Table
            query=String.format("DROP TABLE %s",tableName);
            statement.executeUpdate(query);
            connection.close();

        } catch (Exception e) {
        }
    }
}
