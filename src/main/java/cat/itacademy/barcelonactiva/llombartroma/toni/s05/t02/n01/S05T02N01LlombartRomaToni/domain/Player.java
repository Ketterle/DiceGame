package cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain;

import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.GameDTO;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.PlayerDTO;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.repository.PlayerRepositoryMySQL;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Date;
import java.time.Instant;
import java.util.*;

@ToString(callSuper = true)
@EqualsAndHashCode
@Entity
@Table(name = "players")
public class Player {

    public static final String DEFAULT_NAME = "ANONYMOUS";
    @Id
    @Getter
    @Setter
    private String id;

    @Column
    @Getter
    @Setter
    private String name;

    @Column
    @Getter
    @Setter
    private String dateOfRegistration;

    @Getter
    @Setter
    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Game> games;

    public Player() {
        this.games = new ArrayList<>();
        this.dateOfRegistration = Date.from(Instant.now()).toString();
    }
    public Player(String name) {
        this.name=name;
        this.games = new ArrayList<>();
        this.dateOfRegistration = Date.from(Instant.now()).toString();
    }
    public static PlayerDTO fromPlayerToPlayerDTO(Player player) {
        ModelMapper modelMapper = new ModelMapper();
        PlayerDTO playerDTO = modelMapper.map(player, PlayerDTO.class);
        playerDTO.setSuccessRate(playerDTO.successRatePlayerCalculator());
        return playerDTO;
    }
}
