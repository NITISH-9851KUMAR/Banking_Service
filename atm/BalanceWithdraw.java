package Banking_Service.atm;

import Banking_Service.passbook.Debit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class BalanceWithdraw extends UI {
    public  BalanceWithdraw(){
        java.util.Scanner sc=new java.util.Scanner(System.in);
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
        }catch(Exception e){
            e.getMessage();
        }

        try{
            Connection connection= DriverManager.getConnection(url,userName,password);
            Statement statement=connection.createStatement();
            String query;
            ResultSet resultSet;

            //Take input as Atm Number from user
            System.out.print("Enter ATM Number    : ");
            String atmNumber=sc.next();
            int atmNumberLength=atmNumber.length();

            //Checking ATM number is 16 digit or not
            while(atmNumberLength!=16){
                System.out.println("ATM Number should be 15 digit!!");
                System.out.print("Re-Enter ATM Number : ");
                atmNumber=sc.next();
                atmNumberLength=atmNumber.length();
            }
            //Checking ATM number exists in our database or not
            query=String.format("SELECT * FROM atm_details WHERE atm_number='%s'",atmNumber);
            resultSet=statement.executeQuery(query);
            //If atm Number is exist then resultSet has next and  'if' will execute ,either else execute
            String pin="0";
            String accNumber="0";

            if(!resultSet.next()) {
                System.out.println("Invalid ATM Number!!");
                connection.close();
                return;
            }
            else{ //if atm number is right then get m-pin number atm_details database
                pin=resultSet.getString("m_pin");
                //check first time if pin is not generate yet then show this message
                if(pin.equals("0")){
                    System.out.println("ATM M-PIN has been not created yet Make it form ATM Machine");
                    return;
                }
                accNumber=resultSet.getString("acc_number"); //use line number 63
            }

            //Checked Blocked Details of Atm
            query=String.format("SELECT block_info FROM atm_details WHERE atm_number='%s'",atmNumber);
            resultSet=statement.executeQuery(query);
            while(resultSet.next()){
                String freeze=resultSet.getString("block_info");
                if(freeze.equals("yes")){
                    System.out.println("Atm is Blocked!");
                    System.out.println("Contact With Bank!!");
                    connection.close();
                    return;
                }
            }


            //Take input M-PIN from user
            System.out.print("Enter M-PIN         : ");
            String mpin=sc.next();
            //check m-pin is four digit or not
            int mpinLength=mpin.length();
            while(mpinLength!=4){
                System.out.println("M-PIN should be 4 digit ");
                System.out.print("R-Enter M-PIN       : ");
                mpin=sc.next();
                mpinLength=mpin.length();
            }

            //check available m-pin and user m-pin is matched or not
            //If not matched print this message
            if(!mpin.equals(pin)){
                System.out.println("Invalid M-PIN ");
                System.out.println("Try again Good Luck!!");
                return;
            }
            //find the balance from the bank_details database
            query=String.format("SELECT acc_balance FROM account_details WHERE acc_number='%s'",accNumber);
            resultSet=statement.executeQuery(query);
            float avBalance=0.0f;
            while(resultSet.next()){
                //find balance form bank_details
                avBalance=resultSet.getFloat("acc_balance");

            }
            //Take input as a Balance from user
            System.out.print("Enter balance       : ");
            float bal=sc.nextFloat();
            //Check available balance is greater than withdraw balance
            if(avBalance>=bal){
                avBalance -= bal;
                query= String.format("UPDATE account_details SET acc_balance='%f' WHERE acc_number='%s' ", avBalance, accNumber);
                int rowAffect = statement.executeUpdate(query);
                if (rowAffect > 0) {
                    System.out.println("\nBalance is Successfully Withdraw");
                    System.out.println("Withdraw Balance: "+bal+"₹");
                    new Debit(accNumber,"ATM",bal,avBalance,0); //update Transaction Details
                }
            }

            //If Available balance is less than Withdraw balance show this message
            else{
                System.out.println("\nAvailable Balance is not sufficient !!");
                System.out.println("Transaction Status: Failure");
            }
        }catch(Exception e){}
    }

}
