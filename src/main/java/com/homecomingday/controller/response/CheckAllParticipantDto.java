package com.homecomingday.controller.response;


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

    private List<joinPeopleDto> joinPeopleList;
}
