package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFountException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserStorage userStorage;

    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    public User getUser(Long id) {
        return userStorage.getUser(id);
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public User addFriend(Long userId, Long friendId) {
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        checkExist(user);
        checkExist(friend);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        userStorage.updateUser(user);
        userStorage.updateUser(friend);
        log.info("User {} added friend {}", userId, friendId);
        return user;
    }

    public User removeFriend(Long userId, Long friendId) {
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        checkExist(user);
        checkExist(friend);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        userStorage.updateUser(user);
        userStorage.updateUser(friend);
        log.info("User {} removed friend {}", userId, friendId);
        return user;
    }

    public Collection<User> listOfFriends(Long userId) {
        User user = userStorage.getUser(userId);
        if (user == null) {
            throw new NotFountException("User not found");
        }
        return user.getFriends().stream().map(userStorage::getUser).toList();
    }

    public Collection<User> listOfCommonFriends(Long userId, Long friendId) {
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        if (user == null || friend == null) {
            throw new NotFountException("User not found");
        }
        return user.getFriends().stream().filter(id -> friend.getFriends().contains(id)).map(userStorage::getUser).toList();
    }

    private void checkExist(User user) {
        if (user == null) {
            throw new NotFountException("User not found");
        }
    }
}
