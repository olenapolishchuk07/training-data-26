import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.TreeSet;
import java.util.Set;

/**
 * Клас BasicDataOperationsUsingSet реалізує операції з множиною HashSet для Instant.
 *
 * <p>Методи класу:</p>
 * <ul>
 *   <li>{@link #executeDataAnalysis()} - Запускає аналіз даних.</li>
 *   <li>{@link #findInSet()} - Пошук значення в множині Instant.</li>
 *   <li>{@link #locateMinMaxInSet()} - Знаходить мінімальне і максимальне значення в множині.</li>
 *   <li>{@link #analyzeDuplicates()} - Порівнює кількість вхідних значень і елементів множини.</li>
 * </ul>
 */
public class BasicDataOperationsUsingSet {
    Instant valueToSearch;
    int sourceValueCount;
    Set<Instant> valueSet = new TreeSet<>();

    /**
     * Конструктор, який iнiцiалiзує об'єкт з готовими даними.
     *
     * @param valueToSearch Значення для пошуку
     * @param valueArray Масив Instant
     */
    BasicDataOperationsUsingSet(Instant valueToSearch, Instant[] valueArray) {
        this.valueToSearch = valueToSearch;
        this.sourceValueCount = valueArray.length;
        this.valueSet = new TreeSet<>(Arrays.asList(valueArray));
    }

    /**
     * Запускає комплексний аналіз даних з використанням множини HashSet.
     *
     * Метод виконує пошук, визначення граничних значень і аналіз повторів у множині Instant.
     */
    public void executeDataAnalysis() {
        findInSet();
        locateMinMaxInSet();
        analyzeDuplicates();

        // зберігаємо до файлу відсортовані унікальні значення з HashSet (без повторів)
        Instant[] sortedSetValues = valueSet.toArray(new Instant[0]);
        Arrays.sort(sortedSetValues);
        DataFileHandler.writeArrayToFile(sortedSetValues, BasicDataOperations.SORTED_FILE_PREFIX + ".set.data");
    }

    /**
     * Здійснює пошук конкретного значення в множині дати та часу.
     */
    private void findInSet() {
        // назву операції будуємо до заміру, щоб склеювання рядка не потрапило у вимір
        String operationName = "пошук елемента '" + valueToSearch + "' в TreeSet<Instant>";
        long timeStart = System.nanoTime();

        // HashSet не перебирає елементи. contains() обчислює hashCode() шуканої дати,
        // за ним одразу визначає «кошик» (bucket) внутрішньої хеш-таблиці і через equals()
        // порівнює лише ті кілька елементів, що потрапили в цей кошик.
        // У середньому це O(1) незалежно від кількості елементів.
        boolean elementExists = this.valueSet.contains(valueToSearch);

        PerformanceTracker.displayOperationTime(timeStart, operationName);

        if (elementExists) {
            System.out.println("Елемент '" + valueToSearch + "' знайдено в TreeSet");
        } else {
            System.out.println("Елемент '" + valueToSearch + "' відсутній в TreeSet.");
        }
    }

    /**
     * Визначає найменше та найбільше значення в множині Instant.
     */
    private void locateMinMaxInSet() {
        if (valueSet == null || valueSet.isEmpty()) {
            System.out.println("TreeSet є пустим або не ініціалізованим.");
            return;
        }

        long timeStart = System.nanoTime();

        // HashSet розкладає елементи за хеш-кодами, а не за порядком дат, тому найменша
        // і найбільша дати можуть лежати будь-де. Collections.min() і Collections.max()
        // обходять усю множину ітератором і порівнюють елементи через compareTo(); кожен виклик O(n).
        Instant minValue = Collections.min(valueSet);
        Instant maxValue = Collections.max(valueSet);

        PerformanceTracker.displayOperationTime(timeStart, "визначення мiнiмальної i максимальної дати в TreeSet<Instant>");

        System.out.println("Найменше значення в TreeSet: " + minValue);
        System.out.println("Найбільше значення в TreeSet: " + maxValue);
    }

    /**
     * Порівнює кількість вхідних значень і елементів множини: різниця — це відкинуті повтори.
     */
    private void analyzeDuplicates() {
        System.out.println("Кiлькiсть вхiдних значень: " + sourceValueCount);
        System.out.println("Кiлькiсть елементiв в TreeSet: " + valueSet.size());
        System.out.println("Повторних входжень, вiдкинутих TreeSet: " + (sourceValueCount - valueSet.size()));
    }
}
