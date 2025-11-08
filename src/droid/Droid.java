package droid;

public abstract class Droid {
    private final String name;
    private final int maxHealth;
    private int health;
    private final int baseDamage;
    private final double accuracy;

    public Droid(String name, int maxHealth, int baseDamage, double accuracy) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.baseDamage = baseDamage;
        this.accuracy = accuracy;
    }

    // === Гетери ===
    public String getName() { return name; }
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public int getBaseDamage() { return baseDamage; }
    public double getAccuracy() { return accuracy; }

    // Тип дроїда (назва класу-нащадка: HealerDroid, WarriorDroid, ...)
    public String getType() {
        return getClass().getSimpleName();
    }

    public boolean isAlive() { return health > 0; }

    public void receiveDamage(int dmg) {
        health -= dmg;
        if (health < 0) health = 0;
    }

    public void heal(int hp) {
        health += hp;
        if (health > maxHealth) health = maxHealth;
    }

    // Базовий урон (підкласи можуть перевизначити і додати рандом/крит тощо)
    public int rollAttackDamage() {
        return getBaseDamage();
    }

    // Базова поведінка: просто атакувати ворога
    public Action decideAction(Context ctx) {
        return Action.attack(ctx.enemy());
    }

    public abstract Droid copy();

    @Override
    public String toString() {
        return "%s [HP=%d/%d, DMG=%d, ACC=%.2f]"
                .formatted(name, health, maxHealth, baseDamage, accuracy);
    }

    // Опис дії дроїда в ході
    public sealed interface Action permits Action.Attack, Action.HealSelf, Action.HealAlly {
        record Attack(Droid target) implements Action {}
        record HealSelf(int amount) implements Action {}
        record HealAlly(Droid target, int amount) implements Action {}

        static Attack attack(Droid target) { return new Attack(target); }
        static HealSelf healSelf(int amount) { return new HealSelf(amount); }
        static HealAlly healAlly(Droid target, int amount) { return new HealAlly(target, amount); }
    }

    // Інформація про бій, яку ми даємо дроїду
    public record Context(Droid self, Droid ally, Droid enemy) {}
}
