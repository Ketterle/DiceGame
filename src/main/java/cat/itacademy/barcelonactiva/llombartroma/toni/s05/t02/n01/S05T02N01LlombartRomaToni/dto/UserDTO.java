package cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto;

import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.GameStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


public class UserDTO implements Comparable{

    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private List<GameDTO> games;
    @Getter
    @Setter
    private double successRate;

    public UserDTO() {
        games = new ArrayList<>();
    }
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
        UserDTO userDTOToBeCompared = (UserDTO)o;
        double comparingValue = this.getSuccessRate()- userDTOToBeCompared.getSuccessRate();
        if(comparingValue>0) {
            return -1;
        }
        else if(comparingValue<0) {
            return 1;
        }
        else {
            return this.getName().compareTo(userDTOToBeCompared.getName());

        }
    }
}
