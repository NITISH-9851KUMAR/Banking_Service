package Banking_Service.bank;

import Banking_Service.function.UpdateSerialNo;
import Banking_Service.passbook.DeleteTransaction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

class AccountDelete extends UI {
    public AccountDelete(){
        Scanner sc=new Scanner(System.in);

        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
        }catch(Exception e){}

        try{
            Connection connection=DriverManager.getConnection(url,userName,password);
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


            new DeleteTransaction(accNumber);//Delete Transaction Table it is in function table
            //Delete from atm_details table respective account number atm details
            query=String.format("DELETE FROM atm_details WHERE acc_number='%s'",accNumber);
            statement.executeUpdate(query);

            //Delete account Number
            query = String.format("DELETE FROM account_details WHERE acc_number='%s'", accNumber);
            int rowAffect = statement.executeUpdate(query);

            if (rowAffect > 0)
            {
                    System.out.println("Account is Successfully Deleted!");
                    System.out.println("Account Number :" + accNumber);
            }
            else
            {
                System.out.println("Denied Account Delete!");
            }

            //Update Serial Number of account_balance Table
            new UpdateSerialNo("account_details"); //call for update serial Number
            connection.close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
