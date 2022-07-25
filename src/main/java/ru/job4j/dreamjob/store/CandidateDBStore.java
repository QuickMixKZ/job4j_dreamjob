package ru.job4j.dreamjob.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;
import java.io.ByteArrayInputStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CandidateDBStore {

    private final BasicDataSource pool;
    public CandidateDBStore(BasicDataSource pool) {
        this.pool = pool;
    }
    private static final Logger LOG = LoggerFactory.getLogger(CandidateDBStore.class.getName());

    public List<Candidate> findAll() {
        List<Candidate> candidates = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM candidate")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    Candidate candidate = new Candidate(it.getInt("id"),
                            it.getString("name"),
                            it.getString("description"),
                            it.getTimestamp("created").toLocalDateTime(),
                            it.getBytes("photo"));
                    candidates.add(candidate);
                }
            }
        } catch (Exception e) {
            LOG.error("Exception in CandidateDBStore", e);
        }
        return candidates;
    }

    public Candidate add(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("INSERT INTO candidate(name, description, created, photo) VALUES (?, ?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, candidate.getName());
            ps.setString(2, candidate.getDescription());
            ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            ps.setBinaryStream(4,  candidate.getPhoto() == null ? null : new ByteArrayInputStream(candidate.getPhoto()));
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    candidate.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception in CandidateDBStore", e);
        }
        return candidate;
    }

    public void update(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("UPDATE candidate SET name = (?),  description = (?), photo = (?) WHERE id = (?)")) {
            ps.setString(1, candidate.getName());
            ps.setString(2, candidate.getDescription());
            ps.setBinaryStream(3, candidate.getPhoto() == null ? null : new ByteArrayInputStream(candidate.getPhoto()));
            ps.setInt(4, candidate.getId());
            ps.execute();
        } catch (SQLException e) {
            LOG.error("Exception in CandidateDBStore", e);
        }
    }

    public Candidate findById(int id) {
        Candidate result = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM candidate WHERE id = (?)")
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    result  = new Candidate(it.getInt("id"),
                            it.getString("name"),
                            it.getString("description"),
                            it.getTimestamp("created").toLocalDateTime(),
                            it.getBytes("photo"));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception in CandidateDBStore", e);
        }
        return result;
    }
}
