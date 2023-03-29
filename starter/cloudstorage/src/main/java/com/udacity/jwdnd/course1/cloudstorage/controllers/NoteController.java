package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping
public class NoteController {
    @Autowired
    private NoteService noteService;
    @Autowired
    private UserService userService;
    private String noteMessage="";


//    public NoteController(NoteService noteService, UserService userService) {
//        this.noteService = noteService;
//        this.userService = userService;
//    }

    @PostMapping("/savenote")
    public String saveNote(Authentication authentication, Note note, Model model, RedirectAttributes redirectAttributes) {
      try {
          Integer userId = userService.getUser(authentication.getName()).getUserId();
          note.setUserId(userId);
          if (note.getNoteId() == null) {
             note.setNoteId(noteService.addNote(note));
              noteMessage="Note added Successfully.";
          } else {
              noteService.updateNote(note);
              noteMessage="Note updated Successfully.";
          }
      }catch (Exception ex){
          ex.printStackTrace();
          noteMessage="Save was not successful." + ex.getMessage();
      }
            redirectAttributes.addAttribute("noteMessage", noteMessage);
        return "redirect:/home";
    }

    @GetMapping("/deletenote/{noteId}")
    public String deleteNote(@PathVariable("noteId") Integer noteId,Authentication authentication,Model model, RedirectAttributes redirectAttributes) {
        try{
            Integer userId = userService.getUser(authentication.getName()).getUserId();
            noteService.deleteNote(userId, noteId);
            noteMessage="Note deleted Successfully.";

        }catch (Exception ex){
            ex.printStackTrace();
            noteMessage="Delete was not successful." + ex.getMessage();
        }
        redirectAttributes.addAttribute("noteMessage", noteMessage);
        return "redirect:/home";
    }

        @GetMapping("/getAllNotes")
    public String getAllNotes(Authentication authentication, Model model) {
        int userId = userService.getUser(authentication.getName()).getUserId();
        model.addAttribute("notes", this.noteService.getMyNotes(userId));
        return "redirect:/home";
    }

}
