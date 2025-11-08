package battle;

import droid.Droid;
import io.FightLog;
import util.Rnd;

public class BattleEngine {
    private final FightLog log = new FightLog();

    public FightLog getLog() { return log; }

    public void fight1v1(Droid A, Droid B) {
        log.header("Бій 1×1: %s vs %s".formatted(A.getName(), B.getName()));
        Droid attacker = Rnd.chance(0.5) ? A : B;
        Droid defender = attacker == A ? B : A;
        log.line("Перший хід: %s", attacker.getName());

        while (A.isAlive() && B.isAlive()) {
            step(attacker, defender, null, null);
            // міняємось ролями
            Droid tmp = attacker; attacker = defender; defender = tmp;
        }
        Droid winner = A.isAlive() ? A : B;
        log.footer("Переможець: %s (HP %d)", winner.getName(), winner.getHealth());
        log.flushToConsole();
    }

    protected void step(Droid atk, Droid def, Droid atkAlly, Droid defAlly) {
        var ctx = new Droid.Context(atk, atkAlly, def);
        var action = atk.decideAction(ctx);

        switch (action) {
            case Droid.Action.Attack a -> {
                if (Rnd.chance(atk.getAccuracy())) {
                    int dmg = atk.rollAttackDamage();
                    a.target().receiveDamage(dmg);
                    log.line("%s влучає в %s на %d (HP %d)",
                            atk.getName(), def.getName(), dmg, def.getHealth());
                } else {
                    log.line("%s промах!", atk.getName());
                }
            }
            case Droid.Action.HealSelf h -> {
                atk.heal(h.amount());
                log.line("%s лікує себе на +%d (HP %d)", atk.getName(), h.amount(), atk.getHealth());
            }
            case Droid.Action.HealAlly h -> {
                if (atkAlly != null) {
                    h.target().heal(h.amount());
                    log.line("%s лікує %s на +%d (HP %d)",
                            atk.getName(), h.target().getName(), h.amount(), h.target().getHealth());
                } else {
                    log.line("%s хотів лікувати союзника, але його немає — атака замість цього (промах)", atk.getName());
                }
            }
        }
    }
}
