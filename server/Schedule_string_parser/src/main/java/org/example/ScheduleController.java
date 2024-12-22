package org.example;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

@RestController
@RequestMapping("/generate")
public class ScheduleController {

    private final ScheduleData scheduleData;

    public ScheduleController() {
        this.scheduleData = new ScheduleData();
        String directoryPath = "src/main/resources"; // Путь к директории с файлами

        try {
            // Получаем список всех файлов с именем Schedule_course_*.xls
            File[] scheduleFiles = new File(directoryPath).listFiles((dir, name) -> name.startsWith("Schedule_course_") && name.endsWith(".xls"));

            if (scheduleFiles != null) {
                for (File file : scheduleFiles) {
                    System.out.println("Обрабатывается файл: " + file.getName());
                    ScheduleData parsedData = CustomScheduleParser.parseSchedule(file.getAbsolutePath());
                    this.scheduleData.merge(parsedData); // Объединяем данные
                }
            }

            System.out.println("Все данные успешно загружены.");
        } catch (IOException e) {
            System.err.println("Ошибка чтения файлов: " + e.getMessage());
        }
    }

    @GetMapping(path = "/{group}", produces = "application/octet-stream")
    public ResponseEntity<byte[]> getScheduleTable(@PathVariable("group") String group) {
        if (!scheduleData.getGroups().containsKey(group)) {
            return ResponseEntity.notFound().build();
        }

        HashMap<String, HashMap<String, String>> groupSchedule = scheduleData.getGroups().get(group);

        try {
            // Генерируем Excel-файл
            byte[] excelFileBytes = generateExcelFile(groupSchedule);

            if (excelFileBytes == null || excelFileBytes.length == 0) {
                throw new RuntimeException("Ошибка при создании Excel-файла.");
            }

            // Возвращаем байты Excel-файла
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"schedule.xls\"")
                    .contentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM)
                    .body(excelFileBytes);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private byte[] generateExcelFile(HashMap<String, HashMap<String, String>> schedule) throws IOException {
        try (Workbook workbook = scheduleData.exportToExcelByPairAndDay(schedule);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }
}
