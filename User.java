package workoutplannernea;

public class User{
    private int UserID;
    private String FirstName, Surname;
    
    public User(int userid, String firstname, String surname){
        this.UserID = userid;
        this.FirstName = firstname;
        this.Surname = surname;
    }
    
    public int getID(){
        return UserID;
    }
    
    public String getFirst(){
        return FirstName;
    }
    
    public String getSurname(){
        return Surname;
    }
        
}
