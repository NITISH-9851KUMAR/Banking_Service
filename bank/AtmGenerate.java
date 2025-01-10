package Banking_Service.bank;

import Banking_Service.function.AtmNumber;
import Banking_Service.function.Date;
import Banking_Service.function.UpdateSerialNo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class AtmGenerate extends UI {
    java.util.Scanner sc=new java.util.Scanner(System.in);
    public AtmGenerate(){
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
            query=String.format("SELECT acc_number FROM account_details WHERE acc_number='%S'",accNumber);
            resultSet=statement.executeQuery(query);
            while(!resultSet.next()) {
                System.out.println("Invalid Account Number !!");
                connection.close();
                return;
            }

            //Taking freeze feedback from account detailsaccNumber
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

                //Check atm is already generate
                query = String.format("SELECT  atm_number from atm_details WHERE acc_number='%s'", accNumber);
                resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    System.out.println("Atm is generate Already!!");
                    connection.close();
                    return;
                }

                //Take input Mobile Number From User
                System.out.print("Enter Mobile  Number: ");
                String mobileNumber = sc.next();
                int mobileNumberLength = mobileNumber.length();
                //Mobile number should be 10 digits
                while (mobileNumberLength != 10) {
                    System.out.println("Mobile Number Should be 10 digit!");
                    System.out.print("\nRe-Enter Mobile Number: ");
                    mobileNumber = sc.next();
                    mobileNumberLength = mobileNumber.length();
                }

                //check mobile exists on given account number
                query = String.format("SELECT mob_number FROM account_details WHERE acc_number='%s'", accNumber);
                resultSet = statement.executeQuery(query);
                if (resultSet.next()) {
                    //we already check account number valid or not
                    //if mobile number is exist on the given account number then generate atm number
                    if (resultSet.getString("mob_number").equals(mobileNumber)) {

                        // Add a delay of 3 second
                        System.out.print("Applying for ATM card");
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

                        //Take value of date through function
                        String date = Date.date();

                        //Take value of atm Number through function
                        String atmNumber= AtmNumber.atmNumber();
                        int pin = 0;
                        query = String.format("INSERT INTO atm_details(acc_number,atm_number,mob_number,m_pin,date_time) VALUES('%s','%s','%s',%d    ,'%s')", accNumber, atmNumber, mobileNumber, pin, date);
                        int rowAff = statement.executeUpdate(query);
                        if (rowAff > 0) {
                            System.out.println("Congratulations! Your ATM card application has been successfully Applied.!!");
                            System.out.println("Thank you for banking with Nitiya Bank. ");
                            //Update serial Number in function Table
                            new UpdateSerialNo("atm_details"); //call for update serial Number
                        }
                    }
                    //if mobile is number is not exist then show this message on console
                    else System.out.println("Invalid Mobile number!!");
                }
                connection.close();
        }catch(Exception e){
            e.getMessage();
        }
    }
}
