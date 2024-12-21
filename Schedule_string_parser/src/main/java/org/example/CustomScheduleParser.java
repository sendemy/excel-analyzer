package org.example;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class CustomScheduleParser {

    public static void main(String[] args) {
        String directoryPath = "src/main"; // Путь к директории с файлами
        try {
            ScheduleData combinedScheduleData = new ScheduleData();

            // Получаем список всех файлов с именем Schedule_course_*.xls
            File[] scheduleFiles = new File(directoryPath).listFiles((dir, name) -> name.startsWith("Schedule_course_") && name.endsWith(".xls"));

            if (scheduleFiles != null) {
                for (File file : scheduleFiles) {
                    System.out.println("Обрабатывается файл: " + file.getName());
                    ScheduleData scheduleData = parseSchedule(file.getAbsolutePath());
                    combinedScheduleData.merge(scheduleData);
                }
            }

            System.out.println(combinedScheduleData);
        } catch (IOException e) {
            System.err.println("Ошибка чтения файлов: " + e.getMessage());
        }
    }
    public static ScheduleData parseSchedule(String filePath) throws IOException {
        ScheduleData scheduleData = new ScheduleData();
        String[] daysOfWeek = {"Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота"};

        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new HSSFWorkbook(fis)) {

            // Проходим по всем листам в книге
            for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
                Sheet sheet = workbook.getSheetAt(sheetIndex);
                if (sheet == null) continue;

                int colIndex = 2; // Стартовый индекс столбца (C -> индекс 2)
                int groupRowIndex = findGroupRowIndex(sheet); // Ищем строку с первой группой

                while (colIndex < sheet.getRow(0).getLastCellNum()) {
                    // Получаем название группы
                    Row groupRow = sheet.getRow(groupRowIndex);
                    if (groupRow == null) break;

                    Cell groupCell = groupRow.getCell(colIndex);
                    if (groupCell == null || groupCell.getCellType() != CellType.STRING) break;

                    String groupName = normalizeString(groupCell.getStringCellValue());

                    int dayIndex = 0;
                    int pairRowIndex = groupRowIndex + 2;

                    // Считываем пары для текущей группы
                    while (pairRowIndex < sheet.getLastRowNum()) {
                        if (dayIndex >= daysOfWeek.length) break;

                        for (int i = 0; i < 6; i++) { // 6 пар в день
                            String pair1Col1 = getCellValue(sheet, pairRowIndex, colIndex);
                            String pair1Col2 = getCellValue(sheet, pairRowIndex, colIndex + 1);
                            String pair2Col1 = getCellValue(sheet, pairRowIndex + 1, colIndex);
                            String pair2Col2 = getCellValue(sheet, pairRowIndex + 1, colIndex + 1);

                            // Убираем дубликаты между всеми значениями
                            String subject = combinePairs(pair1Col1, pair1Col2, pair2Col1, pair2Col2);
                            if (!subject.equals("—")) {
                                scheduleData.addSubject(groupName, daysOfWeek[dayIndex], String.valueOf(i + 1), subject);
                            }
                            pairRowIndex += 2; // Переход к следующей паре
                        }
                        pairRowIndex++;
                        dayIndex++;
                    }
                    colIndex += 2; // Сдвигаем `colIndex` на два столбца, чтобы перейти к следующей группе
                }
            }
        }
        return scheduleData;
    }

    /**
     * Находим индекс строки с названием первой группы.
     * Считаем 2 непустые строки в первом столбце (C).
     */
    private static int findGroupRowIndex(Sheet sheet) {
        int colIndex = 2; // Столбец C
        int nonEmptyCount = 0;
        for (int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row == null) continue;

            Cell cell = row.getCell(colIndex);
            if (cell != null && cell.getCellType() == CellType.STRING && !cell.getStringCellValue().trim().isEmpty()) {
                nonEmptyCount++;
                if (nonEmptyCount == 2) {
                    return rowIndex; // Возвращаем индекс 2-й непустой строки
                }
            }
        }
        return -1;
    }

    /**
     * Получаем значение ячейки с учётом объединённых диапазонов.
     */
    private static String getCellValue(Sheet sheet, int rowIndex, int colIndex) {
        Row row = sheet.getRow(rowIndex);
        if (row == null) return "—";

        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
            CellRangeAddress mergedRegion = sheet.getMergedRegion(i);
            if (mergedRegion.isInRange(rowIndex, colIndex)) {
                row = sheet.getRow(mergedRegion.getFirstRow());
                colIndex = mergedRegion.getFirstColumn();
                break;
            }
        }

        Cell cell = row.getCell(colIndex);
        return normalizeString((cell != null && cell.getCellType() == CellType.STRING) ? cell.getStringCellValue() : "—");
    }

    /**
     * Убираем повторяющиеся элементы между строками и колонками.
     */
    private static String combinePairs(String... values) {
        Set<String> uniqueParts = new LinkedHashSet<>();
        for (String value : values) {
            if (!value.equals("—")) {
                uniqueParts.addAll(Arrays.asList(value.split("\\s*/\\s*")));
            }
        }
        return uniqueParts.isEmpty() ? "—" : String.join(" / ", uniqueParts);
    }

    /**
     * Нормализуем строку: удаляем лишние пробелы.
     */
    private static String normalizeString(String input) {
        return input.trim().replaceAll("\\s+", " ");
    }
}