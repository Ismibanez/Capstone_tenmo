package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import okhttp3.internal.http2.Header;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.WildcardType;
import java.util.HashMap;
import java.util.Map;

public class TenmoService {

    private static final String API_BASE_URL = "http://localhost:8080/";
    private AuthenticatedUser user;
    private RestTemplate restTemplate;

    public TenmoService(){
        this.restTemplate = new RestTemplate();
    }

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
        System.out.println(response);
        return response.getBody();
    }

    public void setUser(AuthenticatedUser user) {
        this.user = user;
    }

    public void createTransferRequest(long receiverID, double amount) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);


        HttpEntity<Transfer> entity = new HttpEntity<>(new Transfer(Transfer.Type.Send,user.getUser().getId(),receiverID,amount),headers);

        restTemplate.postForObject(API_BASE_URL + "account/transfer",entity, Transfer.class);
    }
}
