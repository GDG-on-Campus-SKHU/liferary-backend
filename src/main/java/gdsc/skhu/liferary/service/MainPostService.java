package gdsc.skhu.liferary.service;

import gdsc.skhu.liferary.domain.BoardPost;
import gdsc.skhu.liferary.domain.Category;
import gdsc.skhu.liferary.domain.DTO.ImageDTO;
import gdsc.skhu.liferary.domain.DTO.MainPostDTO;
import gdsc.skhu.liferary.domain.MainPost;
import gdsc.skhu.liferary.repository.MainPostRepository;
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
import java.security.Principal;
import java.util.ArrayList;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class MainPostService {
    private final MemberRepository memberRepository;
    private final MainPostRepository mainPostRepository;
    private final ImageService imageService;

    // Create
    @Transactional
    public MainPostDTO.Response save(Principal principal, MainPostDTO.Request request) throws IOException {
        MainPost mainPost = MainPost.builder()
                .title(request.getTitle())
                .author(memberRepository.findByEmail(principal.getName())
                        .orElseThrow(() -> new NoSuchElementException("Member not found")))
                .category(Category.valueOf(request.getCategory().toUpperCase()))
                .context(request.getContext())
                .images(new ArrayList<>())
                .video(request.getVideo())
                .build();
        if(request.getImages() != null) {
            for(MultipartFile file : request.getImages()) {
                ImageDTO.Response image = imageService.uploadImage("main/", file);
                mainPost.getImages().add(image.getStoredImageName());
            }
        }
        mainPost = mainPostRepository.save(mainPost);
        for(int i = 0; i < mainPost.getImages().size(); i++) {
            mainPost.getImages().set(i, imageService.findByStoredImageName(mainPost.getImages().get(i)).getImagePath());
        }
        return new MainPostDTO.Response(mainPostRepository.save(mainPost));
    }

    // Read
    @Transactional
    public MainPostDTO.Response findById(Long id) {
        return new MainPostDTO.Response(mainPostRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("There is no Main Post with this ID")));
    }

    public Page<MainPostDTO.Response> findByCategory(Pageable pageable, String category) {
        return mainPostRepository.findByCategory(pageable, category).map(MainPostDTO.Response::new);
    }

    public Page<MainPostDTO.Response> findAll(Pageable pageable) {
        return mainPostRepository.findAll(pageable).map(MainPostDTO.Response::new);
    }

    // Update
    @Transactional
    public MainPostDTO.Response update(Principal principal, MainPostDTO.Update update, Long id) throws IOException {
        MainPost oldMainPost = mainPostRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("There is no Main Post with this ID"));
        MainPost newMainPost;
        if(principal.getName().equals(oldMainPost.getAuthor().getEmail())) {
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
        
        if(update.getImages() != null) {
            for(MultipartFile file : update.getImages()) {
                ImageDTO.Response image = imageService.uploadImage("main/", file);
                newMainPost.getImages().add(image.getStoredImageName());
            }
        }
        newMainPost = mainPostRepository.save(newMainPost);
        for(int i = 0; i < newMainPost.getImages().size(); i++) {
            newMainPost.getImages().set(i, imageService.findByStoredImageName(newMainPost.getImages().get(i)).getImagePath());
        }
        return this.findById(id);
    }

    // Delete
    @Transactional
    public ResponseEntity<String> delete(Long id) {
        try {
            MainPost mainPost = mainPostRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Main post not found"));
            if(mainPost.getImages() != null) {
                for(String path : mainPost.getImages()) {
                    imageService.deleteImage(path);
                }
            }
            mainPostRepository.deleteById(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>("Exception occurred", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok("Delete success");
    }
}
