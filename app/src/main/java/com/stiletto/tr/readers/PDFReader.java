package com.stiletto.tr.readers;

import android.util.Log;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.IOException;

/**
 * Created by yana on 24.12.16.
 */

public class PDFReader {

    public static String getPage(String filePath, String  fileName, int pageNumber) {

        try {
            PdfReader reader = new PdfReader(filePath);
            return PdfTextExtractor.getTextFromPage(reader, pageNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileName;

    }

    public static String parseAsText(String filePath) {

        StringBuilder stringBuilder = new StringBuilder();

        try {
            PdfReader reader = new PdfReader(filePath);

            int pages = reader.getNumberOfPages();
            for (int page = 1; page <= pages; page++) {

                stringBuilder.append(PdfTextExtractor.getTextFromPage(reader, page));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }


    public static String parseAsText(String filePath, int pageStart, int pagesCount) {

        StringBuilder stringBuilder = new StringBuilder();

        try {
            PdfReader reader = new PdfReader(filePath);

            int pages = pageStart + pagesCount;
            pages = pages > reader.getNumberOfPages() ? reader.getNumberOfPages() : pages;
            for (int page = pageStart; page <= pages; page++) {

                stringBuilder.append(PdfTextExtractor.getTextFromPage(reader, page));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }
}
