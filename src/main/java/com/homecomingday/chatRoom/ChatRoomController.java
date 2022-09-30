package com.homecomingday.chatRoom;

import com.amazonaws.services.kms.model.NotFoundException;
import com.homecomingday.chat.RedisRepository;
import com.homecomingday.chat.responseDto.ChatMessageTestDto;
import com.homecomingday.chatRoom.requestDto.ChatRoomUserRequestDto;
import com.homecomingday.chatRoom.responseDto.ChatRoomListResponseDto;
import com.homecomingday.chatRoom.responseDto.ChatRoomOtherMemberInfoResponseDto;
import com.homecomingday.chatRoom.responseDto.ChatRoomResponseDto;
import com.homecomingday.chatRoom.responseDto.ChatRoomUuidDto;
import com.homecomingday.domain.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final RedisRepository redisRepository;
    private final ChatRoomRepository chatRoomRepository;

    //방생성
    @PostMapping ("/chat/rooms")
    public String createChatRoom(
        @RequestBody ChatRoomUserRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String chatRoomUuid = chatRoomService.createChatRoom(requestDto, userDetails);
        Long chatPartnerUserId = requestDto.getUserId();
        Long myUserId = userDetails.getMember().getId();

        // redis repository에 채팅방에 존재하는 사람 마다 안 읽은 메세지의 갯수 초기화
        redisRepository.initChatRoomMessageInfo(chatRoomUuid, myUserId);
        redisRepository.initChatRoomMessageInfo(chatRoomUuid, chatPartnerUserId);

        return chatRoomUuid;
    }

    //내가 가진 채팅방 조회
    @GetMapping ("/chat/rooms/{page}")
    public ChatRoomListResponseDto getChatRoom (@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                @PathVariable int page
                                                 ) {
        //page -= 1;
        return chatRoomService.getChatRoom(userDetails, page);
    }

    //채팅방 삭제
    @DeleteMapping("chat/rooms/{roomId}")
    public void deleteChatRoom(@PathVariable String roomId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        //roonId=uuid
        //방번호랑 나간 사람
        ChatRoom chatroom = chatRoomRepository.findByChatRoomUuid(roomId).orElseThrow(
                () -> new NotFoundException("존재하지 않는 채팅방입니다.")
        );

        chatRoomService.deleteChatRoom(chatroom, userDetails.getMember());
    }

    //이전 채팅 메시지 불러오기
    @GetMapping("/chat/rooms/{roomId}/messages")
    public List<ChatMessageTestDto> getPreviousChatMessage(@PathVariable String roomId, @AuthenticationPrincipal UserDetailsImpl userDetails){

        return chatRoomService.getPreviousChatMessage(roomId, userDetails);
    }

    //채팅방 입장시 상대정보 조회
    @GetMapping("/chat/rooms/otherUserInfo/{roomId}")
    public ChatRoomOtherMemberInfoResponseDto getOtherUserInfo(
            @PathVariable String roomId,
            @AuthenticationPrincipal UserDetailsImpl userDetails){

        return chatRoomService.getOtherUserInfo(roomId, userDetails);
    }
}
