package cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto;

import lombok.Getter;
import lombok.Setter;

/* This class sets how players are retrieved and showed to the user. Gets rid of id, email, password and even games */
public final class PlayerWithoutGamesDTO implements Comparable{

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private double successRate;


    @Override
    public int compareTo(Object o) {
        PlayerWithoutGamesDTO playerWithoutGamesDTOToBeCompared = (PlayerWithoutGamesDTO) o;
        double comparingValue = this.getSuccessRate()- playerWithoutGamesDTOToBeCompared.getSuccessRate();
        if(comparingValue>0) {
            return -1;
        }
        else if(comparingValue<0) {
            return 1;
        }
        else {
            return this.getName().compareTo(playerWithoutGamesDTOToBeCompared.getName());
        }
    }
}
