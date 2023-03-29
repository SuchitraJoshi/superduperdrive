package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class NoteService {
    //@Autowired
    private NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("Creating NoteService bean");
    }

    public Integer addNote(Note note) {
       return noteMapper.insertNote(note);
    }
    public Integer updateNote(Note note) {
        return noteMapper.updateNote(note);
    }
    public Integer deleteNote(Integer userId,Integer noteId) {
        return noteMapper.deleteNote(userId, noteId);
    }
    public List<Note> getMyNotes(Integer userId) {
        return noteMapper.getMyMessages(userId);
    }
}

