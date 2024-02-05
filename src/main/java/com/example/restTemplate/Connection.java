package com.example.restTemplate;

import com.example.restTemplate.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Connection {

    private final String URL = "http://94.198.50.185:7081/api/users";
    private final RestTemplate restTemplate;

    private List<String> cookies;

    @Autowired
    public Connection(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    private void app(){

        ResponseEntity<List<User>> users = getAllUser();
        System.out.println(users.getBody());

        User addUser = new User(3L, "James", "Brown", (byte) 35);
        ResponseEntity<String> responseEntityAddUser = addUser(addUser);
        System.out.println(responseEntityAddUser.getBody());

        addUser.setName("Thomas");
        addUser.setLastName("Shelby");
        ResponseEntity<String> responseEntityUpdateUser = updateUser(addUser);
        System.out.println(responseEntityUpdateUser.getBody());

        ResponseEntity<String> responseEntityDeleteUser = deleteUser(3L);
        System.out.println(responseEntityDeleteUser.getBody());
    }

    //Получить список всех пользователей
    public ResponseEntity<List<User>> getAllUser(){

        ResponseEntity<List<User>> responseEntity =
                restTemplate.exchange(URL, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<User>>() {});
        cookies = responseEntity.getHeaders().get("Set-Cookie").stream().peek(x-> System.out.println(x)).collect(Collectors.toList());
        return responseEntity;
    }

    //сохранить свой session id, который получен через cookie (необходимо использовать заголовок в последующих запросах)


    //Сохранить пользователя с id = 3, name = James, lastName = Brown, age = на ваш выбор. В случае успеха вы получите первую часть кода.
    public ResponseEntity<String> addUser(User user){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Cookie", cookies.stream().collect(Collectors.joining(";")));
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        System.out.println(httpHeaders);

        HttpEntity<User> httpEntity = new HttpEntity<>(user, httpHeaders);
        ResponseEntity<String>responseEntity = restTemplate.postForEntity(URL, httpEntity, String.class);
        return responseEntity;
    }

    //Изменить пользователя с id = 3. Необходимо поменять name на Thomas, а lastName на Shelby. В случае успеха вы получите еще одну часть кода.
    public ResponseEntity<String> updateUser(User user){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Cookie", cookies.stream().collect(Collectors.joining(";")));
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        System.out.println(httpHeaders);

        HttpEntity<User> httpEntity = new HttpEntity<>(user, httpHeaders);
        ResponseEntity<String>responseEntity = restTemplate.exchange(URL, HttpMethod.PUT, httpEntity, String.class);
        return responseEntity;
    }

    //Удалить пользователя с id = 3. В случае успеха вы получите последнюю часть кода.
    public ResponseEntity<String> deleteUser(Long id){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Cookie", cookies.stream().collect(Collectors.joining(";")));
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        System.out.println(httpHeaders);

        HttpEntity<User> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<String>responseEntity = restTemplate.exchange(URL + "/" + id, HttpMethod.DELETE, httpEntity, String.class);
        return responseEntity;
    }






}
