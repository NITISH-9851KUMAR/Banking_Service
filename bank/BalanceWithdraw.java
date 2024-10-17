package Banking_Service.bank;

import Banking_Service.passbook.Debit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class BalanceWithdraw extends UI {
    public BalanceWithdraw(){
        try {
            Scanner sc = new Scanner(System.in);
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
            query= String.format("SELECT acc_number FROM account_details WHERE acc_number='%S'", accNumber);
            resultSet=statement.executeQuery(query);
            //if account number is not found in our database show this message and close connection return this function
            while (!resultSet.next()) {
                System.out.println("Invalid Account Number!!");
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

            //Take input as a Balance form user
            System.out.println("\nMinimum Balance Should be 1000₹ ");
            System.out.println("Else Fine charge imposed 5% of Withdraw Balance ");
            System.out.print("Enter balance: ");
            float bal=sc.nextFloat();

            //Query for know available balance in account number
            query=String.format("SELECT acc_balance FROM account_details WHERE acc_number='%s'",accNumber);
            resultSet=statement.executeQuery(query);
            float avBalance=0.0f;
            while(resultSet.next()){
                avBalance=resultSet.getFloat("acc_balance");
            }

            //Check available balance is greater than withdraw balance
            if(avBalance>=bal){
                avBalance -=bal;
                query= String.format("UPDATE account_details SET acc_balance='%f' WHERE acc_number='%s' ", avBalance, accNumber);
                int rowAffect = statement.executeUpdate(query);
                if (rowAffect > 0) {
                    System.out.println("\nBalance is Successfully Withdraw ");
                }
                //If balance is less than 1000 than calculate fine charge
                double fineCharge=0;
                if( bal<1000 ){
                    fineCharge=(float)bal*0.05f;//five percentage of withdraw balance
                    double withBal=bal-fineCharge;
                    System.out.printf("Withdraw Balance : %.2f\n",withBal);
                    System.out.printf("Fined Charge     : %.2f\n",fineCharge);
                }
                //If balance is greater than 1000
                //Then withdraw balance successfully
                if(bal>=1000){
                    System.out.println("Withdraw Balance: "+bal);
                }
                System.out.printf("Available Balance: %.2f\n",avBalance);
                System.out.println("Transaction Status: Successfully");
                new Debit(accNumber,"Bank",bal,avBalance,fineCharge);
                connection.close();
            }

            //If Available balance is less than Withdraw balance
            else{
                System.out.println("\nAvailable Balance is not sufficient !!");
                System.out.println("Transaction Status: Failure");
            }
            connection.close();
        }catch (Exception e) {
            e.getMessage();
        }
    }//withdraw function is terminated there
}//Main class is terminate there

