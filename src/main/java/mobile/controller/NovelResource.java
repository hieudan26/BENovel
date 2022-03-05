package mobile.controller;

import mobile.Service.ChapterService;
import mobile.Service.EmailService;
import mobile.Service.NovelService;
import mobile.model.Entity.Chapter;
import mobile.model.Entity.Novel;
import mobile.model.payload.response.BaseCustomResponse.RecordNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.PageRequest;

import java.util.List;

@RestController
@RequestMapping("api/novels")
@RequiredArgsConstructor
public class NovelResource {
    private static final Logger LOGGER = LogManager.getLogger(NovelResource.class);

    private final NovelService novelService;
    private final ChapterService chapterService;

    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<List<Novel>> getNovels(
                                                    @RequestParam(defaultValue = "None") String status,
                                                    @RequestParam(defaultValue = "tentruyen") String sort,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "3") int size) {


        Pageable pageable = PageRequest.of(page, size,Sort.by(sort));
        List<Novel> novelList = null;
        if(status.equals("None"))
            novelList = novelService.getNovels(pageable);
        else
            novelList =  novelService.findAllByStatus(status,pageable);

        if(novelList == null) {
            throw new RecordNotFoundException("No Novel existing " );
        }
        return new ResponseEntity<List<Novel>>(novelList, HttpStatus.OK);
    }

    @GetMapping("/theloai/{theloai}")
    @ResponseBody
    public ResponseEntity<List<Novel>> getNovelsByType(
            @PathVariable String theloai,
            @RequestParam(defaultValue = "tentruyen") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {
        Pageable pageable = PageRequest.of(page, size,Sort.by(sort));
        List<Novel> novelList = null;
        novelList =  novelService.SearchByType(theloai,pageable);

        if(novelList == null) {
            throw new RecordNotFoundException("No Novel existing " );
        }
        return new ResponseEntity<List<Novel>>(novelList, HttpStatus.OK);
    }

    @GetMapping("/search")
    @ResponseBody
    public ResponseEntity<List<Novel>> searchNovel(
            @RequestParam(defaultValue = "") String theloai,
            @RequestParam(defaultValue = "") String value,
            @RequestParam(defaultValue = "tentruyen") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {
        Pageable pageable = PageRequest.of(page, size,Sort.by(sort));
        List<Novel> novelList = null;
        if(theloai.equals(""))
        {
            novelList =  novelService.SearchByTentruyen(value,pageable);
        }
        else {
            novelList =  novelService.SearchByTypeAndTentruyen(theloai,value,pageable);
        }

        if(novelList == null) {
            throw new RecordNotFoundException("No Novel existing " );
        }
        return new ResponseEntity<List<Novel>>(novelList, HttpStatus.OK);
    }



    @GetMapping("/novel/{name}")
    @ResponseBody
    public ResponseEntity<Novel> getNovelByName(@PathVariable String name) {

        Novel novel = novelService.findByName(name);
        if(novel == null) {
            throw new RecordNotFoundException("No Novel existing " );
        }
        return new ResponseEntity<Novel>(novel, HttpStatus.OK);
    }

    @GetMapping("/novel/{name}/chuong")
    @ResponseBody
    public ResponseEntity<List<Chapter>> getChapterpagination(@PathVariable String name,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "3") int size) {
        Pageable pageable = PageRequest.of(page, size,Sort.by("chapnumber"));

        Novel novel = novelService.findByName(name);
        if(novel == null) {
            throw new RecordNotFoundException("Not found novel: "+name);
        }

        List<Chapter> chapterList = chapterService.findByDauTruyen(novel.getId(),pageable);
        if(chapterList == null) {
            throw new RecordNotFoundException("No Chapter existing");
        }
        return new ResponseEntity<List<Chapter>>(chapterList, HttpStatus.OK);
    }

    @GetMapping("/novel/{name}/chuong/{chapterNumber}")
    @ResponseBody
    public ResponseEntity<Chapter> getChapter(@PathVariable String name,@PathVariable int chapterNumber) {
        Novel novel = novelService.findByName(name);
        Chapter chapter = chapterService.findByDauTruyenAndChapterNumber(novel.getId(),chapterNumber);
        if(chapter == null) {
            throw new RecordNotFoundException("No Chapter existing " );
        }
        return new ResponseEntity<Chapter>(chapter, HttpStatus.OK);
    }

    @GetMapping("/tacgia/{tacgia}")
    @ResponseBody
    public ResponseEntity<List<Novel>> searchNovelByTacgia(
            @PathVariable String tacgia,
            @RequestParam(defaultValue = "tentruyen") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {
        Pageable pageable = PageRequest.of(page, size,Sort.by(sort));
        List<Novel> novelList =  novelService.SearchByTacgia(tacgia,pageable);

        if(novelList == null) {
            throw new RecordNotFoundException("No Novel existing " );
        }
        return new ResponseEntity<List<Novel>>(novelList, HttpStatus.OK);
    }
}
