package com.spring.todoApp.controller;


import java.util.List;
// import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.todoApp.entity.Note;
import com.spring.todoApp.entity.User;
import com.spring.todoApp.repository.UserRepository;
// import com.spring.todoApp.entity.User;
// import com.spring.todoApp.repository.UserRepository;
import com.spring.todoApp.service.NoteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/notes")
@ControllerAdvice
public class NoteController {
    @Autowired
    private NoteService noteService;

    @Autowired
    private UserRepository userRepository;

    // get all notes
    @GetMapping("/notes")
    public ResponseEntity<List<Note>> getAllNotes(User user){
        List<Note> note = noteService.getAllNotes(user);
        return new ResponseEntity<>(note, HttpStatus.FOUND);        
    }

    // sorted by date
    @GetMapping("/{userId}/notesDescInDate")
    public ResponseEntity<List<Note>> retrieveAllNotesInDateDescOrder(@PathVariable Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Note not found with id: " + userId));
        List<Note> notes = noteService.findNoteByUserInDateDescOrder(user.getId());
        return new ResponseEntity<>(notes,HttpStatus.OK);
    }

    // // sorted by priority
    @GetMapping("/{userId}/sortByPriority")
    public ResponseEntity<List<Note>> retrieveAllNotesInPriorityOrder(@PathVariable Integer userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Note not found with id: " + userId));
        List<Note> notes = noteService.findNoteByUserInPriority(user.getId());
        return new ResponseEntity<>(notes, HttpStatus.OK);
    }

    // search note
    @GetMapping("/{userId}/searchNote")
    public ResponseEntity<List<Note>> searchNoteByUser(@RequestParam String keyword,@PathVariable Integer userId){
        // User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Note not found with id: " + userId));
        List<Note> notes = noteService.searchNoteByUser(keyword, userId);
        return new ResponseEntity<>(notes, HttpStatus.FOUND);
    }

    // create note
    @PostMapping("/{userId}")
    public ResponseEntity<Note> createNote(@Valid @RequestBody Note notes,@PathVariable Integer userId){
        System.out.println("userId" + userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Note not found with id: " + userId));
        Note note =  noteService.createNote(notes, user);
        return new ResponseEntity<>(note,HttpStatus.CREATED);
    }

    // delete note
    @DeleteMapping("notes/{noteId}")
    public ResponseEntity<String> deleteNote(@PathVariable Integer noteId){
        noteService.deleteNote(noteId);
        return new ResponseEntity<>("Deleted",HttpStatus.OK);
    }

    // update note
    @PutMapping("/{id}")
    public ResponseEntity<String> updateNote(@PathVariable Integer id,@RequestBody Note note){
        noteService.updateNote(id, note);
        return new ResponseEntity<>("Content Updated", HttpStatus.OK);
    }
}
