package workoutplannernea;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public class DatabaseConnection {
    private Connection conn = null;
    
    public DatabaseConnection() {
        try
        {
            conn = DriverManager.getConnection("jdbc:sqlite:WorkoutPlanner.db");//Specify the database, since relative in the main project folder
            conn.setAutoCommit(false);
        } 
        catch (Exception e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
    
    public void close(){
        try
        {
            conn.close();
        } 
        catch (SQLException ex)
        {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String[] getUserNames(){
        String[] users = new String[getNoOfUsers()];
        Statement stmt;
        ResultSet rs;
        int count = 0;

        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT FirstName, Surname FROM Users ORDER BY UserID ASC");
            while(rs.next()){
                String FirstName = rs.getString("FirstName");
                String Surname = rs.getString("Surname");
                String fullName = FirstName + " " + Surname;
                users[count] = fullName;
                count++;
            }
            stmt.close();
            rs.close();
        }
        catch(SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return users;
    }
    
    public User[] getUsers(){
        User[] users = new User[getNoOfUsers()];
        Statement stmt;
        ResultSet rs;
        int count = 0;

        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT UserID, FirstName, Surname FROM Users ORDER BY UserID ASC");
            while(rs.next()){
                int UserID = rs.getInt("UserID");
                String FirstName = rs.getString("FirstName");
                String Surname = rs.getString("Surname");
                User user = new User(UserID, FirstName, Surname);
                users[count] = user;
                count++;
            }
            stmt.close();
            rs.close();
        }
        catch(SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return users;
    }
    
    public User getUserFromID(int uID){
        Statement stmt;
        ResultSet rs;
        User user = null;
        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT UserID, FirstName, Surname FROM Users WHERE UserID = " + uID);
            while(rs.next()){
                int UserID = rs.getInt("UserID");
                String FirstName = rs.getString("FirstName");
                String Surname = rs.getString("Surname");
                user = new User(UserID, FirstName, Surname);
            }
            stmt.close();
            rs.close();
        }
        catch(SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        
        return user;
        
    }
    
    public User getUserFromName(String firstname){
        Statement stmt;
        ResultSet rs;
        User user = null;
        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT UserID, FirstName, Surname FROM Users WHERE FirstName = '" + firstname + "'");
            String FirstName = rs.getString("FirstName");
            int UserID = rs.getInt("UserID");
            String Surname = rs.getString("Surname");
            user = new User(UserID, FirstName, Surname);
            stmt.close();
            rs.close();
        }
        catch(SQLException e){
            System.err.println(e.getClass().getName() + ":" + e.getMessage());
        }
        return user;
    }
    
    public int getNoOfUsers(){
        Statement stmt;
        ResultSet rs;
        int number = 5;
        
        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT COUNT(UserID) FROM Users");
            number = rs.getInt("COUNT(UserID)");
            stmt.close();
            rs.close();
        }
        catch(SQLException e){
            System.err.println(e.getClass().getName() + ":" + e.getMessage());
        }
        return number;
    }
    
    public void addUser(String FirstName, String Surname){
        Statement stmt;
        try{
            stmt = conn.createStatement();
            stmt.execute("INSERT INTO Users(FirstName, Surname) VALUES('" + FirstName + "', '" + Surname + "')");
            stmt.close();
            conn.commit();
        }
        catch(SQLException e){
            System.err.println(e.getClass().getName() + ":" + e.getMessage());
        }
    }
    
    public void deleteUser(int UserID){
        Statement stmt;
        try{
            stmt = conn.createStatement();
            stmt.execute("DELETE FROM Users WHERE UserID = " + UserID);
            stmt.close();
            conn.commit();
        }
        catch(SQLException e){
            System.err.println(e.getClass().getName() + ":" + e.getMessage());
        }
    }
    
    public void modifyUser(int uID, String firstName, String surname){
        Statement stmt;
        String sql = "UPDATE Users SET FirstName = '" + firstName + "', Surname = '" + surname + "' WHERE UserID =" + uID;
        try{
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
            conn.commit();
        }
        catch(SQLException e){
            System.err.println(e.getClass().getName() + ":" + e.getMessage());
        }
    }
    
    public void deleteUserTimesAndPreferences(int deleteID) { //This method is for when a user is deleted
        Statement stmt;
        try{
            stmt = conn.createStatement();
            stmt.execute("DELETE FROM Times WHERE UserID = " + deleteID);
            stmt.execute("DELETE FROM Preferences WHERE UserID = " + deleteID);
            stmt.close();
            conn.commit();
        }
        catch(SQLException e){
            System.err.println(e.getClass().getName() + ":" + e.getMessage());
        }
    }
    
    public void deleteWorkoutPreferences(int deleteID){
        Statement stmt;
        try{
            stmt = conn.createStatement();
            stmt.execute("DELETE FROM Preferences WHERE ComponentID = (SELECT ComponentID FROM WorkoutComponent WHERE WorkoutID = " + deleteID + ")");
            stmt.close();
            conn.commit();
        }
        catch(SQLException e){
            System.err.println(e.getClass().getName() + ":" + e.getMessage());
        }
    }

    public void deleteComponents(int deleteID){
        Statement stmt;
        try{
            stmt = conn.createStatement();
            stmt.execute("DELETE FROM WorkoutComponent WHERE WorkoutID = " + deleteID);
            stmt.close();
            conn.commit();
        }
        catch(SQLException e){
            System.err.println(e.getClass().getName() + ":" + e.getMessage());
        }
    }
    public boolean userExists(int uID){
        Statement stmt;
        ResultSet rs;
        int number = 0;
        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT COUNT(UserID) FROM Users WHERE UserID = " + uID);
            number = rs.getInt("Count(UserID)");
            stmt.close();
            rs.close();
        }
        catch(SQLException e){
            System.err.println(e.getClass().getName() + ":" + e.getMessage());
        }
        return 0 < number;
    }

    public boolean zeroUsers(){
        Statement stmt;
        ResultSet rs;
        int count = 1;

        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT COUNT(UserID) FROM Users");
            count = rs.getInt("COUNT(UserID)");
            stmt.close();
            rs.close();
        }
        catch(SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        return count == 0;
    }

    public boolean userHasTime(int uID, int exID){
        Statement stmt;
        ResultSet rs;
        int number = 0;
        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT COUNT(TimeTaken) FROM Times WHERE UserID = " + uID + " AND ExerciseID = " + exID);
            number = rs.getInt("COUNT(TimeTaken)");
            stmt.close();
            rs.close();
        }
        catch(SQLException e){
            System.err.println(e.getClass().getName() + ":" + e.getMessage());
        }
        return number != 0;
    }
    
    public void displayUsers(){
        Statement stmt;
        ResultSet rs;
        
        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT UserID, FirstName, Surname FROM USERS");
            while(rs.next()){
                System.out.println("UserID: " + rs.getInt("UserID") + ", First Name : " + rs.getString("FirstName") + ", LastName: " + rs.getString("Surname"));
            }
            stmt.close();
            rs.close();
        }
        catch(SQLException e){
            System.err.println(e.getClass().getName() + ":" + e.getMessage());
        }
    }
    
    public String getExerciseFromID(int exID){
        Statement stmt;
        ResultSet rs;
        String Name = ""; 
       try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT ExerciseName FROM Exercises WHERE ExerciseID = " + exID);
            Name = rs.getString("ExerciseName");
            stmt.close();
            rs.close();
        }
        catch(SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
       return Name;
    }
    
    public String[] getExerciseFromIDs(int[] IDs){
        String[] exercises = new String[IDs.length];
        for(int i = 0; i < IDs.length; i++){
            exercises[i] = getExerciseFromID(IDs[i]);
        }
        return exercises;
    }
    
    public int getComponentID(int exID, int wID){
        Statement stmt;
        ResultSet rs;
        int compID = -1;
        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT ComponentID FROM WorkoutComponent WHERE WorkoutID = " + wID + " AND ExerciseID = " + exID);
            compID = rs.getInt("ComponentID");
        }
        catch(SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return compID;
    }
    
    public boolean exerciseExists(int exID){
        Statement stmt;
        ResultSet rs;
        int number = 0;
        
        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT COUNT(ExerciseID) FROM Exercises WHERE ExerciseID = " + exID);
            number = rs.getInt("COUNT(ExerciseID)");
            stmt.close();
            rs.close();
        }
        catch(SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return number != 0;
    }
    
    public void displayExercises(){
        Statement stmt;
        ResultSet rs;
        
        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT ExerciseID, ExerciseName FROM Exercises");
            while(rs.next()){
                int exID = rs.getInt("ExerciseID");
                String exName = rs.getString("ExerciseName");
                System.out.println("Exercise ID: " + exID + ", Exercise Name: " + exName);
            }
            stmt.close();
            rs.close();
        }
        catch(SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }
    
    public void addWorkout(String workoutName){
        Statement stmt;
        
        try{
            stmt = conn.createStatement();
            stmt.execute("INSERT INTO WORKOUT(WorkoutName) VALUES('" + workoutName + "')");
            stmt.close();
            conn.commit();
        }
        catch(SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void deleteWorkout(int wID){
        Statement stmt;
        
        try{
            stmt = conn.createStatement();
            stmt.execute("DELETE FROM Workout WHERE WorkoutID = " + wID);
            stmt.execute("DELETE FROM WorkoutComponent WHERE WorkoutID = " + wID);
            stmt.close();
            conn.commit();
        }
        catch(SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }
    
    public void addExToWorkout(int exID, int wID, int sets){
        Statement stmt;
        
        try{
            stmt = conn.createStatement();
            stmt.executeUpdate("INSERT INTO WorkoutComponent(ExerciseID, WorkoutID, Sets) VALUES(" + exID + ", " + wID + ", " + sets + ")");
            stmt.close();
            conn.commit();
        }
        catch(SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }
    
    public boolean exerciseInWorkout(int exID, int wID){
        Statement stmt;
        ResultSet rs;
        int number = 0;
        
        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT COUNT(ComponentID) FROM WorkoutComponent WHERE ExerciseID = " + exID + " AND WorkoutID = " + wID);
            number = rs.getInt("COUNT(ComponentID)");
            stmt.close();
            rs.close();
        }
        catch(SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return number != 0;
    }
    
    public void deleteExerciseFromWorkout(int exID, int wID){
        Statement stmt;
        
        try{
            stmt = conn.createStatement();
            stmt.execute("DELETE FROM WorkoutComponent WHERE ExerciseID = " + exID + " AND WorkoutID = " + wID);
            stmt.close();
            conn.commit();
        }
        catch(SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public int getWorkoutIDFromName(String name) {
        Statement stmt;
        ResultSet rs;
        int number = -1;
        
        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT WorkoutID FROM WORKOUT WHERE WORKOUTNAME = '" + name + "'");
            number = rs.getInt("WorkoutID");
            stmt.close();
            rs.close();
        }
        catch(SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        
        return number;
    }
    
    public boolean workoutHasExercises(int wID){
        Statement stmt;
        ResultSet rs;
        int number = 0;
        
        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT COUNT(ComponentID) FROM WorkoutComponent WHERE WorkoutID = " + wID);
            number = rs.getInt("COUNT(ComponentID)");
            stmt.close();
            rs.close();
        }
        catch(SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return number != 0;
    }
    
    public boolean workoutExists(int wID){
        Statement stmt;
        ResultSet rs;
        int number = 0;
        
        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT COUNT(WorkoutID) FROM Workout WHERE WorkoutID = " + wID);
            number = rs.getInt("COUNT(WorkoutID)");
            stmt.close();
            rs.close();
        }
        catch(SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return number != 0;
    }

    public boolean zeroWorkouts(){
        Statement stmt;
        ResultSet rs;
        int count = 1;

        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT COUNT(WorkoutID) FROM Workout");
            count = rs.getInt("COUNT(WorkoutID)");
            stmt.close();
            rs.close();
        }
        catch(SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }

        return count == 0;
    }

    public void displayWorkouts(){
        Statement stmt;
        ResultSet rs;
        
        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT WorkoutID, WorkoutName FROM Workout");
            while(rs.next()){
                int wID = rs.getInt("WorkoutID");
                String wName = rs.getString("WorkoutName");
                System.out.println("Workout ID: " + wID + ", Workout Name: " + wName);
            }
            stmt.close();
            rs.close();
        }
        catch(SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }
    
    public void displayExercisesInWorkout(int wID){
        Statement stmt;
        ResultSet rs;
        
        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT Exercises.ExerciseName, WorkoutComponent.Sets, WorkoutComponent.ExerciseID FROM Exercises, WorkoutComponent "
                    + "WHERE Exercises.ExerciseID = WorkoutComponent.ExerciseID AND WorkoutComponent.WorkoutID = " + wID);
            
            while(rs.next()){
                String eName = rs.getString("ExerciseName"); //ename = Exercise Name
                int exID = rs.getInt("ExerciseID");
                int sets = rs.getInt("Sets");
                System.out.println("Exercise ID : " + exID + ", " + sets + " sets of " + eName);
            }
            stmt.close();
            rs.close();
        }
        catch(SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void changeSetsOfExercise(int wID, int exID, int newSets) {
        Statement stmt;
        
        try{
            stmt = conn.createStatement();
            stmt.executeUpdate("UPDATE WorkoutComponent SET Sets = " + newSets + " WHERE ExerciseID = " + exID + " AND WorkoutID = " + wID);
            stmt.close();
            conn.commit();
        }
        catch(SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }
    
    public int[] getExerciseIDsFromWorkout(int wID){
        Statement stmt;
        ResultSet rs;
        LinkedList linky = new LinkedList();
        
        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT ExerciseID FROM WorkoutComponent WHERE WorkoutID = " + wID);
            
            while(rs.next()){
                int exID = rs.getInt("ExerciseID");
                linky.append(exID);
            }
            stmt.close();
            rs.close();
        }
        catch(SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        
        return linky.asArray();
    }
    
    public int noOfExercisesInWorkout(int wID){
        Statement stmt;
        ResultSet rs;
        int number = 0;
        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT COUNT(ExerciseID) FROM WorkoutComponent WHERE WorkoutID = " + wID);
            number = rs.getInt("COUNT(ExerciseID)");
            stmt.close();
            rs.close();
        }
        catch(SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return number;
    }
    
    public String[] getExercisesFromWorkout(int wID){
        String[] exercises = new String[noOfExercisesInWorkout(wID)];
        Statement stmt;
        ResultSet rs;
        int count = 0;
        
        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT Exercises.ExerciseName FROM Exercises INNER JOIN WorkoutComponent ON WorkoutComponent.ExerciseID = Exercises.ExerciseID AND WorkoutComponent.WorkoutID = " + wID + ";");
            
            while(rs.next()){
                String eName = rs.getString("ExerciseName"); //ename = Exercise Name
                exercises[count] = eName;
                count++;
            }
            stmt.close();
            rs.close();
        }
        catch(SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return exercises;
    }
    
    public void addTime(int uID, int exID, int time){
        Statement stmt;
        
        try{
            stmt = conn.createStatement();
            stmt.executeUpdate("INSERT INTO Times(UserID, ExerciseID, TimeTaken) VALUES("+ uID + ", " + exID + ", " + time + ")");
            stmt.close();
            conn.commit();
        }
        catch(SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }
    
    public void addPreference(int uID, int compID, int preference){
        Statement stmt;
        try{
            stmt = conn.createStatement();
            stmt.executeUpdate("INSERT INTO Preferences(UserID, ComponentID, Preference) VALUES(" + uID + ", " + compID + ", " + preference + ")");
            stmt.close();
            conn.commit();
        }
        catch(SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }
    
    public int[][] getPreferences(int wID){
        int noOfUsers = getNoOfUsers();
        int exercises = noOfExercisesInWorkout(wID);
        int[][] preferences = new int[noOfUsers][exercises];
        User[] users = getUsers();
        int count = 0;
        Statement stmt;
        ResultSet rs = null;
        try{
            stmt = conn.createStatement();
            
            for(int x = 0; x < noOfUsers; x++){
                rs = stmt.executeQuery("SELECT ExerciseID FROM WorkoutComponent, Preferences WHERE Preferences.UserID = " + users[x].getID()
                        + " AND Preferences.ComponentID = WorkoutComponent.ComponentID AND WorkoutComponent.WorkoutID = " + wID + " ORDER BY Preference ASC");
                while(rs.next()){
                    preferences[x][count] = rs.getInt(1);
                    count++;
                }
                count = 0;
            }
            stmt.close();
            rs.close();
        }
        catch(SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        
        return preferences;
    }
    
    public WorkoutComponent[] getWorkoutComponents(){
        WorkoutComponent[] components = new WorkoutComponent[getNoOfComponents()];
        int count = 0;
        Statement stmt;
        ResultSet rs;
        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT ComponentID, WorkoutID, ExerciseID FROM WorkoutComponent");
            while(rs.next()){
                components[count] = new WorkoutComponent(rs.getInt("ComponentID"), rs.getInt("WorkoutID"), rs.getInt("ExerciseID"));
                count++;
            }
        }
        catch(SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return components;
    }
    
    public int getNoOfComponents(){
        Statement stmt;
        ResultSet rs;
        int number = -1;
        
        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT COUNT(ComponentID) FROM WorkoutComponent");
            number = rs.getInt("COUNT(ComponentID)");
            stmt.close();
            rs.close();
        }
        catch(SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return number;
    }
    
    public int[][] getTimes(int wID){ //This set of data will create the machines preferences. We need to get each machine and then 
        int noOfUsers = getNoOfUsers();
        int noOfExercises = noOfExercisesInWorkout(wID);
        int[][] times = new int[noOfExercises][noOfUsers];
        int[] exerciseIDs = getExerciseIDsFromWorkout(wID);
        int count = 0;
        Statement stmt;
        ResultSet rs = null;
        try{
            stmt = conn.createStatement();
            for(int x = 0; x < noOfExercises; x++){
                int currentExID = exerciseIDs[x];
                rs = stmt.executeQuery("SELECT UserID FROM Times WHERE ExerciseID = " + currentExID + " ORDER BY TimeTaken ASC");
                while(rs.next()){
                    times[x][count] = rs.getInt("UserID");
                    count++;
                }
                count = 0;
            }
            stmt.close();
            rs.close();
        }
        catch(SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return times;
    }
    
    public int[] getRequiredTimes(){
        Statement stmt;
        ResultSet rs;
        LinkedList IDs = new LinkedList();
        try{
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT DISTINCT(ExerciseID) FROM WorkoutComponent");
            while(rs.next()){
                IDs.append(rs.getInt("DISTINCT(ExerciseID)"));
            }
            stmt.close();
            rs.close();
        }
        catch(SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return IDs.asArray();
    }
    
    public boolean moreUsers(int wID){
        return getNoOfUsers() > noOfExercisesInWorkout(wID);
    }

    
}

//wID = WorkoutID
//exID = ExerciseID
//compID = ComponentID
