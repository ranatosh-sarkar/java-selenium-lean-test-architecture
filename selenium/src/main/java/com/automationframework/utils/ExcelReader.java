package com.automationframework.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.*;

public class ExcelReader {

    public static List<Map<String, String>> readSheet(String classpathXlsx, String sheetName) {
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(classpathXlsx);
             Workbook wb = new XSSFWorkbook(Objects.requireNonNull(is))) {

            Sheet sheet = wb.getSheet(sheetName);
            Iterator<Row> rows = sheet.iterator();
            if (!rows.hasNext()) return Collections.emptyList();

            // header
            Row header = rows.next();
            List<String> cols = new ArrayList<>();
            header.forEach(c -> cols.add(c.getStringCellValue().trim()));

            // data
            List<Map<String, String>> data = new ArrayList<>();
            while (rows.hasNext()) {
                Row r = rows.next();
                Map<String, String> row = new LinkedHashMap<>();
                for (int i = 0; i < cols.size(); i++) {
                    Cell cell = r.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    row.put(cols.get(i), cellToString(cell));
                }
                data.add(row);
            }
            return data;
        } catch (Exception e) {
            LoggerUtil.error("Excel read error: " + classpathXlsx + " / " + sheetName, e);
            return Collections.emptyList();
        }
    }

    private static String cellToString(Cell c) {
        switch (c.getCellType()) {
            case STRING: return c.getStringCellValue();
            case NUMERIC: return DateUtil.isCellDateFormatted(c) ? c.getDateCellValue().toString() : String.valueOf(c.getNumericCellValue());
            case BOOLEAN: return String.valueOf(c.getBooleanCellValue());
            case FORMULA: return c.getCellFormula();
            default: return "";
        }
    }
}
