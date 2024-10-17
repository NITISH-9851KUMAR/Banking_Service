package Banking_Service.bank;

import Banking_Service.function.AccNumber;
import Banking_Service.function.Date;
import Banking_Service.function.UpdateSerialNo;
import Banking_Service.passbook.CreateTransaction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class AccountOpen extends UI {
    public AccountOpen(){
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
        }catch(Exception e){
            e.getMessage();
        }
        try{
            Connection connection=DriverManager.getConnection(url,userName,password);
            Statement statement=connection.createStatement();
            Scanner sc=new Scanner(System.in);
            String query;
            ResultSet resultSet;

            //First Name
            System.out.print("Enter First Name      : ");
            String fName=sc.next();
            //Last Name
            System.out.print("Enter Last Name       : ");
            String lName=sc.next();
            //Balance for credit in Bank
            System.out.print("Enter Balance         : ");
            int bal=sc.nextInt();
            //Mobile Number
            System.out.print("Enter Mobile Number   : ");
            String mobNumber=sc.next();
            int mobNumberLength=mobNumber.length();
            while(mobNumberLength!=10){
                System.out.println("Please Enter Valid Mobile Number \n");
                System.out.print("Re-Enter Mobile Number: ");
                mobNumber=sc.next();
                mobNumberLength=mobNumber.length();
            }
            //Prn Number , check prn number is 12 digit valid
            System.out.print("Enter PRN Number      : ");
            String prnNumber=sc.next();
            int prnLength=prnNumber.length();
            while(prnLength!=12){ //check length is equal to 12
                System.out.println("Please Enter Valid PRN Number \n");
                System.out.print("Re-Enter Aadhaar Number: ");
                prnNumber=sc.next();
                prnLength=prnNumber.length();
            }

            //Check PRN Number through database is valid or not
            query=String.format("SELECT prn FROM prn_details WHERE prn='%s'",prnNumber);
            resultSet=statement.executeQuery(query);
            while(!resultSet.next()){
                System.out.println(" *---* Message for User *---*");
                System.out.println("PRN Number does not exist");
                System.out.println("User Another PRN Number!");
                return;
            }


            //Query for check prn number is already exist
            query="SELECT prn_number FROM account_details";
            resultSet=statement.executeQuery(query);
            while(resultSet.next()){
                String prn=resultSet.getString("prn_number");
                if(prnNumber.equals(prn)){
                    System.out.println(" *---* Message for User *---*");
                    System.out.println("\nPRN Number is already exists in our Bank!");
                    System.out.println("Please try again using Another PRN Number!");
                    System.out.println("Good Luck");
                    return;
                }
            }

            //Account Number get From AccNumber class which is defined in function Package
            String accNumber= AccNumber.accNumber();
            //check account number is already exists if exist re-generate account number
            query="SELECT acc_number FROM account_details";
            resultSet=statement.executeQuery(query);
            while(resultSet.next()){
                String ac=resultSet.getString("acc_number");
                if(accNumber.equals(ac)) {
                    accNumber= AccNumber.accNumber();
                }
                else break;
            }

            String date= Date.date(); //Get the date through Date class which is defined in function package

            //Query for creating new account , it will save data in database
            query=String.format("INSERT INTO account_details(first_name,last_name,acc_balance,mob_number,prn_number,acc_number,date_time) " +
                    "VALUES('%s','%s',%d,'%s','%s','%s','%s')",fName,lName,bal,mobNumber,prnNumber,accNumber,date);
            int rowAffect=statement.executeUpdate(query);
            //If query is successfully execute then show account details
            if(rowAffect>0){
                System.out.println();
                System.out.println("Account Opened Successfully!!");
                System.out.println("*-----*  Account Details  *-----*\n");
                System.out.println("Account Holder Name: "+fName +" "+lName);
                System.out.println("Account Balance    : "+bal);
                System.out.println("Mobile Number      : "+mobNumber);
                System.out.println("PRN Number         : "+prnNumber);
                System.out.println("Account Number     : "+accNumber);
                System.out.println("IFSC CODE          : PRSH0009851");
                System.out.println("Bank Name          : NITIYA BANK");
                System.out.println("Date and Time      : "+date);
            }
            else System.out.println("Account is not Open Successfully!!");

            //Create a table of Transaction Details
            new CreateTransaction(prnNumber,fName,bal,date,"BANK");

            //Update serial Number in account_details Table
            new UpdateSerialNo("account_details"); //call for update serial Number

        }catch(Exception e){
            e.getMessage();
        }
    }
}
