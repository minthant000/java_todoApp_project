package com.spring.todoApp.service;

import java.util.List;
import com.spring.todoApp.entity.Note;

public interface NoteService {
    Note savenote(Note note);
    List<Note> getAllNote();
    Note getNoteById(Integer id);
    Note updateNote(Note note,Integer id);
    void deleteNote(Integer id);
}
