package droid;

public class HealerDroid extends Droid {
    public HealerDroid(String name) {
        super(name, 110, 15, 0.75);
    }

    @Override
    public Action decideAction(Context ctx) {
        // Якщо HP менше 40% — лікуємось, інакше атакуємо
        if ((double) getHealth() / getMaxHealth() < 0.4) {
            return Action.healSelf(20);
        }
        // Якщо є союзник і в нього HP < 50% — підлікувати союзника
        if (ctx.ally() != null && (double) ctx.ally().getHealth() / ctx.ally().getMaxHealth() < 0.5) {
            return Action.healAlly(ctx.ally(), 15);
        }
        return Action.attack(ctx.enemy());
    }

    @Override
    public Droid copy() { return new HealerDroid(getName()); }
}
