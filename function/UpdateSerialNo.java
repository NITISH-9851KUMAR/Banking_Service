package Banking_Service.function;

import Banking_Service.bank.UI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
public class UpdateSerialNo extends UI {
        public UpdateSerialNo(String tableName) {
            try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
            } catch (Exception e) {
                e.getMessage();
            }

            try {
                Connection connection = DriverManager.getConnection(url, userName, password);
                Statement statement = connection.createStatement();
                String query;
                ResultSet resultSet;

                //Query count Serial Number
                query = String.format("SELECT count(sr_no) FROM %s",tableName);
                resultSet = statement.executeQuery(query);
                int countSrNumber = 0; //store how many serial number in account_details
                if (resultSet.next()) {
                    countSrNumber = resultSet.getInt("count(sr_no)");
                }

                //Store prn number in prn array;
                String accNumArr[]=new String[countSrNumber];
                query = String.format("SELECT * FROM %s",tableName);
                resultSet = statement.executeQuery(query);
                int i=0;
                while(resultSet.next()) {
                    accNumArr[i] = resultSet.getString("acc_number");
                    i++;
                }

                //Query for update Serial Number
                for (i = 0; i <countSrNumber; i++) {
                    query=String.format("UPDATE %s SET sr_no=%d WHERE acc_number='%s'",tableName,(i+1),accNumArr[i]);
                    statement.addBatch(query);
                }
                statement.executeBatch();
                connection.close(); //close connection

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
}
