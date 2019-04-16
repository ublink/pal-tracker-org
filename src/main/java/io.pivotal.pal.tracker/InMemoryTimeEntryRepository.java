package io.pivotal.pal.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTimeEntryRepository implements TimeEntryRepository{

    private Map<Long, TimeEntry> timeEntries;
    private long sequence = 0L;
    public InMemoryTimeEntryRepository() {
        this.timeEntries = new HashMap<>();
    }

    public TimeEntry create(TimeEntry timeEntry) {
        sequence++;
        long timeEntryId = sequence;
        timeEntry.setId(timeEntryId);
        timeEntries.put(timeEntryId, timeEntry);
        return timeEntry;
    }

    public TimeEntry find(long id) {
        TimeEntry timeEntry = timeEntries.get(id);
        return timeEntry;
    }

    public List list() {
        return new ArrayList(timeEntries.values());
    }

    public TimeEntry update(long id, TimeEntry timeEntry) {

        if(timeEntries.containsKey(id)) {
            timeEntries.put(id, timeEntry);
            timeEntry.setId(id);
        }
        else
            return null;

        return timeEntry;
    }

    public void delete(long id) {
        timeEntries.remove(id);
    }
}
