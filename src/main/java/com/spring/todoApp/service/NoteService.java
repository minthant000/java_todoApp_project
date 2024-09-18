package com.spring.todoApp.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
// import org.springframework.web.bind.annotation.PathVariable;

import com.spring.todoApp.entity.Note;
import com.spring.todoApp.entity.Priority;
import com.spring.todoApp.entity.User;
import com.spring.todoApp.repository.NoteRepository;
// import com.spring.todoApp.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoteService {
    @Autowired
    private NoteRepository noteRepository;

    // private UserRepository userRepository;

    //get all notes
    public List<Note> getAllNotes(User user){
        return noteRepository.findAll();
    }

    // get note sorted by date
    public List<Note> findNoteByUserInDateDescOrder(Integer userId) {
        return noteRepository.findAllNoteByUserIdOrderedByDateDesc(userId);
    }
    
    // get notes sorted by priority
    public List<Note> findNoteByUserInPriority(Integer userId){
        return noteRepository.findAllNoteByUserIdOrderedByPriority(userId);
    }
    // create note
    public Note createNote(Note note,User user){
        Note newNote = Note.builder()
            .title(note.getTitle())
            .description(note.getDescription())
            .priority(note.getPriority())
            .user(user)
            .build();
        return noteRepository.save(newNote);
    }

    // update note
    public Note updateNote(Integer id, Note updateNote){
        Optional<Note> existingNote = noteRepository.findById(id);
        if(existingNote.isPresent()){
            Note note = existingNote.get();
            note.setTitle(updateNote.getTitle());
            note.setDescription(updateNote.getDescription());
            note.setPriority(updateNote.getPriority());
            note.setUpdated_at(new Date());
            return noteRepository.save(note);
        }
        return null;
    }

    // delete note
    public void deleteNote(Integer id){
        Note note = noteRepository.findById(id).orElseThrow(() -> new RuntimeException("Note not found with id: " + id));
        noteRepository.delete(note);
    }

    // // search by keyword
    // public List<Note> searchNotes(String keyword){
    //     return noteRepository.searchByKeyword(keyword);
    // }

    
}
