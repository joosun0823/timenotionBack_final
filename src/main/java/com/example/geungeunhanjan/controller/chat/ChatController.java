package com.example.geungeunhanjan.controller.chat;

import com.example.geungeunhanjan.domain.dto.board.BoardDTO;
import com.example.geungeunhanjan.domain.dto.board.LifeUserInfoDTO;
import com.example.geungeunhanjan.domain.dto.chat.ChatRoomDTO;
import com.example.geungeunhanjan.domain.vo.chat.ChatMessageVO;
import com.example.geungeunhanjan.mapper.chat.ChatMapper;
import com.example.geungeunhanjan.service.MyPageService;
import com.example.geungeunhanjan.service.chat.ChatService;
import com.example.geungeunhanjan.domain.dto.chat.ChatMessageDTO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.HEAD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping("/chatting")
    public String chatting(Model model, HttpSession session) {

        // 로그인 여부 확인

        Long uniId = (Long) session.getAttribute("uniId");
        if (uniId == null) {

            return "redirect:/user/login";
        }

//        Long uniId = (Long) session.getAttribute("uniId");

        Long roomId = (Long) session.getAttribute("roomId");
        ChatMessageVO chatMessage = chatService.getMessageMostRecent(roomId);
        System.out.println("★★★★★★★★★chatMessage = " + chatMessage);
        model.addAttribute("chatMessage", chatMessage);
        List<ChatRoomDTO> chatRooms = chatService.getChatRoomsByUserId(uniId);
        System.out.println("★★★★★★★★★★chatRooms = " + chatRooms);
        model.addAttribute("chatRooms", chatRooms);
        return "/chat/chatList";
    }

    @GetMapping("/room/{roomId}")
    public String chatRoom(@PathVariable Long roomId, Model model, HttpSession session) {
        ChatRoomDTO chatRoom = chatService.getChatRoomsByRoomId(roomId);
        List<ChatMessageDTO> chatMessageList = chatService.getMessagesByRoomId(roomId);
        Long loginUser = (Long) session.getAttribute("uniId");
        /* ---- */
        ChatRoomDTO chatInfo = chatService.getChatIInfoByRoomId(roomId);
        model.addAttribute("chatInfo", chatInfo);
        /* ---- */
        model.addAttribute("chatRoom", chatRoom);
        model.addAttribute("chatMessageList", chatMessageList);
        System.out.println("chatMessageList = " + chatMessageList);
        model.addAttribute("loginUser", loginUser);
        return "/chat/chatDetail";
    }

    @PostMapping("/check-or-create-room")
    @ResponseBody
    public Map<String, Object> checkOrCreateRoom(@RequestBody Map<String, Long> payload, HttpSession session) {
        Long senderId = (Long) session.getAttribute("uniId");
        Long receiverId = payload.get("receiverId");

        Map<String, Object> response = new HashMap<>();
        try {
            Long roomId = chatService.checkOrCreateRoom(senderId, receiverId);
            response.put("success", true);
            response.put("roomId", roomId);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return response;
    }

    @PostMapping("/send-message")
    @ResponseBody
    public Map<String, Object> sendMessage(@RequestBody Map<String, Object> payload, HttpSession session) {
        Long senderId = (Long) session.getAttribute("uniId");
        Long roomId = (Long) payload.get("roomId");
        String messageContent = (String) payload.get("messageContent");

        Map<String, Object> chatMessage = new HashMap<>();
        chatMessage.put("roomId", roomId);
        chatMessage.put("senderId", senderId);
        chatMessage.put("messageContent", messageContent);

        Map<String, Object> response = new HashMap<>();
        try {
            chatService.insertChatMessage(chatMessage);
            response.put("success", true);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return response;
    }

    @PostMapping("/save-message")
    public ResponseEntity<?> saveMessage(@RequestBody ChatMessageDTO message) {
        Map<String, Object> response = new HashMap<>();
        try {
            Long roomId = message.getRoomId();

            Map<String, Object> messageMap = message.toMap();
            messageMap.put("roomId", roomId);

            chatService.insertChatMessage(messageMap);
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }


    @MessageMapping("/message")
    public void send(ChatMessageDTO message) {
        messagingTemplate.convertAndSend("/topic/messages", message);
    }



}



