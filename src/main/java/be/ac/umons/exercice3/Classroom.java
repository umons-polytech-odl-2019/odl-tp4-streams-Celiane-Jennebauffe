package be.ac.umons.exercice3;

import be.ac.umons.exercice2.Student;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Classroom {
    private Set<Student> students = new HashSet<>();

    public void addStudent(Student student) {
        Objects.requireNonNull(student, "student may not be null");

        if (students.contains(student)) {
            throw new DuplicateStudentException(student.getRegistrationNumber());
        } else {
            students.add(student);
        }

    }

    public double averageScore() {

        /*double sum = 0;
        int cpt = 0;
        for (Student student : students) {
            for (Map.Entry<String, Integer> courses : student.getScoreByCourse().entrySet()) {
                sum += courses.getValue();
                cpt++;
            }
        }
        return (sum / cpt);*/
        return students.stream()
                .flatMapToInt(Student->Student.getScoreByCourse().values().stream().mapToInt(Integer::intValue))
                .average()
                .orElse(0.0);
           //flatMap associe un student avec le résultat d'un flux (applatir)
           // on fait coorespondre un etudiant a quelque chose
        // le qqlch est une map  dont je n'extrais que les valeurs
        //je crée un nv stream sur ces valeurs + transf en entiers
        // le résultat de  ligne 35 est une collection d'entiers sur lequel je fais une moyenne
        //orElse au cas ou je n'aurai pas d'éléments sur lequel calculer (sans ça je renvoie un null qui n'est pas le type demandé)

        //besoin d'un flux d'entiers car on veut utiliser average
    }

    public int countStudents() {
        return students.size();
    }

    public List<Student> topScorers(String course, int n) {

        Map<Student, OptionalInt> scoreByStudent = students.stream()
            .collect(Collectors.toMap(Function.identity(), student -> student.getScore(course)));
        return scoreByStudent.entrySet().stream()
            .filter(entry -> entry.getValue().isPresent())
            .sorted(Map.Entry.comparingByValue((o1, o2) -> -Integer.compare(o1.getAsInt(), o2.getAsInt())))
            .limit(n)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }

    public List<Student> successfulStudents() {

        /*Set<Student> studentSet = new TreeSet<>(
                Comparator.comparingDouble(student -> -student.averageScore()));
        for (Student s : students) {
            if (s.isSuccessful()) {
                studentSet.add(s);

        }

        List<Student> studentList = new ArrayList<>();
        for (Student s : studentSet)
            studentList.add(s);
        return studentList; */
        return students.stream()
                .filter(Student::isSuccessful) //isSuccessfull méthode de Student (renvoie true or false)
                .sorted(Comparator.comparingDouble(student->-student.averageScore()))
                .collect(Collectors.toList());
        //on récupère les étudiants qui ont réussi
        //on trie les notes par ordre décroissant (signe moins) des étudiants (en utiliant un lambda qui fait corresppndre l'étudiant à sa moyenne annuelle)
        //on renvoit une list car type demandé
    }
}
