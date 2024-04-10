package AtmManagementSystem;
import java.sql.*;
import java.sql.Date;
import java.util.*;
public class AtmOperations {

    private Connection con;
    private Scanner sc;
   public AtmOperations(Connection con,Scanner sc)
    {
        this.con=con;
        this.sc=sc;
    }
    public void deposit(long account,long amount)
    {
         long currBalance=fetchBalance(con,account);
         long updatedBalance=currBalance+amount;
         updateBalance(con,account,updatedBalance);
         try
         {
             String query="INSERT INTO TransactionHistory (accno, transaction_type, amount,balance) VALUES (?, ?, ?, ?)";
             PreparedStatement ps=con.prepareStatement(query);
             ps.setLong(1,account);
             ps.setString(2,"Deposit");
             ps.setLong(3,amount);
             ps.setLong(4,updatedBalance);
             ps.executeUpdate();
         }
         catch(Exception e)
         {
             e.printStackTrace();
         }
        System.out.println("Deposited successfully....");
    }
    public static long fetchBalance(Connection con,long account)
    {
        try
        {
            String query="select balance from AccountDetails where accno=?";
            PreparedStatement ps=con.prepareStatement(query);
            ps.setLong(1,account);
            ResultSet rs=ps.executeQuery();
            if(rs.next())
            {
                return rs.getLong("balance");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return 0;
    }
    public static void updateBalance(Connection con,long account,long upBalance)
    {
        try
        {
            String query = "UPDATE AccountDetails SET balance = ? WHERE accno = ?";
            PreparedStatement ps=con.prepareStatement(query);
            ps.setLong(1,upBalance);
            ps.setLong(2,account);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void withdraw(long account, long am) {
       long currBalance=fetchBalance(con,account);
       if(currBalance>am)
       {
           long updatedBalance=currBalance-am;
           updateBalance(con,account,updatedBalance);
           try
           {
               String query="INSERT INTO TransactionHistory (accno, transaction_type, amount,balance) VALUES (?, ?, ?, ?)";
               PreparedStatement ps=con.prepareStatement(query);
               ps.setLong(1,account);
               ps.setString(2,"Withdraw");
               ps.setLong(3,am);
               ps.setLong(4,updatedBalance);
               ps.executeUpdate();
           }catch (Exception e)
           {
               e.printStackTrace();
           }
           System.out.println("Withdrawal amount of "+am+" is successfull....");
       }
       else {
           System.out.println("Insufficient Balance......");
       }

    }

    public void balanceEnquiry(long account) {
       try
       {
           String query="select * from AccountDetails where accno=?";
           PreparedStatement ps=con.prepareStatement(query);
           ps.setLong(1,account);
           ResultSet rs=ps.executeQuery();
           System.out.println("Balance Enquiry:");
           System.out.println("*-------------------*----------------*-----------------*");
           System.out.println("| Account Number    | Name           | Balance         |");
           System.out.println("*-------------------*----------------*-----------------*");
           while(rs.next())
           {
               long accno=rs.getLong("accno");
               String name=rs.getString("name");
               long balance=rs.getLong("balance");
               System.out.printf("| %-17s | %-14s | %-15s |\n",accno,name,balance);
               System.out.println("*-------------------*----------------*-----------------*");
           }
       }catch(Exception e)
       {
           e.printStackTrace();
       }
    }

    public void transactionHistory(long account) {
        try
        {
            String query="select * from TransactionHistory where accno=?";
            PreparedStatement ps=con.prepareStatement(query);
            ps.setLong(1,account);
            ResultSet rs=ps.executeQuery();
            System.out.println("Transaction History:");
            System.out.println("*-------------------*-------------------*------------------*--------------*---------------------------------*------------------------*");
            System.out.println("| Transaction Id    | Account No        | Transaction Type | Amount       | Transaction Time                | Balance                |");
            System.out.println("*-------------------*-------------------*------------------*--------------*---------------------------------*------------------------|");
            while(rs.next())
            {
                int transid=rs.getInt("transaction_id");
                long accno=rs.getLong("accno");
                String transtype=rs.getString("transaction_type");
                long balance=rs.getLong("amount");
                Timestamp timestamp = rs.getTimestamp("transaction_time");
                long amount=rs.getLong("balance");
                System.out.printf("| %-17s | %-17s | %-16s | %-12s | %-31s | %-22s |\n",transid,accno,transtype,balance,timestamp,amount);
                System.out.println("*-------------------*-------------------*------------------*--------------*---------------------------------*------------------------*");
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
