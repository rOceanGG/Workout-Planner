package workoutplannernea;

import java.util.InputMismatchException;
import java.util.Scanner;

public class WorkoutPlannerNEA {
    public static void main(String[] args) {
        DatabaseConnection dbconn = new DatabaseConnection();
        int exitKey = 0;
        boolean typeTestPassed;
        Scanner sc = new Scanner(System.in);

        System.out.println("Hello new user and welcome to this workout planner. To get started \nwe are going to need you to enter the names of everyone in your gym group.");
        System.out.println("To get started please enter how many people are in your group \n(If you have already entered your group in then please enter 0)");
        int noOfUsers = -1;
        typeTestPassed = false;
        while(!typeTestPassed || noOfUsers < 0){
            try{
                typeTestPassed = false;
                System.out.println("Please enter the number of users you want to add: ");
                noOfUsers = sc.nextInt();
                typeTestPassed = true;
            }
            catch(InputMismatchException e){
                System.out.println("You must enter a number");
                sc.next();
            }
        }
        
        for (int i = 0; i < noOfUsers; i++) {
            System.out.println("Please enter User " + (i + 1) + "'s first name");
            String first = sc.next().trim();
            System.out.println("Please enter User " + (i + 1) + "'s surname");
            String surname = sc.next().trim();
            dbconn.addUser(first, surname);
            if (!dbconn.zeroWorkouts()){
                int[] exerciseIDs = dbconn.getRequiredTimes();
                String[] requiredTimes = dbconn.getExerciseFromIDs(exerciseIDs);
                for(int j = 0; j < exerciseIDs.length; j++){
                    System.out.println("Please enter how long it takes you to do " + requiredTimes[j] + ": ");
                    int time = -1;
                    typeTestPassed = false;
                    while(!typeTestPassed || time < 1){
                        try{
                            typeTestPassed = false;
                            time = sc.nextInt();
                            typeTestPassed = true;
                        }
                        catch(InputMismatchException e){
                            System.out.println("Please enter a valid time: ");
                            sc.next();
                        }
                    }
                    dbconn.addTime(dbconn.getUserFromName(first).getID(), exerciseIDs[j], time);
                }

                WorkoutComponent[] requiredPreferences = dbconn.getWorkoutComponents();
                int uID = dbconn.getUserFromName(first).getID();
                for(WorkoutComponent wc : requiredPreferences){
                    System.out.println("Please enter your preference for " + dbconn.getExerciseFromID(wc.getExerciseID()) + " in workout " + wc.getWorkoutID());
                    int preference = -1;
                    typeTestPassed = false;
                    while(!typeTestPassed || preference < 1 || preference > 10){
                        try{
                            typeTestPassed = false;
                            preference = sc.nextInt();
                            typeTestPassed = true;
                        }
                        catch(InputMismatchException e){
                            System.out.println("Please enter a valid time: ");
                            sc.next();
                        }
                    }
                    dbconn.addPreference(uID, wc.getComponentID(), preference);
                }
            }
        }
        dbconn.displayUsers();

        System.out.println("Thank you for entering you users in. Now what do you want to do?");
        while(exitKey == 0){
            System.out.println("Press 1 for user management");
            System.out.println("Press 2 for workout management");
            System.out.println("Press 3 to get schedules for your group");
            typeTestPassed = false;
            int firstChoice = -1;
            while(!typeTestPassed || firstChoice < 1 || firstChoice > 3){
                try{
                    typeTestPassed = false;
                    System.out.println("Please enter you choice: ");
                    firstChoice = sc.nextInt();
                    typeTestPassed = true;
                }
                catch(InputMismatchException e){
                    System.out.println("You must enter a valid number");
                    sc.next();
                }
            }
            int secondChoice = -1;

            switch (firstChoice) {
                case 1:
                    System.out.println("Press 1 to add a user");
                    System.out.println("Press 2 to delete a user");
                    System.out.println("Press 3 to modify a user");
                    System.out.println("Press 4 to display users");

                    typeTestPassed = false;
                    while(!typeTestPassed || secondChoice < 1 || secondChoice > 4){
                        try{
                            typeTestPassed = false;
                            System.out.println("Please enter your choice");
                            secondChoice = sc.nextInt();
                            typeTestPassed = true;
                        }
                        catch(InputMismatchException e){
                            System.out.println("You must enter a valid number");
                            sc.next();
                        }
                    }

                    switch (secondChoice) {
                        case 1:
                            System.out.println("Please enter the users first name: ");
                            String first = sc.next().trim();
                            System.out.println("Please enter the users last name: ");
                            String last = sc.next().trim();
                            dbconn.addUser(first, last);

                            if (!dbconn.zeroWorkouts()){
                                int[] exerciseIDs = dbconn.getRequiredTimes();
                                String[] requiredTimes = dbconn.getExerciseFromIDs(exerciseIDs);
                                for(int i = 0; i < exerciseIDs.length; i++){
                                    System.out.println("Please enter how long it takes you to do " + requiredTimes[i] + ": ");
                                    int time = -1;
                                    typeTestPassed = false;
                                    while(!typeTestPassed || time < 1){
                                        try{
                                            typeTestPassed = false;
                                            System.out.println("Please enter your time");
                                            time = sc.nextInt();
                                            typeTestPassed = true;
                                        }
                                        catch(InputMismatchException e){
                                            System.out.println("You must enter a valid time");
                                            sc.next();
                                        }
                                    }
                                    dbconn.addTime(dbconn.getUserFromName(first).getID(), exerciseIDs[i], time);
                                }

                                WorkoutComponent[] requiredPreferences = dbconn.getWorkoutComponents();
                                int uID = dbconn.getUserFromName(first).getID();
                                for(WorkoutComponent wc : requiredPreferences){
                                    System.out.println("Please enter your preference for " + dbconn.getExerciseFromID(wc.getExerciseID()) + " in workout " + wc.getWorkoutID());
                                    int preference = -1;
                                    typeTestPassed = false;
                                    while(!typeTestPassed || preference < 1 || preference > 10){
                                        try{
                                            typeTestPassed = false;
                                            System.out.println("Please enter your preference: ");
                                            preference = sc.nextInt();
                                            typeTestPassed = true;
                                        }
                                        catch(InputMismatchException e){
                                            System.out.println("You mist enter a valid preference");
                                            sc.next();
                                        }
                                    }
                                    dbconn.addPreference(uID, wc.getComponentID(), preference);
                                }
                            }
                            break;

                        case 2:
                            if(dbconn.zeroUsers()){
                                System.out.println("There are no users to delete");
                                break;
                            }
                            dbconn.displayUsers();

                            System.out.println("Please enter the UserID of the user you want to delete: ");
                            int deleteID = -1;
                            typeTestPassed = false;
                            while(!typeTestPassed || !dbconn.userExists(deleteID)){
                                try{
                                    typeTestPassed = false;
                                    System.out.println("Please enter a UserID: ");
                                    deleteID = sc.nextInt();
                                    typeTestPassed = true;
                                }
                                catch(InputMismatchException e){
                                    System.out.println("You must enter a valid UserID");
                                    sc.next();
                                }
                            }

                            dbconn.deleteUser(deleteID);
                            dbconn.deleteUserTimesAndPreferences(deleteID);
                            break;

                        case 3:
                            if(dbconn.zeroUsers()){
                                System.out.println("There are no users to modify");
                                break;
                            }
                            dbconn.displayUsers();

                            System.out.println("Please enter the UserID of the user you want to modify");
                            int modifyID = -1;
                            typeTestPassed = false;
                            while(!typeTestPassed || !dbconn.userExists(modifyID)){
                                try{
                                    typeTestPassed = false;
                                    System.out.println("Please enter a UserID: ");
                                    modifyID = sc.nextInt();
                                    typeTestPassed = true;
                                }
                                catch(InputMismatchException e){
                                    System.out.println("You must enter a valid UserID");
                                    sc.next();
                                }
                            }
                            System.out.println("Please enter the new first name: ");
                            String newFirst = sc.next();
                            System.out.println("Please enter the new last name: ");
                            String newLast = sc.next();

                            dbconn.modifyUser(modifyID, newFirst, newLast);

                        case 4:
                            if(dbconn.zeroUsers()) System.out.println("There are no users in your group.");
                            else dbconn.displayUsers();
                            break;
                    }
                    break;
                case 2:
                    System.out.println("Press 1 to create a workout");
                    System.out.println("Press 2 to delete a workout");
                    System.out.println("Press 3 to modify a workout");
                    System.out.println("Press 4 to display workouts");
                    System.out.println("Press 5 to see the exercises in a workout");
                    typeTestPassed = false;
                    while(!typeTestPassed || secondChoice < 1 || secondChoice > 5){
                        try{
                            typeTestPassed = false;
                            System.out.println("Please enter your choice: ");
                            secondChoice = sc.nextInt();
                            typeTestPassed = true;
                        }
                        catch(InputMismatchException e){
                            System.out.println("You must enter a valid number");
                            sc.next();
                        }
                    }

                    switch (secondChoice) {
                        case 1:
                            System.out.println("Please enter the name of the new workout");
                            String workoutName = sc.next();
                            dbconn.addWorkout(workoutName);

                            int workoutID = dbconn.getWorkoutIDFromName(workoutName);
                            System.out.println("How many exercises are going to be in this workout? ");
                            int noOfExercises = -1;
                            typeTestPassed = false;
                            while(!typeTestPassed || noOfExercises < 1){
                                try{
                                    typeTestPassed = false;
                                    System.out.println("Please enter a number: ");
                                    noOfExercises = sc.nextInt();
                                    typeTestPassed = true;
                                }
                                catch(InputMismatchException e){
                                    System.out.println("You must enter a valid number");
                                    sc.next();
                                }
                            }

                            dbconn.displayExercises();

                            for (int i = 0; i < noOfExercises; i++) {
                                System.out.println("Please enter the ExerciseID of the (next) exercise you want to add: ");
                                int exID = -1;
                                while(!typeTestPassed || !dbconn.exerciseExists(exID) || dbconn.exerciseInWorkout(exID, workoutID)){
                                    try{
                                        typeTestPassed = false;
                                        System.out.println("Please enter an ExerciseID: ");
                                        exID = sc.nextInt();
                                        typeTestPassed = true;
                                    }
                                    catch(InputMismatchException e){
                                        System.out.println("You must enter a valid exerciseID");
                                        sc.next();
                                    }
                                }
                                System.out.println("And how many sets would you like to do? ");
                                int sets = -1;
                                typeTestPassed = false;
                                while(!typeTestPassed || sets < 1){
                                    try{
                                        typeTestPassed = false;
                                        System.out.println("Please enter the number of sets: ");
                                        sets = sc.nextInt();
                                        typeTestPassed = true;
                                    }
                                    catch(InputMismatchException e){
                                        System.out.println("You must enter a valid number");
                                        sc.next();
                                    }
                                }
                                dbconn.addExToWorkout(exID, workoutID, sets);
                                if(i == 0){
                                    System.out.println("An overview on the preference system: The more you want an exercise towards the beginning, the closer you enter a number to 1."
                                            + " If you want an exercise towards the end of the workout then your number should be closer to 10");
                                }
                                User[] users = dbconn.getUsers();
                                String exercise = dbconn.getExerciseFromID(exID);
                                for(int j = 0; j < users.length; j++){

                                    System.out.println(users[j].getFirst() + ", please enter your preference for " + exercise + " : ");
                                    int preference = -1;
                                    typeTestPassed = false;
                                    while(!typeTestPassed || preference < 1 || preference > 10){
                                        try{
                                            typeTestPassed = false;
                                            System.out.println("Please enter your preference: ");
                                            preference = sc.nextInt();
                                            typeTestPassed = true;
                                        }
                                        catch(InputMismatchException e){
                                            System.out.println("You must enter a valid preference");
                                            sc.next();
                                        }
                                    }

                                    if(!dbconn.userHasTime(users[j].getID(), exID)){
                                        System.out.println("Please enter how long it takes you to do " + sets + " sets of " + exercise + ": ");
                                        int time = -1;
                                        typeTestPassed = false;
                                        while(!typeTestPassed || time < 1){
                                            try{
                                                typeTestPassed = false;
                                                System.out.println("Please enter a time: ");
                                                time = sc.nextInt();
                                                typeTestPassed = true;
                                            }
                                            catch(InputMismatchException e){
                                                System.out.println("You must enter a valid time");
                                                sc.next();
                                            }
                                        }
                                        dbconn.addTime(users[j].getID(), exID, time);
                                    }
                                    dbconn.addPreference(users[j].getID(), dbconn.getComponentID(exID, workoutID), preference);
                                }


                            }
                            System.out.println("You have now added " + workoutName + " to your workout list");
                            break;

                        case 2:
                            if(dbconn.zeroWorkouts()){
                                System.out.println("There are no workouts to delete.");
                                break;
                            }
                            dbconn.displayWorkouts();

                            System.out.println("Please enter the Workout ID of the workout you want to delete: ");
                            int deleteID = -1;
                            while(!typeTestPassed || !dbconn.workoutExists(deleteID)){
                                try{
                                    typeTestPassed = false;
                                    System.out.println("Please enter a WorkoutID");
                                    deleteID = sc.nextInt();
                                    typeTestPassed = true;
                                }
                                catch(InputMismatchException e){
                                    System.out.println("You must enter a valid WorkoutID");
                                    sc.next();
                                }
                            }
                            dbconn.deleteWorkoutPreferences(deleteID);
                            dbconn.deleteComponents(deleteID);
                            dbconn.deleteWorkout(deleteID);
                            
                            System.out.println("The workout with WorkoutID " + deleteID + " has been deleted");
                            break;

                        case 3:
                            if(dbconn.zeroWorkouts()){
                                System.out.println("There are no workouts to modify.");
                                break;
                            }
                            dbconn.displayWorkouts();

                            System.out.println("Please enter the Workout ID of the workout you want to modify: ");
                            int modifyID = -1;
                            while(!typeTestPassed || !dbconn.workoutExists(modifyID)){
                                try{
                                    typeTestPassed = false;
                                    System.out.println("Please enter a WorkoutID: ");
                                    modifyID = sc.nextInt();
                                    typeTestPassed = true;
                                }
                                catch(InputMismatchException e){
                                    System.out.println("You must enter a valid WorkoutID");
                                    sc.next();
                                }
                            }

                            System.out.println("Press 1 to add an exercise to this workout");
                            System.out.println("Press 2 to delete an exercise in this workout");
                            System.out.println("Press 3 to change the number of sets of a workout in this exercise");
                            typeTestPassed = false;
                            int thirdChoice = -1;
                            while(!typeTestPassed || thirdChoice < 1 || thirdChoice > 3){
                                try{
                                    typeTestPassed = false;
                                    System.out.println("Please enter your choice: ");
                                    thirdChoice = sc.nextInt();
                                    typeTestPassed = true;
                                }
                                catch(InputMismatchException e){
                                    System.out.println("You must enter a valid choice");
                                    sc.next();
                                }
                            }

                            switch (thirdChoice) {
                                case 1:
                                    dbconn.displayExercises();
                                    System.out.println("Please enter the ExerciseID of the exercise you want to add: ");
                                    int exID = -1;
                                    typeTestPassed = false;
                                    while(!typeTestPassed || !dbconn.exerciseExists(exID) || dbconn.exerciseInWorkout(exID, modifyID)){
                                        try{
                                            typeTestPassed = false;
                                            System.out.println("Please enter an ExerciseID: ");
                                            exID = sc.nextInt();
                                            typeTestPassed = true;
                                        }
                                        catch(InputMismatchException e){
                                            System.out.println("You must enter a valid ExerciseID: ");
                                            sc.next();
                                        }
                                    }
                                    System.out.println("And how many sets would you like to do? ");
                                    int sets = -1;
                                    typeTestPassed = false;
                                    while(!typeTestPassed || sets < 1){
                                        try{
                                            typeTestPassed = false;
                                            System.out.println("Please enter how many sets you want to do: ");
                                            sets = sc.nextInt();
                                            typeTestPassed = true;
                                        }
                                        catch(InputMismatchException e){
                                            System.out.println("You must enter a valid number of sets");
                                            sc.next();
                                        }
                                    }
                                    dbconn.addExToWorkout(exID, modifyID, sets);
                                    
                                    int compID = dbconn.getComponentID(exID, modifyID);
                                    
                                    User[] users = dbconn.getUsers();
                                    String newExercise = dbconn.getExerciseFromID(exID);
                                    
                                    for(User u : users){
                                        System.out.println(u.getFirst() + ", please enter your preference for " + newExercise + ": ");
                                        int preference = -1;
                                        typeTestPassed = false;
                                        while(!typeTestPassed || preference < 1){
                                            try{
                                                typeTestPassed = false;
                                                System.out.println("Please enter your preference: ");
                                                preference = sc.nextInt();
                                                typeTestPassed = true;
                                            }
                                            catch(InputMismatchException e){
                                                System.out.println("You must enter a valid preference");
                                                sc.next();
                                            }
                                        }
                                        dbconn.addPreference(u.getID(), compID, preference);
                                    }
                                    
                                    for(User u : users){
                                        if(!dbconn.userHasTime(u.getID(), exID)){
                                            System.out.println(u.getFirst() + ", please enter how long it takes you to do " + sets + " sets of " + newExercise + ": ");
                                            int time = -1;
                                            typeTestPassed = false;
                                            while(!typeTestPassed || time < 1){
                                                try{
                                                    typeTestPassed = false;
                                                    System.out.println("Please enter your time: ");
                                                    time = sc.nextInt();
                                                    typeTestPassed = true;
                                                }
                                                catch(InputMismatchException e){
                                                    System.out.println("You must enter a valid time");
                                                    sc.next();
                                                }
                                            }
                                            dbconn.addTime(u.getID(), exID, time);
                                        }
                                    }
                                    
                                    break;

                                case 2:
                                    if(!dbconn.workoutHasExercises(modifyID)){
                                        System.out.println("This workout has no exercises to delete");
                                        break;
                                    }
                                    dbconn.displayExercisesInWorkout(modifyID);
                                    System.out.println("Enter the Exercise ID of the exercise you want to delete: ");
                                    int delID = -1;
                                    typeTestPassed = false;
                                    while(!typeTestPassed || !dbconn.exerciseInWorkout(delID, modifyID)){
                                        try{
                                            typeTestPassed = false;
                                            System.out.println("Please enter the ExerciseID: ");
                                            exID = sc.nextInt();
                                            typeTestPassed = true;
                                        }
                                        catch(InputMismatchException e){
                                            System.out.println("You must enter a valid exerciseID");
                                            sc.next();
                                        }
                                    }
                                    dbconn.deleteExerciseFromWorkout(delID, modifyID);
                                    break;

                                case 3:
                                    if(!dbconn.workoutHasExercises(modifyID)){
                                        System.out.println("This workout has no exercises to modify");
                                        break;
                                    }
                                    dbconn.displayExercisesInWorkout(modifyID);
                                    System.out.println("Please enter the Exercise ID of the exercise you want to change: ");
                                    exID = -1;
                                    typeTestPassed = false;
                                    while(!typeTestPassed || !dbconn.exerciseInWorkout(exID, modifyID)){
                                        try{
                                            typeTestPassed = false;
                                            System.out.println("Please enter a valid ExerciseID: ");
                                            exID = sc.nextInt();
                                            typeTestPassed = true;
                                        }
                                        catch(InputMismatchException e){
                                            System.out.println("You must enter a valid exerciseID");
                                            sc.next();
                                        }
                                    }
                                    System.out.println("What is the new number of sets? ");
                                    int newSets = -1;
                                    typeTestPassed = false;
                                    while(!typeTestPassed || newSets < 1){
                                        try{
                                            typeTestPassed = false;
                                            System.out.println("Please enter how many sets you want to do: ");
                                            newSets = sc.nextInt();
                                            typeTestPassed = true;
                                        }
                                        catch(InputMismatchException e){
                                            System.out.println("You must enter a valid number of sets");
                                            sc.next();
                                        }
                                    }
                                    dbconn.changeSetsOfExercise(modifyID, exID, newSets);
                                    break;

                            }
                            break;

                        case 4:
                            if(dbconn.zeroWorkouts()){
                                System.out.println("There are no workouts to display.");
                                break;
                            }
                            dbconn.displayWorkouts();
                            break;

                        case 5:
                            if(dbconn.zeroWorkouts()){
                                System.out.println("There are no workouts to view.");
                                break;
                            }
                            dbconn.displayWorkouts();
                            System.out.println("Please enter the Workout ID of the workout you want to display: ");
                            int displayID = -1;
                            typeTestPassed = false;
                            while(!typeTestPassed || !dbconn.workoutExists(displayID)){
                                try{
                                    typeTestPassed = false;
                                    System.out.println("Please enter a WorkoutID: ");
                                    displayID = sc.nextInt();
                                    typeTestPassed = true;
                                }
                                catch(InputMismatchException e){
                                    System.out.println("You must enter a valid WorkoutID");
                                    sc.next();
                                }
                            }
                            dbconn.displayExercisesInWorkout(displayID);
                            break;
                    }
                    break;
                case 3:
                    if(dbconn.zeroWorkouts()){
                        System.out.println("There are no workouts to get schedules for.");
                        break;
                    }
                    User[] users = dbconn.getUsers();
                    dbconn.displayWorkouts();
                    System.out.println("Please enter the workout ID of the workout you want schedules for: ");
                    int wID = -1;
                    typeTestPassed = false;
                    while(!typeTestPassed || !dbconn.workoutExists(wID)){
                        try{
                            typeTestPassed = false;
                            System.out.println("Please enter a WorkoutID: ");
                            wID = sc.nextInt();
                            typeTestPassed = true;
                        }
                        catch(InputMismatchException e){
                            System.out.println("You must enter a valid WorkoutID");
                            sc.next();
                        }
                    }
                    GaleShapley gs = new GaleShapley(wID);
                    GenericHashMap<String, String[]> timetables = gs.fullMatching();
                    dbconn.displayUsers();
                    System.out.println("Please enter the UserID of the timetable you want to see. Enter -1 to see all timetables: ");
                    int viewID = 0;
                    typeTestPassed = false;
                    while(!typeTestPassed || (!dbconn.userExists(viewID) && viewID != -1)){
                        try{
                            typeTestPassed = false;
                            System.out.println("Please enter your choice: ");
                            viewID = sc.nextInt();
                            typeTestPassed = true;
                        }
                        catch(InputMismatchException e){
                            System.out.println("You must enter a valid choice");
                            sc.next();
                        }
                    }
                    if(viewID == -1) gs.displayEntireHashMap();
                    else{
                        String firstName = dbconn.getUserFromID(viewID).getFirst();
                        String[] selectedTimetable = timetables.peek(firstName);
                        System.out.println(firstName + ": ");
                        for(String s : selectedTimetable){
                            System.out.println(s);
                        }
                    }
                    break;
            }
            System.out.println("Please enter 0 to go back to the main menu. To exit enter any other number: ");
            exitKey = sc.nextInt();
        }
        dbconn.close();
        System.out.println("Goodbye and Thank You");
    }
}