package com.realestate.service;


import com.realestate.EqtApplication;
import com.realestate.domain.EqtRealEstates;
import com.realestate.repos.EqtRealEstatesRepo;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class RealEstateService {

    public InputFile getPictureFromResources(String pictureName) {
        ClassLoader classLoader = EqtApplication.class.getClassLoader();
        InputStream imageStream = classLoader.getResourceAsStream(pictureName);
        return new InputFile(imageStream, pictureName);
    }

    public void setRealEstateWillingnessAtTable(EqtRealEstatesRepo eqtRealEstatesRepo, String oldValue, String newValue) {
        Iterable<EqtRealEstates> realEstates = eqtRealEstatesRepo.findAll();
        for (EqtRealEstates realEstate : realEstates) {
            if (realEstate.getWillingness().contains(oldValue)) {
                realEstate.setWillingness(newValue);
            }
            eqtRealEstatesRepo.save(realEstate);
        }
    }

    public void setPicturesInResourcesAndDb(EqtRealEstatesRepo eqtRealEstatesRepo, int startId, int endId) throws
            IOException {
        for (long id = startId; id <= endId; id++) {
            if (eqtRealEstatesRepo.findById(id).isPresent()) {
                EqtRealEstates realEstate = eqtRealEstatesRepo.findById(id).get();
                String oldPicture1 = realEstate.getPicture1();
                String oldPicture2 = realEstate.getPicture2();
                String oldPicture3 = realEstate.getPicture3();
                String project = realEstate.getProject();
                String size = realEstate.getSize();
                String basicName = project.replaceAll(" ", "").concat(size.replaceAll(" ", "").replaceAll("/", ""));
                String resourcePictureName1 = basicName.concat("1.jpeg");
                String resourcePictureName2 = basicName.concat("2.jpeg");
                String resourcePictureName3 = basicName.concat("3.jpeg");
                URL url1 = null;
                URL url2 = null;
                URL url3 = null;
                try {
                    url1 = new URL(oldPicture1);
                    url2 = new URL(oldPicture2);
                    url3 = new URL(oldPicture3);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                InputStream inputStream1 = Objects.requireNonNull(url1).openStream();
                InputStream inputStream2 = Objects.requireNonNull(url2).openStream();
                InputStream inputStream3 = Objects.requireNonNull(url3).openStream();

                try (FileOutputStream outputStream1 = new FileOutputStream("src/main/resources/".concat(resourcePictureName1))) {
                    byte[] buffer1 = new byte[2048];
                    int length1;
                    while ((length1 = inputStream1.read(buffer1)) != -1) {
                        outputStream1.write(buffer1, 0, length1);
                    }
                }
                try (FileOutputStream outputStream2 = new FileOutputStream("src/main/resources/".concat(resourcePictureName2))) {
                    byte[] buffer2 = new byte[2048];
                    int length2;
                    while ((length2 = inputStream2.read(buffer2)) != -1) {
                        outputStream2.write(buffer2, 0, length2);
                    }
                }
                try (FileOutputStream outputStream3 = new FileOutputStream("src/main/resources/".concat(resourcePictureName3));) {
                    byte[] buffer3 = new byte[2048];
                    int length3;
                    while ((length3 = inputStream3.read(buffer3)) != -1) {
                        outputStream3.write(buffer3, 0, length3);
                    }
                }
                inputStream1.close();
                inputStream2.close();
                inputStream3.close();
                realEstate.setPicture1(resourcePictureName1);
                realEstate.setPicture2(resourcePictureName2);
                realEstate.setPicture3(resourcePictureName3);
                eqtRealEstatesRepo.save(realEstate);
            }
        }
    }

    public List<List<String>> getExcelRealEstateSheetData(String excelFilePath, int rows) {
        List<List<String>> tableValues = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(excelFilePath)) {
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheetAt(0);
            for (int row = 1; row < rows; row++) {
                List<String> rowValues = new ArrayList<>();
                Row currentRow = sheet.getRow(row);
                rowValues.add(currentRow.getCell(0).toString());
                rowValues.add(currentRow.getCell(1).toString());
                rowValues.add(currentRow.getCell(2).toString());
                rowValues.add(currentRow.getCell(3).toString());
                rowValues.add(String.valueOf((long) Double.parseDouble(currentRow.getCell(4).toString())));
                rowValues.add(currentRow.getCell(5).toString());
                rowValues.add(currentRow.getCell(6).toString());
                rowValues.add(currentRow.getCell(7).toString());
                rowValues.add(currentRow.getCell(8).toString());
                rowValues.add(currentRow.getCell(9).toString());
                tableValues.add(rowValues);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tableValues;
    }

    public void addExcelDataToDbTable(EqtRealEstatesRepo eqtRealEstatesRepo) {
        List<List<String>> values = getExcelRealEstateSheetData("D:/Java/Beach Front база EQT.xlsx", 71);
        for (List<String> strings : values) {
            EqtRealEstates eqtRealEstates = new EqtRealEstates();
            eqtRealEstates.setNeighbourhood(strings.get(0));
            eqtRealEstates.setProject(strings.get(1));
            eqtRealEstates.setType(strings.get(2));
            eqtRealEstates.setWillingness(strings.get(3));
            eqtRealEstates.setPrice(strings.get(4));
            eqtRealEstates.setSize(strings.get(5));
            eqtRealEstates.setDescription(strings.get(6));
            eqtRealEstates.setPicture1(strings.get(7));
            eqtRealEstates.setPicture2(strings.get(8));
            eqtRealEstates.setPicture3(strings.get(9));
            eqtRealEstatesRepo.save(eqtRealEstates);
        }
    }

}

