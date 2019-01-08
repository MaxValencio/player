package net.maxvalencio;

import java.io.*;
import java.util.ArrayList;
import java.util.List;



class FileFinder {

    String directoryOfSearch = "Z:/Music/top 50";
    private List<Mp3Track> trecks;


    FileFinder() {

        String[] filesList;

        File file = new File(directoryOfSearch);
        filesList = file.list(new DirFilter(".mp3"));
        trecks = new ArrayList<Mp3Track>();


        for (int i = 0; i < filesList.length; i++) {
            String name = filesList[i];
            String path = directoryOfSearch + "\\\\" + filesList[i];

            trecks.add(new Mp3Track(name, path));
        }
    }

    List<Mp3Track> getTracks() {
        return trecks;
    }


    class DirFilter implements FilenameFilter {
        private String expression;
        DirFilter(String expression){
            this.expression = expression;
        }
        public boolean accept(File file, String name) {
            return name.endsWith(expression);
        }

    }
}
