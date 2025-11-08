package battle;

import droid.Droid;
import io.FightLog;
import util.Rnd;

import java.util.ArrayList;
import java.util.List;

public class TeamBattleEngine extends BattleEngine {

    // Використовуємо лог батьківського класу через getLog()


    @Override
    public FightLog getLog() {
        // просто повертаємо лог батька
        return super.getLog();
    }

    public void fightTeams(List<Droid> A, List<Droid> B) {
        // шапка в лог
        getLog().header("Бій Команда×Команда: A(%d) vs B(%d)".formatted(A.size(), B.size()));

        int round = 1;

        while (alive(A) && alive(B)) {
            getLog().line("— Раунд %d —", round++);

            // Хід команди A
            stepTeam(A, B);
            if (!alive(B)) break;

            // Хід команди B
            stepTeam(B, A);
        }

        String winner = alive(A) ? "Команда A" : "Команда B";
        getLog().footer("Перемогла %s", winner);

        // друкуємо весь лог (і раунди, і удари, і лікування)
        getLog().flushToConsole();
    }

    private boolean alive(List<Droid> team) {
        return team.stream().anyMatch(Droid::isAlive);
    }

    private Droid pickRandomAlive(List<Droid> team) {
        List<Droid> alive = new ArrayList<>();
        for (Droid d : team) {
            if (d.isAlive()) {
                alive.add(d);
            }
        }
        if (alive.isEmpty()) return null;
        return alive.get(Rnd.nextInt(alive.size()));
    }

    private void stepTeam(List<Droid> atkTeam, List<Droid> defTeam) {
        Droid attacker = pickRandomAlive(atkTeam);
        Droid defender = pickRandomAlive(defTeam);
        Droid ally     = pickRandomAlive(atkTeam);

        if (attacker == null || defender == null) return;

        if (ally == attacker) {
            ally = pickRandomAlive(atkTeam);
        }

        // Ключовий момент:
        // super.step() зробить всю атаку/хіл і запише це в той самий FightLog,
        // який ми потім виводимо в fightTeams().
        super.step(attacker, defender, ally, pickRandomAlive(defTeam));
    }
}
