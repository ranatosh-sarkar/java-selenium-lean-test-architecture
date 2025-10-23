package com.automationframework.utils;

import org.testng.annotations.DataProvider;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DataProviderUtil {

    @DataProvider(name = "excelLoginData")
    public static Iterator<Object[]> excelLoginData() {
        List<Map<String, String>> rows =
                ExcelReader.readSheet("testdata/LoginData.xlsx", "Sheet1");
        return rows.stream()
                .map(m -> new Object[]{ m.get("username"), m.get("password") })
                .iterator();
    }

    @DataProvider(name = "jsonHomeData")
    public static Iterator<Object[]> jsonHomeData() {
        List<Map<String, Object>> rows =
                JsonReader.readAsListOfMaps("testdata/HomeData.json");
        return rows.stream()
                .map(m -> new Object[]{ m })
                .iterator();
    }
}
