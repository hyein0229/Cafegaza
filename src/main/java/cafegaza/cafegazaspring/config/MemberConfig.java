package cafegaza.cafegazaspring.config;

import cafegaza.cafegazaspring.repository.FollowRepository;
import cafegaza.cafegazaspring.repository.MemberRepository;
import cafegaza.cafegazaspring.service.MemberService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@PropertySource("classpath:application.properties")
public class MemberConfig {

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;

    public MemberConfig(MemberRepository memberRepository, FollowRepository followRepository){
        this.memberRepository = memberRepository;
        this.followRepository = followRepository;
    }

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository, followRepository, javaMailSender());
    }

    @Value("${mail.smtp.port}")
    private int port;

    @Value("${mail.smtp.socketFactory.port}")
    private int socketPort;

    @Value("${mail.smtp.auth}")
    private boolean auth;

    @Value("${mail.smtp.starttls.enable}")
    private boolean starttls;

    @Value("${mail.smtp.starttls.required}")
    private boolean starttls_required;

    @Value("${mail.smtp.socketFactory.fallback}")
    private boolean fallback;

    @Value("${AdminMail.id}")
    private String id;

    @Value("${AdminMail.password}")
    private String password;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.gmail.com");
        javaMailSender.setUsername(id);
        javaMailSender.setPassword(password);
        javaMailSender.setPort(port);
        javaMailSender.setJavaMailProperties(getMailProperties());
        javaMailSender.setDefaultEncoding("UTF-8");

        return javaMailSender;
    }

    private Properties getMailProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.socketFactory.port", socketPort);
        properties.put("mail.smtp.auth", auth);
        properties.put("mail.smtp.starttls.enable", starttls);
        properties.put("mail.smtp.starttls.required", starttls_required);
        properties.put("mail.smtp.socketFactory.fallback",fallback);
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        return properties;
    }
}
