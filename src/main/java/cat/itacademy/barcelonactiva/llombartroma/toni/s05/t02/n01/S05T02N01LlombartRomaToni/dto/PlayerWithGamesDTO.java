package cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto;

import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.Game;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.GameStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

/* This class sets how players are retrieved and showed to the user. Gets rid of id, email and password */
public final class PlayerWithGamesDTO implements Comparable {

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private double successRate;


    @Getter
    @Setter
    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Game> games;

    public double successRatePlayerCalculator() {
        double numOfGames = this.getGames().size();
        if(numOfGames==0) {
            return 0;
        }

        else {
            double numOfSuccess = this.getGames().stream().filter(s->s.getGameStatus().equals(GameStatus.WIN)).count();
            return numOfSuccess/numOfGames*100;
        }
    }

    @Override
    public int compareTo(Object o) {
        PlayerWithGamesDTO playerWithGamesDTOToBeCompared = (PlayerWithGamesDTO)o;
        double comparingValue = this.getSuccessRate()- playerWithGamesDTOToBeCompared.getSuccessRate();
        if(comparingValue>0) {
            return -1;
        }
        else if(comparingValue<0) {
            return 1;
        }
        else {
            return this.getName().compareTo(playerWithGamesDTOToBeCompared.getName());

        }
    }
}
