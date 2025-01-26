package Banking_Service.bank;

import Banking_Service.function.UpdateSerialNo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

class AtmDelete extends UI {
    Scanner sc=new Scanner(System.in);
    public AtmDelete(){
            try {
                Connection connection = DriverManager.getConnection(url, userName, password);
                Statement statement = connection.createStatement();
                String query;
                ResultSet resultSet;

                //Take input Account Number form user
                System.out.print("Enter Account Number: ");
                String accNumber = sc.next();
                int aLength=accNumber.length();
                //Account number should be 15 digits
                while(aLength!=15){
                    System.out.println("Account Number Should be 15 digit!");
                    System.out.print("Enter Account Number: ");
                    accNumber=sc.next();
                    aLength=accNumber.length();
                }

                //Check account number is valid ,show in our database
                query=String.format("SELECT acc_number FROM account_details WHERE acc_number='%S'",accNumber);
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
                //Take input Mobile Number From User
                System.out.print("Enter Mobile  Number: ");
                String mobNumber = sc.next();
                    int mobNumberLength = mobNumber.length();
                    //Mobile number should be 10 digits
                    while (mobNumberLength != 10) {
                        System.out.println("Mobile Number Should be 10 digit!");
                        System.out.print("Enter Mobile  Number: ");
                        mobNumber = sc.next();
                        mobNumberLength = mobNumber.length();
                    }

                    //check mobile exists on given account number
                    query = String.format("SELECT mob_number FROM account_details WHERE mob_number='%s'", mobNumber);
                    resultSet = statement.executeQuery(query);
                    while (!resultSet.next()) {
                        System.out.println("Invalid Mobile Number, Not found on this Associated Account Number:" + accNumber);
                        connection.close();
                        return;
                    }

                    //List Atm Number from database corresponding account number
                    query = String.format("SELECT atm_number FROM atm_details WHERE acc_number='%s'", accNumber);
                    resultSet= statement.executeQuery(query);
                    //if atm is not generated yet then resultSet has no next
                    String atmNumber;
                    if(resultSet.next()) {
                        atmNumber=resultSet.getString("atm_number");
                    }

                    else{
                        System.out.println("ATM is not generated yet!");
                        connection.close();
                        return;
                    }

                    //Query for Delete ATM Number
                    query=String.format("DELETE FROM atm_details WHERE atm_number='%s'",atmNumber);
                    int rowAffect=statement.executeUpdate(query);
                    if (rowAffect > 0) {
                        System.out.println("ATM Details is Deleted Successfully!");
                    }

                //Update serial Number in atm_details Table
                new UpdateSerialNo("atm_details"); //call for update serial Number
                connection.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}
