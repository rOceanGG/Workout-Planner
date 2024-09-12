package workoutplannernea;

public class GaleShapley {
    final private String[] people;
    final private String[] exercises;
    final private User[] users;
    final private int[] userIDs;
    final private int[] exerciseIDs;
    final private int[][] userPreferences;
    final private int[][] exercisePreferences;
    final private String[][] timetables;
    final private GenericHashMap<String, String[]> GHM;
    final boolean type;
    
    
    public GaleShapley(int wID){
        DatabaseConnection dbconn = new DatabaseConnection();
        int noOfUsers = dbconn.getNoOfUsers();
        people = dbconn.getUserNames();
        userIDs = new int[noOfUsers];
        exercises = dbconn.getExercisesFromWorkout(wID);
        users = dbconn.getUsers();
        GHM = new GenericHashMap<>(users.length);
        String[] arr;
        if(dbconn.moreUsers(wID)) arr = new String[users.length];
        else arr = new String[exercises.length];
        for(int i = 0; i < users.length; i++){
            userIDs[i] = users[i].getID();
            String username = users[i].getFirst();
            GHM.add(username, arr);
        }
        exerciseIDs = dbconn.getExerciseIDsFromWorkout(wID);
        userPreferences = dbconn.getPreferences(wID);
        exercisePreferences = dbconn.getTimes(wID);
        type = dbconn.moreUsers(wID);
        dbconn.close();
        if(type) timetables = new String[people.length][people.length];
        else timetables = new String[people.length][exercises.length];

        
    }
    
    
    public GenericHashMap<String, String[]> fullMatching(){
        if(type){
            for(int i = 0; i < people.length; i++){
                int[] matches = typeTwoMatch();
                addToTimeTables(matches, i);
            }
        }
        else{
            for(int i = 0; i < exercises.length; i++){
                int[] matches = typeOneMatch();
                addToTimeTables(matches, i);
            }
        }
        addToHashMap();
        return GHM;
    }
    
    public int[] typeOneMatch(){ // We use this matching algorithm if there are more machines than users
        int userIndex; //This is in reference to the user's position in the machines preferences
        int exerciseIndex; // And this is for the machines position in the users preference
        
        int[] userMatchIndexes = setArrConstant(-1, people.length); // Index of matched Exercise in own preferences
        int[] exerciseMatchIndexes = setArrConstant(-1, exercises.length); //Index of matched User in own preferences 
        
        int[] userNextProposal = setArrConstant(0, people.length);
        
        boolean[] matchedUsers = setArrFalse(people.length);
        boolean[] matchedExercises = setArrFalse(exercises.length);
        
        while(containsFalse(matchedUsers)){
            for(int i = 0; i < people.length; i++){
                if(!matchedUsers[i]){ //user will 'propose' if not matched
                    exerciseIndex = indexFromID(userPreferences[i][userNextProposal[i]], exerciseIDs);
                    if(!matchedExercises[exerciseIndex]){// Automatic matching if both user and exercise are unmatched
                        matchedExercises[exerciseIndex] = true;
                        matchedUsers[i] = true;
                        userMatchIndexes[i] = indexFromID(exerciseIDs[exerciseIndex], userPreferences[i]);
                        exerciseMatchIndexes[exerciseIndex] = indexFromID(userIDs[i], exercisePreferences[exerciseIndex]);
                    }
                    else{
                        userIndex = indexFromID(userIDs[i], exercisePreferences[exerciseIndex]);
                        if(exerciseMatchIndexes[exerciseIndex] > userIndex){ //in this case the previously matched user needs to be unmatched and replaced by the current proposer
                            int rejectedUserIndex = indexFromID(exercisePreferences[exerciseIndex][exerciseMatchIndexes[exerciseIndex]], userIDs);
                            userMatchIndexes[rejectedUserIndex] = -1;
                            matchedUsers[rejectedUserIndex] = false;
                            exerciseMatchIndexes[exerciseIndex] = userIndex;
                            userMatchIndexes[i] = indexFromID(exerciseIDs[exerciseIndex], userPreferences[i]);
                            matchedExercises[exerciseIndex] = true;
                            matchedUsers[i] = true;                       
                        }
                    }
                    userNextProposal[i]++;
                }
            }
        }
        int[] matchedExerciseIDs = exIDsFromPreferenceIDs(userMatchIndexes);
        alterUserAndExercisePreferences(userMatchIndexes, exerciseMatchIndexes);
        return matchedExerciseIDs;
    }
    
    public int[] typeTwoMatch(){// We use this matching method if there are more users than machines
        int exerciseIndex;
        int userIndex;
        
        int[] exerciseMatchIndexes = setArrConstant(-1, exercises.length);
        int[] userMatchIndexes = setArrConstant(-1, people.length);
        
        int[] exerciseNextProposal = setArrConstant(0, exercises.length);
                    
        boolean[] matchedExercises = setArrFalse(exercises.length);
        boolean[] matchedUsers = setArrFalse(people.length);
        
        while(containsFalse(matchedExercises)){
            for(int i = 0; i < exercises.length; i++){
                if(!matchedExercises[i]){
                    userIndex = indexFromID(exercisePreferences[i][exerciseNextProposal[i]], userIDs);
                    if(!matchedUsers[userIndex]){
                        matchedUsers[userIndex] = true;
                        matchedExercises[i] = true;
                        exerciseMatchIndexes[i] = indexFromID(userIDs[userIndex], exercisePreferences[i]);
                        userMatchIndexes[userIndex] = indexFromID(exerciseIDs[i], userPreferences[userIndex]);
                    }
                    else{
                        exerciseIndex = indexFromID(exerciseIDs[i], userPreferences[userIndex]);
                        if(userMatchIndexes[userIndex] > exerciseIndex){
                            int rejectedExercisesIndex = indexFromID(userPreferences[userIndex][userMatchIndexes[userIndex]], exerciseIDs);
                            exerciseMatchIndexes[rejectedExercisesIndex] = -1;
                            matchedExercises[rejectedExercisesIndex] = false;
                            userMatchIndexes[userIndex] = exerciseIndex;
                            exerciseMatchIndexes[i] = indexFromID(userIDs[userIndex], exercisePreferences[i]);
                            matchedUsers[userIndex] = true;
                            matchedExercises[i] = true;
                        }
                    }
                    exerciseNextProposal[i]++;
                }
            }    
        }
        int[] matchedExerciseIDs = exIDsFromPreferenceIDs(userMatchIndexes);
        alterUserAndExercisePreferences(userMatchIndexes, exerciseMatchIndexes);
        return matchedExerciseIDs;
    }
    
    private int[] setArrConstant(int constant, int length){
        int[] arr = new int[length];
        for(int i = 0; i < length; i++){
            arr[i] = constant;
        }
        return arr;
    }
    
    private boolean[] setArrFalse(int length){
        boolean[] arr = new boolean[length];
        for(int i = 0; i < arr.length; i++){
            arr[i] = false;
        }
        return arr;
    }
    
    private static boolean containsFalse(boolean[] arr){
        for(boolean b : arr){
            if(!b) return true;   
        }
        return false;
    }
    
    private int indexFromID(int ID, int[] arr){
        for(int i = 0; i < arr.length; i++){
            if(arr[i] == ID) return i;
        }
        return -1;
    }
    
    private int[] exIDsFromPreferenceIDs(int[] userMatchIndexes){
        int[] exIDs = new int[userMatchIndexes.length];
        for(int i = 0; i < exIDs.length; i++){
            if(userMatchIndexes[i] != -1){
                exIDs[i] = userPreferences[i][userMatchIndexes[i]];
            }
            else exIDs[i] = -1;
        }
        return exIDs;
    }
    
    private void addToTimeTables(int[] matchedExerciseIDs, int count){
        DatabaseConnection dbconn = new DatabaseConnection();
        for(int i = 0; i < people.length; i++){
            if(matchedExerciseIDs[i] == -1){
                timetables[i][count] = "Rest";
            }
            else{
                timetables[i][count] = dbconn.getExerciseFromID(matchedExerciseIDs[i]);
            }
        }
        dbconn.close();
    }
    
    
    
    private void alterUserAndExercisePreferences(int[] userMatchIndexes, int[] exerciseMatchIndexes){ //Surprisingly this is simple. I add each array to a linked list. Delete the matched value and at it to the end to move each ID
        for(int i = 0; i < people.length; i++){
            if(userMatchIndexes[i] != -1){
                LinkedList linky = new LinkedList(userPreferences[i]);
                linky.deleteAt(userMatchIndexes[i]);
                linky.append(userPreferences[i][userMatchIndexes[i]]);
                userPreferences[i] = linky.asArray();
            }
            
        }
        for(int j = 0; j < exercises.length; j++){
            if(exerciseMatchIndexes[j] != -1){
                LinkedList linky = new LinkedList(exercisePreferences[j]);
                linky.deleteAt(exerciseMatchIndexes[j]);
                linky.append(exercisePreferences[j][exerciseMatchIndexes[j]]);
                exercisePreferences[j] = linky.asArray();
            }
        }
    }
    
    private void addToHashMap(){
        for(int i = 0; i < users.length; i++){
           GHM.alter(users[i].getFirst(), timetables[i]);
        }
    }
    
    public void displayEntireHashMap(){
        for(int i = 0; i < users.length; i++){
            String[] singleTimeTable = GHM.peek(users[i].getFirst());
            System.out.println(users[i].getFirst() + ": ");
            for(String s : singleTimeTable){
                System.out.println(s);
            }
            System.out.println("");
        }
        
    }
}