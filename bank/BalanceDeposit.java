package Banking_Service.bank;

import Banking_Service.passbook.Credit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

class BalanceDeposit extends UI {
    Scanner sc = new Scanner(System.in);
    public BalanceDeposit(){
        try {
            try{
                Class.forName("oracle.jdbc.driver.OracleDriver");
            }catch(Exception e){}

            Connection connection = DriverManager.getConnection(url, userName, password);
            Statement statement = connection.createStatement();
            String query;
            ResultSet resultSet;

            //Take input Account Number form user
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
            query= String.format("SELECT acc_number FROM account_details WHERE acc_number='%s'", accNumber);
            resultSet= statement.executeQuery(query);
            //If account number is not valid acc_number close the connection and show this message and return this function
            while (!resultSet.next()) {
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

            //Take input Balance from user
            System.out.println("\nMinimum Balance Should be 1000₹ ");
            System.out.println("Else Fine charge imposed  2% of Deposit Balance ");
            System.out.print("Enter balance: ");
            float bal=sc.nextFloat();

            //Query for know available balance in account number
            query=String.format("SELECT acc_balance FROM account_details WHERE acc_number='%s'",accNumber);
            resultSet=statement.executeQuery(query);
            float avBalance=0.0f;
            while(resultSet.next()){
                //available balance
                avBalance=resultSet.getFloat("acc_balance");
            }

            //Check available balance is greater than withdraw balance
            if(bal>0){
                double fineCharge=0;
                //If balance is less than 1000 than calculate fine charge
                if( bal<1000 ){
                    fineCharge=(float)bal*0.02f;//five percentage of withdraw balance
                    double withBal=bal-fineCharge;
                    avBalance += (float) withBal;
                    System.out.println("\nDeposit Balance: "+withBal);
                    System.out.println("Fine Charge: "+fineCharge);
                }

                //Then withdraw balance successfully
                //If the Deposit balance is greater than 1000 then it will successfully Deposit
                if(bal>=1000){
                    avBalance += (float) bal;
                    System.out.println("Deposit Balance: "+bal);
                }

                //Query for deposit the balance in the respective bank account
                query= String.format("UPDATE account_details SET acc_balance='%f' WHERE acc_number='%s' ", avBalance, accNumber);
                int rowAffect = statement.executeUpdate(query);
                if (rowAffect > 0) {
                    System.out.println("Transaction Status: Successfully!");
                    //Credit class which add Credit Balance Transaction Table
                    new Credit(accNumber,"Bank",bal,avBalance,fineCharge);
                    connection.close();
                }
            }
            else{
                System.out.println("\nDeposit Balance is not sufficient !!");
                System.out.println("Transaction Status: Failure");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//Deposit balance is terminated there
}//Main class is terminated there
