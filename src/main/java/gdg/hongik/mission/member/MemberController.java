package gdg.hongik.mission.member;

// 2주차 강의(Controller/Service) 실습
// 컴파일에러 나지 않도록 주석처리

/*
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequiredArgsConstructor // NoArgs
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/members")
    public ResponseEntity<Void> createMember(@RequestBody MemberCreateRequest request) {
        Long memberId = memberService.createMember(request);
        return ResponseEntity.created(URI.create("/members/" + memberId)).build();
    }

    @GetMapping("/members")
    public ResponseEntity<List<Member>> getAllMember() {
        // Service 계층에서 회원 목록을 가져온다
        List<Member> members = memberService.getAllMembers();
        return ResponseEntity.ok(members);
    }

    @GetMapping("/members/{memberId}")
    public ResponseEntity<Member> getMember(@PathVariable Long memberId) {
        Member member = memberService.getMemberId(memberId);
        return ResponseEntity.ok(member);
    }

    @PatchMapping("/members/{memberId}")
    public ResponseEntity<Void> updateMember(@PathVariable Long memberId, @RequestBody MemberUpdateRequest request) {
        memberService.updateMember(memberId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/members/{memberId}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long memberId) {
        memberService.deleteMember(memberId);
        return ResponseEntity.noContent().build(); // 204 No content
    }
}
*/
