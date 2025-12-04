package crs.file;

import java.io.*;
import java.util.*;

public class UserFileHandler {

    // Read all lines from file
    public static ArrayList<String> readLines(String filename) {
        ArrayList<String> lines = new ArrayList<>();

        try {
            File file = new File(filename);

            if (!file.exists()) {
                file.createNewFile(); // create empty file if not exists
                return lines;
            }

            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                lines.add(sc.nextLine());
            }

            sc.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lines;
    }

    // Write lines to file
    public static void writeLines(String filename, ArrayList<String> lines) {
        try {
            FileWriter fw = new FileWriter(filename);

            for (String l : lines) {
                fw.write(l + "\n");
            }

            fw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
