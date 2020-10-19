package User;

public class UserList {
    public String addUserAccount(String username,String password, String role){
        return "insert into users values('"+username+"','"+password+"','"+role+"');";
    }

    public String LoginUser(String username,String password){
        return "select *from users where username='"+username+"' and password='"+password+"';";
    }

}

