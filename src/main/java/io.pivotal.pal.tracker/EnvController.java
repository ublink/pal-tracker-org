package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EnvController {

    private String message;

    public EnvController(){}

    public EnvController(@Value("${welcome.message}") String message){
        this.message = message;
    }

    @GetMapping("/")
    public String sayHello() {
        return "hello";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
