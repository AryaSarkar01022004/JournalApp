package com.arya.journalApp.controller;

import com.arya.journalApp.entity.JournalEntry;
import com.arya.journalApp.entity.User;
import com.arya.journalApp.service.JournalEntryService;
import com.arya.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {

    @Autowired
    private  JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;


    @GetMapping("/{username}")
    public ResponseEntity<?> getAllJournalEntriesOfUser(@PathVariable String username){
        User user = userService.findByUsername(username);
        List<JournalEntry> all = user.getJournalEntries();
        if(all != null && !all.isEmpty()){
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("{username}")
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry, @PathVariable String username){
        try{

            journalEntryService.saveEntry(myEntry,username);
            return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("id/{myId}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable ObjectId myId){
        Optional<JournalEntry> journalEntry = journalEntryService.findById(myId);
        if(journalEntry.isPresent()){
            return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @DeleteMapping("/id/{username}/{myId}")
    public ResponseEntity<?>  deleteJournalEntryById(@PathVariable ObjectId myId, @PathVariable String username){
         journalEntryService.deleteById(myId,username);
         return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/id/{username}/{myId}")
    public ResponseEntity<?> updateJournalById(@PathVariable ObjectId myId,@RequestBody JournalEntry updatedEntry,@PathVariable String username){
        JournalEntry old = journalEntryService.findById(myId).orElse(null);
        if(old != null){
          old.setTitle(updatedEntry.getTitle()!=null&&!updatedEntry.getTitle().equals(" ")? updatedEntry.getTitle():old.getTitle());
           old.setContent(updatedEntry.getContent()!=null&&!updatedEntry.getContent().equals(" ")? updatedEntry.getContent():old.getContent());
           journalEntryService.saveEntry(old);
           return new ResponseEntity<>(old, HttpStatus.OK);
       }
         return new ResponseEntity<>(HttpStatus.NOT_FOUND);


    }

}
