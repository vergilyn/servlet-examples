package com.vergilyn.examples.tomcat;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @author VergiLyn
 * @bolg http://www.cnblogs.com/VergiLyn/
 * @date 2017/1/8
 */
@Controller
public class HelloController {

    @RequestMapping("/index")
    @ResponseBody
    public String index(Model model) {
        model.addAttribute("time", System.currentTimeMillis());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "index";
    }

}
