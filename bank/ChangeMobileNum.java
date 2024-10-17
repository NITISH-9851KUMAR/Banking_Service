package Banking_Service.bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

class ChangeMobileNum extends UI {
    public ChangeMobileNum(){
        try {
            Scanner sc = new Scanner(System.in);
            Connection connection = DriverManager.getConnection(url, userName, password);
            Statement statement = connection.createStatement();
            String query;
            ResultSet resultSet;

            //Take input Account Number form user
            System.out.print("Enter Account Number   : ");
            String accNumber = sc.next();
            int accNumberLength = accNumber.length();
            //Account number should be 15 digits
            while (accNumberLength != 15) {
                System.out.println("Account Number Should be 15 digit!");
                System.out.print("\nRe-Enter Account Number: ");
                accNumber = sc.next();
                accNumberLength = accNumber.length();
            }

            //Check account number is valid ,show in our database
            query = String.format("SELECT acc_number FROM account_details WHERE acc_number='%S'", accNumber);
            resultSet= statement.executeQuery(query);
            //If account number is not valid show this message and close the connection and return this function
            while (!resultSet.next()) {
                System.out.println("Invalid Account Number !!");
                connection.close();
                return;
            }

            //Taking freeze feedback from account details
            query=String.format("SELECT freeze_info FROM account_details WHERE acc_number='%s'",accNumber);
            resultSet=statement.executeQuery(query);
            String freezeDtls=null;
            while(resultSet.next()){
                freezeDtls=resultSet.getString("freeze_info");
                if(freezeDtls.equals("yes")){
                    System.out.println("Sorry! your account is freeze");
                    System.out.println("Contact The bank to unfreeze account");
                    connection.close();
                    return;
                }
            }

            //Take old input Mobile Number From User
            System.out.print("Enter Old Mobile Number: ");
            String oldMobNumber=sc.next();
            int oldMobNumberLength = oldMobNumber.length();
            //Mobile number should be 10 digits
            while (oldMobNumberLength != 10) {
                System.out.println("Mobile Number Should be 10 digit!");
                System.out.print("\nRe-Enter Mobile Number: ");
                oldMobNumber = sc.next();
                oldMobNumberLength = oldMobNumber.length();
            }
            //check mobile exists on given account number or not
            query=String.format("SELECT mob_number FROM account_details WHERE mob_number='%s'",oldMobNumber);
            resultSet=statement.executeQuery(query);
            while(!resultSet.next()){
                System.out.println("Invalid Mobile Number");
                connection.close();
                return;
            }

            //Take input new Number from user
            System.out.print("Enter New Mobile Number: ");
            String newMobNumber=sc.next();
            int newMobNumberLength = newMobNumber.length();
            //Mobile number should be 10 digits
            if (newMobNumberLength != 10) {
                System.out.println("Mobile Number Should be 10 digit!");
                System.out.print("\nRe-Enter Mobile Number: ");
                newMobNumber = sc.next();
                newMobNumberLength = newMobNumber.length();
            }

            //if new number is equal to old number then we don't need to update mobile number
            if(oldMobNumber.equals(newMobNumber)){
                System.out.println("Please Enter New Number");
                System.out.println("This Mobile Number is already added!");
                connection.close();
                return;
            }


            //Query for update Mobile Number
            query=String.format("UPDATE account_details SET mob_number='%s' WHERE acc_number='%s'",newMobNumber,accNumber);
            int rowAffect=statement.executeUpdate(query);
            //If query is execute successfully show message to the user
            if(rowAffect>0){
                System.out.println("Mobile Number Successfully Update!! ");
            }
            else System.out.println("Mobile Number Not Changed!!");
            connection.close();
        } catch (Exception e) {
            e.getMessage();
        }
    }
}
