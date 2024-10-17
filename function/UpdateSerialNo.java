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

                //Update Serial Number of account_balance Table

                //Query count Serial Number

                query = String.format("SELECT count(sr_no) FROM %s",tableName);
                resultSet = statement.executeQuery(query);
                int countSrNumber = 0;
                if (resultSet.next()) {
                    countSrNumber = resultSet.getInt("count(sr_no)");
                }
                //Query For Store serial Number in array
                query =String.format("SELECT * FROM %s",tableName);
                resultSet = statement.executeQuery(query);
                int i = 0;
                int storeSrNumber[] = new int[countSrNumber];
                while (resultSet.next()) {
                    storeSrNumber[i] = resultSet.getInt("sr_no");
                    i++;
                }
                //Query for update Serial Number
                for (i = 1; i <= countSrNumber; i++) {
                    query = String.format("UPDATE %s SET sr_no=%d WHERE sr_no=%d",tableName, i, storeSrNumber[i - 1]);
                    statement.addBatch(query);
                }
                statement.executeBatch();
                connection.close(); //close connection
            } catch (Exception e) {
                e.getMessage();
            }
        }
}
