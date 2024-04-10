package AtmManagementSystem;

import java.math.BigInteger;
import java.sql.*;
import java.util.Random;
import java.util.Scanner;

public class CreateAccount {

    private Connection con;
    private Scanner sc;
    public CreateAccount(Connection con, Scanner sc) {
        this.con=con;
        this.sc=sc;
    }
    public void accountDetails() {
        System.out.println("-------CreateAccount-------");
        System.out.println("Enter your name");
        String name=sc.next();
        System.out.println("Enter your mobile number");
        long phno=sc.nextLong();
        System.out.println("Automatically generate your account number..........");
        long accno=generateAccountNo();
        System.out.println("your account number is: "+accno);
        System.out.println("Deposit minimum rupees-100");
        long balance=sc.nextLong();
        try
        {
            String query="insert into AccountDetails(accno,name,phno,balance) values(?,?,?,?)";
            PreparedStatement ps=con.prepareStatement(query);
            ps.setLong(1,accno);
            ps.setString(2,name);
            ps.setLong(3,phno);
            ps.setLong(4,balance);
            int affectedRows=ps.executeUpdate();
            if(affectedRows>0)
            {
                System.out.println("Your account is created successfully.....!!!!!");
            }
            else {
                System.out.println("Something went wrong....");
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static long generateAccountNo()
    {
        Random random = new Random();
        long base = (long) Math.pow(10, 15); // 10 to the power of 15
        long random9Digit = (long) (Math.random() * base);
        long random16DigitNumber = base + random9Digit;
        return random16DigitNumber;
    }
    public boolean checkPin(int pin,long accno)
    {
        try {
            String query="select * from AccountDetails where atmpin = ? && accno=?";
            PreparedStatement ps=con.prepareStatement(query);
            ps.setInt(1,pin);
            ps.setLong(2,accno);
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

    public void RegisterPin(long account) {
        System.out.println("Enter 4 digit pin");
        int pin=sc.nextInt();
        try
        {
            String query="UPDATE AccountDetails SET atmpin = ? WHERE accno = ?";
            PreparedStatement ps=con.prepareStatement(query);
            ps.setInt(1,pin);
            ps.setLong(2,account);
            int affectedRows=ps.executeUpdate();
            if(affectedRows>0)
            {
                System.out.println("Atm pin set successfully.....!!");
            }
            else {
                System.out.println("Error");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
