package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class TenmoService {

    private static final String API_BASE_URL = "http://localhost:8080/";
    private AuthenticatedUser user;
    private RestTemplate restTemplate;

    public TenmoService(){}

    private HttpEntity<Void> makeHttpEntityWithToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getToken());
        return new HttpEntity<>(headers);
    }

    public Double getBalance(){
        Double balance = null;
        try {
            ResponseEntity<Double> response = restTemplate.exchange(API_BASE_URL + "/account/balance", HttpMethod.GET,
                    makeHttpEntityWithToken(), Double.class);
            balance = response.getBody();
        }catch(RestClientResponseException | ResourceAccessException e){
            System.err.println("Could not accesses balance, please try again.");
        }
        return balance;
    }
    public User[] listUsers(){
        ResponseEntity<User[]> response = restTemplate.exchange(API_BASE_URL + "/users", HttpMethod.GET,
                makeHttpEntityWithToken(), User[].class);
        return response.getBody();
    }

    public void setUser(AuthenticatedUser user) {
        this.user = user;
    }
}
