package com.bissani.takenotes;

import android.content.Context;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Note implements Serializable {

    private long noteDateTime;
    private String noteFileName;
    private String noteContent;

    public Note(long dateInMillis, String title, String content) {
        noteDateTime = dateInMillis;
        noteFileName = title;
        noteContent = content;
    }

    public void setDateTime(long dateTime) {
        noteDateTime = dateTime;
    }

    public void setTitle(String title) {
        noteFileName = title;
    }

    public void setContent(String content) {
        noteContent = content;
    }

    public long getDateTime() {
        return noteDateTime;
    }

    public String getDateTimeFormatted(Context context) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy -   HH:mm:ss"
                , context.getResources().getConfiguration().locale);
        formatter.setTimeZone(TimeZone.getDefault());
        return formatter.format(new Date(noteDateTime));
    }

    public String getTitle() {
        return noteFileName;
    }

    public String getContent() {
        return noteContent;
    }
}
