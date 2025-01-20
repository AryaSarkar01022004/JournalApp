package com.arya.journalApp.controller;

import com.arya.journalApp.entity.JournalEntry;
import com.arya.journalApp.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;



@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {

    @Autowired
    private  JournalEntryService journalEntryService;



    @GetMapping
    public List<JournalEntry> getAll(){
        return journalEntryService.getAll();
    }

    @PostMapping
    public JournalEntry createEntry(@RequestBody JournalEntry myEntry){
        myEntry.setDate(LocalDateTime.now());
        journalEntryService.saveEntry(myEntry);
        return myEntry;
    }
    @GetMapping("id/{myId}")
    public JournalEntry getJournalEntryById(@PathVariable ObjectId myId){
        return journalEntryService.findById(myId).orElse(null);

    }
    @DeleteMapping("/id/{myId}")
    public boolean  deleteJournalEntryById(@PathVariable ObjectId myId){
         journalEntryService.deleteById(myId);
         return true;
    }

    @PutMapping("/id/{id}")
    public JournalEntry updateJournalById(@PathVariable ObjectId id,@RequestBody JournalEntry updatedEntry){
        JournalEntry old = journalEntryService.findById(id).orElse(null);
        if(old != null){
            old.setTitle(updatedEntry.getTitle()!=null&&!updatedEntry.getTitle().equals(" ")? updatedEntry.getTitle():old.getTitle());
            old.setContent(updatedEntry.getContent()!=null&&!updatedEntry.getContent().equals(" ")? updatedEntry.getContent():old.getContent());
        }

        journalEntryService.saveEntry(old);
        return old;
    }

}
