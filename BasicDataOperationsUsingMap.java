import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.LinkedHashMap;

/**
 * Клас BasicDataOperationsUsingMap реалізує операції з колекціями типу Map для зберігання пар ключ-значення.
 *
 * <p>Ключ - record {@link Fern} з характеристиками папороті, значення - ділянка ботанічного саду папороті.</p>
 *
 * <p>Операції, які виконуються для кожного типу Map:</p>
 * <ul>
 *   <li>пошук за ключем;</li>
 *   <li>пошук за значенням (сортування списку пар + binarySearch);</li>
 *   <li>виведення пар ключ-значення на консоль до і після сортування;</li>
 *   <li>сортування за ключами;</li>
 *   <li>визначення найменшого й найбільшого ключа;</li>
 *   <li>додавання пари ключ-значення;</li>
 *   <li>видалення за ключем;</li>
 *   <li>видалення за значенням.</li>
 * </ul>
 */
public class BasicDataOperationsUsingMap {
        private static final Fern KEY_TO_SEARCH_AND_DELETE = new Fern("Адіантум", 35.0);
        private static final Fern KEY_TO_ADD = new Fern("Платицеріум", 55.0);

        private static final String VALUE_TO_SEARCH_AND_DELETE = "Тропічна оранжерея";
        private static final String VALUE_TO_ADD = "Зимовий сад";

    /**
     * Компаратор для сортування Map.Entry за значеннями String (ділянкою ботанічного саду).
     * Використовує природний порядок рядків - String.compareTo().
     */
    private static final Comparator<Map.Entry<Fern, String>> CUSTOM_COMPARATOR = Map.Entry.comparingByValue();

    private HashMap<Fern, String> hashMap;
    private LinkedHashMap<Fern, String> linkedHashMap;
    private boolean linkedHashMapSorted = false;

    /**
     * Record Fern зберігає характеристики папороті: рід (genus) і довжина листка (frondLength).
     *
     * <p>Record автоматично генерує:</p>
     * <ul>
     *   <li>приватні final поля genus і frondLength та канонічний конструктор;</li>
     *   <li>методи доступу genus() і frondLength();</li>
     *   <li>equals() і hashCode(), які враховують усі компоненти record (genus і frondLength).</li>
     * </ul>
     *
     * <p>Вручну реалізовано:</p>
     * <ul>
     *   <li>compareTo() - природний порядок: рід за спаданням, потім довжина листка за зростанням;</li>
     *   <li>toString() - формат Fern{genus='...', frondLength=...}, доповнений хеш-кодом.</li>
     * </ul>
     *
     * @param genus рід папороті, наприклад "Нефролепіс"
     * @param frondLength довжина листка (вайї), см
     */
    public record Fern(String genus, Double frondLength) implements Comparable<Fern> {

        /**
         * Компактний конструктор перевіряє, що характеристики задані.
         * Завдяки цьому в compareTo() не потрібно окремо обробляти значення null.
         */
        public Fern {
            Objects.requireNonNull(genus, "genus не може бути null");
            Objects.requireNonNull(frondLength, "frondLength не може бути null");
        }

        /**
         * Порівнює цю папороть з іншим для визначення природного порядку сортування.
         *
         * <p>Критерій порівняння: рід (genus) за зростанням, а для однакових рідів - довжина листка (frondLength) за спаданням.</p>
         *
         * <p>Метод використовується:</p>
         * <ul>
         *   <li>LinkedHashMap - для автоматичного впорядкування ключів;</li>
         *   <li>Collections.sort() - для сортування списку ключів.</li>
         * </ul>
         *
         * <p>Метод узгоджений з equals(): compareTo() повертає 0 тоді й лише тоді,
         * коли genus і frondLength однакові, тобто коли equals() повертає true.</p>
         *
         * @param other папороть для порівняння
         * @return від'ємне число, якщо цей папороть іде раніше за other; 0, якщо вони рівні;
         *         додатне число, якщо цей папороть іде пізніше за other
         */
        @Override
        public int compareTo(Fern other) {
            // Спочатку порівнюємо за родом (за спаданням - other порівнюється з this)
            int genusComparison = other.genus.compareTo(this.genus);
            if (genusComparison != 0) {
                return genusComparison;
            }
            // Якщо роди однакові, порівнюємо за довжиною листка (за зростанням)
            return this.frondLength.compareTo(other.frondLength);
        }

        /**
         * Повертає рядкове представлення папороті.
         * Хеш-код додано, щоб у логах було видно, від чого залежить порядок елементів у HashMap.
         *
         * @return рядок у форматі Fern{genus='...', frondLength=..., hashCode=...}
         */
        @Override
        public String toString() {
            return "Fern{genus='" + genus + "', frondLength=" + frondLength + ", hashCode=" + hashCode() + "}";
        }
    }

    /**
     * Конструктор, який ініціалізує об'єкт готовими даними.
     *
     * @param hashMap HashMap з початковими даними (ключ: Fern, значення: ділянка ботанічного саду)
     * @param linkedHashMap LinkedHashMap з початковими даними (ключ: Fern, значення: ділянка ботанічного саду)
     */
    BasicDataOperationsUsingMap(HashMap<Fern, String> hashMap, LinkedHashMap<Fern, String> linkedHashMap) {
        this.hashMap = hashMap;
        this.linkedHashMap = linkedHashMap;
    }

    /**
     * Виконує комплекс операцій з HashMap і LinkedHashMap: пошук, виведення, сортування, додавання, видалення.
     */
    public void executeDataOperations() {
        // Спочатку працюємо з HashMap
        System.out.println("========= Операції з HashMap =========");
        System.out.println("Початковий розмір HashMap: " + hashMap.size());

        // Пошук до сортування
        findByKeyInHashMap();
        findByValueInHashMap();
        findMinMaxKeyInHashMap();

        printHashMap();
        sortHashMap();
        printHashMap();

        // Пошук після сортування
        findByKeyInHashMap();
        findByValueInHashMap();
        findMinMaxKeyInHashMap();

        addEntryToHashMap();

        removeByKeyFromHashMap();
        removeByValueFromHashMap();

        System.out.println("Кінцевий розмір HashMap: " + hashMap.size());

        // Потім обробляємо LinkedHashMap
        System.out.println("\n\n========= Операції з LinkedHashMap =========");
        System.out.println("Початковий розмір LinkedHashMap: " + linkedHashMap.size());

        // Пошук до сортування
        findByKeyInLinkedHashMap();
        findByValueInLinkedHashMap();
        findMinMaxKeyInLinkedHashMap();

        printLinkedHashMap();
        sortLinkedHashMap();
        printLinkedHashMap();

        // Пошук після сортування
        findByKeyInLinkedHashMap();
        findByValueInLinkedHashMap();
        findMinMaxKeyInLinkedHashMap();

        addEntryToLinkedHashMap();

        removeByKeyFromLinkedHashMap();
        removeByValueFromLinkedHashMap();

        System.out.println("Кінцевий розмір LinkedHashMap: " + linkedHashMap.size());
    }

    // ===== Методи для HashMap =====

    /**
     * Виводить вміст HashMap.
     * HashMap не гарантує жодного порядку елементів: він залежить від hashCode() ключів.
     */
    private void printHashMap() {
        System.out.println("\n=== Пари ключ-значення в HashMap ===");
        long timeStart = System.nanoTime();

        for (Map.Entry<Fern, String> entry : hashMap.entrySet()) {
            System.out.println("  " + entry.getKey() + " -> " + entry.getValue());
        }

        PerformanceTracker.displayOperationTime(timeStart, "виведення пар ключ-значення в HashMap");
    }

    /**
     * Сортує HashMap за ключами.
     * Використовує Collections.sort() з природним порядком Fern (Fern.compareTo())
     * і перезаписує hashMap новою HashMap, заповненою у відсортованому порядку.
     *
     * <p>Увага: HashMap не зберігає порядок додавання, тому після сортування порядок
     * виведення не зміниться. Зберегти відсортований порядок може LinkedHashMap.</p>
     */
    private void sortHashMap() {
        long timeStart = System.nanoTime();

        // Створюємо список ключів і сортуємо за природним порядком Fern
        List<Fern> sortedKeys = new ArrayList<>(hashMap.keySet());
        Collections.sort(sortedKeys);

        // Створюємо нову HashMap і додаємо пари у порядку відсортованих ключів
        HashMap<Fern, String> sortedHashMap = new HashMap<>();
        for (Fern key : sortedKeys) {
            sortedHashMap.put(key, hashMap.get(key));
        }

        // Перезаписуємо оригінальну hashMap
        hashMap = sortedHashMap;

        PerformanceTracker.displayOperationTime(timeStart, "сортування HashMap за ключами");
    }

    /**
     * Здійснює пошук елемента за ключем в HashMap.
     * Використовує Fern.hashCode() для вибору кошика та Fern.equals() для порівняння ключів.
     */
    void findByKeyInHashMap() {
        long timeStart = System.nanoTime();

        boolean found = hashMap.containsKey(KEY_TO_SEARCH_AND_DELETE);

        PerformanceTracker.displayOperationTime(timeStart, "пошук за ключем в HashMap");

        if (found) {
            String value = hashMap.get(KEY_TO_SEARCH_AND_DELETE);
            System.out.println("Елемент з ключем '" + KEY_TO_SEARCH_AND_DELETE + "' знайдено. Ділянка ботанічного саду: " + value);
        } else {
            System.out.println("Елемент з ключем '" + KEY_TO_SEARCH_AND_DELETE + "' відсутній в HashMap.");
        }
    }

    /**
     * Здійснює пошук елемента за значенням в HashMap.
     * Сортує список Map.Entry за значеннями та використовує бінарний пошук.
     */
    void findByValueInHashMap() {
        long timeStart = System.nanoTime();

        // Створюємо список пар та сортуємо його за значеннями
        List<Map.Entry<Fern, String>> entries = new ArrayList<>(hashMap.entrySet());
        Collections.sort(entries, CUSTOM_COMPARATOR);

        // Тимчасова пара для пошуку: ключ не потрібен, порівнюється лише значення
        Map.Entry<Fern, String> searchEntry = new AbstractMap.SimpleEntry<>(null, VALUE_TO_SEARCH_AND_DELETE);

        int position = Collections.binarySearch(entries, searchEntry, CUSTOM_COMPARATOR);

        PerformanceTracker.displayOperationTime(timeStart, "бінарний пошук за значенням в HashMap");

        if (position >= 0) {
            Map.Entry<Fern, String> foundEntry = entries.get(position);
            System.out.println("Ділянка ботанічного саду '" + VALUE_TO_SEARCH_AND_DELETE + "' знайдено. Папороть: " + foundEntry.getKey());
        } else {
            System.out.println("Ділянка ботанічного саду '" + VALUE_TO_SEARCH_AND_DELETE + "' відсутнє в HashMap.");
        }
    }

    /**
     * Визначає найменший і найбільший ключі в HashMap.
     * HashMap не зберігає порядок ключів, тому потрібен перебір усіх ключів - O(n),
     * і сортування HashMap цього не змінює.
     */
    void findMinMaxKeyInHashMap() {
        if (hashMap.isEmpty()) {
            System.out.println("HashMap порожня: найменшого й найбільшого ключа немає.");
            return;
        }

        long timeStart = System.nanoTime();

        Fern minKey = Collections.min(hashMap.keySet());
        Fern maxKey = Collections.max(hashMap.keySet());

        PerformanceTracker.displayOperationTime(timeStart, "визначення найменшого й найбільшого ключа в HashMap");

        System.out.println("Найменший ключ: " + minKey + ", найбільший ключ: " + maxKey);
    }

    /**
     * Додає новий запис до HashMap.
     */
    void addEntryToHashMap() {
        long timeStart = System.nanoTime();

        hashMap.put(KEY_TO_ADD, VALUE_TO_ADD);

        PerformanceTracker.displayOperationTime(timeStart, "додавання запису до HashMap");

        System.out.println("Додано новий запис: папороть='" + KEY_TO_ADD + "', ділянка ботанічного саду='" + VALUE_TO_ADD + "'");
    }

    /**
     * Видаляє запис з HashMap за ключем.
     */
    void removeByKeyFromHashMap() {
        long timeStart = System.nanoTime();

        String removedValue = hashMap.remove(KEY_TO_SEARCH_AND_DELETE);

        PerformanceTracker.displayOperationTime(timeStart, "видалення за ключем з HashMap");

        if (removedValue != null) {
            System.out.println("Видалено запис з ключем '" + KEY_TO_SEARCH_AND_DELETE + "'. Ділянка ботанічного саду була: " + removedValue);
        } else {
            System.out.println("Ключ '" + KEY_TO_SEARCH_AND_DELETE + "' не знайдено для видалення.");
        }
    }

    /**
     * Видаляє записи з HashMap за значенням.
     */
    void removeByValueFromHashMap() {
        long timeStart = System.nanoTime();

        List<Fern> keysToRemove = new ArrayList<>();
        for (Map.Entry<Fern, String> entry : hashMap.entrySet()) {
            if (VALUE_TO_SEARCH_AND_DELETE.equals(entry.getValue())) {
                keysToRemove.add(entry.getKey());
            }
        }

        for (Fern key : keysToRemove) {
            hashMap.remove(key);
        }

        PerformanceTracker.displayOperationTime(timeStart, "видалення за значенням з HashMap");

        System.out.println("Видалено " + keysToRemove.size() + " записів з ділянкою ботанічного саду '" + VALUE_TO_SEARCH_AND_DELETE + "'");
    }

    // ===== Методи для LinkedHashMap =====

    /**
     * Виводить вміст LinkedHashMap.
     * LinkedHashMap зберігає порядок додавання елементів і використовує hashCode() та equals()
     */
    private void printLinkedHashMap() {
        System.out.println("\n=== Пари ключ-значення в LinkedHashMap ===");
        long timeStart = System.nanoTime();

        for (Map.Entry<Fern, String> entry : linkedHashMap.entrySet()) {
            System.out.println("  " + entry.getKey() + " -> " + entry.getValue());
        }

        PerformanceTracker.displayOperationTime(timeStart, "виведення пар ключ-значення в LinkedHashMap");
    }

    /**
     * Сортує LinkedHashMap за ключами.
     */
    private void sortLinkedHashMap() {
        long timeStart = System.nanoTime();

        List<Fern> sortedKeys = new ArrayList<>(linkedHashMap.keySet());
        Collections.sort(sortedKeys);

        LinkedHashMap<Fern, String> sortedLinkedHashMap = new LinkedHashMap<>();
        for (Fern key : sortedKeys) {
            sortedLinkedHashMap.put(key, linkedHashMap.get(key));
        }

        linkedHashMap = sortedLinkedHashMap;
        linkedHashMapSorted = true;

        PerformanceTracker.displayOperationTime(timeStart, "сортування LinkedHashMap за ключами");
    }

    /**
     * Здійснює пошук елемента за ключем в LinkedHashMap.
     * Використовує Fern.compareTo() для навігації по червоно-чорному дереву.
     */
    void findByKeyInLinkedHashMap() {
        long timeStart = System.nanoTime();

        boolean found = linkedHashMap.containsKey(KEY_TO_SEARCH_AND_DELETE);

        PerformanceTracker.displayOperationTime(timeStart, "пошук за ключем в LinkedHashMap");

        if (found) {
            String value = linkedHashMap.get(KEY_TO_SEARCH_AND_DELETE);
            System.out.println("Елемент з ключем '" + KEY_TO_SEARCH_AND_DELETE + "' знайдено. Ділянка ботанічного саду: " + value);
        } else {
            System.out.println("Елемент з ключем '" + KEY_TO_SEARCH_AND_DELETE + "' відсутній в LinkedHashMap.");
        }
    }

    /**
     * Здійснює пошук елемента за значенням в LinkedHashMap.
     * Сортує список Map.Entry за значеннями та використовує бінарний пошук.
     */
    void findByValueInLinkedHashMap() {
        long timeStart = System.nanoTime();

        List<Map.Entry<Fern, String>> entries = new ArrayList<>(linkedHashMap.entrySet());
        Collections.sort(entries, CUSTOM_COMPARATOR);

        Map.Entry<Fern, String> searchEntry = new AbstractMap.SimpleEntry<>(null, VALUE_TO_SEARCH_AND_DELETE);

        int position = Collections.binarySearch(entries, searchEntry, CUSTOM_COMPARATOR);

        PerformanceTracker.displayOperationTime(timeStart, "бінарний пошук за значенням в LinkedHashMap");

        if (position >= 0) {
            Map.Entry<Fern, String> foundEntry = entries.get(position);
            System.out.println("Ділянка ботанічного саду '" + VALUE_TO_SEARCH_AND_DELETE + "' знайдено. Папороть: " + foundEntry.getKey());
        } else {
            System.out.println("Ділянка ботанічного саду '" + VALUE_TO_SEARCH_AND_DELETE + "' відсутнє в LinkedHashMap.");
        }
    }

    /**
     * Визначає найменший і найбільший ключі в LinkedHashMap.
     * LinkedHashMap реалізує SequencedMap (Java 21) і впорядкована за Fern.compareTo(),
     * тому перший і останній записи - це найменший і найбільший ключі; перебір не потрібен.
     */
    void findMinMaxKeyInLinkedHashMap() {
        if (linkedHashMap.isEmpty()) {
            System.out.println("LinkedHashMap порожня: найменшого й найбільшого ключа немає.");
            return;
        }

        long timeStart = System.nanoTime();

        Fern minKey;
        Fern maxKey;
        if (linkedHashMapSorted) {
            // Порядок додавання збігається з порядком ключів - перебір не потрібен
            minKey = linkedHashMap.firstEntry().getKey();
            maxKey = linkedHashMap.lastEntry().getKey();
        } else {
            // Порядок додавання не пов'язаний з compareTo() - потрібен перебір
            minKey = Collections.min(linkedHashMap.keySet());
            maxKey = Collections.max(linkedHashMap.keySet());
        }

        PerformanceTracker.displayOperationTime(timeStart, "визначення найменшого й найбільшого ключа в LinkedHashMap");

        System.out.println("Найменший ключ: " + minKey + ", найбільший ключ: " + maxKey);
    }

    /**
     * Додає новий запис до LinkedHashMap.
     */
    void addEntryToLinkedHashMap() {
        long timeStart = System.nanoTime();

        linkedHashMap.put(KEY_TO_ADD, VALUE_TO_ADD);
        linkedHashMapSorted = false;

        PerformanceTracker.displayOperationTime(timeStart, "додавання запису до LinkedHashMap");

        System.out.println("Додано новий запис: папороть='" + KEY_TO_ADD + "', ділянка ботанічного саду='" + VALUE_TO_ADD + "'");
    }

    /**
     * Видаляє запис з LinkedHashMap за ключем.
     */
    void removeByKeyFromLinkedHashMap() {
        long timeStart = System.nanoTime();

        String removedValue = linkedHashMap.remove(KEY_TO_SEARCH_AND_DELETE);

        PerformanceTracker.displayOperationTime(timeStart, "видалення за ключем з LinkedHashMap");

        if (removedValue != null) {
            System.out.println("Видалено запис з ключем '" + KEY_TO_SEARCH_AND_DELETE + "'. Ділянка ботанічного саду була: " + removedValue);
        } else {
            System.out.println("Ключ '" + KEY_TO_SEARCH_AND_DELETE + "' не знайдено для видалення.");
        }
    }

    /**
     * Видаляє записи з LinkedHashMap за значенням.
     */
    void removeByValueFromLinkedHashMap() {
        long timeStart = System.nanoTime();

        List<Fern> keysToRemove = new ArrayList<>();
        for (Map.Entry<Fern, String> entry : linkedHashMap.entrySet()) {
            if (VALUE_TO_SEARCH_AND_DELETE.equals(entry.getValue())) {
                keysToRemove.add(entry.getKey());
            }
        }

        for (Fern key : keysToRemove) {
            linkedHashMap.remove(key);
        }

        PerformanceTracker.displayOperationTime(timeStart, "видалення за значенням з LinkedHashMap");

        System.out.println("Видалено " + keysToRemove.size() + " записів з ділянкою ботанічного саду '" + VALUE_TO_SEARCH_AND_DELETE + "'");
    }

    /**
     * Головний метод для запуску програми.
     */
    public static void main(String[] args) {
        // Початкові дані (ключ: Fern, значення: ділянка ботанічного саду).
        // Map.of() створює незмінну Map, тому з неї створюються копії, які можна змінювати.
        Map<Fern, String> ferns = Map.of(
            new Fern("Нефролепіс", 60.5), "Тропічна оранжерея",
            new Fern("Адіантум", 35.0), "Зимовий сад",
            new Fern("Птерис", 45.2), "Тінистий сад",
            new Fern("Нефролепіс", 72.8), "Пальмова оранжерея",
            new Fern("Асплініум", 80.1), "Тропічна оранжерея",
            new Fern("Щитник", 95.4), "Лісова ділянка",
            new Fern("Адіантум", 28.6), "Японський сад",
            new Fern("Страусник", 120.0), "Водний сад",
            new Fern("Орляк", 110.3), "Альпінарій",
            new Fern("Птерис", 38.7), "Субтропічна оранжерея"
        );

        HashMap<Fern, String> hashMap = new HashMap<>(ferns);
        LinkedHashMap<Fern, String> linkedHashMap = new LinkedHashMap<>(ferns);

        // Створюємо об'єкт і виконуємо операції
        BasicDataOperationsUsingMap operations = new BasicDataOperationsUsingMap(hashMap, linkedHashMap);
        operations.executeDataOperations();
    }
}
