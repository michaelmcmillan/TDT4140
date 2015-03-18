package models;

/**
 * Created by lassedrevland on 18.03.15.
 */
public class Room {
    private int id;
    private String name;
    private int capacity;

    public Room(int id, String name,int capacity) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public String toString() {
        return name + " (" + capacity + ")";
    }
}
