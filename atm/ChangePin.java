package Banking_Service.atm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ChangePin extends UI {
    public ChangePin(){
        java.util.Scanner sc=new java.util.Scanner(System.in);
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
            if(!resultSet.next()) {
                System.out.println("Invalid ATM Number!!");
                connection.close();
                return;
            }

            //Checked Blocked Details of Atm
            query=String.format("SELECT * FROM atm_details WHERE atm_number='%s'",atmNumber);
            resultSet=statement.executeQuery(query);
            String pin=null;
            while(resultSet.next()){
                String freeze=resultSet.getString("block_info");
                pin=resultSet.getString("m_pin");
                if(freeze.equals("yes")){
                    System.out.println("Atm is Blocked!");
                    System.out.println("Contact With Bank!!");
                    connection.close();
                    return;
                }
                if(pin.equals("0")){
                    System.out.println("ATM M-PIN has been not created yet Make it from ATM Machine");
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
                System.out.print("\nRe-Enter Mobile  Number  : ");
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
            //Take input old M-PIN from user
            System.out.print("Enter OLD M-PIN          : ");
            String oldPin=sc.next();
            if(oldPin.equals(pin)){//if old pin is match then change m pin
                System.out.print("Enter new M-PIN          : ");
                String mpin1=sc.next();
                System.out.print("Re-Enter new M-PIN       : ");
                String mpin2=sc.next();
                while(!mpin1.equals(mpin2)){ //if new m-pin and re-enter new m-pin is not match
                    System.out.println("New M-PIN is Not match");
                    System.out.print("Enter new M-PIN          : ");
                    mpin1=sc.next();
                    System.out.print("Re-Enter new M-PIN       : ");
                    mpin2=sc.next();
                }
                //Query for change m-pin
                query=String.format("UPDATE atm_details SET m_pin='%s' WHERE atm_number='%s'",mpin1,atmNumber);
                int rowAffected=statement.executeUpdate(query);
                if(rowAffected>0){

                    // Add a delay of 3 second
                    System.out.print("\nChanging M-Pin");
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
                        else System.out.print(".");
                    }

                    System.out.println("M-PIN Change Successfully!");
                    System.out.println("New M-PIN                : "+mpin1);
                    connection.close();
                }
            }
            else{ //if old pin not match then print this message
                System.out.println("OLD M-PIN is Not Matched!!");
                System.out.println("Try Again!!");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
