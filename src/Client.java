import Role.RoleTypes;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        String serveraddress = "127.0.0.1";
        int port = 4000;
        Scanner sc = new Scanner(System.in);
        int choice = 1;
        Socket client = new Socket(serveraddress, port);
        System.out.println("Connected to " + client.getRemoteSocketAddress());
        DataOutputStream out = new DataOutputStream(client.getOutputStream());
        DataInputStream in = new DataInputStream(client.getInputStream());
        do {
            //System.out.println(in.readUTF());
            do {
                //System.out.println(in.readUTF());
                MainWindow1 mw=new MainWindow1();
                while(mw.isVisible());
                choice = mw.choice;
                //System.out.println(choice);
                out.writeByte(choice);
                switch (choice) {
                    case 1:
                       // System.out.println(in.readUTF());
                        LoginPage pg=new LoginPage();
                        while(pg.isVisible()){
                            System.out.print("");
                        }
                        String username = pg.username;
                        out.writeUTF(username);
                        //System.out.println(in.readUTF());
                        String password = pg.password;
                        out.writeUTF(password);
                        String msg=in.readUTF();
                        new MessageBox(msg);
                        choice = in.read();
                        break;
                    case 2:
                        //System.out.println(in.readUTF());
                        SignUp su=new SignUp();
                        while(su.isVisible()){
                            System.out.print("");
                        }
                        username = su.username;
                        out.writeUTF(username);
                        if(in.read()==1){
                            new MessageBox(in.readUTF());
                            break;
                        }
                        //System.out.println(in.readUTF());
                        password = su.password;
                        out.writeUTF(password);
                        //System.out.println(in.readUTF());
                       // RoleTypes r = new RoleTypes();
                      //  r.print_roleTypes();
                        String role_input = su.role;
                        out.writeUTF(role_input);
                        new MessageBox(in.readUTF());
                        break;
                }
            } while (choice != 0);
            choice=1;
            String str=in.readUTF();
            int c1;
            if(str.equals("admin")){

                Scanner sc1 = new Scanner(System.in);
                System.out.println(in.readUTF());
                c1=sc1.nextInt();
                out.writeByte(c1);
                do{    System.out.println(in.readUTF());
                    String date = sc1.next();
                    out.flush();
                    out.writeUTF(date);
                    System.out.println(in.readUTF());
                    System.out.println(in.readUTF());
                    //out.writeUTF(sc1.next());
                    //System.out.println(in.readUTF());
                    c1=sc1.nextInt();
                    out.writeByte(c1);
                }while(c1!=0);
            }
            do{
               //System.out.println(in.readUTF());
                MainMenu mm=new MainMenu(in.readUTF());
                //System.out.println(in.readUTF());
                while(mm.isVisible()){
                    System.out.print("");
                }
                choice=mm.choice;
                out.writeByte(choice);
                switch(choice){
                    case 1:
                        System.out.println(in.readUTF());
                        sc.nextLine();
                        String message=sc.nextLine();
                        out.writeUTF(message);
                        System.out.println(in.readUTF());
                        String details = sc.nextLine();
                        out.writeUTF(details);
                        System.out.println(in.readUTF());
                        Integer a = sc.nextInt();
                        out.writeByte(a);
                        System.out.println(in.readUTF());
                        sc.nextLine();
                        String loc = sc.nextLine();
                        out.writeUTF(loc);
                        System.out.println(in.readUTF());
                        choice=0;
                        break;
                    case 2:
                        System.out.println(in.readUTF());
                        sc.nextLine();
                        String rec_loc = sc.nextLine();
                        out.writeUTF(rec_loc);
                        System.out.println(in.readUTF());
                        System.out.println(in.readUTF());
                        choice=0;
                        break;
                    case 3:
                        System.out.println(in.readUTF());
                        choice=0;
                }


            }while(choice!=0);
            System.out.println(in.readUTF());
            choice=sc.nextInt();
            out.writeByte(choice);
        } while (choice != 1);
        client.close();
    }
}
class MainWindow1 extends JFrame implements ActionListener{
    JLabel l1;
    JButton login,signup;
    public int choice;
    public MainWindow1(){
        setLayout(new FlowLayout());
        l1=new JLabel("Please Login or Sign Up to continue!");
        login=new JButton("LOGIN");
        signup=new JButton("SIGN UP");
        add(l1);
        add(login);
        add(signup);
        login.addActionListener(this);
        signup.addActionListener(this);
        setVisible(true);
        setSize(250,150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public void actionPerformed(ActionEvent ae){
        if(ae.getSource()==login){
            choice=1;
            dispose();
        }
        else if(ae.getSource()==signup){
            choice=2;
            dispose();
        }
        choice=0;
    }

}
class LoginPage extends JFrame implements ActionListener{
    JLabel username1,password1;
    JButton login;
    JTextField u,p;
    public String username,password;
    public LoginPage(){
        setLayout(new FlowLayout());
        username1=new JLabel("username");
        u=new JTextField();
        u.setColumns(20);

        password1=new JLabel("password");
        p=new JTextField();
        p.setColumns(20);

        login=new JButton("LOGIN");
        add(username1);
        add(u);
        add(password1);
        add(p);
        add(login);

        login.addActionListener(this);
        setVisible(true);
        setSize(300,150);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
public void actionPerformed(ActionEvent ae){
        username=u.getText();
        password=p.getText();
        if(ae.getSource()==login){
            dispose();
        }
}
}
class SignUp extends JFrame implements ActionListener {
    JLabel username1, password1;
    JButton signup;
    JTextField u, p;
    JRadioButton r1, r2, r3, r4, r5;
    public String username, password, role;

    public SignUp() {
        setLayout(new FlowLayout());
        username1 = new JLabel("username");
        u = new JTextField();
        u.setColumns(20);

        password1 = new JLabel("password");
        p = new JTextField();
        p.setColumns(20);
        r1 = new JRadioButton("Restaurant_Manager");
        r2 = new JRadioButton("Individual_donor");
        r3 = new JRadioButton("NGO_Manager");
        r4 = new JRadioButton("System_admin");


        ButtonGroup bg = new ButtonGroup();
        bg.add(r1);
        bg.add(r2);
        bg.add(r3);
        bg.add(r4);


        signup = new JButton("SIGN UP");
        add(username1);
        add(u);
        add(password1);
        add(p);
        add(r1);
        add(r2);
        add(r3);
        add(r4);
        add(signup);


        signup.addActionListener(this);
        setVisible(true);
        setSize(300, 250);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent ae) {
        username = u.getText();
        password = p.getText();
        if (r1.isSelected()) {
            role = "Restaurant_Manager";
        } else if (r2.isSelected()) {
            role = "Individual_donor";
        } else if (r3.isSelected()) {
            role = "NGO_Manager";
        } else if (r4.isSelected()) {
            role = "System_admin";
        }
        if (ae.getSource() == signup) {
            dispose();
        }
    }
}
class MainMenu extends JFrame implements ActionListener{
    JLabel welcome;
    JButton donate,receive,getreceipt;
    public int choice;
    public MainMenu(String wel){
        setLayout(new FlowLayout());
        welcome =new JLabel(wel);
        donate=new JButton("Donate");
        receive=new JButton("Receive");
        getreceipt=new JButton("Get Receipt");
        add(welcome);
        add(donate);
        add(receive);
        add(getreceipt);

        donate.addActionListener(this);
        receive.addActionListener(this);
        getreceipt.addActionListener(this);
        setVisible(true);
        setSize(300,150);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public void actionPerformed(ActionEvent ae){
        if(ae.getSource()==donate){
            choice=1;
            dispose();
        }
        else if(ae.getSource()==receive){
            choice=2;
            dispose();
        }
        else if(ae.getSource()==getreceipt){
            choice=3;
            dispose();
        }
    }

}
 class MessageBox{
    JFrame f;
    public MessageBox(String message){
        f=new JFrame();
        JOptionPane.showMessageDialog(f,message);
    }

}