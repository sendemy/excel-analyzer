package org.example;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class CustomScheduleParser {

    static class Schedule {
        String groupName;
        Map<String, List<String[]>> weeklySchedule = new LinkedHashMap<>();

        public Schedule(String groupName) {
            this.groupName = groupName;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Group: ").append(groupName).append("\n");
            weeklySchedule.forEach((day, pairs) -> {
                sb.append("  ").append(day).append(":\n");
                for (int i = 0; i < pairs.size(); i++) {
                    String[] pair = pairs.get(i);
                    sb.append("    ").append(i + 1).append(" пара: ").append(pair[0]).append(" / ").append(pair[1]).append("\n");
                }
            });
            return sb.toString();
        }
    }

    public static void main(String[] args) {
        String filePath = "src/main/Schedule_course_2.xls"; // путь к файлу
        try {
            List<Schedule> schedules = parseSchedule(filePath);
            schedules.forEach(System.out::println);
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
        }
    }

    public static List<Schedule> parseSchedule(String filePath) throws IOException {
        List<Schedule> scheduleList = new ArrayList<>();
        String[] daysOfWeek = {"Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота"};

        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new HSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0);

            int colIndex = 2; // Стартовый индекс столбца (C -> индекс 2)
            int groupRowIndex = findGroupRowIndex(sheet); // Ищем строку с первой группой

            while (colIndex < sheet.getRow(0).getLastCellNum()) {
                // Получаем название группы с проверкой на null и тип ячейки
                Row groupRow = sheet.getRow(groupRowIndex);
                if (groupRow == null) {
                    System.err.println("Ошибка: строка с группой пуста!");
                    break;
                }

                Cell groupCell = groupRow.getCell(colIndex);
                if (groupCell == null || groupCell.getCellType() != CellType.STRING) {
                    System.err.println("Ошибка: ячейка с названием группы пуста или имеет неверный тип!");
                    break;
                }

                String groupName = normalizeString(groupCell.getStringCellValue());
                Schedule schedule = new Schedule(groupName);

                int dayIndex = 0;
                int pairRowIndex = groupRowIndex + 2; // Начинаем чтение с первой пары

                // Считываем пары для текущей группы
                while (pairRowIndex < sheet.getLastRowNum()) {
                    if (dayIndex >= daysOfWeek.length) break;

                    List<String[]> dayPairs = new ArrayList<>();
                    for (int i = 0; i < 6; i++) { // 6 пар в день
                        String pair1Col1 = getCellValue(sheet, pairRowIndex, colIndex);
                        String pair1Col2 = getCellValue(sheet, pairRowIndex, colIndex + 1);
                        String pair2Col1 = getCellValue(sheet, pairRowIndex + 1, colIndex);
                        String pair2Col2 = getCellValue(sheet, pairRowIndex + 1, colIndex + 1);

                        // Убираем дубликаты между всеми значениями
                        String pair = combinePairs(pair1Col1, pair1Col2, pair2Col1, pair2Col2);

                        dayPairs.add(new String[]{pair, "—"}); // Только первая пара, вторая пустая
                        pairRowIndex += 2; // Переход к следующей паре
                    }
                    schedule.weeklySchedule.put(daysOfWeek[dayIndex], dayPairs);

                    pairRowIndex++;
                    dayIndex++;
                }

                scheduleList.add(schedule);
                colIndex += 2; // Сдвигаем `colIndex` на два столбца, чтобы перейти к следующей группе
            }
        }

        return scheduleList;
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