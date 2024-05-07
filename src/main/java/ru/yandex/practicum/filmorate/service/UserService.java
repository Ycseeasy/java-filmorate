package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FriendsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class UserService {

    @Autowired
    public InMemoryUserStorage storage;

    public UserService(InMemoryUserStorage storage) {
        this.storage = storage;
    }

    public List<User> getFriendsList(Long userId) {
        User user = storage.search(userId);
        List<User> friendsList = new ArrayList<>();
        Set<Long> friendsIdList = user.getFriendsId();
        for (long id : friendsIdList) {
            friendsList.add(storage.search(id));
        }
        return friendsList;
    }

    public User deleteFriend(long userId, long friendId) {
        User user = storage.search(userId);
        User friend = storage.search(friendId);

        if (user.getFriendsId().remove(friendId)) {
            friend.getFriendsId().remove(userId);
            log.info("Пользователь c id - {} успешно удалён из друзей", friendId);
        }
        return user;
    }

    public User addFriend(long userId, long friendId) {
        User user = storage.search(userId);
        User friend = storage.search(friendId);

        if (user.getFriendsId().add(friendId)) {
            friend.getFriendsId().add(userId);
            log.info("Пользователь c id - {} успешно добавлен в друзья", friendId);
        } else {
            throw new FriendsException("Пользователь c id - " + friendId + " уже есть в списке друзей");
        }
        return user;
    }

    public List<User> getMutualFriend(long userId, long friendId) {
        log.info("Выгрузка общих друзей у пользователей с ID: {} и {}", userId, friendId);
        List<User> commonFriendsList = new ArrayList<>();
        User user = storage.search(userId);
        User friend = storage.search(friendId);
        Set<Long> userFriendsSet = user.getFriendsId();
        Set<Long> friendFriendsSet = friend.getFriendsId();

        userFriendsSet.retainAll(friendFriendsSet);

        for (long id : userFriendsSet) {
            commonFriendsList.add(storage.search(id));
        }

        if (commonFriendsList.isEmpty()) {
            throw new NotFoundException("Общие друзья не найдены");
        }
        return commonFriendsList;
    }
}