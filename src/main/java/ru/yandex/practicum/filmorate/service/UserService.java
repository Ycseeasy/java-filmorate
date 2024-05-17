package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
public class UserService {

    private final InMemoryUserStorage storage;

    public List<User> getFriendsList(User user) {
        List<User> friendsList = new ArrayList<>();
        Set<Long> friendsIdList = user.getFriendsId();
        for (long id : friendsIdList) {
            friendsList.add(storage.search(id));
        }
        return friendsList;
    }

    public User deleteFriend(User user, User friend) {
        if (user.getFriendsId().remove(friend.getId())) {
            friend.getFriendsId().remove(user.getId());
            log.info("Пользователь c id - {} успешно удалён из друзей пользователя с id - {}",
                    friend.getId(), user.getId());
        }
        return user;
    }

    public User addFriend(User user, User friend) {

        if (user.getFriendsId().add(friend.getId())) {
            friend.getFriendsId().add(user.getId());
            log.info("Пользователь c id - {} успешно добавлен в друзья пользователю с id - {}",
                    friend.getId(), user.getId());
        } else {
            throw new FriendsException("Пользователь c id - " + friend.getId() + " уже есть в списке друзей");
        }
        return user;
    }

    public List<User> getMutualFriend(User user, User friend) {
        List<User> commonFriendsList = new ArrayList<>();
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

    public User create(User user) {
        return storage.create(user);
    }

    public User update(User user) {
        return storage.update(user);
    }

    public User search(long id) {
        return storage.search(id);
    }

    public List<User> getAll() {
        return storage.getAll();
    }
}