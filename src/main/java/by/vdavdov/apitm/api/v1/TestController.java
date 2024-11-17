package by.vdavdov.apitm.api.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class TestController {

    @GetMapping("/unsecured")
    public String unsecured() {
        return "unsecured data";
    }

    @GetMapping("/secured")
    public String secured() {
        return "secured data";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin data";
    }

    @GetMapping("/info")
    public String userData(Principal principal) {
        return principal.getName();
    }
}
