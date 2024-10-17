package Banking_Service.bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

class AtmDetails extends UI {
    java.util.Scanner sc = new java.util.Scanner(System.in);

    //function that add space in atm number after four digits
    public StringBuilder Print(String atmNumber){
        StringBuilder str=new StringBuilder();
        System.out.print("ATM Number             : ");
        for(int i=0;i<=15;i++) { //print first 4 digits number and print space
            if(i==4 || i==8 ||i==12) str.append(" ");
            str.append(atmNumber.charAt(i));
        }
        return str;
    }

    public AtmDetails(){
        try {
            Connection connection = DriverManager.getConnection(url,userName, password);
            Statement statement = connection.createStatement();
            String query=null;
            ResultSet resultSet;

            //Take input Account Number form user
            System.out.print("Enter Account Number   : ");
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

            //Taking freeze feedback from account_details Table
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
                System.out.print("Enter Mobile  Number   : ");
                String mobNumber = sc.next();
                int mobNumberLength = mobNumber.length();
                //Mobile number should be 10 digits
                while (mobNumberLength != 10) {
                    System.out.println("Mobile Number Should be 10 digit!");
                    System.out.print("\nRe-Enter Mobile  Number: ");
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
               query= String.format("SELECT * FROM atm_details WHERE acc_number='%s'", accNumber);
                resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    String atmNumber = (resultSet.getString("atm_number"));
                    int pin = (resultSet.getInt("m_pin"));

                    StringBuilder atmNumberWithSpace = Print(atmNumber); //Function add space after four digits in atm number
                    System.out.println(atmNumberWithSpace);

                    if (pin == 0) {
                        System.out.println("ATM M-PIN              : 0 ,PIN has been not created yet ,Make it form ATM Machine");
                    } else System.out.println("ATM M-PIN              : " + pin);
                    String date = resultSet.getString("Date_Time");
                    System.out.println("ATM Apply Date         : " + date);

                    connection.close();
                    return;
                }

                //if atm is not generated yet then resultSet has no next
                while (!resultSet.next()) {
                    System.out.println("ATM is not generated yet!!");
                    break;
                }
                connection.close();
        } catch (Exception e) {
            e.getMessage();
        }
    }

}