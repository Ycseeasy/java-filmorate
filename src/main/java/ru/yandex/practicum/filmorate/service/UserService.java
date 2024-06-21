package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FriendshipRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.exception.FriendsException;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userBase;
    private final FriendshipRepository friendshipBase;


    public List<User> getFriendsList(long id) {
        log.info("Отображения списка друзей у пользователя с ID {}", id);
        userBase.findById(id);
        return userBase.getFriendList(id);
    }

    public User addFriend(long userId, long futureFriendId) {
        log.info("Добавления в список друзей пользователя с ID {} пользователю с ID - {}", futureFriendId, userId);
        User user = userBase.findById(userId);
        userBase.findById(futureFriendId);
        Optional<Friendship> searchResult = friendshipBase.findFriendship(userId, futureFriendId);
        if (searchResult.isPresent()) {
            throw new FriendsException("Пользователь с id - " + futureFriendId
                    + " уже состоит в списке друзей у пользователя с id - " + userId);
        }
        Friendship newFriendship = new Friendship(userId, futureFriendId);
        friendshipBase.save(newFriendship);
        log.info("Пользователь с ID - {} успешно был добавлен в список друзей пользователя с ID {}",
                futureFriendId, userId);
        return user;
    }

    public User deleteFriend(long userId, long friendId) {
        log.info("Удаление из списка друзей пользователя с ID {} пользователю с ID - {}", friendId, userId);
        User user = userBase.findById(userId);
        userBase.findById(friendId);
        Optional<Friendship> searchResult = friendshipBase.findFriendship(userId, friendId);
        if (searchResult.isPresent()) {
            if (friendshipBase.deleteFriendship(userId, friendId)) {
                return user;
            } else {
                throw new InternalServerException("Не удалось удалить пользователя из списка друзей");
            }
        } else {
            return user;
        }
    }

    public List<User> getMutualFriend(long userId, long friendId) {
        log.info("Выгрузка списка общих друзей у пользователей с ID {} и {}", userId, friendId);
        List<User> userFriendList = userBase.getFriendList(userId);
        List<User> friendFriendList = userBase.getFriendList(friendId);
        userFriendList.retainAll(friendFriendList);
        return userFriendList;
    }

    public User create(User user) {
        log.info("""
                Добавление в базу данных пользователя:\s
                email - {}
                login - {}
                Имя - {}
                День рождения - {}
                """, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
        return userBase.save(user);
    }

    public User update(User user) {
        log.info("""
                Обновления данных пользователя с ID {}:\s
                email - {}
                login - {}
                Имя - {}
                День рождения - {}
                """, user.getId(), user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
        return userBase.update(user);
    }

    public User search(long id) {
        return userBase.findById(id);
    }

    public List<User> getAll() {
        return userBase.findAll();
    }
}