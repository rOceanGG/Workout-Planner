package workoutplannernea;

public class GenericHashMap<K, V> {
    
    private class Entry<K, V>{
        private K key;
        private V value;
        
        public Entry(K k, V v) {
            this.key = k;
            this.value = v;
        }
    }
    
    public GenericHashMap(int size){
      this.SIZE = size;
      table = new Entry[SIZE];
    }
    private int elements = 0;
    private final int SIZE;
    private Entry<K, V> table[];
    

    
    public void add(K key, V value){
        if(isFull() || contains(key)){
            throw new IllegalArgumentException();
        }
        
        Entry<K, V> newEntry = new Entry<>(key, value);
        int index = Math.abs(key.hashCode()) % SIZE;
        if(table[index] == null){
            table[index] = newEntry;
            elements++;
            return;
        }
        else{
            int count = 0;
            int checkingIndex = (index + 1) % SIZE;
            
            while(count < SIZE - 1){
                if(table[checkingIndex] == null){
                    table[checkingIndex] = newEntry;
                    elements++;
                    return;
                }
                checkingIndex = (checkingIndex + 1) % SIZE;
                count++;
            }
        }
    }
    
    public V peek(K key){
        
        if(isEmpty()){
            return null;
        }
        
        int hash = Math.abs(key.hashCode()) % SIZE;
        
        int count = 0;
        while(count < SIZE){
            try{
                if(table[hash].key.equals(key)){
                    return table[hash].value;
                }
                count++;
                hash = (hash + 1) % SIZE;
            }
            catch(NullPointerException e){
                count++;
                hash = (hash + 1) % SIZE;
            }
        }
        
        return null;
    }

    public void alter(K key, V newValue){
        if(isEmpty() || !contains(key)){
            throw new IllegalArgumentException();
        }

        int index = indexInTable(key);
        table[index].value = newValue;
    }
    
    public Entry<K, V> remove(K key){
        if(isEmpty()){
            throw new IllegalArgumentException("IS EMPTY");
        }
        if(!contains(key)){
            throw new IllegalArgumentException("NOT CONTAINED");
        }
        
        int index = key.hashCode() % SIZE;
        int count = 0;
            
        if(table[index].key == key){
            Entry<K, V> e = table[index];
            table[index] = null;
            elements--;
            return e;
        }
        index = index++ % SIZE;
        while(count < SIZE - 1){
            if(table[index].key == key){
                Entry<K, V> e = table[index];
                table[index] = null;
                elements--;
                return e;
            }
            count++;
            index = (index + 1) % SIZE;
        }
        return null;
    }
    
    public boolean contains(K key){
        return peek(key) != null;
    }
    
    public boolean isFull(){
        return elements == SIZE;
    }
    
    public boolean isEmpty(){
        return elements == 0;
    }


    public int indexInTable(K key){
        int currentHash = Math.abs(key.hashCode()) % SIZE;
        int count = 0;
        while(count < SIZE){
            try{
                if(table[currentHash].key == key){
                    return currentHash;
                }
                count++;
                currentHash = (currentHash + 1) % SIZE;
            }
            catch(NullPointerException e){
                count++;
                currentHash = (currentHash + 1) % SIZE;
            }
        }
        return -1;
    }
    
    public int getSize(){
        return SIZE;
    }
    
    
}

