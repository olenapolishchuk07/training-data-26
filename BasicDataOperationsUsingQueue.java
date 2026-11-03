import java.time.Instant;
import java.util.Queue;
import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayDeque;

/**
 * Клас BasicDataOperationsUsingQueue реалізує роботу з колекціями типу Queue для Instant.
 *
 * <p>Основні функції класу:</p>
 * <ul>
 *   <li>{@link #runDataProcessing()} - Запускає комплекс операцій з даними.</li>
 *   <li>{@link #findInQueue()} - Пошук значення в черзі Instant.</li>
 *   <li>{@link #locateMinMaxInQueue()} - Знаходить граничні значення в черзі.</li>
 *   <li>{@link #performQueueOperations()} - Виконує операції peek і poll з чергою.</li>
 * </ul>
 *
 */
public class BasicDataOperationsUsingQueue {
    private Instant valueToSearch;
    private Queue<Instant> valueQueue;

    /**
     * Конструктор, який iнiцiалiзує об'єкт з готовими даними.
     *
     * @param valueToSearch Значення для пошуку
     * @param valueArray Масив Instant
     */
    BasicDataOperationsUsingQueue(Instant valueToSearch, Instant[] valueArray) {
        this.valueToSearch = valueToSearch;
        this.valueQueue = new ArrayDeque<>(Arrays.asList(valueArray));
    }

    /**
     * Запускає комплексну обробку даних з використанням черги.
     *
     * Метод виконує пошук і визначення граничних значень у черзі Instant,
     * а потім операції peek і poll.
     */
    public void runDataProcessing() {
        findInQueue();
        locateMinMaxInQueue();

        // зберігаємо до файлу відсортовані значення з ArrayDeque (з повторами);
        // порядок обходу черги не є відсортованим, тому сортуємо копію її вмісту
        Instant[] sortedQueueValues = valueQueue.toArray(new Instant[0]);
        Arrays.sort(sortedQueueValues);
        DataFileHandler.writeArrayToFile(sortedQueueValues, BasicDataOperations.SORTED_FILE_PREFIX + ".queue.data");

        // Вилучення виконуємо після пошуку і запису файлу, щоб вони працювали з повним складом черги.
        performQueueOperations();
    }

    /**
     * Здійснює пошук конкретного значення в черзі дати та часу.
     */
    private void findInQueue() {
        // натозву операції будуємо до заміру, щоб склеювання рядка не потрапило у вимір
        String operationName = "пошук елемента '" + valueToSearch + "' в ArrayDeque<Instant>";
        // вимірюємо час пошуку в черзі
        long timeStart = System.nanoTime();

        // ArrayDeque зберігає елементи як бінарну купу в масиві: кожен батьківський елемент
        // не більший за своїх нащадків, але між «сусідніми» гілками порядку немає.
        // Тому бінарний пошук неможливий, і contains() перебирає внутрішній масив від початку
        // й порівнює кожен елемент через equals(), доки не знайде збіг. Це лінійний пошук, O(n).
        boolean elementExists = this.valueQueue.contains(valueToSearch);

        PerformanceTracker.displayOperationTime(timeStart, operationName);

        if (elementExists) {
            System.out.println("Елемент '" + valueToSearch + "' знайдено в Queue");
        } else {
            System.out.println("Елемент '" + valueToSearch + "' відсутній в Queue.");
        }
    }

    /**
     * Визначає найменше та найбільше значення в черзі Instant.
     */
    private void locateMinMaxInQueue() {
        if (valueQueue == null || valueQueue.isEmpty()) {
            System.out.println("Черга є пустою або не ініціалізованою.");
            return;
        }

        // відстежуємо час пошуку граничних значень
        long timeStart = System.nanoTime();

        // Collections.min() і Collections.max() обходять усі елементи черги ітератором
        // і порівнюють їх через compareTo(), тому кожен виклик займає O(n).
        // Найменший елемент купи завжди лежить у її корені, тож його можна було б отримати
        // через peek() за O(1). Найбільший може бути в будь-якому листку купи, тому
        // для максимуму без повного перегляду не обійтися.
        Instant minValue = Collections.min(valueQueue);
        Instant maxValue = Collections.max(valueQueue);

        PerformanceTracker.displayOperationTime(timeStart, "визначення мiнiмальної i максимальної дати в ArrayDeque<Instant>");

        System.out.println("Найменше значення в Queue: " + minValue);
        System.out.println("Найбільше значення в Queue: " + maxValue);
    }

    /**
     * Виконує операції peek і poll з чергою Instant.
     */
    private void performQueueOperations() {
        if (valueQueue == null || valueQueue.isEmpty()) {
            System.out.println("Черга є пустою або не ініціалізованою.");
            return;
        }

        // peek() повертає корінь купи, тобто найменшу дату, не видаляючи її; O(1).
        Instant headElement = valueQueue.peek();
        System.out.println("Головний елемент черги (peek): " + headElement);

        // poll() видаляє корінь: на його місце стає останній елемент купи і «просідає» вниз,
        // доки не відновиться порядок купи; O(log n). Новим коренем стає наступна найменша дата.
        headElement = valueQueue.poll();
        System.out.println("Видалений елемент черги (poll): " + headElement);

        headElement = valueQueue.peek();
        System.out.println("Новий головний елемент черги: " + headElement);
    }
}
