
import java.util.*;

class Team {
    private String name;
    private int points;
    private int goalDifference;

    Team(String name, int points, int goalDifference) {
        this.name = name;
        this.points = points;
        this.goalDifference = goalDifference;
    }

    String getName() {
        return name;
    }

    int getPoints() {
        return points;
    }

    int getGoalDifference() {
        return goalDifference;
    }

    @Override
    public String toString() {
        return name + "(" + points + "," + goalDifference + ")";
    }
}

// Generic Leaderboard class
class Leaderboard<T> {

    // TODO 1: Return a sorted copy without modifying original list
    List<T> sortedCopy(List<T> original,
                       Comparator<? super T> comparator) {

        List<T> copy = new ArrayList<>(original);
        copy.sort(comparator);
        return copy;
    }
}

public class Main {

    public static void main(String[] args) {

        // Create teams
        List<Team> teams = new ArrayList<>();

        teams.add(new Team("Cobras", 6, 1));
        teams.add(new Team("Panthers", 9, 4));
        teams.add(new Team("Eagles", 9, 6));
        teams.add(new Team("Bisons", 9, 4));

        System.out.println("Q5 - SPORTS LEADERBOARD");
        System.out.println("------------------------------");

        System.out.println("Original Team List:");
        System.out.println(teams);

        // TODO 2: Create ranking Comparator
        Comparator<Team> rankingComparator =
            Comparator.comparingInt(Team::getPoints)
                .reversed()
                .thenComparing(
                    Comparator.comparingInt(
                        Team::getGoalDifference
                    ).reversed()
                )
                .thenComparing(Team::getName);

        // Create generic leaderboard
        Leaderboard<Team> leaderboard = new Leaderboard<>();

        // Get sorted copy
        List<Team> ranking =
            leaderboard.sortedCopy(teams, rankingComparator);

        System.out.println("\nFINAL TEAM RANKING:");
        for (int i = 0; i < ranking.size(); i++) {
            System.out.println((i + 1) + ". " + ranking.get(i));
        }

        // Verify original list is unchanged
        System.out.println("\nOriginal List After Sorting:");
        System.out.println(teams);

        // Additional test: equal points and goal difference
        System.out.println("\nTIE-BREAKING TEST:");

        List<Team> tieTest = new ArrayList<>();

        tieTest.add(new Team("Zebras", 10, 5));
        tieTest.add(new Team("Ants", 10, 5));
        tieTest.add(new Team("Lions", 10, 7));

        System.out.println(
            leaderboard.sortedCopy(tieTest, rankingComparator)
        );

        System.out.println("\nTESTING COMPLETED");
    }
}
