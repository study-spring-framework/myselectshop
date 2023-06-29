package com.sparta.myselectshop.controller;

import com.sparta.myselectshop.dto.ProductMypriceRequestDto;
import com.sparta.myselectshop.dto.ProductRequestDto;
import com.sparta.myselectshop.dto.ProductResponseDto;
import com.sparta.myselectshop.security.UserDetailsImpl;
import com.sparta.myselectshop.service.ProductService;
import lombok.RequiredArgsConstructor;
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
     *  @return: List<ProductResponseDto> : 유저가 등록한 모든 상품들
     */
    @GetMapping("/products")
    public List<ProductResponseDto> getProducts(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return productService.getProducts(userDetails.getUser());
    }

    /*
     *  전체상품 조희(어드민 전용)
     *  @return: List<ProductResponseDto> : 모든유저가 등록한 모든 상품들
     */
    @GetMapping("/admin/products")
    public List<ProductResponseDto> getAllProducts() {
        return productService.getAllProducts();
    }
}
