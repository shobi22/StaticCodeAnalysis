package com.project.staticss.java8;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AccessProject {
    
    private static List<File> javaFiles = new ArrayList<>();

    /**
     * Gets the relevant java files
     * @param file
     * @return 
     */
    public static List<File> getJavaFiles(File file) {
        File files[] = null;
        if (file.isFile()) {
            System.out.println(file.getAbsolutePath());
        } else {
            files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().endsWith(".java") || files[i].getName().endsWith(".JAVA")) {
                    javaFiles.add(files[i]);
                }
                if (files[i].isDirectory()) {
                    getJavaFiles(files[i].getAbsoluteFile());
                }
            }
        }
        return javaFiles;
    }
    
    /**
     * Checks whether java files exist
     * @param file
     * @return boolean
     */
    public static boolean javaFilesExists(File file){
        javaFiles.clear();
        getJavaFiles(file);
        return !javaFiles.isEmpty();
    }
    
    /**
     * Returns the files
     * @return List
     */
    public List getFiles(){
        return javaFiles;
    }

//    public static void main(String args[]) {
//        File mainFolder = new File("C:\\Users\\Public\\Documents\\CS585staticCode");
//        List<File> files = getJavaFiles(mainFolder);
//        
//        for (File file : files) {
//            System.out.println(file.getName());
//        }
//    }
}
