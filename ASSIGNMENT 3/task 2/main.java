
import java.io.IOException;

// Parent custom checked exception
class ExamException extends Exception {
    ExamException(String message) {
        super(message);
    }

    ExamException(String message, Throwable cause) {
        super(message, cause);
    }
}

// Custom exceptions for portal failures
class ExamClosedException extends ExamException {
    ExamClosedException(String message) {
        super(message);
    }
}

class StudentNotRegisteredException extends ExamException {
    StudentNotRegisteredException(String message) {
        super(message);
    }
}

class FileTooLargeException extends ExamException {
    FileTooLargeException(String message) {
        super(message);
    }
}

class SubmissionStorageException extends ExamException {
    SubmissionStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}

// Storage layer
class Storage {
    void save(String fileName, long sizeKb, boolean diskFailure)
            throws SubmissionStorageException {

        try {
            if (diskFailure) {
                throw new IOException("Disk quota exceeded");
            }

            System.out.println("Saved " + fileName);

        } catch (IOException e) {
            throw new SubmissionStorageException(
                "Could not store " + fileName, e
            );
        }
    }
}

// Service layer
class ExamService {
    Storage storage = new Storage();

    void submit(String fileName, long sizeKb,
                boolean examOpen, boolean registered,
                boolean diskFailure) throws ExamException {

        if (!examOpen) {
            throw new ExamClosedException(
                "The exam window is closed."
            );
        }

        if (!registered) {
            throw new StudentNotRegisteredException(
                "Student is not registered for this course."
            );
        }

        if (sizeKb > 500) {
            throw new FileTooLargeException(
                "Answer file exceeds the 500 KB limit."
            );
        }

        storage.save(fileName, sizeKb, diskFailure);
    }
}

// Controller layer
class ExamController {
    ExamService service = new ExamService();

    void handleSubmission(String fileName, long sizeKb,
                          boolean examOpen, boolean registered,
                          boolean diskFailure) {

        try {
            service.submit(fileName, sizeKb,
                           examOpen, registered, diskFailure);

        } catch (ExamException e) {
            System.out.println("Error: " + e.getMessage());

            if (e.getCause() != null) {
                System.out.println(
                    "Cause: " + e.getCause().getMessage()
                );
            }
        }
    }
}

// Main class
public class Main {
    public static void main(String[] args) {

        ExamController controller = new ExamController();

        System.out.println("TEST 1: Exam window closed");
        controller.handleSubmission(
            "answer01.pdf", 100, false, true, false
        );

        System.out.println("\nTEST 2: Student not registered");
        controller.handleSubmission(
            "answer02.pdf", 100, true, false, false
        );

        System.out.println("\nTEST 3: File exceeds size limit");
        controller.handleSubmission(
            "answer03.pdf", 800, true, true, false
        );

        System.out.println("\nTEST 4: Storage failure");
        controller.handleSubmission(
            "answer04.pdf", 100, true, true, true
        );
    }
}
