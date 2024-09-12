package workoutplannernea;

public class LinkedList{
   Element front;
   private int elements = 0;
 
   public boolean isEmpty(){
       return elements == 0;
   }
   
   public LinkedList(){
       
   }
   
   public LinkedList(int[] arr){
       for(int i : arr){
           append(i);
       }    
   }
 
   public void append(int value){
       Element element = new Element();
       element.value = value;
       element.next = null;
 
       if(front==null){
           front = element;
       }
       else{
           Element e = front;
           while(e.next != null){
               e = e.next;
           }
           e.next = element;
           element.previous = e;
       }
       elements++;
    }
 
   public int length(){
       return elements;
   }
 
   public int index(int value){
       int index = -1;
       int currentIndex = 0;
       Element e = front;
       
       while (e != null)
       {
           if (e.value == value)
           {
               index = currentIndex;
               break;
           }
           e = e.next;
           currentIndex++;
       }
       return index;
   }
  
   public void deleteAt(int index){
       if(index >= elements || isEmpty()){
           throw new IllegalArgumentException("Error in DeleteAt");
       }
       else{
           Element element = front;
           for(int i = 0; i < index; i++){
               element = element.next;
           }
           if(index == 0){
               front = element.next;
               element.next.previous = null;
           }
           else{
               if(element.previous != null) element.previous.next = element.next;
               if(element.next !=null) element.next.previous = element.previous;
           }
           elements--;
       }
   }
 
   public boolean search(int value){
       return index(value) != -1;
   }
   
   public int[] asArray(){
       int[] arr = new int[elements];
       Element e = front;
       for(int i = 0; i < elements; i++){
           arr[i] = e.value;
           e = e.next;
       }
       return arr;
   }
}