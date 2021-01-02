package lesson1;

import java.util.ArrayList;
import java.util.Iterator;

public class Box<T extends Fruit> {
    private ArrayList<T> fruits;
    private float weight;

    public Box() {
        this.weight = 0;
        this.fruits = new ArrayList<>();
    }

    public void addFruit(T fruit) {
        fruits.add(fruit);
        weight += fruit.getWeight();
    }

    public boolean compare(Box<?> box) {
        if (Math.abs(this.weight - box.weight)<0.01) {
            return true;
        }
        return false;
    }

    public float weight() {
        return this.weight;
    }

    public void changeBox(Box<T> box) {
        Iterator<T> fruitIterator = fruits.iterator();

        while (fruitIterator.hasNext()) {
            box.addFruit(fruitIterator.next());
            fruitIterator.remove();
        }
    }
}
