package fourman.backend.domain.freeBoard.controller;
import fourman.backend.domain.freeBoard.controller.requestForm.FreeBoardCommentRequestForm;
import fourman.backend.domain.freeBoard.entity.FreeBoardComment;
import fourman.backend.domain.freeBoard.service.FreeBoardCommentService;
import fourman.backend.domain.questionboard.controller.requestForm.CommentRequestForm;
import fourman.backend.domain.questionboard.entity.Comment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/free-board/comment")
public class FreeBoardCommentController {

    @Autowired
    private FreeBoardCommentService freeBoardCommentService;

    @PostMapping("/register")
    public void freeBoardCommentRegister(@RequestBody FreeBoardCommentRequestForm freeBoardCommentRequestForm) {
        log.info("freeBoardCommentRegister()");
        freeBoardCommentService.register(freeBoardCommentRequestForm);
    }

    @GetMapping("/list/{boardId}")
    public List<FreeBoardComment> freeBoardCommentList (@PathVariable("boardId") Long boardId) {
        log.info("commentList");
        return freeBoardCommentService.commentList(boardId);
    }

    @DeleteMapping("/delete/{commentId}")
    public void commentDelete(@PathVariable("commentId") Long commentId) {
        log.info("commentDelete()");
        freeBoardCommentService.commentDelete(commentId);
    }

//    @PutMapping("/modify/{commentId}")
//    public FreeBoardComment commentModify(@PathVariable("commentId") Long commentId,
//                                 @RequestBody FreeBoardCommentRequestForm freeBoardCommentRequestForm) {
//        return freeBoardCommentService.commentModify(commentId, freeBoardCommentRequestForm);
//    }

}
