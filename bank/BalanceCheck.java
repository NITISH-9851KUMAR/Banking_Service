package Banking_Service.bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

class BalanceCheck extends UI {
    public BalanceCheck() {
        {
            try{
                Class.forName("oracle.jdbc.driver.OracleDriver");
            }catch(Exception e){}
            try {
                Scanner sc = new Scanner(System.in);
                Connection connection = DriverManager.getConnection(url, userName, password);
                Statement statement = connection.createStatement();
                String query;
                ResultSet resultSet;

                //Take input Account Number form user
                System.out.print("Enter Account Number: ");
                String accNumber = sc.next();
                int accNumberLength=accNumber.length();
                //Account number should be 15 digits
                while(accNumberLength!=15){
                    System.out.println("Account Number Should be 15 digit!");
                    System.out.print("\nRe-Enter Account Number: ");
                    accNumber=sc.next();
                    accNumberLength=accNumber.length();
                }

                // Add a delay of 3 second
                System.out.print("Fetching Bank Balance");
                for(int i=0;i<4; i++){
                    try{
                        Thread.sleep(1000);
                    }catch(Exception e){
                        System.out.println(e.getMessage());
                    }

                    if(i==3) {
                        System.out.print("\n\n");
                        break;
                    }
                    else
                        System.out.print(".");
                }

                //Check account number is valid ,show in our database
                query=String.format("SELECT acc_number FROM account_details WHERE acc_number='%S'",accNumber);
                resultSet=statement.executeQuery(query);
                while(!resultSet.next()) {
                    System.out.println("Invalid Account Number !!");
                    connection.close();
                    return;
                }

                //Taking freeze feedback from account details
                query=String.format("SELECT freeze_info FROM account_details WHERE acc_number='%s'",accNumber);
                resultSet=statement.executeQuery(query);
                String freezeDtls;
                while(resultSet.next()){
                    freezeDtls=resultSet.getString("freeze_info");
                    if(freezeDtls.equals("yes")){
                        System.out.println("Sorry! your account is freeze");
                        System.out.println("Contact The bank to unfreeze account");
                        connection.close();
                        return;
                    }
                }

                //Query for check account balance from database
                query= String.format("SELECT acc_balance FROM account_details WHERE acc_number='%s'", accNumber);
                resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    System.out.printf("Account Balance     : %.2f₹\n", resultSet.getFloat("acc_balance"));
                    break;
                }
                connection.close();
            } catch (Exception e) {
                e.getMessage();
            }
        }
    }
}
