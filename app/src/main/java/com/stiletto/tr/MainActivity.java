package com.stiletto.tr;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.stiletto.tr.readers.EPUBReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.TOCReference;
import nl.siegmann.epublib.epub.EpubReader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.activity_main);
//
//        TextView textView = (TextView) findViewById(R.id.text_view);
//        textView.setMovementMethod(LinkMovementMethod.getInstance());
//        textView.setCustomSelectionActionModeCallback(new StyleCallback(textView));
//

//        File file = new File("/storage/emulated/0/Download/451_za_Farenheitom.pdf");
//        try {
//            String text = PDFReader.parseAsText(file.getPath(), 1, 10);
//            textView.setText(addClickablePart(text), TextView.BufferType.SPANNABLE);
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.d("IPDF", "ERROR");
//        }

//        File epubFile = new File("/storage/emulated/0/Download/The Picture of Dorian Gray by Oscar Wilde.epub");
//
//        try {
//            InputStream epubInputStream = new FileInputStream(epubFile);
//            Book book = (new EpubReader()).readEpub(epubInputStream);
//            textView.setText(book.getTitle());
//            String text = EPUBReader.readContent(book.getTableOfContents().getTocReferences(), 0);
//            Spanned spanned = null;
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//                spanned = Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY);
//            }else {
//                spanned = Html.fromHtml(text);
//            }
//            textView.setText(spanned);
////            textView.setText(text);
////            textView.setText(addClickablePart(text), TextView.BufferType.SPANNABLE);
//        } catch (IOException e) {
//            Log.e("epublib", e.getMessage());
//        }

    }


    private SpannableStringBuilder addClickablePart(String str) {
        SpannableStringBuilder builder = new SpannableStringBuilder(str);

        String[] arr = str.split(" ");

        int currentIndex = 0;
        for (final String text : arr) {

            int indexStart = str.indexOf(text, currentIndex);
            int indexEnd = indexStart + text.length();
            currentIndex = indexEnd + 1;

            Log.d("CLICK_STR", "str: " + text + "[" + indexStart + ":" + indexEnd + "]");
            builder.setSpan(new InteractiveSpan() {

                @Override
                public void onClick(View widget) {

                    Log.d("CLICK_STR", "onClick: " + text);
                    Toast.makeText(MainActivity.this, text,
                            Toast.LENGTH_SHORT).show();
                }

            }, indexStart, indexEnd, 0);
        }

        return builder;
    }


    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        int count = 0;
        try {
            while ((line = reader.readLine()) != null && count <= 100) {
                sb.append(line).append("\n");
                Log.d("CLICK_STR", count + ". line: " + line);
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

    public static String getStringFromFile(File file) {
        FileInputStream fileInputStream = null;
        String text = "";
        try {
            fileInputStream = new FileInputStream(file);
            text = convertStreamToString(fileInputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return text;
    }

}
