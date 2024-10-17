package Banking_Service.bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

class AccountUnfreeze extends UI {
    public AccountUnfreeze(){
        Scanner sc=new Scanner(System.in);
        try{
            Class.forName("com.mysql.jc.jdbc.Driver");
        }catch(Exception e){}

        try{
            Connection connection=DriverManager.getConnection(url,userName,password);
            Statement statement=connection.createStatement();
            String query;
            ResultSet resultSet;

            //Take Account Number as input from user
            System.out.print("Enter Account Number: ");
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
            resultSet = statement.executeQuery(query);
            while (!resultSet.next()) {
                System.out.println("Invalid Account Number !!");
                connection.close();
                return;
            }

            //Taking freeze feedback from account details
            query=String.format("SELECT freeze_info FROM account_details WHERE acc_number='%s'",accNumber);
            resultSet=statement.executeQuery(query);
            String freezeDtls=null;
            if(resultSet.next()){
                freezeDtls=resultSet.getString("freeze_info");
            }
            //if freezeDls=no account unfreeze we don't need to unfreeze
            if(freezeDtls.equals("no")){
                System.out.println("Account is already Unfreeze");
            }
            //if freezeDls=yes we need to unfreeze account
            else{
                //Query for Remove freeze from account
                query=String.format("UPDATE account_details SET freeze_info='%s' WHERE acc_number='%s'","no",accNumber);
                int rowAffect=statement.executeUpdate(query);
                if(rowAffect>0){
                    System.out.println("Account is successfully Unfreeze!");
                }
            }
        }catch(Exception e){}
    }
}
