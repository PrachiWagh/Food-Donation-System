
import User.UserList;

import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.sql.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    public static void main(String[] args)throws IOException {
        //Server is listening on port 4000
        ServerSocket ss = new ServerSocket(4000);
        UserList current_list = new UserList();

        ExecutorService executor= Executors.newFixedThreadPool(10);

        //running infinite loop for getting client request
        while (true) {
            Socket s = null;
            try {
                //socket object to receive incoming client requests
                s = ss.accept();
                System.out.println("A new client is connected:" + s);
                //obtaining input and output streams
                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                System.out.println("Assigning new thread for this client");
                //create a new thread object

                Thread t = new ClientHandler(s, dis, dos, current_list);
                //invoking start method
                executor.execute(t);
                //t.join();

            } catch (Exception e) {
                s.close();
                e.printStackTrace();
            }

        }

    }
}
//Client Handler Class
class ClientHandler extends Thread{
    UserList current_list;
    String current_user;
    Scanner sc=new Scanner(System.in);
    final DataInputStream in;
    final DataOutputStream out;
    final Socket s;
    int choice=1;


    // Constructor
    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos,UserList l)
    {
        this.s = s;
        this.in = dis;
        this.out = dos;
        this.current_list=l;

    }
    @Override
    public void run() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sdl_a3?autoReconnect=true&useSSL=false","root","prachi@123");
            Statement stmt = con.createStatement();
            String sql;


            while (true) {
                try {
                    do {
                       // out.writeUTF("Please Login/Sign up to continue!");
                        do {
                          //  out.writeUTF("Enter \n1.Login \n2.Sign Up :");
                            choice = in.read();
                            switch (choice) {
                                case 1:
                                    //out.writeUTF("Enter username:");
                                    String username = in.readUTF();
                                   // out.writeUTF("Enter password:");
                                    String password = in.readUTF();
                                    sql = current_list.LoginUser(username, password);
                                    ResultSet rs = stmt.executeQuery(sql);
                                    if (rs.next()) {
                                        choice = 0;
                                        current_user = username;
                                        out.writeUTF("Login successful!");
                                    } else {
                                        choice = 1;
                                        out.writeUTF("Sorry! no such user found!");
                                    }
                                    out.writeByte(choice);
                                    break;
                                case 2:
                                   // out.writeUTF("Enter username:");
                                    username = in.readUTF();
                                    sql="select * from users where username='"+username+"';";
                                    ResultSet rs1=stmt.executeQuery(sql);
                                    if(rs1.next()){
                                        out.writeByte(1);
                                        out.writeUTF("Username already exists!");
                                        break;
                                    }
                                    else {
                                        out.writeByte(0);
                                       // out.writeUTF("Enter password:");
                                        password = in.readUTF();
                                       // out.writeUTF("Enter your role from the following list:");
                                        //out.(r.print_role_options());
                                        String role_input = in.readUTF();
                                        sql = current_list.addUserAccount(username, password, role_input);
                                        stmt.executeUpdate(sql);
                                        out.writeUTF("Thank you for signing up! Please Login to continue.");
                                        break;
                                    }
                            }
                        } while (choice != 0);
                        choice = 1;
                        if (current_user.equals("admin")) {
                            out.writeUTF("admin");

                            out.writeUTF("D you want to see logs?(0/1)");
                            int c1 = in.readByte();
                            String str1="";
                            do {
                                if (c1 == 1) {
                                    out.writeUTF("Please enter the date in (yyyy-mm-dd) format:");
                                    String date = in.readUTF();
                                    String sql1 = "select *from requests join receipts on requests.req_id=receipts.rec_id join food_info on requests.food_id=food_info.id join logs on requests.req_id=logs.req_id where Donation_date='" + date + "';";
                                    ResultSet r1 = stmt.executeQuery(sql1);
                                    while (r1.next()) {
                                        str1=str1+"\n"+r1.getInt("req_id") + " " + r1.getString("sender") + " " + r1.getString("receiver") + " " + r1.getString("details");
                                    }
                                    out.writeUTF(str1);
                                }
                                str1="";
                                out.writeUTF("Do you want to see more logs?(0/1)");
                                c1 = in.readByte();

                            } while (c1 != 0);
                            break;


                        } else {
                            do {
                                out.writeUTF(current_user);
                                out.writeUTF("Welcome!" + current_user);
                                //out.writeUTF("Enter \n1.Donate \n2.Receive \n3.GetReceipt:");
                                choice = in.read();
                                switch (choice) {
                                    case 1:
                                        out.writeUTF("Please enter your message:");
                                        String message1 = in.readUTF();
                                        out.writeUTF("Please enter details of food:");
                                        String details = in.readUTF();
                                        out.writeUTF("Please enter Hours to perish:");
                                        Integer a = in.read();
                                        out.writeUTF("Please enter location:");
                                        String loc = in.readUTF();
                                        sql = "insert into food_info values(null,'" + details + "'," + a + ");";
                                        stmt.executeUpdate(sql);
                                        sql = "select last_insert_id();";
                                        ResultSet rs1 = stmt.executeQuery(sql);
                                        rs1.next();
                                        int food_id = rs1.getInt(1);
                                        sql = "insert into requests values(null,'" + message1 + "','" + current_user + "','" + loc + "'," + food_id + ",'incomplete');";
                                        stmt.executeUpdate(sql);
                                        out.writeUTF("Thanks for making a donation!");
                                        System.out.println(current_user + " made a donation!");
                                        choice = 0;
                                        break;
                                    case 2:
                                        synchronized (this) {
                                            out.writeUTF("Please enter receiving location:");
                                            String rec_loc = in.readUTF();
                                            out.writeUTF("Thanks! Here are the details of food you'll receive!");
                                            sql = "select req_id,message,sender,s_location,details,hours_to_perish from requests join food_info on requests.food_id=food_info.id where status='incomplete' order by(hours_to_perish) limit 1;";
                                            rs1 = stmt.executeQuery(sql);
                                            if (rs1.next()) {
                                                out.writeUTF("\nMessage: " + rs1.getString("message") + "\nSender: " + rs1.getString("sender") + "\nLocation: " + rs1.getString("s_location") + "\ndetails: " + rs1.getString("details") + "\nHoursToPerish: " + rs1.getInt("hours_to_perish"));
                                                int req_id = rs1.getInt("req_id");
                                                sql = "insert into receipts values(" + req_id + ",'" + current_user + "','" + rec_loc + "');";
                                                stmt.executeUpdate(sql);
                                                sql = "update requests set status='complete' where req_id=" + req_id + ";";
                                                stmt.executeUpdate(sql);
                                                sql = "insert into logs values(" + req_id + ",curdate());";
                                                stmt.executeUpdate(sql);
                                            } else {
                                                out.writeUTF("Sorry no requests found!");
                                            }
                                            choice = 0;
                                        }
                                        break;
                                    case 3:
                                        sql = "select *from requests join receipts on requests.req_id=receipts.rec_id join food_info on requests.food_id=food_info.id where sender='" + current_user + "' or receiver='" + current_user + "';";
                                        rs1 = stmt.executeQuery(sql);
                                        String str = "";
                                        while (rs1.next()) {

                                            str = str + "\nMessage: " + rs1.getString("message") + "\nSender: " + rs1.getString("sender") + "\nLocation: " + rs1.getString("s_location") + "\ndetails: " + rs1.getString("details") + "\nHoursToPerish: " + rs1.getInt("hours_to_perish") + "\nReceiver: " + rs1.getString("receiver") + "\nRec_location: " + rs1.getString("r_location") + "\n\n";

                                        }
                                        if (str == "") {
                                            str = "Sorry no receipts found!";
                                        }
                                        out.writeUTF(str);
                                        choice = 0;
                                }

                            } while (choice != 0);
                            out.writeUTF("Enter 1 to exit,0 to homepage:");
                            choice = in.read();
                        }
                    }
                    while (choice != 1) ;




                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }

            try {
                // closing resources
                this.in.close();
                this.out.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }catch (Exception e){

            System.out.println(e);
        }
    }



}