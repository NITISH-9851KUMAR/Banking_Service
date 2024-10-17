package Banking_Service.passbook;

import Banking_Service.bank.UI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class PrintTransaction extends UI {
    Scanner sc=new Scanner(System.in);
    public PrintTransaction(){
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
        }catch(Exception e){}

        try{

            Connection connection= DriverManager.getConnection(url,userName,password);
            Statement statement=connection.createStatement();
            String query;
            ResultSet resultSet;

            //Take input account Number from the user
            System.out.print("Enter Account Number: ");
            String accNumber=sc.next();
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

            //Taking freeze feedback from account_details table
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

            //Account Holder Transaction Table Name
            query=String.format("SELECT * FROM account_details WHERE acc_number='%s'",accNumber);
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
            //Name of Transaction Table


            query=String.format("SELECT * FROM %s",tableName); //Transaction table
            resultSet=statement.executeQuery(query);
            int sr_no=0;
            String date="",details="";
            double debit=0,credit=0,balance=0,fineCharge=0;


            //Print Transaction Table
            System.out.println("\n                  *----*  Transaction Details *----*\n");
            System.out.println("Sr_No    Date       Time          Details     Debit    Credit    Balance    Fine Charge");
            while(resultSet.next()){
                sr_no=resultSet.getInt("sr_no");
                date=resultSet.getString("date_time");
                details=resultSet.getString("details");
                debit=resultSet.getDouble("debit");
                credit=resultSet.getDouble("credit");
                balance=resultSet.getDouble("balance");
                fineCharge=resultSet.getDouble("fine_charge");
                if(details.equals("ATM")){
                     System.out.printf("%s        %s      %s         %.2f     %.2f    %.2f     %.2f",sr_no,date,details,debit,credit,balance,fineCharge);
                }
                else System.out.printf("%s        %s      %s        %.2f     %.2f      %.2f     %.2f",sr_no,date,details,debit,credit,balance,fineCharge);
                System.out.println();
            }
            System.out.println("\n\n");
            connection.close();
        }catch(Exception e){
            e.getMessage();
        }
    }
}
