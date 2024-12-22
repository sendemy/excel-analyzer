package org.example;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import javax.sql.rowset.serial.SerialBlob;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class ScheduleData {
    // Структура: группа -> день недели -> пара -> предмет
    //HashMap<String/*Группа*/, HashMap<String/*День недели*/, HashMap<String/*Пара*/,String/*Предмет*/>>> Groups;
    private final HashMap<String, HashMap<String, HashMap<String, String>>> groups;
    public ScheduleData() {
        this.groups = new HashMap<>();
    }

    public void addSubject(String group, String day, String pair, String subject) {
        groups
                .computeIfAbsent(group, g -> new HashMap<>())
                .computeIfAbsent(day, d -> new HashMap<>())
                .put(pair, subject);
    }

    public HashMap<String, HashMap<String, HashMap<String, String>>> getGroups() {
        return groups;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        groups.forEach((group, days) -> {
            sb.append("Группа: ").append(group).append("\n");
            days.forEach((day, pairs) -> {
                sb.append("  ").append(day).append(":\n");
                pairs.forEach((pair, subject) ->
                        sb.append("    ").append(pair).append(" пара: ").append(subject).append("\n")
                );
            });
        });
        return sb.toString();
    }
    public void merge(ScheduleData other) {
        other.getGroups().forEach((group, days) ->
                days.forEach((day, pairs) ->
                        pairs.forEach((pair, subject) ->
                                this.addSubject(group, day, pair, subject)
                        )
                )
        );
    }
    public Workbook exportToExcelByPairAndDay(HashMap<String, HashMap<String, String>> schedule) throws IOException {

        // Получение всех дней недели и пар
        List<String> daysOfWeek = Arrays.asList("Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота");
        Set<String> pairs = new TreeSet<>(Comparator.comparingInt(Integer::parseInt)); // Сортируем пары по числовому значению

        schedule.forEach((day, daySchedule) -> pairs.addAll(daySchedule.keySet()));

        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("Расписание");

        // Создаём стили для заголовков
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle pairStyle = workbook.createCellStyle();
        pairStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        pairStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        pairStyle.setAlignment(HorizontalAlignment.CENTER);

        // Создаём первую строку (заголовок)
        Row headerRow = sheet.createRow(0);
        Cell pairHeaderCell = headerRow.createCell(0);
        pairHeaderCell.setCellValue("Пары");
        pairHeaderCell.setCellStyle(pairStyle);

        for (int i = 0; i < daysOfWeek.size(); i++) {
            Cell cell = headerRow.createCell(i + 1);
            cell.setCellValue(daysOfWeek.get(i));
            cell.setCellStyle(headerStyle);
        }

        // Заполнение данных
        int rowIndex = 1;
        for (String pair : pairs) {
            Row row = sheet.createRow(rowIndex++);
            Cell pairCell = row.createCell(0);
            pairCell.setCellValue(pair);
            pairCell.setCellStyle(pairStyle);

            for (int i = 0; i < daysOfWeek.size(); i++) {
                String day = daysOfWeek.get(i);
                String subject = schedule.getOrDefault(day, new HashMap<>()).getOrDefault(pair, "—");
                row.createCell(i + 1).setCellValue(subject);
            }
        }

        // Автоматическое выравнивание колонок
        for (int i = 0; i <= daysOfWeek.size(); i++) {
            sheet.autoSizeColumn(i);
        }

        return workbook;
    }


}
