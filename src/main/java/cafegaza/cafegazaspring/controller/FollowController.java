package cafegaza.cafegazaspring.controller;

import cafegaza.cafegazaspring.domain.Member;
import cafegaza.cafegazaspring.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;

    @PostMapping("/follow")
    public ResponseEntity follow(@RequestParam("pageMemberId") Long pageMemberId, @SessionAttribute(name = "sessionId", required = false) Long sessionId) {
        followService.follow(sessionId, pageMemberId);

        return ResponseEntity.ok("follow success");
    }

    @PostMapping("/unfollow")
    public ResponseEntity unfollow(@RequestParam("pageMemberId") Long pageMemberId, @SessionAttribute(name = "sessionId", required = false) Member sessionId) {
        followService.unFollow(sessionId, pageMemberId);

        return ResponseEntity.ok("unfollow success");
    }
}
