package AtmManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class AtmManagement {
    private static final String url="jdbc:mysql://localhost:3306/atm";
    private static final String username="root";
    private static final String password="12345678";

    public static boolean checkAccount(Connection con, long acc)
    {
        try {
            String query="select * from AccountDetails where accno = ?";
            PreparedStatement ps=con.prepareStatement(query);
            ps.setLong(1,acc);
            ResultSet rs=ps.executeQuery();
            while(rs.next())
            {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public static void response(Scanner sc, CreateAccount rsp, AtmOperations atmop,long account) {
        while (true) {
            System.out.println("Welcome to our ATM");
            System.out.println("If you're new to ATM system Press 1 in the choice to create ATM pin");
            System.out.println("otherwise Press 2 if exit means Press any key");
            int choice = sc.nextInt();
            if (choice == 1) {
                rsp.RegisterPin(account);
            } else if(choice==2){
             System.out.println("Enter your ATM pin:");
                int pin = sc.nextInt();
                if (rsp.checkPin(pin,account)) {
                    System.out.println("1. Deposit");
                    System.out.println("2. Withdraw");
                    System.out.println("3. Balance Enquiry");
                    System.out.println("4. Transaction History");
                    System.out.println("5. Exit");
                    System.out.println("Enter your choice:");
                    int ch=sc.nextInt();
                    switch (ch) {
                        case 1:
                            System.out.println("Amount you want to deposit");
                            long amount=sc.nextLong();
                            atmop.deposit(account,amount);
                            break;
                        case 2:
                            System.out.println("Amount you want to withdraw");
                            long am=sc.nextLong();
                            atmop.withdraw(account,am);
                            break;
                        case 3:
                            atmop.balanceEnquiry(account);
                            break;
                        case 4:
                            atmop.transactionHistory(account);
                            break;
                        case 5:
                            System.exit(1);
                            break;
                        default:
                            System.out.println("Invalid choice");
                    }
                } else {
                    System.out.println("Invalid pin, Try again!!!!!");
                }
            }
            else {
                return;
            }
        }
    }

            public static void main(String[] args) {

                Scanner sc=new Scanner(System.in);
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection con = DriverManager.getConnection(url, username, password);
                    CreateAccount rsp = new CreateAccount(con, sc);
                    AtmOperations atmop=new AtmOperations(con,sc);
                    while (true) {
                        System.out.println("-> Press-1 if you need new account");
                        System.out.println("-> Otherwise press any key to continue your ATM operation");
                        int ch=sc.nextInt();
                        if(ch==1)
                        {
                            rsp.accountDetails();
                        }
                        System.out.println("Enter Account No:");
                        long acno = sc.nextLong();
                        if (checkAccount(con, acno)) {
                           response(sc,rsp,atmop,acno);
                        }
                        else {
                            rsp.accountDetails();
                        }
                    }
                }
        catch(Exception e)
            {
                e.printStackTrace();
            }

        }
    }

