package com.sparta.myselectshop.service;

import com.sparta.myselectshop.dto.FolderResponseDto;
import com.sparta.myselectshop.entity.Folder;
import com.sparta.myselectshop.entity.User;
import com.sparta.myselectshop.repository.FolderRepository;
import com.sparta.myselectshop.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FolderService {
    private final FolderRepository folderRepository;
    public void addFolders(List<String> folderNames, User user) {
        // 로그인한 회원에 폴더들 등록

        List<Folder> existFolderList = folderRepository.findAllByUserAndNameIn(user,folderNames);
        // 한번에 조건을 주기 위해서는 In을 넣어준다

        List<Folder> folderList = new ArrayList<>();

        for (String folderName : folderNames) {
            // 이미 생성한 폴더가 아닌 경우만 폴더 생성
            if(!isExistFolderName(folderName, existFolderList)){
                Folder folder = new Folder(folderName, user);
                folderList.add(folder);
            }
            else {
                throw new IllegalArgumentException("폴더명이 중복되었습니다");
            }
        }

        folderRepository.saveAll(folderList);
    }

    public List<FolderResponseDto> getFolders(User user) {
        // 로그인한 회원이 등록된 모든 폴더 조회

        List<Folder> folderList = folderRepository.findAllByUser(user);
        List<FolderResponseDto> folderResponseDtoList = new ArrayList<>();

        for (Folder folder : folderList) {
            folderResponseDtoList.add(new FolderResponseDto(folder));
        }

        return folderResponseDtoList;
    }

    private Boolean isExistFolderName(String folderName, List<Folder> existFolderList) {
        for (Folder existFolder : existFolderList) {
            if(folderName.equals(existFolder.getName())){
                return true;
            }
        }
        return false;
    }
}
