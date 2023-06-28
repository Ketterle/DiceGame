package cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.controllers;

import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.Game;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.GameDTO;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.PlayerDTO;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.PlayerRankingDTO;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.services.ServiceToController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

@RestController
public class DiceGameController {
    final ServiceToController serviceToController;

    public DiceGameController(ServiceToController serviceToController) {
        this.serviceToController = serviceToController;
    }

    @PutMapping(value = EndpointConstantsOperation.UPDATE_PLAYER)
    public ResponseEntity<PlayerDTO> updatePlayer(@RequestParam String name, @PathVariable int id) {
        try {
            Optional<PlayerDTO> updatedPlayerOptional = serviceToController.update(name, id);
            if (updatedPlayerOptional.isPresent()) {
                return new ResponseEntity<>(updatedPlayerOptional.get(), HttpStatus.OK);
            } else {
                throw new PlayerNotFoundException();
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping(EndpointConstantsOperation.ADD_GAME)
    public ResponseEntity<Game> addGame(@PathVariable int id) {
        Optional<Game> optionalGame = serviceToController.newGame(id);
        return optionalGame.map(game -> new ResponseEntity<>(game, HttpStatus.CREATED)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @GetMapping(EndpointConstantsOperation.GET_PLAYER_GAMES)
    public ResponseEntity<List<GameDTO>> getPlayerGames(@PathVariable int id) {
        Optional<List<GameDTO>> optionalGames = serviceToController.getPlayerGames(id);
        return optionalGames.map(games -> new ResponseEntity<>(games, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @GetMapping(EndpointConstantsOperation.GET_ALL_PLAYERS)
    public ResponseEntity<List<PlayerRankingDTO>> getAllPlayers() {
        Optional<List<PlayerRankingDTO>> optionalPlayers = serviceToController.getAllPlayers();
        return optionalPlayers.map(playerRankingDTOS -> new ResponseEntity<>(playerRankingDTOS, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @DeleteMapping(EndpointConstantsOperation.DELETE_PLAYER)
    public ResponseEntity<PlayerDTO> delete(@PathVariable int id) {
        Optional<PlayerDTO> optionalPlayer = serviceToController.delete(id);
        return optionalPlayer.map(playerDTO -> new ResponseEntity<>(playerDTO, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @GetMapping(EndpointConstantsOperation.GET_PLAYERS_RANKING)
    public ResponseEntity<List<PlayerRankingDTO>> getPlayersRanking() {
        Optional<List<PlayerRankingDTO>> optionalPlayers = serviceToController.playersRanking();
        return optionalPlayers.map(playerRankingDTOS -> new ResponseEntity<>(playerRankingDTOS, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @GetMapping(EndpointConstantsOperation.GET_AVERAGE_SUCCESS)
    public ResponseEntity<Double> getAverageSuccess() {
        OptionalDouble averageSuccess = serviceToController.averageSuccess();

        if (averageSuccess.isPresent()) {
            return ResponseEntity.ok(averageSuccess.getAsDouble());
        } else {
            return ResponseEntity.noContent().build();
        }
    }
    @GetMapping(EndpointConstantsOperation.GET_BEST_PLAYER)
    public ResponseEntity<PlayerRankingDTO> getBestPlayer() {
        Optional<PlayerRankingDTO> optionalBestPlayer = serviceToController.bestPlayer();
        return optionalBestPlayer.map(playerRankingDTO -> new ResponseEntity<>(playerRankingDTO, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @GetMapping(EndpointConstantsOperation.GET_WORST_PLAYER)
    public ResponseEntity<PlayerRankingDTO> getWorstPlayer() {
        Optional<PlayerRankingDTO> optionalWorstPlayer = serviceToController.worstPlayer();
        return optionalWorstPlayer.map(playerRankingDTO -> new ResponseEntity<>(playerRankingDTO, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    public static class EndpointConstantsOperation {
        public static final String UPDATE_PLAYER = "/dicegame/player/{id}/update";
        public static final String ADD_GAME = "/dicegame/player/{id}/newgame";
        public static final String GET_PLAYER_GAMES = "/dicegame/player/{id}/allgames";
        public static final String GET_ALL_PLAYERS = "/dicegame/player/allplayers";
        public static final String DELETE_PLAYER = "/dicegame/player/{id}/deleteplayer";
        public static final String GET_PLAYERS_RANKING = "/dicegame/player/playersranking";
        public static final String GET_AVERAGE_SUCCESS = "/dicegame/player/averagesuccess";
        public static final String GET_BEST_PLAYER = "/dicegame/player/bestplayer";
        public static final String GET_WORST_PLAYER = "/dicegame/player/worstplayer";
    }

}
