package com.smsreading.android;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.smsreading.android.commanInterface.IStaticDatabase;
import com.smsreading.android.database.DatabaseHelper;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import dto.ExcelDatadto;

/**
 * Created by root on 7/9/16.
 */

public class DatabaseActivity extends Activity implements IStaticDatabase {


    private TextView setTextT;
    private Button openfilebtn;
    private FileInputStream inStream;
    private InputStream inputstram;
    private Button showfilebtn;
    private TextView showexecelData;
    private Button storeindatabase;
    private ArrayList<ExcelDatadto> arrayList;
    private ArrayList<ExcelDatadto> arrayListValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set data
        //XlsxCon controller = new XlsxCon(this);
        Workbook wb = new HSSFWorkbook();

        setContentView(R.layout.database_layout);
        setTextT = (TextView) findViewById(R.id.textview1);
        showexecelData = (TextView) findViewById(R.id.textview2);
        openfilebtn = (Button) findViewById(R.id.button1);
        showfilebtn = (Button) findViewById(R.id.button2);
        storeindatabase = (Button) findViewById(R.id.button3);
        Stetho.initializeWithDefaults(this);
        setData();
        getData();

        openfilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openfile();
            }
        });
        showfilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CopyReadAssets();
            }
        });
        storeindatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                putExcelDataInDB();
            }
        });
    }

    private void putExcelDataInDB() {
        try {
            arrayList = new ArrayList<>();
            arrayListValue = new ArrayList<>();
            inputstram = getAssets().open("Test.xls");
            POIFSFileSystem myFileSystem = new POIFSFileSystem(inputstram);
            // Create a workbook using the File System
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);

            HSSFSheet mySheet = myWorkBook.getSheetAt(0);
            Row row = mySheet.getRow(0);
            // int noOfColumns = sh.getRow(0).getPhysicalNumberOfCells();
            //or
            int noOfColumns = mySheet.getRow(0).getLastCellNum();
            //assume first row is column header row
            for (int col = 0; col < noOfColumns; col++) {
                ExcelDatadto dto = new ExcelDatadto();
                Cell cell = row.getCell(col);
                dto.setColumnName(cell.getStringCellValue());
                //String columnName = cell.getStringCellValue();
                arrayList.add(dto);
                //System.out.println("<field_name> "+columnName +"</field_name>");
            }

            if (arrayList.size() > 0) {
                DatabaseHelper.onCreateCustom(this, arrayList, noOfColumns);
            }
            /** We now need something to iterate through the cells. **/
            Iterator<Row> rowIter = mySheet.rowIterator();
            StringBuilder sb = new StringBuilder();
            while (rowIter.hasNext()) {
                ContentValues cv = null;
                HSSFRow myRow = (HSSFRow) rowIter.next();
                int rowNumber = myRow.getRowNum();
                if (rowNumber != 0) {
                    Iterator cellIter = myRow.iterator();
                    while (cellIter.hasNext()) {
                        HSSFCell myCell = (HSSFCell) cellIter.next();
                        ExcelDatadto dto = new ExcelDatadto();
                        dto.setColumnValue(myCell.toString());
                        Log.i("qqqqqqqqq", "value qqqq    " + myCell.toString());
                        arrayListValue.add(dto);
                    }


                }

                int loop= arrayListValue.size()/4;
                int k=0;
                for (int ii = 0; ii < loop; ii++) {
                    ContentValues contentValues   = new ContentValues();
                    for (int i = 0; i < arrayList.size(); i++,k++) {
                        ExcelDatadto dto1 = arrayList.get(i);
                        ExcelDatadto dto2 = arrayListValue.get(k);
                        contentValues.put(dto1.getColumnName(), dto2.getColumnValue());
                        Log.i("qqqqqqqqq", "value  "  + dto1.getColumnName() + "" + dto2.getColumnValue());
                    }

                    if (contentValues != null) {
                            long value = DatabaseHelper.getinstance(this).insertData("excel_table", null, contentValues);
                            Log.i("qqqqqqqqq", "value " + value);

                        } else
                            Log.i("qqqqqqqqq", "unable to added contenprovider ");
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openfile() {
        //  wv.loadUrl("file:///android_asset/" + mFileName + ".html");   // now it will not fail here

        AssetManager assetManager = getAssets();


        /// File file =new File("file:///android_asset/Test.xls");
        try {
            inputstram = assetManager.open("Test.xls");
            getAssets().open("Test.xls").toString();
            // Log.v("qqqqqqqqqqqqq", getAssets().open("Test.xls").toString());
            //inStream = new FileInputStream(file.getPath());
            // inStream.read();
            // inStream.close();
//            byte[] buffer = new byte[inputstram.available()];
//            //read the text file as a stream, into the buffer
//            inputstram.read(buffer);
//            //create a output stream to write the buffer into
//            ByteArrayOutputStream oS = new ByteArrayOutputStream();
//            //write this buffer to the output stream
//            oS.write(buffer);
//            //Close the Input and Output streams
//            oS.close();
//            inputstram.close();

            // Create a POIFSFileSystem object
            POIFSFileSystem myFileSystem = new POIFSFileSystem(inputstram);

            // Create a workbook using the File System
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);

            // Get the first sheet from workbook
            HSSFSheet mySheet = myWorkBook.getSheetAt(0);

            /** We now need something to iterate through the cells. **/
            Iterator<Row> rowIter = mySheet.rowIterator();
            StringBuilder sb = new StringBuilder();
            while (rowIter.hasNext()) {
                HSSFRow myRow = (HSSFRow) rowIter.next();
                Iterator<Cell> cellIter = myRow.cellIterator();
                while (cellIter.hasNext()) {
                    HSSFCell myCell = (HSSFCell) cellIter.next();
//                    Log.e("FileUtils", "Cell Value: " + myCell.toString()+ " Index :" +myCell.getColumnIndex());
                    // Toast.makeText(getApplicationContext(), "cell Value: " +
                    // myCell.toString(), Toast.LENGTH_SHORT).show();
                    //  sb.append("Cell Value: " + myCell.toString()+ " Index :" +myCell.getColumnIndex());
                    sb.append(myCell.toString() + "  ");
                }
                //sb.append(System.getProperty("line.separator"));
                sb.append("\n");
            }
            showexecelData.setText(sb.toString() + "");

            //return the output stream as a String
            //  setTextT.setText(oS.toString()+""); ;


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void CopyReadAssets() {
        AssetManager assetManager = getAssets();

        InputStream in = null;
        OutputStream out = null;
        File file = new File(getFilesDir(), "Test.xls");
        try {
            in = assetManager.open("Test.xls");
            out = openFileOutput(file.getName(), Context.MODE_WORLD_READABLE);

            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(
                Uri.parse("file://" + getFilesDir() + "/Test.xls"),
                "application/vnd.ms-excel");

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Log.i("unable", "unable to handle activity ");
            Toast.makeText(this, "Unable to open excel file ", Toast.LENGTH_SHORT);
        }


    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    private void getData() {
        String name = DatabaseHelper.getinstance(this).getData(IStaticDatabase.SQL_SELECT_QUERY);
        if (name != null) {
            setTextT.setText(name + "");
        } else
            setTextT.setText("No data found" + "");
    }

    private void setData() {

        ContentValues cv = new ContentValues();
        cv.put(IStaticDatabase.COLUMN_NAME_NAME, "Test 123 promax");

        long value = DatabaseHelper.getinstance(this).insertData(IStaticDatabase.TABLE_NAME, null, cv);
        // Log.v("value =", value + "");
    }
}
