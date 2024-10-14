package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;

@Slf4j
@Component("memory_user")
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    @Override
    public User getUser(Long id) {
        return users.get(id);
    }

    @Override
    public User addUser(User user) {
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("User {} added", user.getId());
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("User not found");
        }
        users.put(user.getId(), user);
        log.info("User {} updated", user.getId());
        return user;
    }

    @Override
    public void deleteUser(Long id) {
        users.remove(id);
        log.info("User {} deleted", id);
    }

    @Override
    public Collection<User> getUserFriends(Long userId) {
        User user = getUser(userId);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        return user.getFriends().stream().map(this::getUser).toList();
    }

    @Override
    public Collection<User> getCommonFriends(Long userId, Long friendId) {
        User user = getUser(userId);
        User friend = getUser(friendId);
        if (user == null || friend == null) {
            throw new NotFoundException("User not found");
        }
        return user.getFriends().stream().filter(id -> friend.getFriends().contains(id)).map(this::getUser).toList();
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        User user = getUser(userId);
        User friend = getUser(friendId);
        checkExist(user);
        checkExist(friend);
        user.getFriends().add(friendId);
        updateUser(user);
        log.info("User {} added friend {}", userId, friendId);
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        User user = getUser(userId);
        User friend = getUser(friendId);
        checkExist(user);
        checkExist(friend);
        user.getFriends().remove(friendId);
        updateUser(user);
        log.info("User {} removed friend {}", userId, friendId);

    }

    private void checkExist(User user) {
        if (user == null) {
            throw new NotFoundException("User not found");
        }
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
