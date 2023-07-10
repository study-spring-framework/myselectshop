package com.sparta.myselectshop.dto;

import com.sparta.myselectshop.entity.Folder;
import com.sparta.myselectshop.entity.Product;
import com.sparta.myselectshop.entity.ProductFolder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ProductResponseDto {
    private Long id;
    private String title;
    private String link;
    private String image;
    private int lprice;
    private int myprice;

    // 폴더 조회 강의에서 추가 된 코드: 폴더에 대한 정보 보내주기(ResponseDto로 변환해서)
    // -> 관심상품 하나에 여러개의 폴더가 해시태그처럼 추가 될 수 있다는 요구사항 만족하기 위해 사용!
    private List<FolderResponseDto> productFolderList = new ArrayList<>();

    public ProductResponseDto(Product product) {
        this.id = product.getId();
        this.title = product.getTitle();
        this.link = product.getLink();
        this.image = product.getImage();
        this.lprice = product.getLprice();
        this.myprice = product.getMyprice();
        // 생성자를 통해서 productRsponseDto 만들 떄 pram으로 product를 받아 오는데 ()
        for (ProductFolder productFolder : product.getProductFolderList()) {
            productFolderList.add(new FolderResponseDto(productFolder.getFolder()));
        }
    }
}