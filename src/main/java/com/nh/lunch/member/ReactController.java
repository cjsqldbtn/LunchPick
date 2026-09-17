package com.nh.lunch.member;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReactController {
	@GetMapping({"/kakaologin","/naverlogin"})
    public String forwardReactRoutes() {
        return "forward:/index.html";
    }
}
