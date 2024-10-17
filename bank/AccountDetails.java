package Banking_Service.bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

class AccountDetails extends UI {
    public AccountDetails(){
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
        }catch(Exception e){
            e.getMessage();
        }

        try{
            Scanner sc=new Scanner(System.in);
            Connection connection= DriverManager.getConnection(url,userName,password);
            Statement statement=connection.createStatement();
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


            //Check account number is valid ,show in our database
            query=String.format("SELECT acc_number FROM account_details WHERE acc_number='%s'",accNumber);
            resultSet=statement.executeQuery(query);
            while(!resultSet.next()) {
                System.out.println("Invalid Account Number !!");
                connection.close();
                return;
            }


            //Taking freeze feedback from account details
            query=String.format("SELECT freeze_info FROM account_details WHERE acc_number='%s'",accNumber);
            resultSet=statement.executeQuery(query);
            String freezeDtls=null;
            while(resultSet.next()) {
                freezeDtls = resultSet.getString("freeze_info");
                if (freezeDtls.equals("yes")) {
                    System.out.println("Sorry! your account is freeze");
                    System.out.println("Contact The bank to unfreeze account");
                    connection.close();
                    return;
                }
            }

            //Query for Account Details
            query = String.format("SELECT * FROM account_details WHERE acc_number='%s'", accNumber);
            resultSet = statement.executeQuery(query);

                //Show account Details if account number exists
                while (resultSet.next()) {
                    String fName = resultSet.getString("first_name");
                    String lName = resultSet.getString("last_name");
                    int bal = resultSet.getInt("acc_balance");
                    String number = resultSet.getString("mob_number");
                    String prnNumber = resultSet.getString("prn_number");
                    String acct_number = resultSet.getString("acc_number");
                    String date = resultSet.getString("Date_Time");

                    System.out.println();
                    System.out.println("*-----*  Account Details  *-----*\n");
                    System.out.println("Account Holder Name: " + fName + " " + lName);
                    System.out.println("Account Balance    : " + bal + "₹");
                    System.out.println("Mobile Number      : " + number);
                    System.out.println("PRN Number         : " + prnNumber);
                    System.out.println("Account Number     : " + acct_number);
                    System.out.println("IFSC CODE          : PRSH0009851");
                    System.out.println("Bank Name          : NITIYA Bank Of India");
                    System.out.println("Account Open Date  : " + date);
                }
                connection.close();
        }catch(Exception e){
            e.getMessage();
        }
    }
}
