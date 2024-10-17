package Banking_Service.bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class AtmBlocked extends UI {
        public AtmBlocked() {
            java.util.Scanner sc=new java.util.Scanner(System.in);

            try {
                Connection connection = DriverManager.getConnection(url, userName, password);
                Statement statement=connection.createStatement();
                String query;
                ResultSet resultSet;


                //Take Account Number as input from user
                System.out.print("Enter Account Number: ");
                String accNumber = sc.next();
                int accNumberLength=accNumber.length();
                //Account number should be 15 digits
                while(accNumberLength!=15){
                    System.out.println("Account Number Should be 15 digit!");
                    System.out.print("Enter Account Number: ");
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

                    //Take input as Atm Number from user
                    System.out.print("Enter ATM Number    : ");

                    String atmNumber = sc.next();
                    int atmNumberLength = atmNumber.length();

                    //Checking ATM number is 16 digit or not
                    while (atmNumberLength != 16) {
                        System.out.println("ATM Number should be 15 digit!!");
                        System.out.print("Re-Enter ATM Number : ");
                        atmNumber = sc.next();
                        atmNumberLength = atmNumber.length();
                    }
                    //Checking ATM number exists in our database or not
                    query = String.format("SELECT * FROM atm_details WHERE atm_number='%s'", atmNumber);
                    resultSet = statement.executeQuery(query);
                    String pin = null; //use line number 96
                    String blockDtls = null;
                    if (!resultSet.next()) {//If Query is Not execute then
                        System.out.println("Invalid Atm Number ");
                        connection.close();
                        return;
                    }
                    //If query is execute
                    else { //if atm number is right then get m-pin number from atm_details table
                        pin = resultSet.getString("m_pin"); //user line number 58
                        String acc1_number = resultSet.getString("acc_number");
                        blockDtls = resultSet.getString("block_info");
                        if(pin.equals("0")){
                            System.out.println("Pin is Not Created Yet!");
                            connection.close();
                            return;
                        }

                        if (blockDtls.equals("yes")) {
                            System.out.println("Atm Is already Blocked!");
                            connection.close();
                            return;
                        }

                        if (!accNumber.equals(acc1_number)) {
                            System.out.println("Input Account Number and exist account Number on given Atm  Number is Not Matched!");
                            connection.close();
                            return;
                        }
                    }

                    //Take input M-PIN from user
                    System.out.print("Enter M-PIN         : ");
                    String mpin = sc.next();
                    //check m-pin is four digit or not
                    int mpin_length = mpin.length();
                    while (mpin_length != 4) {
                        System.out.println("M-PIN should be 4 digit ");
                        System.out.print("R-Enter M-PIN       : ");
                        mpin = sc.next();
                        mpin_length = mpin.length();
                    }
                    //check available m-pin and user m-pin is matched or not
                    //If not matched print this message
                    if (!mpin.equals(pin)) {
                        System.out.println("Invalid M-PIN ");
                        System.out.println("Try again Good Luck!!");
                        return;
                    }
                    //Starting Freeze from there
                    //check Atm is already freeze or not
                    else {
                        query = String.format("UPDATE atm_details SET block_info='%s' WHERE atm_number='%s'", "yes", atmNumber);
                        statement.executeUpdate(query);
                        System.out.println("Atm is Successfully Blocked!");
                    }
                    connection.close();
            }catch(Exception e){}
        }
}
