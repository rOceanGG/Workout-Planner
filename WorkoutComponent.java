package workoutplannernea;

public class WorkoutComponent {
    private int ComponentID, WorkoutID, ExerciseID;
    
    public WorkoutComponent(int compID, int wID, int exID){
        ComponentID = compID;
        WorkoutID = wID;
        ExerciseID = exID;
    }
    
    public int getComponentID(){ return ComponentID;}
    
    public int getWorkoutID(){ return WorkoutID;}
    
    public int getExerciseID(){ return ExerciseID;}
    
}
