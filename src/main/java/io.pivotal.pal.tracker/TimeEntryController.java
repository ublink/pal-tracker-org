package io.pivotal.pal.tracker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TimeEntryController {

    private TimeEntryRepository timeEntryRepository;

    public TimeEntryController(TimeEntryRepository timeEntryRepository) {

        this.timeEntryRepository = timeEntryRepository;
    }

    @PostMapping("/time-entries")
    public @ResponseBody  ResponseEntity create(@RequestBody TimeEntry timeEntryToCreate) {
        TimeEntry createdEntry = timeEntryRepository.create(timeEntryToCreate);
        ResponseEntity response = new ResponseEntity(createdEntry, HttpStatus.CREATED);
        return response;
    }

    @GetMapping("/time-entries/{timeEntryId}")
    public ResponseEntity read(@PathVariable long timeEntryId) {
        TimeEntry timeEntry =  timeEntryRepository.find(timeEntryId);
        if (null == timeEntry)
            return new ResponseEntity(timeEntry, HttpStatus.NOT_FOUND);
        return new ResponseEntity(timeEntry, HttpStatus.OK);
    }

    @GetMapping("/time-entries")
    public @ResponseBody ResponseEntity list() {
        List timeEntries = timeEntryRepository.list();
        return new ResponseEntity(timeEntries, HttpStatus.OK);
    }

    @PutMapping("/time-entries/{timeEntryId}")
    public ResponseEntity update(@PathVariable long timeEntryId, @RequestBody TimeEntry expected) {
        TimeEntry updated = timeEntryRepository.update(timeEntryId, expected);
        if (null == updated)
            return new ResponseEntity(updated, HttpStatus.NOT_FOUND);
        return new ResponseEntity(updated, HttpStatus.OK);
    }

    @DeleteMapping("/time-entries/{timeEntryId}")
    public ResponseEntity delete(@PathVariable long timeEntryId) {
        timeEntryRepository.delete(timeEntryId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
