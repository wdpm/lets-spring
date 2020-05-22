package io.github.wdpm.service;

import io.github.wdpm.domain.Post;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.*;

/**
 * Rest Template usage examples
 *
 * @author evan
 * @refer https://attacomsian.com/blog/http-requests-resttemplate-spring-boot
 * @date 2020/5/22
 */
@Service
public class RestService {
    private final RestTemplate restTemplate;

    public RestService(RestTemplateBuilder restTemplateBuilder) {
        // Spring Boot provides RestTemplateBuilder class to configure and create an instance of RestTemplate
        // set connection and read timeouts
        this.restTemplate = restTemplateBuilder
                .setConnectTimeout(Duration.ofSeconds(500))
                .setReadTimeout(Duration.ofSeconds(500))
                .basicAuthentication("username", "password")
                .build();
    }

    //region Get
    public String getPostsPlainJSON() {
        String url = "https://jsonplaceholder.typicode.com/posts";
        return this.restTemplate.getForObject(url, String.class);
    }

    public Post[] getPostsAsObject() {
        String url = "https://jsonplaceholder.typicode.com/posts";
        return this.restTemplate.getForObject(url, Post[].class);
    }

    public Post getPostWithUrlParameters() {
        String url = "https://jsonplaceholder.typicode.com/posts/{id}";
        return this.restTemplate.getForObject(url, Post.class, 1);
    }

    public Post getPostWithResponseHandling() {
        String               url      = "https://jsonplaceholder.typicode.com/posts/{id}";
        ResponseEntity<Post> response = this.restTemplate.getForEntity(url, Post.class, 1);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            return null;
        }
    }

    public Post getPostWithCustomHeaders() {
        String url = "https://jsonplaceholder.typicode.com/posts/{id}";

        // create headers
        HttpHeaders headers = new HttpHeaders();
        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        // set custom header
        headers.set("x-request-src", "desktop");

        // build the request
        HttpEntity<String> entity = new HttpEntity<>("body", headers);

        // use `exchange` method for HTTP call
        ResponseEntity<Post> response = this.restTemplate.exchange(url, HttpMethod.GET, entity, Post.class, 1);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            return null;
        }
    }
    //endregion

    //region POST
    public Post createPost() {
        String url = "https://jsonplaceholder.typicode.com/posts";

        // create headers
        HttpHeaders headers = new HttpHeaders();
        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // create a map for post parameters
        Map<String, Object> map = new HashMap<>();
        map.put("userId", 1);
        map.put("title", "Introduction to Spring Boot");
        map.put("body", "Spring Boot makes it easy to create stand-alone, production-grade Spring based Applications.");

        // build the request
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        // send POST request
        ResponseEntity<Post> response = this.restTemplate.postForEntity(url, entity, Post.class);

        // check response status code
        if (response.getStatusCode() == HttpStatus.CREATED) {
            return response.getBody();
        } else {
            return null;
        }
    }

    public Post createPostWithObject() {
        String url = "https://jsonplaceholder.typicode.com/posts";

        // create headers
        HttpHeaders headers = new HttpHeaders();
        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // create a post object
        Post post = new Post(1, "Introduction to Spring Boot",
                "Spring Boot makes it easy to create stand-alone, production-grade Spring based Applications.");

        // build the request
        HttpEntity<Post> entity = new HttpEntity<>(post, headers);

        // send POST request
        return restTemplate.postForObject(url, entity, Post.class);
    }
    //endregion

    //region PUT
    public void updatePost() {
        String url = "https://jsonplaceholder.typicode.com/posts/{id}";

        // create headers
        HttpHeaders headers = new HttpHeaders();
        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // create a post object
        Post post = new Post(4, "New Title", "New Body");

        // build the request
        HttpEntity<Post> entity = new HttpEntity<>(post, headers);

        // send PUT request to update post with `id` 10
        this.restTemplate.put(url, entity, 10);
    }

    public Post updatePostWithResponse() {
        String url = "https://jsonplaceholder.typicode.com/posts/{id}";

        // create headers
        HttpHeaders headers = new HttpHeaders();
        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // create a post object
        Post post = new Post(4, "New Title", "New Body");

        // build the request
        HttpEntity<Post> entity = new HttpEntity<>(post, headers);

        // send PUT request to update post with `id` 10
        ResponseEntity<Post> response = this.restTemplate.exchange(url, HttpMethod.PUT, entity, Post.class, 10);

        // check response status code
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            return null;
        }
    }
    //endregion

    //region DELETE
    public void deletePost() {
        String url = "https://jsonplaceholder.typicode.com/posts/{id}";

        // send DELETE request to delete post with `id` 10
        this.restTemplate.delete(url, 10);
    }
    //endregion

    //region HEAD
    public HttpHeaders retrieveHeaders() {
        String url = "https://jsonplaceholder.typicode.com/posts";

        // send HEAD request
        return this.restTemplate.headForHeaders(url);
    }
    //endregion

    //region OPTIONS
    public Set<HttpMethod> allowedOperations() {
        String url = "https://jsonplaceholder.typicode.com/posts";

        // send HEAD request
        return this.restTemplate.optionsForAllow(url);
    }
    //endregion

    public String unknownRequest() {
        try {
            String url = "https://jsonplaceholder.typicode.com/404";
            return this.restTemplate.getForObject(url, String.class);
        } catch (HttpStatusCodeException ex) {
            // raw http status code e.g `404`
            System.out.println(ex.getRawStatusCode());
            // http status code e.g. `404 NOT_FOUND`
            System.out.println(ex
                    .getStatusCode()
                    .toString());
            // get response body
            System.out.println(ex.getResponseBodyAsString());
            // get http headers
            HttpHeaders headers = ex.getResponseHeaders();
            if (headers != null) {
                System.out.println(headers.get("Content-Type"));
                System.out.println(headers.get("Server"));
            }
        }

        return null;
    }

    public void setBasicAuthHeadersManually() {
        // request url
        String url = "https://jsonplaceholder.typicode.com/posts";

        // create auth credentials
        String authStr = "username:password";
        String base64Creds = Base64
                .getEncoder()
                .encodeToString(authStr.getBytes());

        // create headers
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);

        // create request
        HttpEntity request = new HttpEntity(headers);

        // make a request
        ResponseEntity<String> response = new RestTemplate().exchange(url, HttpMethod.GET, request, String.class);

        // get JSON response
        String json = response.getBody();
    }

    public void setBasicAuthHeaders() {
        // create headers
        HttpHeaders headers = new HttpHeaders();
        // Authorization: Basic <credentials>
        // Credentials is a base64 encoded string created by combing both user name and password with a colon (:).
        headers.setBasicAuth("username", "password");
    }
}
