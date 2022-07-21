package ru.job4j.dreamjob.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.store.CandidateStore;
import ru.job4j.dreamjob.store.PostDBStore;
import ru.job4j.dreamjob.store.PostStore;

import java.util.Collection;
import java.util.List;

@ThreadSafe
@Service
public class PostService {
    private final PostDBStore store;
    private final CityService cityService;

    public PostService(PostDBStore store, CityService cityService) {
        this.store = store;
        this.cityService = cityService;
    }

    public List<Post> findAll() {
        List<Post> posts = store.findAll();
        posts.forEach(
                post -> post.setCity(
                        cityService.findById(post.getCityID())
                )
        );
        return store.findAll();
    }

    public void add(Post post) {
        store.add(post);
    }

    public Post findById(int id) {
        Post post = store.findById(id);
        post.setCity(cityService.findById(post.getCityID()));
        return post;
    }

    public void update(Post post) {
        store.update(post);
    }

}
