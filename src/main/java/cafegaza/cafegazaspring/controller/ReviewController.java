package cafegaza.cafegazaspring.controller;

import cafegaza.cafegazaspring.domain.Review;
import cafegaza.cafegazaspring.domain.uploadFile.ReviewImage;
import cafegaza.cafegazaspring.dto.ReviewDto;
import cafegaza.cafegazaspring.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /**
        해당 페이지의 리뷰 목록 가져오기
     */
    @ResponseBody
    @GetMapping("/review/{cafeId}") // ?page=0&size=15
    public Page<ReviewDto> reviewList(@PathVariable("cafeId") Long cafeId, Pageable pageable) {
        return reviewService.findList(cafeId, pageable);
    }

    /**
        리뷰 작성을 위한 폼 가져오기
     */
    @GetMapping("/review/{cafeId}/new")
    public String createReviewForm(@PathVariable("cafeId") Long cafeId, Model model) { // 작성자 정보도 필요

        model.addAttribute("reviewForm", new ReviewForm());
        return "createReviewForm";

    }

    /**
        작성된 리뷰 폼을 받아 리뷰 생성
     */
    @PostMapping("/review/{cafeId}/new")
    public String createReview(@PathVariable("cafeId") Long cafeId, @Valid ReviewForm reviewForm, BindingResult result) throws Exception { // 작성자 정보도 필요

        if (result.hasErrors()) {
            List<ObjectError> list =  result.getAllErrors();
            for(ObjectError e : list) {
                System.out.println(e.getDefaultMessage());
            }
            return "createReviewForm";
        }

        reviewService.create(cafeId, reviewForm);
        return "redirect:/";
    }

    /**
        리뷰 수정 폼 가져오기
     */
    @GetMapping("/review/{reviewId}/edit")
    public String updateReviewForm(@PathVariable("reviewId") Long reviewId, Model model) {
        Review findReview = reviewService.findOne(reviewId); // 수정 대상 리뷰

        // 수정을 위해 이전 리뷰 내용을 넣어서 반환
        ReviewForm reviewForm = new ReviewForm();
        reviewForm.setId(findReview.getReviewId());
        reviewForm.setContent(findReview.getContent());
        reviewForm.setRate(findReview.getRate());

        List<MultipartFile> multipartFiles = new ArrayList<>();
        for (ReviewImage reviewImage : findReview.getReviewImages()) {
            MultipartFile file = (MultipartFile) new File(reviewImage.getFilePath());
            multipartFiles.add(file);
        }

        model.addAttribute("reviewForm", reviewForm);
        return "review/update";

    }

    /**
        리뷰 수정
     */
    @PostMapping("/review/{reviewId}/edit")
    public String updateReview(@PathVariable("reviewId") Long reviewId, @Valid ReviewForm reviewForm, BindingResult result) throws Exception {

        if(result.hasErrors()) {
            return "review/update";
        }

        reviewService.update(reviewId, reviewForm);
        return "redirect:/";

    }

    /**
        리뷰 삭제
     */
    @GetMapping("/review/{reviewId}/delete")
    public String deleteReview(@PathVariable("reviewId") Long reviewId) {

        reviewService.delete(reviewId);
        return "redirect:/";

    }

}
