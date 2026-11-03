import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;

/**
 * Клас DataFileHandler управляє роботою з файлами даних Instant.
 */
public class DataFileHandler {
    /**
     * Завантажує масив об'єктів Instant з файлу.
     * 
     * @param filePath Шлях до файлу з даними.
     * @return Масив об'єктів Instant.
     */
    public static Instant[] loadArrayFromFile(String filePath) {
        Instant[] temporaryArray = new Instant[1000];
        int currentIndex = 0;

        try (BufferedReader fileReader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = fileReader.readLine()) != null) {
                // Видаляємо можливі невидимі символи та BOM
                currentLine = currentLine.trim().replaceAll("^\\uFEFF", "");
                if (!currentLine.isEmpty()) {
                    Instant parsedValue = Instant.parse(currentLine);
                    temporaryArray[currentIndex++] = parsedValue;
                }
            }
        } catch (IOException ioException) {
            throw new java.io.UncheckedIOException("Не вдалося прочитати " + filePath, ioException);
        }

        Instant[] resultArray = new Instant[currentIndex];
        System.arraycopy(temporaryArray, 0, resultArray, 0, currentIndex);

        return resultArray;
    }

    /**
     * Зберігає масив об'єктів Instant у файл.
     * 
     * @param valueArray Масив об'єктів Instant.
     * @param filePath Шлях до файлу для збереження.
     */
    public static void writeArrayToFile(Instant[] valueArray, String filePath) {
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(filePath))) {
            for (Instant valueElement : valueArray) {
                fileWriter.write(valueElement.toString());
                fileWriter.newLine();
            }
        } catch (IOException ioException) {
            throw new java.io.UncheckedIOException("Не вдалося записати " + filePath, ioException);
        }
    }
}
