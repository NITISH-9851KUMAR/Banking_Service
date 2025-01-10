package Banking_Service.atm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class BalanceCheck extends UI {
    public BalanceCheck(){
        java.util.Scanner sc=new java.util.Scanner(System.in);
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
        }catch(Exception e){}
        try{
            Connection connection=DriverManager.getConnection(url,userName,password);
            Statement statement=connection.createStatement();
            String query;
            ResultSet resultSet;

            //Take input Atm Number from user
            System.out.print("Enter ATM Number   : ");
            String atmNumber=sc.next();
            int lengthAtmNumber=atmNumber.length();

            //Checking ATM number is 16 digit or not
            while(lengthAtmNumber!=16){
                System.out.println("ATM Number should be 15 digit!!");
                System.out.print("Re-Enter ATM Number: ");
                atmNumber=sc.next();
                lengthAtmNumber=atmNumber.length();
            }

            //Check ATM number from our database exists or not
            query=String.format("SELECT * FROM atm_details WHERE atm_number='%s'",atmNumber);
            resultSet=statement.executeQuery(query);
            if(!resultSet.next()){
                System.out.println("Invalid ATM Number!!");
                connection.close();
                return;
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


            //Check ATM number from our database exists or not
            query=String.format("SELECT * FROM atm_details WHERE atm_number='%s'",atmNumber);
            resultSet=statement.executeQuery(query);
            //If atm Number is exist then resultSet has next if execute either else execute
            String pin="0";
            String accNumber="0";
            if(resultSet.next()) {
                //if atm number is right then get m-pin from atm_details database
                pin=resultSet.getString("m_pin"); //user line number 58

                //check first time if pin is not generate yet the show this message
                if(pin.equals("0")){
                    System.out.println("ATM M-PIN has been not created yet Make it from ATM Machine");
                    return;
                }
                accNumber=resultSet.getString("acc_number");
            }

            //Take input old M-PIN from user
            System.out.print("Enter M-PIN        : ");
            String mpin=sc.next();
            //check m-pin is four digit or not
            int mpinLength=mpin.length();
            while(mpinLength!=4){
                System.out.println("M-PIN should be 4 digit ");
                System.out.print("Re-Enter M-PIN     : ");
                mpin=sc.next();
                mpinLength=mpin.length();
            }
            if(!mpin.equals(pin)){ //if pin is not match then print this message
                System.out.println("Invalid M-PIN ");
                System.out.println("Try again Good Luck!!");
                return;
            }

            //find the balance from the bank_details database
            String query1=String.format("SELECT acc_balance FROM account_details WHERE acc_number='%s'",accNumber);
            resultSet=statement.executeQuery(query1);
            while(resultSet.next()){

                //add  delay of 3 second
                System.out.print("Fetching Bank Balance");
                for(int i=0;i<4; i++){
                    try{
                        Thread.sleep(1000);
                    }catch(Exception e){
                        System.out.println(e.getMessage());
                    }
                    if(i==3) {
                        System.out.print("\n\n");
                        break;
                    }
                    else
                        System.out.print(".");
                }

                double balance=resultSet.getDouble("acc_balance");
                System.out.println("Available Balance  : "+balance+"₹");
            }
            connection.close();
        }catch(Exception e){}
    }
}
