
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

// Student class with equals() but NO hashCode()
class BrokenStudent {
    String regNo;
    String name;

    BrokenStudent(String regNo, String name) {
        this.regNo = regNo;
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof BrokenStudent)) return false;

        BrokenStudent other = (BrokenStudent) obj;
        return regNo.equals(other.regNo);
    }
}

// Correct Student class
class Student {
    String regNo;
    String name;

    Student(String regNo, String name) {
        this.regNo = regNo;
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Student)) return false;

        Student other = (Student) obj;
        return regNo.equals(other.regNo);
    }

    // TODO 1 completed
    @Override
    public int hashCode() {
        return Objects.hash(regNo);
    }

    @Override
    public String toString() {
        return regNo + " - " + name;
    }
}

public class Main {

    public static void main(String[] args) {

        System.out.println("Q3 - HASHSET DUPLICATE TEST");
        System.out.println("-----------------------------");

        // TEST 1: Add the same object twice
        Set<BrokenStudent> test1 = new HashSet<>();

        BrokenStudent s1 =
            new BrokenStudent("CSA001", "Arun");

        test1.add(s1);
        test1.add(s1);

        System.out.println("TEST 1 - Same object twice");
        System.out.println("Number of students: " + test1.size());

        // TEST 2: Different objects with same registration number
        Set<BrokenStudent> test2 = new HashSet<>();

        test2.add(new BrokenStudent("CSA002", "Bala"));
        test2.add(new BrokenStudent("CSA002", "Bala"));

        System.out.println("\nTEST 2 - Duplicate registration number");
        System.out.println("Number of students: " + test2.size());

        // TEST 3: Correct implementation
        Set<Student> test3 = new HashSet<>();

        test3.add(new Student("CSA003", "Charan"));
        test3.add(new Student("CSA003", "Charan"));

        System.out.println("\nTEST 3 - After implementing hashCode()");
        System.out.println("Number of students: " + test3.size());

        // Test changing registration number after insertion
        Set<Student> test4 = new HashSet<>();

        Student s4 = new Student("CSA004", "Deepak");
        test4.add(s4);

        s4.regNo = "CSA099";

        System.out.println("\nTEST 4 - Change registration after insertion");
        System.out.println("Contains changed student: "
                           + test4.contains(s4));

        System.out.println("\n-----------------------------");
        System.out.println("TESTING COMPLETED");
    }
}
