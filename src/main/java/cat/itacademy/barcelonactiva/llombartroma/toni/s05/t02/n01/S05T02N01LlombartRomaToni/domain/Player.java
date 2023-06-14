package cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain;

import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.PlayerDTO;
import jakarta.persistence.*;
import lombok.*;
import org.modelmapper.ModelMapper;

import java.sql.Date;
import java.time.Instant;
import java.util.*;

@ToString(callSuper = true)
@EqualsAndHashCode
@Entity
@Table(name = "players")
@Data
@NoArgsConstructor
public class Player {
    {
        this.games = new ArrayList<>();
        this.dateOfRegistration = Date.from(Instant.now()).toString();
    }

    public static final String DEFAULT_NAME = "ANONYMOUS";
    @Id
    private String id;
    @Column
    private String name;
    @Column
    private String dateOfRegistration;
    @Column
    private String email;
    @Column
    private String password;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Game> games;

    public Player(String name, String email, String password) {
        this.name=name;
        this.email=email;
        this.password=password;
    }
    public static PlayerDTO fromPlayerToPlayerDTO(Player player) {
        ModelMapper modelMapper = new ModelMapper();
        PlayerDTO playerDTO = modelMapper.map(player, PlayerDTO.class);
        playerDTO.setSuccessRate(playerDTO.successRatePlayerCalculator());
        return playerDTO;
    }
}
