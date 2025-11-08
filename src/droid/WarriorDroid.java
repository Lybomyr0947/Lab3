package droid;

public class WarriorDroid extends Droid {
    public WarriorDroid(String name) {
        super(name, 120, 20, 0.8); // здоров'я, дамаг, точність
    }

    @Override
    public Droid copy() { return new WarriorDroid(getName()); }
}
