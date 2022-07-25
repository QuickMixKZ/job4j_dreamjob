package ru.job4j.dreamjob.store;

import org.junit.Test;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.model.Post;

import static org.junit.Assert.*;

public class PostDBStoreTest {

    @Test
    public void whenCreatePost() {
        PostDBStore store = new PostDBStore(new Main().loadPool());
        Post post = new Post(0, "Java Job", 1);
        store.add(post);
        Post postInDb = store.findById(post.getId());
        assertEquals(post.getName(), postInDb.getName());
    }

    @Test
    public void whenCreateThenUpdatePost() {
        PostDBStore store = new PostDBStore(new Main().loadPool());
        Post post = new Post(0, "Java Job", 1);
        store.add(post);
        post.setName("Python Developer");
        store.update(post);
        Post postInDb = store.findById(post.getId());
        assertEquals(post.getName(), postInDb.getName());
    }

    @Test
    public void whenCreateTwoPosts() {
        PostDBStore store = new PostDBStore(new Main().loadPool());
        Post post = new Post(0, "Java Job");
        Post post1 = new Post(1, "Python Job");
        post = store.add(post);
        post1 = store.add(post1);
        Post postInDB = store.findById(post.getId());
        Post post1InDB = store.findById(post1.getId());
        assertEquals(post.getName(), postInDB.getName());
        assertEquals(post1.getName(), post1InDB.getName());
    }
}