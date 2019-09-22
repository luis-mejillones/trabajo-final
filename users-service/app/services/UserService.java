package services;

import akka.japi.Pair;
import model.Kudos;
import model.User;
import org.springframework.util.StringUtils;
import play.Logger;
import util.message.Message;
import util.message.MessageType;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;


public class UserService {
    private KudosService kudosService;
    private MessageSender messageSender;

    @Inject
    public UserService(KudosService kudosService, MessageSender messageSender) {
        this.kudosService = kudosService;
        this.messageSender = messageSender;
    }

    public User create(User user) {
//        user.save();
        Logger.info("User created with id: " + user.id);

        return user;
    }

    public List<User> getAll() {
        List<User> list = new ArrayList<>();
        Logger.info("Users retrieved: " + list.size());

        return list;
    }

    public User getById(Integer id) {
        User out = new User();
        Logger.info("User find by id: " + out.id);

        return out;
    }

    public Pair<User, List<Kudos>> getDetailById(Integer id) {
//        User out = User.find.ref(id);
//        Logger.info("User find by id: " + out.id);
//
//        List<Kudos> kudos = this.kudosService.getKudos(id);

//        return new Pair<>(out, kudos);
        return new Pair<>(new User(), new ArrayList<>());
    }

    public void delete(Integer id) {
//        User.find.ref(id).delete();
//        this.sendMessage(MessageType.DELETE_USER, id.toString());
        Logger.info("User delete with id: " + id);
    }

    public List<User> find(String nickname, String name) {
        if (!StringUtils.isEmpty(nickname) && StringUtils.isEmpty(name)) {
            return this.findByNickname(nickname);
        }

        if (StringUtils.isEmpty(nickname) && !StringUtils.isEmpty(name)) {
            return this.findByName(name);
        }

        return this.findByBoth(nickname, name);
    }

    private List<User> findByBoth(String nickname, String name) {
        List<User> list = new ArrayList<>(); //User.find.query().where()
//                .or()
//                .ilike("nickname", "%" + nickname + "%")
//                .ilike("full_name", "%" + name + "%")
//                .endOr()
//                .orderBy("id asc")
//                .setFirstRow(0)
//                .setMaxRows(100)
//                .findPagedList()
//                .getList();

        Logger.info("User find by nickname and name criteria '" + nickname + " or " + name + "'");
        Logger.info("User records found: " + list.size());

        return list;
    }

    private List<User> findByNickname(String nickname) {
        List<User> list = new ArrayList<>(); //User.find.query().where()
//                .ilike("nickname", "%" + nickname + "%")
//                .orderBy("id asc")
//                .setFirstRow(0)
//                .setMaxRows(100)
//                .findPagedList()
//                .getList();

        Logger.info("User find by nickname criteria '" + nickname + "'");
        Logger.info("User records found: " + list.size());

        return list;
    }

    private List<User> findByName(String name) {
        List<User> list = new ArrayList<>(); //User.find.query().where()
//                .ilike("full_name", "%" + name + "%")
//                .orderBy("id asc")
//                .setFirstRow(0)
//                .setMaxRows(100)
//                .findPagedList()
//                .getList();

        Logger.info("User find by name criteria '" + name + "'");
        Logger.info("User records found: " + list.size());

        return list;
    }

    public void sendMessage(MessageType type, String message) {
        Message msg = new Message();
        msg.setMessageType(type);
        msg.setContent(message);

        this.messageSender.send(msg);
    }

    public void updateKudosQty(String msg) {
        Kudos kudos = new Kudos();
        try {
            kudos.fromString(msg);
        } catch (Exception e) {
            Logger.error(">>> Error parsing received message: '" + msg + "' " + e.getMessage());

            return;
        }

        User user = this.getById(kudos.targetId);
        user.kudosQty++;
//        user.update();
        Logger.info(">>> Kudos incremented for targetId:" + kudos.targetId + " to " + user.kudosQty);
    }
}
