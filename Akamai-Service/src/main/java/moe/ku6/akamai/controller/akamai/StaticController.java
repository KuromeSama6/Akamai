package moe.ku6.akamai.controller.akamai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/", consumes = "*/*", produces = "text/html")
public class StaticController {
    @Qualifier("webApplicationContext")
    @Autowired
    private ResourceLoader resourceLoader;

    @GetMapping(value = "", consumes = "*/*", produces = "text/html")
    public ResponseEntity<Resource> GetGeneralStatus() {
        var ret = resourceLoader.getResource("classpath:static/index.html");
        return ResponseEntity.ok().body(ret);
    }
}
