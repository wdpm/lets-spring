package io.github.wdpm;

import io.github.wdpm.service.RestService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author evan
 * @date 2020/5/22
 */
@SpringBootApplication
public class App implements CommandLineRunner {

    private final RestService restService;

    public App(RestService restService) {
        this.restService = restService;
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        // fetch posts as plain JSON
        System.out.println(restService.getPostsPlainJSON());

        // fetch posts as object
        // for (Post post: restService.getPostsAsObject()) {
        // 	System.out.println(post);
        // }

        // fetch post with url parameter
        // System.out.println(restService.getPostWithUrlParameters());

        // fetch post with response handling
        // System.out.println(restService.getPostWithResponseHandling());

        // fetch post with response handling
        // System.out.println(restService.getPostWithCustomHeaders());

        // will connect timeout
        // create a new post
        // System.out.println(restService.createPost());

        // create a new post with object
        // System.out.println(restService.createPostWithObject());

        // update post
        // restService.updatePost();

        // update post with response handling
        // System.out.println(restService.updatePostWithResponse());

        // delete post
        // restService.deletePost();

        // retrieve headers
        // System.out.println(restService
        //         .retrieveHeaders()
        //         .getCacheControl());

        // list allowed operations
        // for (HttpMethod method : restService.allowedOperations()) {
        //     System.out.println(method.name());
        // }

        // get request with error handling
        // System.out.println(restService.unknownRequest());
    }
}

