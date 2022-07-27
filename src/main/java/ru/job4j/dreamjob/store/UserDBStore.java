package ru.job4j.dreamjob.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDBStore {

    private final BasicDataSource pool;

    private static final Logger LOG = LoggerFactory.getLogger(PostDBStore.class.getName());

    public UserDBStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (Connection cn = pool.getConnection();
            PreparedStatement ps = cn.prepareStatement("SELECT * FROM users")) {
                try (ResultSet it = ps.executeQuery()) {
                    while (it.next()) {
                        User user = new User(it.getInt("id"),
                                it.getString("name"),
                                it.getString("password"));
                        users.add(user);
                    }
                }
        } catch (SQLException e) {
            LOG.error("Exception in UserDBStore", e);
        }
        return users;
    }

    public Optional<User> add(User user) {
        Optional<User> result = Optional.of(user);
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("INSERT INTO users(email, password) VALUES (?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.execute();
            try (ResultSet it = ps.getGeneratedKeys()) {
                if (it.next()) {
                    result.get().setId(it.getInt("id"));
                }
            }
        } catch (PSQLException | SQLIntegrityConstraintViolationException e) {
            result = Optional.empty();
        } catch (SQLException e) {
            LOG.error("Exception in UserDBStore", e);
        }
        return result;
    }

    public void update(User user) {
        try (Connection cn = pool.getConnection();
        PreparedStatement ps = cn.prepareStatement("UPDATE users SET email = (?), password = (?) WHERE id = (?)")) {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.setInt(3, user.getId());
            ps.execute();
        } catch (SQLException e) {
            LOG.error("Exception in UserDBStore", e);
        }
    }

    public User findByID(int id) {
        User user = null;
        try (Connection cn = pool.getConnection();
        PreparedStatement ps = cn.prepareStatement("SELECT * FROM users WHERE id = (?)")) {
            ps.setInt(1, id);
            ResultSet it = ps.executeQuery();
            if (it.next()) {
                user = new User(it.getInt("id"),
                        it.getString("email"),
                        it.getString("password"));
            }
        } catch (SQLException e) {
            LOG.error("Exception in UserDBStore");
        }
        return user;
    }

    public void deleteAll() {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("DELETE FROM users")) {
            ps.execute();
        } catch (SQLException e) {
            LOG.error("Exception in UserDBStore", e);
        }
    }
}
