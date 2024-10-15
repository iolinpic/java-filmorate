package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.mapper.FriendshipMapper;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@JdbcTest
@Import({UserRepository.class, UserMapper.class, FriendshipRepository.class, FriendshipMapper.class})
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserRepositoryTest {
    private final UserRepository userRepository;
    private final List<User> defaultUsersList = new ArrayList<>();

    private void createTestData() {
        defaultUsersList.clear();

        User user = new User();
        user.setName("test");
        user.setEmail("test@test.com");
        user.setLogin("test");
        user.setBirthday(LocalDate.now());

        User user2 = new User();
        user2.setName("test2");
        user2.setEmail("test2@test.com");
        user2.setLogin("test2");
        user2.setBirthday(LocalDate.now());

        user = userRepository.addUser(user);
        user2 = userRepository.addUser(user2);

        defaultUsersList.add(user);
        defaultUsersList.add(user2);
    }

    @Test
    void shouldReturnEmptyListByDefault() {
        Collection<User> users = userRepository.getUsers();
        assertTrue(users.isEmpty());
    }

    @Test
    void shouldFindDefaultCreated() {
        createTestData();
        Collection<User> users = userRepository.getUsers();
        assertEquals(2, users.size());
        assertEquals(defaultUsersList.get(0).getName(), users.stream().toList().get(0).getName());
    }


    @Test
    void shouldReturnUserById() {
        createTestData();
        User user = userRepository.getUser(defaultUsersList.get(0).getId());
        assertNotNull(user);
        assertEquals(defaultUsersList.get(0).getName(), user.getName());
    }

    @Test
    void shouldReturnNullIfNotFound() {
        User user = userRepository.getUser(1L);
        assertNull(user);
    }

    @Test
    void shouldSuccessfullyAddUser() {
        User user = new User();
        user.setName("test");
        user.setEmail("test@test.com");
        user.setLogin("test3");
        user.setBirthday(LocalDate.now());
        userRepository.addUser(user);
        assertEquals(1, userRepository.getUsers().size());
    }

    @Test
    void shouldSuccessfullyUpdateUser() {
        createTestData();
        User user = defaultUsersList.get(0);
        user.setName("testUpdated");
        userRepository.updateUser(user);
        assertEquals(user, userRepository.getUser(user.getId()));
    }

    @Test
    void shouldSuccessfullyDeleteUser() {
        createTestData();
        userRepository.deleteUser(defaultUsersList.get(0).getId());
        assertNull(userRepository.getUser(defaultUsersList.get(0).getId()));
    }

    @Test
    void shouldSuccessfullyAddFriend() {
        createTestData();
        userRepository.addFriend(defaultUsersList.get(0).getId(), defaultUsersList.get(1).getId());
        assertEquals(1, userRepository.getUser(defaultUsersList.get(0).getId()).getFriends().size());
        assertEquals(0, userRepository.getUser(defaultUsersList.get(1).getId()).getFriends().size());
    }

    @Test
    void shouldSuccessfullyDeleteFriend() {
        createTestData();
        userRepository.addFriend(defaultUsersList.get(0).getId(), defaultUsersList.get(1).getId());
        userRepository.removeFriend(defaultUsersList.get(0).getId(), defaultUsersList.get(1).getId());
        assertEquals(0, userRepository.getUser(defaultUsersList.get(0).getId()).getFriends().size());
        assertEquals(0, userRepository.getUser(defaultUsersList.get(1).getId()).getFriends().size());
    }

    @Test
    void shouldSuccessfullyReturnUserFriends() {
        createTestData();
        userRepository.addFriend(defaultUsersList.get(0).getId(), defaultUsersList.get(1).getId());
        Collection<User> users = userRepository.getUserFriends(defaultUsersList.get(0).getId());
        assertEquals(1, users.size());
    }

    @Test
    void shouldSuccessfullyReturnCommonFriends() {
        createTestData();
        User user = new User();
        user.setName("test3");
        user.setEmail("test3@test.com");
        user.setLogin("test3@test.com");
        user.setBirthday(LocalDate.now());
        user = userRepository.addUser(user);
        userRepository.addFriend(defaultUsersList.get(0).getId(), defaultUsersList.get(1).getId());
        userRepository.addFriend(user.getId(), defaultUsersList.get(1).getId());
        Collection<User> users = userRepository.getCommonFriends(defaultUsersList.get(0).getId(), user.getId());
        assertEquals(1, users.size());
    }
}