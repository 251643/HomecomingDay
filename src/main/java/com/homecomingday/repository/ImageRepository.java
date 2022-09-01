package com.homecomingday.repository;


import com.homecomingday.domain.Free;
import com.homecomingday.domain.Help;
import com.homecomingday.domain.HoneyTip;
import com.homecomingday.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image,Long> {
    List<Image> findAllByFree(Free freeId);

    List<Image> findAllByHelp(Help helpId);

    List<Image>findAllByHoneyTip(HoneyTip honeyTipId);

    List<Image> findAllByBoardName(Long articleId);
}
