package Banking_Service.atm;

public class UI {

    protected static final String url="jdbc:oracle:thin:@localhost:1521:XE";
    protected static final String userName="system";
    protected static final String password="nitish";

    public static void main(String[] args) {
        java.util.Scanner sc=new java.util.Scanner(System.in);
        int choice;
        do{
            System.out.println("\n*----*  Menu  *----*");
            System.out.println("1.Withdraw Balance");
            System.out.println("2.Check Balance");
            System.out.println("3.Create New M-PIN");
            System.out.println("4.Change M-PIN");
            System.out.println("17.Exit");
            System.out.print("\nEnter you choice: ");
            choice=sc.nextInt();

            switch(choice){

                case 1:
                    new BalanceWithdraw();
                break;

                case 2:
                    new BalanceCheck();
                break;

                case 3:
                    new CreatePin();
                break;

                case 4:
                    new ChangePin();
                break;
            }
        }while(choice!=17);

    }//Main function is terminated there
}//Main Class is terminated there
