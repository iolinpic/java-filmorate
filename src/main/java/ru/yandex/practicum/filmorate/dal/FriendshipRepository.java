package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.Collection;

@Repository
public class FriendshipRepository extends BaseRepository<Friendship> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM user_friend";
    private static final String FIND_ALL_BY_USER_ID_QUERY = "SELECT * FROM user_friend WHERE user_id = ?";


    private static final String DELETE_QUERY = "DELETE FROM user_friend WHERE user_id = ? AND friend_id = ?";
    private static final String ADD_QUERY = "INSERT INTO user_friend (user_id,friend_id) VALUES (?, ?)";

    public FriendshipRepository(JdbcTemplate jdbc, RowMapper<Friendship> mapper) {
        super(jdbc, mapper);

    }

    public Collection<Friendship> getAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Collection<Friendship> findAllByUserId(long userId) {
        return findMany(FIND_ALL_BY_USER_ID_QUERY, userId);
    }

    public void addFriend(Long userId, Long friendId) {
        update(ADD_QUERY, userId, friendId);
    }


    public void deleteFriend(Long userId, Long friendId) {
        delete(DELETE_QUERY, userId, friendId);
    }


}
