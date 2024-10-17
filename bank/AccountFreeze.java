package Banking_Service.bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

class AccountFreeze extends UI {

    public AccountFreeze() {
        Scanner sc = new Scanner(System.in);
        try {
            Connection connection = DriverManager.getConnection(url, userName, password);
            Statement statement = connection.createStatement();
            String query=null;
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
            query= String.format("SELECT acc_number FROM account_details WHERE acc_number='%S'", accNumber);
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
            //if freezeDtls=no then account is not freeze
            //if freezeDtls=yes then account is freeze we don't need ot freeze account

            if(freezeDtls.equals("yes")){
                System.out.println("Account is Already Freeze!");
                System.out.println("Contact the Bank to Unfreeze Account!");
            }
            else{//for freeze account
                query=String.format("UPDATE account_details SET freeze_info='%s' WHERE acc_number='%s'","yes",accNumber);
                int rowAffect=statement.executeUpdate(query);
                if(rowAffect>0){
                    System.out.println("Account is successfully Freeze!");
                }
            }
            connection.close();
        } catch (Exception e) {}
    }
}