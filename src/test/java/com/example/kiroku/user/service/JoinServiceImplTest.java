package com.example.kiroku.user.service;

import com.example.kiroku.user.JoinStatus;
import com.example.kiroku.user.domain.User;
import com.example.kiroku.user.domain.type.UserType;
import com.example.kiroku.user.dto.JoinDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JoinServiceImplTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private JoinServiceImpl joinService;

    private JoinDto joinRequest;
    private User user;
    private User emptyUser;

    @BeforeEach
    void setUp() {
        // 테스트용 JoinRequest 객체 생성
        joinRequest = new JoinDto("testId",
                "password123",
                "01012345678");

        // 테스트용 User 객체 생성
        user = new User("testName",
                "testId",
                "password123",
                "01012345678",
                UserType.ROLE_USER);

        // 빈 User 객체 생성
        emptyUser = User.emptyUser();
    }

    @Test
    @DisplayName("회원 가입 성공 테스트")
    void joinUser_Success() {
        // given
        // UserService의 메소드들이 어떤 값을 반환할지 설정
        when(userService.findUser(anyString())).thenReturn(emptyUser); // 중복 ID 없음
        when(userService.findUserByNickname(anyString())).thenReturn(emptyUser); // 중복 닉네임 없음
        when(userService.findUser(anyString())).thenReturn(emptyUser); // 이미 가입한 회원 없음
        when(userService.saveUser(any(User.class))).thenReturn(user); // 저장 성공

        // when
        JoinStatus result = joinService.joinUser(joinRequest);

        // then
        assertEquals(JoinStatus.SUCCESS, result, "회원 가입이 성공해야 합니다");
    }

    @Test
    @DisplayName("중복 ID로 인한 회원 가입 실패 테스트")
    void joinUser_DuplicateId() {
        // given
        when(userService.findUser(anyString())).thenReturn(user); // 중복 ID 있음
        when(userService.findUserByNickname(anyString())).thenReturn(emptyUser); // 중복 닉네임 없음

        // when
        JoinStatus result = joinService.joinUser(joinRequest);

        // then
        assertEquals(JoinStatus.DUPLICATE_ID, result, "중복 ID로 인해 회원 가입이 실패해야 합니다");
    }

    @Test
    @DisplayName("중복 닉네임으로 인한 회원 가입 실패 테스트")
    void joinUser_DuplicateNickname() {
        // given
        when(userService.findUser(anyString())).thenReturn(emptyUser); // 중복 ID 없음
        when(userService.findUserByNickname(anyString())).thenReturn(user); // 중복 닉네임 있음

        // when
        JoinStatus result = joinService.joinUser(joinRequest);

        // then
        assertEquals(JoinStatus.DUPLICATE_NICKNAME, result, "중복 닉네임으로 인해 회원 가입이 실패해야 합니다");
    }

    /*@Test
    @DisplayName("이미 가입한 회원으로 인한 회원 가입 실패 테스트")
    void joinUser_AlreadyJoin() {
        // given
        when(userService.findUser(anyString())).thenReturn(emptyUser); // 중복 ID 없음
        when(userService.findUserByNickname(anyString())).thenReturn(emptyUser); // 중복 닉네임 없음
        when(userService.findUserToJoin(anyString(), anyString())).thenReturn(user); // 이미 가입한 회원 있음

        // when
        JoinStatus result = joinService.joinUser(joinRequest);

        // then
        assertEquals(JoinStatus.ALREADY_JOIN, result, "이미 가입한 회원으로 인해 회원 가입이 실패해야 합니다");
    }*/

    @Test
    @DisplayName("ID 중복 체크 테스트")
    void checkDuplicateId() {
        // given
        when(userService.findUser("existingId")).thenReturn(user); // 존재하는 ID
        when(userService.findUser("newId")).thenReturn(emptyUser); // 존재하지 않는 ID

        // when
        boolean existingIdResult = joinService.checkDuplicateId("existingId");
        boolean newIdResult = joinService.checkDuplicateId("newId");

        // then
        assertFalse(existingIdResult, "존재하는 ID는 중복으로 판단되어야 합니다");
        assertTrue(newIdResult, "존재하지 않는 ID는 중복이 아니어야 합니다");
    }

    @Test
    @DisplayName("닉네임 중복 체크 테스트")
    void checkDuplicateNickname() {
        // given
        when(userService.findUserByNickname("existingNickname")).thenReturn(user); // 존재하는 닉네임
        when(userService.findUserByNickname("newNickname")).thenReturn(emptyUser); // 존재하지 않는 닉네임

        // when
        boolean existingNicknameResult = joinService.checkDuplicateNickname("existingNickname");
        boolean newNicknameResult = joinService.checkDuplicateNickname("newNickname");

        // then
        assertFalse(existingNicknameResult, "존재하는 닉네임은 중복으로 판단되어야 합니다");
        assertTrue(newNicknameResult, "존재하지 않는 닉네임은 중복이 아니어야 합니다");
    }

    @Test
    @DisplayName("회원 탈퇴 성공 테스트")
    void withdrawal_Success() {
        // given
        String userId = "testId";
        when(userService.findUser(userId)).thenReturn(user); // 존재하는 사용자

        // when
        boolean result = joinService.withdrawal(userId);

        // then
        assertTrue(result, "존재하는 사용자는 탈퇴가 성공해야 합니다");
    }

    @Test
    @DisplayName("존재하지 않는 회원 탈퇴 실패 테스트")
    void withdrawal_UserNotFound() {
        // given
        String userId = "nonExistingId";
        when(userService.findUser(userId)).thenReturn(emptyUser); // 존재하지 않는 사용자

        // when
        boolean result = joinService.withdrawal(userId);

        // then
        assertFalse(result, "존재하지 않는 사용자는 탈퇴가 실패해야 합니다");
    }
}
