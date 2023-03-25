package gdsc.skhu.liferary.service;

import gdsc.skhu.liferary.domain.Category;
import gdsc.skhu.liferary.domain.DTO.ImageDTO;
import gdsc.skhu.liferary.domain.DTO.MainPostDTO;
import gdsc.skhu.liferary.domain.MainPost;
import gdsc.skhu.liferary.domain.Member;
import gdsc.skhu.liferary.repository.mainpost.MainPostRepository;
import gdsc.skhu.liferary.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class MainPostService {
    private final MemberRepository memberRepository;
    private final MainPostRepository mainPostRepository;
    private final ImageService imageService;

    // Create
    public MainPostDTO.Response save(String username, MainPostDTO.Request request) throws IOException {
        MainPost mainPost = MainPost.builder()
                .title(request.getTitle())
                .author(memberRepository.findByEmail(username)
                        .orElseThrow(() -> new NoSuchElementException("Member not found")))
                .category(Category.valueOf(request.getCategory().toUpperCase()))
                .context(request.getContext())
                .images(new ArrayList<>())
                .video(request.getVideo())
                .build();
        saveWithImage(mainPost, request.getImages());
        return new MainPostDTO.Response(mainPost);
    }

    // Read
    @Transactional(readOnly = true)
    public Page<MainPostDTO.Response> findAll(Pageable pageable) {
        return mainPostRepository.findAll(pageable).map(mainPost -> {
            if(mainPost.getImages() != null) {
                mainPost.getImages().replaceAll(
                        storedImageName -> imageService.findByStoredImageName(storedImageName).getImagePath()
                );
            }
            return mainPost;
        }).map(MainPostDTO.Response::new);
    }

    @Transactional(readOnly = true)
    public MainPostDTO.Response findById(Long id) {
        return new MainPostDTO.Response(mainPostRepository.findById(id).map(mainPost -> {
            if(mainPost.getImages() != null) {
                mainPost.getImages().replaceAll(
                        storedImageName -> imageService.findByStoredImageName(storedImageName).getImagePath()
                );
            }
            return mainPost;
        }).orElseThrow(() -> new NoSuchElementException("There is no Main Post with this ID")));
    }

    @Transactional(readOnly = true)
    public Page<MainPostDTO.Response> findByMember(Pageable pageable, String username) {
        Member member = memberRepository.findByEmail(username)
                .orElseThrow(() -> new NoSuchElementException("Member not found"));
        return mainPostRepository.findByAuthor(pageable, member).map(mainPost -> {
            if(mainPost.getImages() != null) {
                mainPost.getImages().replaceAll(
                        storedImageName -> imageService.findByStoredImageName(storedImageName).getImagePath()
                );
            }
            return mainPost;
        }).map(MainPostDTO.Response::new);
    }

    @Transactional(readOnly = true)
    public Page<MainPostDTO.Response> findByCategory(Pageable pageable, String category) {
        return mainPostRepository.findByCategory(pageable, Category.valueOf(category.toUpperCase()))
                .map(mainPost -> {
                    if(mainPost.getImages() != null) {
                        mainPost.getImages().replaceAll(
                                storedImageName -> imageService.findByStoredImageName(storedImageName).getImagePath()
                        );
                    }
                    return mainPost;
                }).map(MainPostDTO.Response::new);
    }

    @Transactional(readOnly = true)
    public Page<MainPostDTO.Response> findByKeyword(Pageable pageable, String keyword) {
        return mainPostRepository.findByKeyword(pageable, keyword).map(mainPost -> {
            if(mainPost.getImages() != null) {
                mainPost.getImages().replaceAll(
                        storedImageName -> imageService.findByStoredImageName(storedImageName).getImagePath()
                );
            }
            return mainPost;
        });
    }

    // Update
    @Transactional
    public MainPostDTO.Response update(String username, MainPostDTO.Update update, Long id) throws IOException {
        MainPost oldMainPost = mainPostRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("There is no Main Post with this ID"));
        MainPost newMainPost;
        if(username.equals(oldMainPost.getAuthor().getEmail())) {
            newMainPost = MainPost.builder()
                    .id(id)
                    .title(update.getTitle())
                    .author(oldMainPost.getAuthor())
                    .category(Category.valueOf(update.getCategory().toUpperCase()))
                    .context(update.getContext())
                    .images(new ArrayList<>())
                    .video(update.getVideo())
                    .build();
        } else {
            throw new AuthorizationServiceException("Unauthorized access");
        }
        saveWithImage(newMainPost, update.getImages());
        return new MainPostDTO.Response(newMainPost);
    }

    // Delete
    @Transactional
    public ResponseEntity<String> delete(Long id) {
        try {
            MainPost mainPost = mainPostRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Main post not found"));
            if(mainPost.getImages() != null) {
                for(String imageName : mainPost.getImages()) {
                    imageService.deleteImage("main/", imageName);
                }
            }
            mainPostRepository.deleteById(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>("Exception occurred", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok("Delete success");
    }

    // Util
    private void saveWithImage(MainPost mainPost, List<MultipartFile> images) throws IOException {
        if (images != null) {
            for (MultipartFile file : images) {
                ImageDTO.Response image = imageService.uploadImage("main/", file);
                mainPost.getImages().add(image.getStoredImageName());
            }
        }
        mainPostRepository.saveAndFlush(mainPost);
        if(mainPost.getImages() != null) {
            mainPost.getImages().replaceAll(storedImageName -> imageService.findByStoredImageName(storedImageName).getImagePath());
        }
    }
}
