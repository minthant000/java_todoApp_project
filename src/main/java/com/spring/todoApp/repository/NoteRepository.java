package com.spring.todoApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.todoApp.entity.Note;

public interface NoteRepository extends JpaRepository<Note, Integer> {
    // CRUD
}
