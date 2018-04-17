package com.bissani.takenotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class NoteActivity extends AppCompatActivity {

    private boolean isViewingOrUpdating;
    private long misViewingOrUpdating;
    
    private String fileName;
    private Note loadedNote = null;

    private EditText noteName;
    private EditText noteContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        noteName = (EditText) findViewById(R.id.noteName);
        noteContent = (EditText) findViewById(R.id.noteContent);
        
        fileName = getIntent().getStringExtra(Utilities.EXTRAS_NOTE_FILENAME);
        
        if(fileName != null && !fileName.isEmpty() && fileName.endsWith(Utilities.FILE_EXTENSION)) {
            loadedNote = Utilities.getNoteByFileName(getApplicationContext(), fileName);
            if (loadedNote != null) {
                noteName.setText(loadedNote.getTitle());
                noteContent.setText(loadedNote.getContent());
                misViewingOrUpdating = loadedNote.getDateTime();
                isViewingOrUpdating = true;
            }
        } else {
            misViewingOrUpdating = System.currentTimeMillis();
            isViewingOrUpdating = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(isViewingOrUpdating) {
            getMenuInflater().inflate(R.menu.menu_note_view, menu);
        }
        else {
            getMenuInflater().inflate(R.menu.menu_note_add, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save_note:
            case R.id.action_update:
                validateAndSaveNote();
                break;
            case R.id.action_delete:
                actionDelete();
                break;
            case R.id.action_cancel:
                actionCancel();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        actionCancel();
    }

    private void actionDelete() {
        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(this)
                .setTitle(R.string.app_name)
                .setMessage(R.string.deleteNoteMessage)
                .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(loadedNote != null && Utilities.deleteFile(getApplicationContext(), fileName)) {
                            Toast.makeText(NoteActivity.this, loadedNote.getTitle() + " foi deletado com sucesso"
                                    , Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(NoteActivity.this, "Não foi possível deletar a nota '" + loadedNote.getTitle() + "'"
                                    , Toast.LENGTH_SHORT).show();
                        }
                        finish();
                    }
                })
                .setNegativeButton("NÃO", null);

        dialogDelete.show();
    }

    private void actionCancel() {
        if(!checkNoteAltered()) {
            finish();
        } else {
            AlertDialog.Builder dialogCancel = new AlertDialog.Builder(this)
                    .setTitle(R.string.app_name)
                    .setMessage(R.string.discardChangesMessage)
                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("NÃO", null);
            dialogCancel.show();
        }
    }

    private boolean checkNoteAltered() {
        if(isViewingOrUpdating) {
            return loadedNote != null && (!noteName.getText().toString().equalsIgnoreCase(loadedNote.getTitle())
                    || !noteContent.getText().toString().equalsIgnoreCase(loadedNote.getContent()));
        } else {
            return !noteName.getText().toString().isEmpty() || !noteContent.getText().toString().isEmpty();
        }
    }
    
    private void validateAndSaveNote() {
        String title = noteName.getText().toString();
        String content = noteContent.getText().toString();

        if(title.isEmpty()) {
            Toast.makeText(NoteActivity.this, R.string.nameNote
                    , Toast.LENGTH_SHORT).show();
            return;
        }
        if(content.isEmpty()) {
            Toast.makeText(NoteActivity.this, R.string.noteContent
                    , Toast.LENGTH_SHORT).show();
            return;
        }
        if(loadedNote != null) {
            misViewingOrUpdating = loadedNote.getDateTime();
        } else {
            misViewingOrUpdating = System.currentTimeMillis();
        }
        if(Utilities.saveNote(this, new Note(misViewingOrUpdating, title, content))) {
            Toast.makeText(this, R.string.noteSaved, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.noteNotSaved, Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}

