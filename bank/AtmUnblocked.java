package Banking_Service.bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class AtmUnblocked extends UI {
    public AtmUnblocked(){
        java.util.Scanner sc=new java.util.Scanner(System.in);
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
                if(freezeDtls.equals("yes")){
                    System.out.println("Account is Freeze!");
                    System.out.println("For unblocked Atm you need to un freeze account!");
                    System.out.println("Contact the Bank to Unfreeze Account!");
                    connection.close();
                    return;
                }
            }

            //Take input as Atm Number from user
            System.out.print("Enter ATM Number    : ");

            String atmNumber=sc.next();
            int uAtmLength=atmNumber.length();

            //Checking ATM number is 16 digit or not
            while(uAtmLength!=16){
                System.out.println("ATM Number should be 15 digit!!");
                System.out.print("Re-Enter ATM Number : ");
                atmNumber=sc.next();
                uAtmLength=atmNumber.length();
            }
            //Checking ATM number exists in our database or not
            query=String.format("SELECT * FROM atm_details WHERE atm_number='%s'",atmNumber);
            resultSet=statement.executeQuery(query);
            String pin=null; //use line number 96
            String blockInfo=null;
            if(!resultSet.next()){//If Query is Not execute then
                System.out.println("Invalid Atm Number!");
                connection.close();
                return;
            }

            else{ //if atm number is right then get m-pin number atm_details database
                pin=resultSet.getString("m_pin"); //user line number 58
                String accNumber1=resultSet.getString("acc_number");
                blockInfo=resultSet.getString("block_info");
                if(pin.equals("0")){
                    System.out.println("Pin is Not Created Yet!");
                    connection.close();
                    return;
                }
                if(!accNumber.equals(accNumber1)){
                    System.out.println("Input Account Number and exist account Number on given Atm  Number is Not Matched!");
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

            //check Atm is already blocked or not
            if(blockInfo.equals("no")){
                System.out.println("Atm Is already Un Blocked!");
            }
            else{
                query=String.format("UPDATE atm_details SET block_info='%s' WHERE atm_number='%s'","no",atmNumber);
                statement.executeUpdate(query);
                System.out.println("Atm is Successfully Un Blocked!");
            }
            connection.close();
        }catch(Exception e){}
    }
}
