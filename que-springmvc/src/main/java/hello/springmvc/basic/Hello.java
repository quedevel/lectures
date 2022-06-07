package hello.springmvc.basic;

import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

@Component("hello")
public class Hello {

    public String hello(){


        return "Hello!";
    }


}
