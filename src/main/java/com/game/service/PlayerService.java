package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.exception.NotFoundException;
import com.game.exception.ValidationException;
import com.game.repository.PlayerRepository;
import com.game.utils.CalculateAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PlayerService {
    private static List<Player> players = new ArrayList<>();
    private PlayerRepository playerRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository){
        this.playerRepository = playerRepository;
    }

    public List<Player> getPlayersList( String name,
                                    String title,
                                    Race race,
                                    Profession profession,
                                    Long after,
                                    Long before,
                                    Boolean banned,
                                    Integer minExperience,
                                    Integer maxExperience,
                                    Integer minLevel,
                                    Integer maxLevel,
                                    PlayerOrder order,
                                    Integer pageNumber,
                                    Integer pageSize){
        Date startDate = (after == null ? null : new Date(after));
        Date endDate = (before == null ? null : new Date(before));
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName()));
        return playerRepository.filteredPlayers(name, title, race, profession, startDate, endDate, banned,
                minExperience, maxExperience, minLevel, maxLevel, pageable);
    }

    public Integer getPlayersCount (String name,
                                    String title,
                                    Race race,
                                    Profession profession,
                                    Long after,
                                    Long before,
                                    Boolean banned,
                                    Integer minExperience,
                                    Integer maxExperience,
                                    Integer minLevel,
                                    Integer maxLevel){
        Date startDate = (after == null ? null : new Date(after));
        Date endDate = (before == null ? null : new Date(before));
        return playerRepository.countOfFilteredPlayers(name, title, race, profession, startDate,
                endDate, banned, minExperience, maxExperience, minLevel, maxLevel);
    }

    public Player createPlayer (Map<String, String> map){
        if (!map.containsKey("name") || !map.containsKey("title") || !map.containsKey("race")
         || !map.containsKey("profession") || !map.containsKey("birthday") || !map.containsKey("experience")) {
            throw new ValidationException("111");
        }
        Player player = new Player();
        player.setBanned(false);
        fillPlayerFields(map, player);
        return playerRepository.saveAndFlush(player);

    }

    public Player getPlayer(String id){
        Long  longId;
        try {
            longId = Long.parseLong(id);
            if (longId <= 0) throw new ValidationException();
        } catch (Exception e){
            throw new ValidationException();
        }
        Player player = playerRepository.findById(longId).orElseThrow(NotFoundException::new);
        return player;
    }

    public Player updatePlayer(Map<String, String> map, String id){
        Long  longId;
        try {
            longId = Long.parseLong(id);
            if (longId <= 0) throw new ValidationException();
        } catch (Exception e){
            throw new ValidationException();
        }
        Player player = playerRepository.findById(longId).orElseThrow(NotFoundException::new);
        fillPlayerFields(map, player);
        return playerRepository.saveAndFlush(player);
    }

    public void  deletePlayer(String id){
        Long  longId;
        try {
            longId = Long.parseLong(id);
            if (longId <= 0) throw new ValidationException();
        } catch (Exception e){
            throw new ValidationException();
        }
        if (!playerRepository.existsById(longId)) {
            throw new NotFoundException();
        } else {
            playerRepository.deleteById(longId);
        }

    }

    private void fillPlayerFields (Map<String, String> map, Player player) {
        for (String key : map.keySet()){
            switch (key){
                case "name":
                    String name = map.get("name");
                    if (name.length() == 0 || name.length() > 12) {
                        throw new ValidationException();
                    } else {
                        player.setName(name);
                    }
                    break;
                case "title":
                    String title = map.get("title");
                    if (title.length() > 30) {
                        throw new ValidationException();
                    } else {
                        player.setTitle(title);
                    }
                    break;
                case "race":
                    player.setRace(Race.valueOf(map.get("race")));
                    break;
                case "profession":
                    player.setProfession(Profession.valueOf(map.get("profession")));
                    break;
                case "birthday":
                    long longBirthday = Long.parseLong(map.get("birthday"));
                    if (longBirthday < 946663200000L || longBirthday > 32535104400000L) {
                        throw new ValidationException();
                    } else {
                        player.setBirthday(new Date(longBirthday));
                    }
                    break;
                case "experience":
                    int exp = Integer.parseInt(map.get("experience"));
                    if (exp < 0 || exp > 10000000) {
                        throw new ValidationException();
                    } else {
                        player.setExperience(exp);
                        int level = (int)((Math.sqrt(2500 + 200 * exp) - 50) / 100);
                        int toNextLevel = 50 * (level + 1) * (level + 2) - exp;
                        player.setLevel(level);
                        player.setUntilNextLevel(toNextLevel);
                    }

                    break;
                case "banned":
                    boolean boolBanned = Boolean.parseBoolean(map.get("banned"));
                    player.setBanned(boolBanned);

            }
        }
    }

}
