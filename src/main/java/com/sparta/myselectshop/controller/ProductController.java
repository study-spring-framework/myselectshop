package com.sparta.myselectshop.controller;

import com.sparta.myselectshop.dto.ProductMypriceRequestDto;
import com.sparta.myselectshop.dto.ProductRequestDto;
import com.sparta.myselectshop.dto.ProductResponseDto;
import com.sparta.myselectshop.entity.User;
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

    /*
     *  관심 상품 등록
     *  @Param: ProductRequestDto(제품정보 전달), UserDetailsImpl(회원정보 전달)
     *  @return: productResponseDto
     */
    @PostMapping("/products")
    public ProductResponseDto createProduct(@RequestBody ProductRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return productService.createProduct(requestDto,userDetails.getUser());
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
}
