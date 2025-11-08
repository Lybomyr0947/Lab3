package droid;

import util.Rnd;

public class SniperDroid extends Droid {
    public SniperDroid(String name) {
        super(name, 90, 30, 0.7);
    }

    @Override
    public int rollAttackDamage() {
        // Шанс криту 20%: *2
        return Rnd.chance(0.2) ? getBaseDamage() * 2 : getBaseDamage();
    }

    @Override
    public Droid copy() { return new SniperDroid(getName()); }
}
