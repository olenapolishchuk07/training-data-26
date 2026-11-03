import java.time.Instant;

/** Координатор пошуку точного значення та значень у заданому інтервалі. */
public class BasicDataOperations {
    static final String PATH_TO_DATA_FILE = "list/Instant.data";
    static final String SORTED_FILE_PREFIX = "list/Instant.sorted";

    public static void main(String[] args) {
        if (args.length != 3 && args.length != 4) {
            System.err.println("Використання: java BasicDataOperations <значення> <нижня-межа> <верхня-межа> [файл-даних]");
            System.err.println("Приклад: java BasicDataOperations 2026-12-08T17:49:47Z 2026-06-01T00:00:00Z 2026-08-31T23:59:59Z");
            System.exit(1);
            return;
        }
        Instant searched;
        Instant lower;
        Instant upper;
        try {
            searched = Instant.parse(args[0]);
            lower = Instant.parse(args[1]);
            upper = Instant.parse(args[2]);
            if (lower.isAfter(upper)) {
                throw new IllegalArgumentException("Нижня межа перевищує верхню");
            }
        } catch (RuntimeException exception) {
            System.err.println("Некоректні аргументи: " + exception.getMessage());
            System.exit(1);
            return;
        }

        String file = args.length == 4 ? args[3] : PATH_TO_DATA_FILE;
        try {
            Instant[] values = DataFileHandler.loadArrayFromFile(file);
            System.out.println("Точне значення для пошуку: " + searched);
            System.out.println("Завантажено значень: " + values.length);
            System.out.println("LIST");
            new BasicDataOperationsUsingList(searched, values).executeDataOperations(lower, upper);
            System.out.println("SET");
            new BasicDataOperationsUsingSet(searched, values).executeDataAnalysis();
            System.out.println("QUEUE");
            new BasicDataOperationsUsingQueue(searched, values).runDataProcessing();
        } catch (Exception exception) {
            System.err.println("Помилка обробки даних: " + exception.getMessage());
            System.exit(1);
        }
    }
}
