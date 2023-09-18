package cafegaza.cafegazaspring.config;

import cafegaza.cafegazaspring.repository.MemberRepository;
import cafegaza.cafegazaspring.service.LoginService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoginConfig {

    private final MemberRepository memberRepository;
    public LoginConfig(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }

    @Bean
    public LoginService loginService() {
        return new LoginService(memberRepository);
    }
}
