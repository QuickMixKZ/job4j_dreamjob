package ru.job4j.dreamjob.store;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.model.User;

import java.util.Optional;

import static org.junit.Assert.*;

public class UserDBStoreTest {

    private static UserDBStore store;

    @BeforeClass
    public static void initialize() {
        store = new UserDBStore(new Main().loadPool());
    }

    @Test
    public void whenAddOneUser() {
        Optional<User> user = Optional.of(new User("email@temp.com", "qwerty"));
        user = store.add(user.get());
        User userInDB = store.findByID(user.get().getId());
        assertEquals(user.get().getEmail(), userInDB.getEmail());
    }

    @Test
    public void whenAddTwoUsersWithSameEmail() {
        User user = new User("email@temp.com", "qwerty");
        User user1 = new User("email@temp.com", "qwerty");
        assertTrue(store.add(user).isPresent());
        assertTrue(store.add(user1).isEmpty());
    }

    @Test
    public void whenAddTwoUsersWithDifferentEmails() {
        User user = new User("email@temp.com", "qwerty");
        User user1 = new User("other@temp.com", "123456");
        assertTrue(store.add(user).isPresent());
        assertTrue(store.add(user1).isPresent());
    }

    @Test
    public void whenAddThenUpdateUser() {
        Optional<User> user = Optional.of(new User("temp@mail.com", "123"));
        user = store.add(user.get());
        user.get().setPassword("qwerty");
        store.update(user.get());
        User userInDB = store.findByID(user.get().getId());
        assertEquals(user.get().getEmail(), userInDB.getEmail());
        assertEquals(user.get().getPassword(), userInDB.getPassword());
    }

    @After
    public void wipeTable() {
        store.deleteAll();
    }
}