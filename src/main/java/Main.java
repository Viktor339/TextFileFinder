import org.apache.commons.io.FileUtils;
import org.apache.commons.io.comparator.NameFileComparator;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static final Charset ENCODING = StandardCharsets.UTF_8;
    public static final String RESULT_FILE_PATH = "D:/folder/";
    public static final String ROOT_DIRECTORY_PATH = "D:/folder";
    public static final String TXT_FILE_EXTENSION = ".txt";
    public static final String DATE_TIME_FORMAT_PATTERN = "yyyy.MM.dd HH-mm-ss";


    public static void main(String[] args) {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_PATTERN);
        LocalDateTime now = LocalDateTime.now();

        File resultFile = new File(RESULT_FILE_PATH + dtf.format(now) + TXT_FILE_EXTENSION);

        List<File> fileList = getFiles(new File(ROOT_DIRECTORY_PATH), new ArrayList<>());
        fileList.sort(NameFileComparator.NAME_COMPARATOR);

        try {
            mergeFilesContentToFile(fileList, resultFile);
        } catch (IOException e) {
            throw new RuntimeException("Error while reading or writing to file", e);
        }

    }

    private static List<File> getFiles(File rootDirectory, List<File> fileList) {

        if (rootDirectory.isDirectory()) {

            File[] directoryFiles = rootDirectory.listFiles();

            if (directoryFiles != null) {

                for (File file : directoryFiles) {
                    if (file.isDirectory()) {
                        getFiles(file, fileList);
                    } else {
                        if (file.getName().toLowerCase().endsWith(TXT_FILE_EXTENSION)) {
                            fileList.add(file);
                        }
                    }
                }
            }
        }

        return fileList;
    }


    private static void mergeFilesContentToFile(List<File> fileList, File resultFile) throws IOException {

        for (File files : fileList) {
            CharSequence fileContent = FileUtils.readFileToString(files, ENCODING);
            FileUtils.write(resultFile, fileContent, ENCODING, true);
        }

    }
}
