package ecommerce.scenarios.registration;

import ecommerce.base.EnvironmentProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AccountExcelReader {

    private static final Logger LOGGER = LogManager.getLogger(AccountExcelReader.class);
    public static final DataFormatter DATE_FORMATTER = new DataFormatter();

    public static AccountSpreadsheet getSpreadsheet() {
        String filename = EnvironmentProperties.getInstance().getRegistrationExcelFileLocation();
        XSSFWorkbook workbook;
        AccountSpreadsheet spreadsheet = new AccountSpreadsheet();
        try {
            workbook = new XSSFWorkbook(new File(filename));
            XSSFSheet userSheet = workbook.getSheetAt(0);
            Map<Integer, RegisteredUser> userMap = getUsers(userSheet);
            XSSFSheet testCaseSheet = workbook.getSheetAt(1);
            Map<Integer, RegistrationCase> testCaseMap = getTestcases(testCaseSheet);
            XSSFSheet loginTestCaseSheet = workbook.getSheetAt(2);
            Map<Integer, LoginCase> loginTestCaseMap = getLoginTestCases(loginTestCaseSheet);
            spreadsheet.setRegisteredUserMap(userMap);
            spreadsheet.setTestCase(testCaseMap);
            spreadsheet.setLoginTestCases(loginTestCaseMap);
        } catch (IOException e) {
            LOGGER.error("IO Exception when reading Registration Spreadsheet");
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            LOGGER.error("InvalidFormatException when reading Registration Spreadsheet");
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        }
        return spreadsheet;
    }

    private static Map<Integer,
            RegistrationCase> getTestcases(XSSFSheet sheet) {
        Map<Integer, RegistrationCase> map = new HashMap<>();
        Iterator<Row> rowIterator = sheet.iterator();
        if (!rowIterator.hasNext()) {
            LOGGER.warn("The excel sheet for registration test cases has no data");
            return map;
        }
        int rowNumber = 2;
        rowIterator.next();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            RegistrationCase testCase = getTestCase(row);
            map.put(testCase.getTestCaseId(), testCase);
            rowNumber++;
        }
        return map;
    }

    private static Map<Integer, RegisteredUser> getUsers(XSSFSheet sheet) {
        Map<Integer, RegisteredUser> map = new HashMap<>();
        Iterator<Row> rowIterator = sheet.iterator();
        if (!rowIterator.hasNext()) {
            LOGGER.warn("The excel sheet for holding registration data has no rows");
            return map;
        }
        rowIterator.next(); // first row is a header row
        int rowNumber = 2;
        String emailSuffix = EnvironmentProperties.getInstance().getEmailSuffix();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            RegisteredUser info = getRegisteredUser(row, emailSuffix);
            map.put(info.getId(), info);
            rowNumber++;
        }
        return map;
    }

    private static RegistrationCase getTestCase(Row row) {
        RegistrationCase testCase = new RegistrationCase();
        testCase.setTestCaseId(getIntegerValue(row.getCell(0), "test case id"));
        testCase.setUserId(getIntegerValue(row.getCell(1), "user id"));

        String successfulText = getStringValue(row.getCell(2));
        if (successfulText.equalsIgnoreCase("yes")) {
            testCase.setSuccessful(true);
        } else if (successfulText.equalsIgnoreCase("no")) {
            testCase.setSuccessful(false);
        } else {
            LOGGER.warn("Test case id " + testCase.getTestCaseId() + " has invalid success text " +
                    "\"" + successfulText + "\"");
        }

        String includesText = getStringValue(row.getCell(3));
        if (includesText.equalsIgnoreCase("yes")) {
            testCase.setIncludes(true);
        } else if (includesText.equalsIgnoreCase("no")) {
            testCase.setIncludes(false);
        } else {
            LOGGER.warn("Test case id " + testCase.getTestCaseId() + " has invalid includes text " +
                    "\"" + includesText + "\"");
        }

        testCase.setExplanation(getStringValue(row.getCell(4)));
        return testCase;
    }

    private static RegisteredUser getRegisteredUser(Row row, String emailSuffix) {
        RegisteredUser info = new RegisteredUser();
        info.setId(getIntegerValue(row.getCell(0), "id"));
        info.setName(getStringValue(row.getCell(1)));
        info.setEmail(getStringValue(row.getCell(2)) + emailSuffix);
        info.setPassword(getStringValue(row.getCell(3)));
        info.setDay(getIntegerValue(row.getCell(4), "day"));
        info.setMonth(getStringValue(row.getCell(5)));
        info.setYear(getIntegerValue(row.getCell(6), "year"));
        info.setFirstName(getStringValue(row.getCell(7)));
        info.setLastName(getStringValue(row.getCell(8)));
        info.setCompany(getStringValue(row.getCell(9)));
        info.setAddress1(getStringValue(row.getCell(10)));
        info.setAddress2(getStringValue(row.getCell(11)));
        info.setCountry(getStringValue(row.getCell(12)));
        info.setState(getStringValue(row.getCell(13)));
        info.setCity(getStringValue(row.getCell(14)));
        info.setZipCode(getStringValue(row.getCell(15)));
        info.setMobileNumber(getStringValue(row.getCell(16)));
        return info;
    }

    private static Map<Integer, LoginCase> getLoginTestCases(XSSFSheet sheet) {
        Map<Integer, LoginCase> map = new HashMap<>();
        Iterator<Row> rowIterator = sheet.iterator();
        if (!rowIterator.hasNext()) {
            LOGGER.warn("The excel sheet for login test cases has no rows");
            return map;
        }
        rowIterator.next();
        String emailSuffix = EnvironmentProperties.getInstance().getEmailSuffix();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            LoginCase testCase = getLoginTestcase(row, emailSuffix);
            map.put(testCase.getId(), testCase);
        }
        return map;
    }

    private static LoginCase getLoginTestcase(Row row, String emailSuffix) {
        LoginCase testCase = new LoginCase();
        testCase.setId(getIntegerValue(row.getCell(0), "test case id"));
        testCase.setEmail(getStringValue(row.getCell(1)) + emailSuffix);
        testCase.setPassword(getStringValue(row.getCell(2)));
        String successfulText = getStringValue(row.getCell(3));
        if (successfulText.equalsIgnoreCase("yes")) {
            testCase.setSuccessful(true);
        } else if (successfulText.equalsIgnoreCase("no")) {
            testCase.setSuccessful(false);
        } else {
            LOGGER.warn("Test case id " + testCase.getId() + " has invalid success text " +
                    "\"" + successfulText + "\"");
        }

        String includesText = getStringValue(row.getCell(4));
        if (includesText.equalsIgnoreCase("yes")) {
            testCase.setIncludes(true);
        } else if (includesText.equalsIgnoreCase("no")) {
            testCase.setIncludes(false);
        } else {
            LOGGER.warn("Test case id " + testCase.getId() + " has invalid includes text " +
                    "\"" + includesText + "\"");
        }

        testCase.setComments(getStringValue(row.getCell(5)));
        return testCase;
    }

    private static String getStringValue(Cell cell) {
        String s = DATE_FORMATTER.formatCellValue(cell);
        return s == null ? "" : s;
    }

    private static Integer getIntegerValue(Cell cell, String cellType) {
        String s = DATE_FORMATTER.formatCellValue(cell);
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            LOGGER.warn("Expecting an integer from cell " + cellType +
                    " with value \"" + s + "\"");
            return -1;
        }
    }


}
