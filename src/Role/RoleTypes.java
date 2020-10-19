package Role;

import java.util.Enumeration;
import java.util.Vector;

public class RoleTypes {
    Enumeration e;
    public RoleTypes(){
        Vector roles=new Vector();
        roles.add("Restaurant_Manager");
        roles.add("Individual_donor");
        roles.add("NGO_Manager");
        roles.add("System_admin");
        roles.add("Driver");
        e=roles.elements();
    }
    public void print_roleTypes(){
        while(e.hasMoreElements()){
            System.out.println(e.nextElement());
        }
    }

}
