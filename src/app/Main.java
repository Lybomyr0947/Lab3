package app;

import battle.BattleEngine;
import battle.TeamBattleEngine;
import droid.*;
import io.FightIO;
import util.Inputs;

import java.util.*;

public class Main {
    // Список усіх створених дроїдів
    static final List<Droid> roster = new ArrayList<>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            printMenu();
            int choice = Inputs.readInt(sc, "Ваш вибір: ");
            switch (choice) {
                case 1 -> createDroid(sc);
                case 2 -> showRoster();
                case 3 -> fightOneVsOne(sc);
                case 4 -> teamVsTeam(sc);
                case 5 -> saveLastFight(sc);
                case 6 -> replayFight(sc);
                case 7 -> {
                    System.out.println("До зустрічі! ");
                    return;
                }
                default -> System.out.println("Невірний пункт меню.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n==== МЕНЮ: БИТВА ДРОЇДІВ ====");
        System.out.println("1) Створити дроїда");
        System.out.println("2) Показати список створених дроїдів");
        System.out.println("3) Бій 1 на 1");
        System.out.println("4) Бій команда на команду");
        System.out.println("5) Записати останній бій у файл");
        System.out.println("6) Відтворити бій зі збереженого файлу");
        System.out.println("7) Вихід");
    }

    private static void createDroid(Scanner sc) {
        System.out.println("Оберіть тип дроїда:");
        System.out.println(" 1) Warrior (збалансований)");
        System.out.println(" 2) Sniper (великий дамаг, менше здоров'я)");
        System.out.println(" 3) Healer (лікує команду)");
        int type = Inputs.readInt(sc, "> ");
        System.out.print("Введіть ім'я дроїда: ");
        String name = sc.next();

        Droid d;
        switch (type) {
            case 1 -> d = new WarriorDroid(name);
            case 2 -> d = new SniperDroid(name);
            case 3 -> d = new HealerDroid(name);
            default -> {
                System.out.println("Невідомий тип. Створюю Warrior за замовчуванням.");
                d = new WarriorDroid(name);
            }
        }
        roster.add(d);
        System.out.println("Створено: " + d);
    }

    private static void showRoster() {
        if (roster.isEmpty()) {
            System.out.println("Список порожній. Спочатку створіть дроїдів.");
            return;
        }
        System.out.println("\n*** СТВОРЕНІ ДРОЇДИ ***");
        for (int i = 0; i < roster.size(); i++) {
            Droid d = roster.get(i);
            System.out.printf("%d) [%s] %s\n", i, d.getType(), d);
        }
    }


    private static void fightOneVsOne(Scanner sc) {
        if (roster.size() < 2) {
            System.out.println("Потрібно мінімум 2 дроїди.");
            return;
        }
        showRoster();
        int a = Inputs.readIndex(sc, "Оберіть індекс 1-го дроїда: ", roster.size());
        int b = Inputs.readIndex(sc, "Оберіть індекс 2-го дроїда: ", roster.size());
        if (a == b) {
            System.out.println("Потрібні різні дроїди.");
            return;
        }

        // Клонуємо, щоб не псувати оригінали у реєстрі
        Droid d1 = roster.get(a).copy();
        Droid d2 = roster.get(b).copy();

        BattleEngine engine = new BattleEngine();
        engine.fight1v1(d1, d2);

        // зберігаємо останній бій для можливості записати/відтворити
        FightIO.setLastLog(engine.getLog());
    }

    private static void teamVsTeam(Scanner sc) {
        if (roster.size() < 4) {
            System.out.println("Потрібно мінімум 4 дроїди, щоб зібрати 2 команди.");
            return;
        }
        showRoster();
        int n = Inputs.readInt(sc, "Скільки дроїдів у команді? ");
        if (n < 1) {
            System.out.println("Розмір команди має бути >= 1.");
            return;
        }
        List<Droid> teamA = pickTeam(sc, n, "A");
        List<Droid> teamB = pickTeam(sc, n, "B");
        if (teamA.size() != n || teamB.size() != n) {
            System.out.println("Команди зібрані некоректно.");
            return;
        }

        // Клонуємо склад команд для чесного бою
        List<Droid> A = teamA.stream().map(Droid::copy).toList();
        List<Droid> B = teamB.stream().map(Droid::copy).toList();

        TeamBattleEngine engine = new TeamBattleEngine();
        engine.fightTeams(A, B);

        FightIO.setLastLog(engine.getLog());
    }

    private static List<Droid> pickTeam(Scanner sc, int n, String label) {
        List<Droid> team = new ArrayList<>();
        System.out.println("\nФормуємо команду " + label + ": виберіть " + n + " індекс(и)");
        for (int i = 0; i < n; i++) {
            int idx = Inputs.readIndex(sc, "Індекс дроїда #" + (i + 1) + ": ", roster.size());
            team.add(roster.get(idx));
        }
        return team;
    }

    private static void saveLastFight(Scanner sc) {
        if (FightIO.getLastLog().isEmpty()) {
            System.out.println("Немає останнього бою для збереження.");
            return;
        }
        System.out.print("Введіть ім'я файлу (без розширення): ");
        String name = sc.next();
        String path = FightIO.saveLastLogToFile(name);
        System.out.println("Збережено: " + path);
    }

    private static void replayFight(Scanner sc) {
        System.out.print("Введіть ім'я файлу для відтворення (без .txt): ");
        String name = sc.next();
        FightIO.replayFromFile(name);
    }
}
