package com.spring.todoApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.spring.todoApp.entity.Note;
import com.spring.todoApp.entity.User;

public interface NoteRepository extends JpaRepository<Note, Integer> {
    
    // @Query("SELECT n from Note WHERE n.title LIKE %:keyword% OR t.description LIKE %:keyword%")
    // List<Note> searchByKeyword(String keyword);

    // List<Note> findAllByOrderByPriorityAsc();

    List<Note> findAllByUser(User user);

    @Query("SELECT n FROM Note n INNER JOIN User u ON n.user.id = u.id WHERE n.user.id = :userId ORDER BY n.created_at DESC")
    List<Note> findAllNoteByUserIdOrderedByDateDesc(@Param("userId") Integer userId);

    @Query("SELECT n FROM Note n INNER JOIN User u ON n.user.id = u.id WHERE n.user.id = :userId ORDER BY n.priority ASC")
    List<Note> findAllNoteByUserIdOrderedByPriority(@Param("userId") Integer userId);
    // List<Note> findALlByOrderByPriorityDesc();
}
