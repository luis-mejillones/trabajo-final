package services;

import akka.japi.Pair;
import model.Kudos;
import model.User;
import org.springframework.util.StringUtils;
import play.Logger;

import java.util.List;


public class UserService {
    private KudosService kudosService;

    public UserService() {
        this.kudosService = new KudosService();
    }

    public User create(User user) {
        user.save();
        Logger.info("User created with id: " + user.id);

        return user;
    }

    public List<User> getAll() {
        List<User> list = User.find.all();
        Logger.info("Users retrieved: " + list.size());
        for(User item: list) {
            List<Kudos> kudos = this.kudosService.getKudos(item.id);
            item.kudosQty = kudos.size();
        }

        return list;
    }

    public Pair<User, List<Kudos>> getById(Integer id) {
        User out = User.find.ref(id);
        Logger.info("User find by id: " + out.id);

        List<Kudos> kudos = this.kudosService.getKudos(id);

        return new Pair<>(out, kudos);
    }

    public void delete(Integer id) {
        User.find.ref(id).delete();
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
        List<User> list = User.find.query().where()
                .or()
                .ilike("nickname", "%" + nickname + "%")
                .ilike("full_name", "%" + name + "%")
                .endOr()
                .orderBy("id asc")
                .setFirstRow(0)
                .setMaxRows(100)
                .findPagedList()
                .getList();

        Logger.info("User find by nickname and name criteria '" + nickname + " or " + name + "'");
        Logger.info("User records found: " + list.size());

        return list;
    }

    private List<User> findByNickname(String nickname) {
        List<User> list = User.find.query().where()
                .ilike("nickname", "%" + nickname + "%")
                .orderBy("id asc")
                .setFirstRow(0)
                .setMaxRows(100)
                .findPagedList()
                .getList();

        Logger.info("User find by nickname criteria '" + nickname + "'");
        Logger.info("User records found: " + list.size());

        return list;
    }

    private List<User> findByName(String name) {
        List<User> list = User.find.query().where()
                .ilike("full_name", "%" + name + "%")
                .orderBy("id asc")
                .setFirstRow(0)
                .setMaxRows(100)
                .findPagedList()
                .getList();

        Logger.info("User find by name criteria '" + name + "'");
        Logger.info("User records found: " + list.size());

        return list;
    }
}
