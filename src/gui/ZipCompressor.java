package gui;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.*;
import javax.swing.JOptionPane;

public class ZipCompressor {

    private List<String> files = new ArrayList<String>();
    private String stdout;

    private final int BUFFER_SIZE = 512;

    public void compressFolder(Path folder, String stdout) {
        //generateFileList(folder);

        try {
            // Objeto para referenciar a los archivos que queremos comprimir
            BufferedInputStream origin = null;
            // Objeto para referenciar el archivo zip de salida
            FileOutputStream dest = new FileOutputStream(stdout);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
            // Buffer de transferencia para mandar datos a comprimir
            byte[] data = new byte[BUFFER_SIZE];
            Iterator i = files.iterator();
            while (i.hasNext()) {
                String filename = (String) i.next();
                FileInputStream fi = new FileInputStream(filename);
                origin = new BufferedInputStream(fi, BUFFER_SIZE);

                ZipEntry entry = new ZipEntry(filename);
                out.putNextEntry(entry);
                // Leemos datos desde el archivo origen y los mandamos al archivo destino
                int count;
                while ((count = origin.read(data, 0, BUFFER_SIZE)) != -1) {
                    out.write(data, 0, count);
                }
                // Cerramos el archivo origen, ya enviado a comprimir
                origin.close();
            }
            // Cerramos el archivo zip
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

    public String getStdout() {
        return stdout;
    }

    public void setStdout(String stdout) {
        this.stdout = stdout;
    }

    public void generateFileList(String folder) {
        String filename;
        File stream = new File(folder);
        File[] filelist = stream.listFiles();
        for (File file : filelist) {
            if (file.isFile()) {
                filename = file.getAbsolutePath();
                System.out.println(filename);
            }
        }
        /*try {
        DirectoryStream<Path> stream = Files.newDirectoryStream(folder);
        for (Path file : stream) {
        if (file.getParent().toString().equals(folder.toString())) {
        files.add(file.getFileName().toAbsolutePath().toString());
        System.out.println(file.getFileName().toAbsolutePath().toString());
        }
        }
        } catch (IOException ex) {
        JOptionPane.showMessageDialog(
        null,
        "Cannot list files in the selected directory",
        "Error compressing file",
        JOptionPane.ERROR_MESSAGE);
        Logger.getLogger(ZipCompressor.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }
}
