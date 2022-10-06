package com.homecomingday.article;


import com.homecomingday.domain.UserDetailsImpl;
import com.homecomingday.util.CacheKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CacheSevice {

    @Caching(evict = {
        @CacheEvict(value = CacheKey.ARTICLE, key = "#articleId"),
//        @CacheEvict(value = "#articleFlag"+"Pop", key = "#userDetails.getMember().getSchoolName()"),
//        @CacheEvict(value = "#articleFlag", key = "#userDetails.getMember().getSchoolName()"),
        @CacheEvict(value = CacheKey.ARTICLES, key = "#articleFlag+ #userDetails.getMember().getSchoolName()"),
        @CacheEvict(value = CacheKey.ARTICLEPOP, key = "#articleFlag+ #userDetails.getMember().getSchoolName()"),
        @CacheEvict(value = CacheKey.SEARCHPOP, key = "#userDetails.getMember().getSchoolName()"),
        @CacheEvict(value = CacheKey.SERACH, key = "#userDetails.getMember().getSchoolName()")
    })
    public boolean deleteBoardCache(long articleId, UserDetailsImpl userDetails, String articleFlag) {
        log.debug("deleteBoardCache - articleId {}, schoolName {}", articleId, userDetails.getMember().getSchoolName());
        return true;
    }
}
