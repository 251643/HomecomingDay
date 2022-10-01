package com.homecomingday.article.responseDto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CheckAllParticipantDto {

    private Long articleId;

    private Integer joinPeople;

    private List<joinPeopleDto> joinPeopleList;
}
