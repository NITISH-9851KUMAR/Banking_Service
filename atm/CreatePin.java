package Banking_Service.atm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class CreatePin extends UI {
    java.util.Scanner sc=new java.util.Scanner(System.in);
    public CreatePin(){

        try{
            Connection connection=DriverManager.getConnection(url,userName,password);
            Statement statement=connection.createStatement();
            String query;
            ResultSet resultSet;

            //Take input Atm Number from user
            System.out.print("Enter ATM Number         : ");
            String atmNumber=sc.next();
            int atmNumberLength=atmNumber.length();

            //Checking ATM number is 16 digit or not
            while(atmNumberLength!=16){
                System.out.println("Invalid ATM Number!!");
                System.out.print("Re-Enter ATM Number      : ");
                atmNumber=sc.next();
                atmNumberLength=atmNumber.length();
            }
            //Check ATM number from our database exists or not
            query=String.format("SELECT * FROM atm_details WHERE atm_number='%s'",atmNumber);
            resultSet=statement.executeQuery(query);
            //If atm Number is exist then resultSet has next if execute either else execute
            //check m-pin is already resultSet or not

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
            //If atm Number is exist then rset has next if execute either else execute
            //check m-pin is already created or not

            while(resultSet.next()) {
                String pin = resultSet.getString("m_pin");
                if (!pin.equals("0")) {
                    System.out.println("M-PIN is already Created!!");
                    connection.close();
                    return;
                }
            }

            //Take input Mobile Number From User
            System.out.print("Enter Mobile  Number     : ");
            String mobNumber = sc.next();
            int mobNumberLength = mobNumber.length();
            //Mobile number should be 10 digits
            while (mobNumberLength != 10) {
                System.out.println("Mobile Number Should be 10 digit!");
                System.out.print("\nRe-Enter Mobile  Number  :");
                System.out.print("Re-Enter new M-PIN       : ");

                mobNumber = sc.next();
                mobNumberLength = mobNumber.length();
            }

            //check mobile exists on given atm number
            query=String.format("SELECT mob_number FROM atm_details WHERE atm_number='%s'",atmNumber);
            resultSet=statement.executeQuery(query);
            while(resultSet.next()){
                //If user mobile number is not equal to given amt mobile number
                //show this message
                if(!resultSet.getString("mob_number").equals(mobNumber)) {
                    System.out.println("Invalid Mobile Number!!");
                    connection.close();
                    return;
                }
            }

            //Query for Create m_pin
            System.out.print("Enter 4 Digit New Pin    : ");
            String pin1=sc.next();
            //check m-pin is four digit or not
            int pinLength=pin1.length();
            while(pinLength!=4){
                System.out.println("M-PIN should be 4 digit ");
                System.out.print("Enter 4 Digit New Pin    : ");
                pin1=sc.next();
                pinLength=pin1.length();
            }
            System.out.print("Re-Enter new M-PIN       : ");
            String pin2=sc.next();
            int pin2Length=pin2.length();
            while(pin2Length!=4){
                System.out.print("Re-Enter new M-PIN       : ");
                 pin2=sc.next();
                 pin2Length=pin2.length();
            }

            if(pin1.equals(pin2)){

                // Add a delay of 3 second
                System.out.print("\nCreating M-Pin");
                for(int i=0;i<4; i++){
                    try{
                        Thread.sleep(1000);
                    }catch(Exception e){
                        System.out.println(e.getMessage());
                    }
                    if(i==3) {
                        System.out.print("\n");
                        break;
                    }
                    else
                        System.out.print(".");
                }

                //Query for Create New M-PIN
                query=String.format("UPDATE atm_details SET m_pin='%s' WHERE atm_number='%s'",pin1,atmNumber);
                int row=statement.executeUpdate(query);
                if(row>0){
                    System.out.println("M-PIN is created Successfully!!");
                    System.out.println("Thanks Q");
                }
            }
            else{
                System.out.println("User Press Pin is Not Matched!");
            }
        }catch(Exception e){
            e.getMessage();
        }
    }
}
