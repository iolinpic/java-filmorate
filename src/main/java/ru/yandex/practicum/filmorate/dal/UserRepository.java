package ru.yandex.practicum.filmorate.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Repository("UserRepository")
public class UserRepository extends BaseRepository<User> implements UserStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM users WHERE id = ?";
    private static final String ADD_QUERY = "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE users SET email=?,login=?,name=?,birthday=? WHERE id=?";

    private static final String USER_FRIENDSHIP_QUERY = "SELECT * FROM users WHERE id IN (SELECT friend_id FROM user_friend WHERE user_id=?)";
    private static final String USER_COMMON_FRIENDSHIP_QUERY = "SELECT * FROM users WHERE id IN (SELECT friend_id FROM user_friend WHERE user_id=?) " +
            "and id IN (SELECT friend_id FROM user_friend WHERE user_id=?)";

    private final FriendshipRepository friendshipRepository;

    public UserRepository(JdbcTemplate jdbc, RowMapper<User> mapper, @Autowired FriendshipRepository friendshipRepository) {
        super(jdbc, mapper);
        this.friendshipRepository = friendshipRepository;
    }


    @Override
    public Collection<User> getUsers() {
        Collection<User> users = findMany(FIND_ALL_QUERY);
        return loadFriends(users);
    }

    @Override
    public User getUser(Long id) {
        User user = findOne(FIND_BY_ID_QUERY, id);
        if (user != null) {
            user.getFriends().addAll(friendshipRepository.findAllByUserId(user.getId())
                    .stream()
                    .map(Friendship::getFriendId)
                    .toList()
            );
        }
        return user;
    }

    @Override
    public User addUser(User user) {
        Long id = insert(ADD_QUERY, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
        user.setId(id);
        return user;
    }

    @Override
    public User updateUser(User user) {
        update(UPDATE_QUERY, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public void deleteUser(Long id) {
        delete(DELETE_QUERY, id);
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        friendshipRepository.addFriend(userId, friendId);
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        friendshipRepository.deleteFriend(userId, friendId);
    }

    @Override
    public Collection<User> getUserFriends(Long userId) {
        Collection<User> users = findMany(USER_FRIENDSHIP_QUERY, userId);
        return loadFriends(users);
    }

    private Collection<User> loadFriends(Collection<User> users) {
        if (!users.isEmpty()) {
            Collection<Friendship> friendshipCollection = friendshipRepository.getAll();
            for (User user : users) {
                user.getFriends().addAll(friendshipCollection
                        .stream()
                        .filter(friendship -> friendship.getUserId().equals(user.getId()))
                        .map(Friendship::getFriendId)
                        .toList()
                );
            }
        }
        return users;
    }

    @Override
    public Collection<User> getCommonFriends(Long userId, Long friendId) {
        Collection<User> users = findMany(USER_COMMON_FRIENDSHIP_QUERY, userId, friendId);
        return loadFriends(users);
    }
}
