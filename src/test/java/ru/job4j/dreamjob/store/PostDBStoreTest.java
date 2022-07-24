package ru.job4j.dreamjob.store;

import org.junit.Test;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.model.Post;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PostDBStoreTest {

    //private static PostDBStore store = new PostDBStore(new Main().loadPool());

    @Test
    public void whenCreatePost() {
        PostDBStore store = new PostDBStore(new Main().loadPool());
        Post post = new Post(0, "Java Job", 1);
        store.add(post);
        Post postInDb = store.findById(post.getId());
        assertThat(postInDb.getName(), is(post.getName()));
    }

    @Test
    public void whenCreateThenUpdatePost() {
        PostDBStore store = new PostDBStore(new Main().loadPool());
        Post post = new Post(0, "Java Job", 1);
        store.add(post);
        Post updatedPost = new Post(0, "Python Job", 1);
        store.update(updatedPost);
        Post postInDb = store.findById(updatedPost.getId());
        assertThat(postInDb.getName(), is(updatedPost.getName()));
    }

    @Test
    public void whenCreateTwoPosts() {
        PostDBStore store = new PostDBStore(new Main().loadPool());
        Post post = new Post(0, "Java Job");
        Post post1 = new Post(1, "Python Job");
        store.add(post);
        store.add(post1);
        Post postInDb = store.findById(post1.getId());
        assertThat(postInDb.getName(), is(post1.getName()));
    }
}