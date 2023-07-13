package com.sparta.myselectshop.controller;

import com.sparta.myselectshop.dto.ProductMypriceRequestDto;
import com.sparta.myselectshop.dto.ProductRequestDto;
import com.sparta.myselectshop.dto.ProductResponseDto;
import com.sparta.myselectshop.entity.ApiUseTime;
import com.sparta.myselectshop.entity.User;
import com.sparta.myselectshop.repository.ApiUseTimeRepository;
import com.sparta.myselectshop.security.UserDetailsImpl;
import com.sparta.myselectshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;
    private final ApiUseTimeRepository apiUseTimeRepository;

    /*
     *  관심 상품 등록
     *  @Param: ProductRequestDto(제품정보 전달), UserDetailsImpl(회원정보 전달)
     *  @return: productResponseDto
     */
    @PostMapping("/products")
    public ProductResponseDto createProduct(@RequestBody ProductRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 측정 시작 시간
        long startTime = System.currentTimeMillis();

        try {
            // 응답 보내기
            return productService.createProduct(requestDto, userDetails.getUser());
        } finally {
            // 측정 종료 시간
            long endTime = System.currentTimeMillis();
            // 수행시간 = 종료 시간 - 시작 시간
            long runTime = endTime - startTime;

            // 로그인 회원 정보
            User loginUser = userDetails.getUser();

            // API 사용시간 및 DB 에 기록
            ApiUseTime apiUseTime = apiUseTimeRepository.findByUser(loginUser)
                    .orElse(null);
            if (apiUseTime == null) {
                // 로그인 회원의 기록이 없으면
                apiUseTime = new ApiUseTime(loginUser, runTime);
            } else {
                // 로그인 회원의 기록이 이미 있으면
                apiUseTime.addUseTime(runTime);
            }

            System.out.println("[API Use Time] Username: " + loginUser.getUsername() + ", Total Time: " + apiUseTime.getTotalTime() + " ms");
            apiUseTimeRepository.save(apiUseTime);
        }
    }

    @PutMapping("/products/{id}")
    public ProductResponseDto updateProduct(@PathVariable Long id, @RequestBody ProductMypriceRequestDto requestDto) {
        return productService.updateProduct(id,requestDto);
    }

    /*
     *  관심 상품 조희
     *  @Param: UserDetailsImpl(회원정보 전달)
     *  @return: Page<ProductResponseDto> : 유저가 등록한 모든 상품들
     */
    @GetMapping("/products")
    public Page<ProductResponseDto> getProducts(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sortBy") String sortBy,
            @RequestParam("isAsc") boolean isAsc,
            @AuthenticationPrincipal UserDetailsImpl userDetails){
        return productService.getProducts(userDetails.getUser(),page-1,size,sortBy,isAsc);
    }

    /**
     * 폴더에 관심 상품을 등록 (ProductController)
     * @param productId 등록을 할 상품의 ID
     * @param folderId 추가할 폴더의 ID
     * @param userDetails 상품의 폴더를 추가 할 떄 해당 상품과 그 해당 폴더가 현재 로그인한 유저의 상품의 폴더가 일치 하는지 확인
     */
    @PostMapping("/products/{productId}/folder")
    public void addFolder(
            @PathVariable Long productId,
            @RequestParam Long folderId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        productService.addFolder(productId,folderId,userDetails.getUser());
    }

    /**
     * 해당 폴더에 등록이 되어 있는 products들을 조회
     * @param folderId 폴더 ID 받아오기
     * @param page 현재페이지
     * @param size 노출 개수
     * @param sortBy 정렬방법
     * @param isAsc 오름차순인가?
     * @param userDetails 유저정보
     * @return
     */
    @GetMapping("/folders/{folderId}/products")
    public Page<ProductResponseDto> getProductsInFolder(
            @PathVariable Long folderId,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sortBy") String sortBy,
            @RequestParam("isAsc") boolean isAsc,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return productService.getProductsInFolder(
                folderId,
                page-1,
                size,
                sortBy,
                isAsc,
                userDetails.getUser()
        );
    }
}
