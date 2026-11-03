import java.time.Instant;
import java.util.Vector;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Клас BasicDataOperationsUsingList реалізує операції з колекціями типу Vector для даних Instant.
 * 
 * <p>Методи класу:</p>
 * <ul>
 *   <li>{@link #executeDataOperations()} - Виконує комплекс операцій з даними.</li>
 *   <li>{@link #performArraySorting()} - Упорядковує масив елементів Instant.</li>
 *   <li>{@link #findInArray()} - Здійснює пошук елемента в масиві Instant.</li>
 *   <li>{@link #locateMinMaxInArray()} - Визначає найменше і найбільше значення в масиві.</li>
 *   <li>{@link #sortList()} - Сортує колекцію List з Instant.</li>
 *   <li>{@link #findInList()} - Пошук конкретного значення в списку.</li>
 *   <li>{@link #locateMinMaxInList()} - Пошук мінімального і максимального значення в списку.</li>
 * </ul>
 */
public class BasicDataOperationsUsingList {
    private boolean listSorted = false;
    private boolean arraySorted = false;
    private Instant valueToSearch;
    private Instant[] valueArray;
    private List<Instant> valueList;

    /**
     * Конструктор, який iнiцiалiзує об'єкт з готовими даними.
     * 
     * @param valueToSearch Значення для пошуку
     * @param valueArray Масив Instant
     */
    BasicDataOperationsUsingList(Instant valueToSearch, Instant[] valueArray) {
        this.valueToSearch = valueToSearch;
        this.valueArray = valueArray.clone();
        this.valueList = new Vector<>(Arrays.asList(valueArray));
    }
    
    /**
     * Виконує комплексні операції з структурами даних.
     * 
     * Метод завантажує масив і список об'єктів Instant, 
     * здійснює сортування та пошукові операції.
     */
    public void executeDataOperations(Instant lower, Instant upper) {
        // спочатку працюємо з колекцією List
        System.out.println("List: до сортування");
        analyzeCondition(valueList, listSorted, lower, upper);
        findInList();
        locateMinMaxInList();
        
        sortList();
        System.out.println("List: після сортування");
        analyzeCondition(valueList, listSorted, lower, upper);
        
        findInList();
        locateMinMaxInList();

        // потім обробляємо масив дати та часу
        System.out.println("Масив: до сортування");
        analyzeCondition(valueArray, arraySorted, lower, upper);
        findInArray();
        locateMinMaxInArray();

        performArraySorting();
        System.out.println("Масив: після сортування");
        analyzeCondition(valueArray, arraySorted, lower, upper);
        
        findInArray();
        locateMinMaxInArray();

        // зберігаємо до окремого файлу відсортований вміст Vector
        DataFileHandler.writeArrayToFile(valueList.toArray(new Instant[0]), BasicDataOperations.SORTED_FILE_PREFIX + ".list.data");
    }

    /**
     * Упорядковує масив об'єктів Instant за зростанням.
     * Фіксує та виводить тривалість операції сортування в наносекундах.
     */
    void performArraySorting() {
        long timeStart = System.nanoTime();

        Arrays.sort(valueArray);
        arraySorted = true;

        PerformanceTracker.displayOperationTime(timeStart, "сортування масиву Instant[]");
    }

    /**
     * Здійснює пошук конкретного значення в масиві дати та часу.
     */
    void findInArray() {
        // назву операції будуємо до заміру, щоб склеювання рядка не потрапило у вимір
        String operationName = "пошук елемента '" + valueToSearch + "' в масивi Instant[], " + (arraySorted ? "після сортування" : "до сортування");
        long timeStart = System.nanoTime();

        int position = -1;
        if (arraySorted) {
            position = Arrays.binarySearch(valueArray, valueToSearch);
        } else {
            for (int i = 0; i < valueArray.length; i++) {
                if (valueArray[i].compareTo(valueToSearch) == 0) {
                    position = i;
                    break;
                }
            }
        }

        PerformanceTracker.displayOperationTime(timeStart, operationName);

        if (position >= 0) {
            System.out.println("Елемент '" + valueToSearch + "' знайдено в масивi за позицією: " + position);
        } else {
            System.out.println("Елемент '" + valueToSearch + "' відсутній в масиві.");
        }
    }

    /**
     * Визначає найменше та найбільше значення в масиві дати та часу.
     */
    void locateMinMaxInArray() {
        if (valueArray == null || valueArray.length == 0) {
            System.out.println("Масив є пустим або не ініціалізованим.");
            return;
        }

        // назву операції будуємо до заміру, щоб склеювання рядка не потрапило у вимір
        String operationName = "визначення мiнiмальної i максимальної дати в масивi Instant[], " + (arraySorted ? "після сортування" : "до сортування");
        long timeStart = System.nanoTime();

        Instant minValue = valueArray[0];
        Instant maxValue = valueArray[0];

        if (arraySorted) {
            // у відсортованому масиві граничні значення лежать на його кінцях
            maxValue = valueArray[valueArray.length - 1];
        } else {
            for (Instant currentValue : valueArray) {
                if (currentValue.compareTo(minValue) < 0) {
                    minValue = currentValue;
                }
                if (currentValue.compareTo(maxValue) > 0) {
                    maxValue = currentValue;
                }
            }
        }

        PerformanceTracker.displayOperationTime(timeStart, operationName);

        System.out.println("Найменше значення в масивi: " + minValue);
        System.out.println("Найбільше значення в масивi: " + maxValue);
    }

    /**
     * Шукає конкретне значення дати та часу в колекції Vector.
     */
    void findInList() {
        // назву операції будуємо до заміру, щоб склеювання рядка не потрапило у вимір
        String operationName = "пошук елемента '" + valueToSearch + "' в Vector<Instant>, " + (listSorted ? "після сортування" : "до сортування");
        long timeStart = System.nanoTime();

        int position = -1;
        if (listSorted) {
            position = Collections.binarySearch(valueList, valueToSearch);
        } else {
            int index = 0;
            for (Instant value : valueList) {
                if (value.compareTo(valueToSearch) == 0) {
                    position = index;
                    break;
                }
                index++;
            }
        }

        PerformanceTracker.displayOperationTime(timeStart, operationName);

        if (position >= 0) {
            System.out.println("Елемент '" + valueToSearch + "' знайдено в Vector за позицією: " + position);
        } else {
            System.out.println("Елемент '" + valueToSearch + "' відсутній в Vector.");
        }
    }

    /**
     * Визначає найменше і найбільше значення в колекції Vector з датами.
     */
    void locateMinMaxInList() {
        if (valueList == null || valueList.isEmpty()) {
            System.out.println("Колекція Vector є пустою або не ініціалізованою.");
            return;
        }

        // назву операції будуємо до заміру, щоб склеювання рядка не потрапило у вимір
        String operationName = "визначення мiнiмальної i максимальної дати в Vector<Instant>, " + (listSorted ? "після сортування" : "до сортування");
        long timeStart = System.nanoTime();

        Instant minValue;
        Instant maxValue;

        if (listSorted) {
            // у відсортованому списку граничні значення лежать на його кінцях
            minValue = valueList.get(0);
            maxValue = valueList.get(valueList.size() - 1);
        } else {
            minValue = Collections.min(valueList);
            maxValue = Collections.max(valueList);
        }

        PerformanceTracker.displayOperationTime(timeStart, operationName);

        System.out.println("Найменше значення в List: " + minValue);
        System.out.println("Найбільше значення в List: " + maxValue);
    }

    /**
     * Упорядковує колекцію List з об'єктами Instant за зростанням.
     * Відстежує та виводить час виконання операції сортування.
     */
    void sortList() {
        long timeStart = System.nanoTime();

        Collections.sort(valueList);
        listSorted = true;

        PerformanceTracker.displayOperationTime(timeStart, "сортування Vector<Instant>");
    }

// BEGIN CONDITION
    /** вибірка даних; sorted=true лише для даних, упорядкованих за зростанням. */
    public int analyzeCondition(Instant[] values, boolean sorted, Instant lower, Instant upper) {
        if (lower.isAfter(upper)) throw new IllegalArgumentException("Нижня межа перевищує верхню");
        String conditionDescription = "значення в інтервалі [" + lower + "; " + upper + "] включно";
        String phase = sorted ? "після сортування" : "до сортування";
        System.out.println("Масив: вибірка даних " + phase + ". Умова: " + conditionDescription);
        // назву операції будуємо до заміру, щоб склеювання рядка не потрапило у вимір
        String operationName = "вибірка даних: масив Instant[], " + phase + ", умова: " + conditionDescription;
        long timeStart = System.nanoTime();
        Instant[] matches = new Instant[values.length];
        int count = 0;
        int inspected = 0;
        if (!sorted) {
            // Невпорядковані дані: перевіряємо кожне входження.
            for (Instant value : values) {
                inspected++;
                if (value.compareTo(lower) >= 0 && value.compareTo(upper) <= 0) {
                    matches[count++] = value;
                }
            }
        } else {
            // Впорядковані дані: ідемо від початку і зупиняємось, коли значення перевищить upper.
            for (int i = 0; i < values.length; i++) {
                Instant value = values[i];
                inspected++;
                if (value.compareTo(upper) > 0) {
                    break;
                }
                if (value.compareTo(lower) >= 0) {
                    matches[count++] = value;
                }
            }
        }
        PerformanceTracker.displayOperationTime(timeStart, operationName);
        // Друк результатів не входить у виміряний час пошуку.
        System.out.println("Переглянуто елементів: " + inspected);
        for (int i = 0; i < count; i++) System.out.println(matches[i]);
        System.out.println("У масиві знайдено " + count + " входжень (повтори враховано). Умова: " + conditionDescription + ".");
        return count;
    }

    /** вибірка даних; sorted=true лише для даних, упорядкованих за зростанням. */
    public int analyzeCondition(List<Instant> values, boolean sorted, Instant lower, Instant upper) {
        if (lower.isAfter(upper)) throw new IllegalArgumentException("Нижня межа перевищує верхню");
        String conditionDescription = "значення в інтервалі [" + lower + "; " + upper + "] включно";
        String phase = sorted ? "після сортування" : "до сортування";
        System.out.println("List: вибірка даних " + phase + ". Умова: " + conditionDescription);
        // назву операції будуємо до заміру, щоб склеювання рядка не потрапило у вимір
        String operationName = "вибірка даних: Vector<Instant>, " + phase + ", умова: " + conditionDescription;
        long timeStart = System.nanoTime();
        Instant[] matches = new Instant[values.size()];
        int count = 0;
        int inspected = 0;
        if (!sorted) {
            // Невпорядковані дані: перевіряємо кожне входження.
            for (Instant value : values) {
                inspected++;
                if (value.compareTo(lower) >= 0 && value.compareTo(upper) <= 0) {
                    matches[count++] = value;
                }
            }
        } else {
            // Впорядковані дані: обхід через ListIterator від початку до межі upper.
            java.util.ListIterator<Instant> iterator = values.listIterator();
            while (iterator.hasNext()) {
                Instant value = iterator.next();
                inspected++;
                if (value.compareTo(upper) > 0) {
                    break;
                }
                if (value.compareTo(lower) >= 0) {
                    matches[count++] = value;
                }
            }
        }
        PerformanceTracker.displayOperationTime(timeStart, operationName);
        // Друк результатів не входить у виміряний час пошуку.
        System.out.println("Переглянуто елементів: " + inspected);
        for (int i = 0; i < count; i++) System.out.println(matches[i]);
        System.out.println("У List знайдено " + count + " входжень (повтори враховано). Умова: " + conditionDescription + ".");
        return count;
    }
    // END CONDITION
}
