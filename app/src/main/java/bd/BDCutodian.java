package bd;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.nio.file.Files;

import root.ivatio.App;

public class BDCutodian {
    public static void saveDB(Context context, String filename) {
//        File db = context.getDatabasePath(App.getDBName());
//        File out = new File(filename);
    }

    public static void loadDB(Context context, String filename) {
        try{
            File iFile = new File(filename);
            BufferedInputStream input = new BufferedInputStream(new FileInputStream(iFile));
            byte buf[] = new byte[(int)iFile.length()];
            input.read(buf, 0, buf.length);
            input.close();


            File oFile = context.getDatabasePath(App.getDBName());
            BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(oFile));
            output.write(0);
            output.write(buf, 0, buf.length);
            output.flush();
            output.close();

        } catch (Exception e) {
            Log.d("DB", e.getMessage());
        }
    }

    public static void copyFile1(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte buffer[] = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0)
                os.write(buffer, 0, length);
        } finally {
            is.close();
            os.close();
        }
    }

    public static void copyFile2(File source, File dest) throws IOException {
        FileChannel sourceChannel = null;
        FileChannel destChannel = null;

        try {
            sourceChannel = new FileInputStream(source).getChannel();
            destChannel = new FileOutputStream(dest).getChannel();
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        } finally {
            sourceChannel.close();
            destChannel.close();
        }
    }
    public static void copyFile3(File source, File dest) throws IOException {
        FileUtils.copyFile(source, dest);
    }

    public static void exportDatabse() {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "//data//"+ "root.ivatio" +"//databases//"+App.getDBName()+"";
                String backupDBPath = "backupname.db";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {
            Log.d("DB", e.getMessage());
        }
    }

}
