package ru.job4j.dreamjob.store;

import org.junit.Test;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.model.Candidate;

import static org.junit.Assert.*;

public class CandidateDBStoreTest {

    @Test
    public void whenCreateCandidate() {
        CandidateDBStore store = new CandidateDBStore(new Main().loadPool());
        Candidate candidate = new Candidate(0, "Mikhail");
        candidate = store.add(candidate);
        Candidate candidateInDB = store.findById(candidate.getId());
        assertEquals(candidate.getName(), candidateInDB.getName());
    }

    @Test
    public void whenCreateThenUpdateCandidate() {
        CandidateDBStore store = new CandidateDBStore(new Main().loadPool());
        Candidate candidate = new Candidate(0, "Mikhail");
        candidate = store.add(candidate);
        candidate.setName("Alexandr");
        store.update(candidate);
        Candidate candidateInDB = store.findById(candidate.getId());
        assertEquals(candidate.getName(), candidateInDB.getName());
    }

    @Test
    public void whenCreateTwoCandidates() {
        CandidateDBStore store = new CandidateDBStore(new Main().loadPool());
        Candidate candidate = new Candidate(0, "Mikhail");
        Candidate candidate1 = new Candidate(0, "Alexandr");
        candidate = store.add(candidate);
        candidate1 = store.add(candidate1);
        Candidate candidateInDB = store.findById(candidate.getId());
        Candidate candidate1InDB = store.findById(candidate1.getId());
        assertEquals(candidate.getName(), candidateInDB.getName());
        assertEquals(candidate1.getName(), candidate1InDB.getName());

    }

}