package Banking_Service.bank;

import Banking_Service.passbook.PrintTransaction;

import java.util.Scanner;

public class UI {
    protected static final String url="jdbc:oracle:thin:@localhost:1521:xe";
    protected static final String userName="system";
    protected static final String password="nitish";

    public static void main(String[] args) {

        Scanner sc=new Scanner(System.in);
        byte choice;
        do {
            System.out.println("\n      *----* Menu *----*");
            System.out.println("      1.Open New Account");
            System.out.println("      2.Account Details!!");
            System.out.println("      3.Check Bank Balance ");
            System.out.println("      4.Withdraw Balance ");
            System.out.println("      5.Deposit Balance ");
            System.out.println("      6.Change Mobile Number ");
            System.out.println("      7.ATM Apply");
            System.out.println("      8.ATM Details");
            System.out.println("      9.Delete Account");
            System.out.println("      10.Freeze Account");
            System.out.println("      11.Unfreeze Account ");
            System.out.println("      12.Delete ATM");
            System.out.println("      13.Blocked ATM");
            System.out.println("      14.UnBlocked ATM");
            System.out.println("      15.Transaction Details");
            System.out.println("      17.Exit\n");
            System.out.print("        Enter Choice: ");
            choice = sc.nextByte();
            switch (choice) {

                case 1: //create new account
                    new AccountOpen();
                    break;

                case 2: //Check account details
                    new AccountDetails();
                    break;

                case 3: //Check Balance
                    new BalanceCheck();
                    break;

                case 4://Withdraw Balance
                    new BalanceWithdraw();
                    break;

                case 5: //Deposit Balance
                    new BalanceDeposit();
                    break;

                case 6: //Change Mobile Number
                    new ChangeMobileNum();
                    break;

                case 7: //Generate ATM
                    new AtmGenerate();
                    break;

                case 8: //show atm Details
                    new AtmDetails();
                    break;

                case 9:  //Delete Account
                    new AccountDelete();
                    break;

                case 10: //Freeze Account
                    new AccountFreeze();
                    break;

                case 11:  //Remove From unfreeze
                    new AccountUnfreeze();
                    break;

                case 12: //Delete Existing ATM
                    new AtmDelete();
                    break;

                case 13: //Block ATM
                    new AtmBlocked();
                    break;

                case 14: //Remove from Unblocked
                    new AtmUnblocked();
                    break;

                case 15: //Print Account Holder Banking Transaction Details
                    new PrintTransaction();
                    break;
            }
        } while (choice != 17);
    }
}
