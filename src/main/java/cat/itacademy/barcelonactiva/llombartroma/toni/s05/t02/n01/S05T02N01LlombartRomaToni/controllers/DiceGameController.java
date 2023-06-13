package cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.controllers;

import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.Game;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.Player;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.GameDTO;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.PlayerDTO;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.services.ServiceToController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

@RestController
@RequestMapping("/dicegame")
public class DiceGameController {
    final ServiceToController serviceToController;

    public DiceGameController(ServiceToController serviceToController) {
        this.serviceToController = serviceToController;
    }

    @PostMapping(EndpointConstants.ADD_PLAYER)
    public ResponseEntity<PlayerDTO> add(@RequestBody Player player) {
        Optional<PlayerDTO> optionalPlayer = serviceToController.add(player);
        return optionalPlayer.map(playerDTO -> new ResponseEntity<>(playerDTO, HttpStatus.CREATED)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @PutMapping(value = EndpointConstants.UPDATE_PLAYER)
    public ResponseEntity<PlayerDTO> updatePlayer(@RequestParam String name, @PathVariable String id) {
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

    @PostMapping(EndpointConstants.ADD_GAME)
    public ResponseEntity<Game> addGame(@PathVariable String id) {
        Optional<Game> optionalGame = serviceToController.newGame(id);
        return optionalGame.map(game -> new ResponseEntity<>(game, HttpStatus.CREATED)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @GetMapping(EndpointConstants.GET_PLAYER_GAMES)
    public ResponseEntity<List<GameDTO>> getPlayerGames(@PathVariable String id) {
        Optional<List<GameDTO>> optionalGames = serviceToController.getPlayerGames(id);
        return optionalGames.map(games -> new ResponseEntity<>(games, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @GetMapping(EndpointConstants.GET_ALL_PLAYERS)
    public ResponseEntity<List<PlayerDTO>> getAllPlayers() {
        Optional<List<PlayerDTO>> optionalPlayers = serviceToController.getAllPlayers();
        return optionalPlayers.map(playerDTOS -> new ResponseEntity<>(playerDTOS, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @DeleteMapping(EndpointConstants.DELETE_PLAYER)
    public ResponseEntity<PlayerDTO> delete(@PathVariable String id) {
        Optional<PlayerDTO> optionalPlayer = serviceToController.delete(id);
        return optionalPlayer.map(playerDTO -> new ResponseEntity<>(playerDTO, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @GetMapping(EndpointConstants.GET_PLAYERS_RANKING)
    public ResponseEntity<List<PlayerDTO>> getPlayersRanking() {
        Optional<List<PlayerDTO>> optionalPlayers = serviceToController.playersRanking();
        return optionalPlayers.map(playerDTOS -> new ResponseEntity<>(playerDTOS, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @GetMapping(EndpointConstants.GET_AVERAGE_SUCCESS)
    public ResponseEntity<Double> getAverageSuccess() {
        OptionalDouble averageSuccess = serviceToController.averageSuccess();

        if (averageSuccess.isPresent()) {
            return ResponseEntity.ok(averageSuccess.getAsDouble());
        } else {
            return ResponseEntity.noContent().build();
        }
    }


    @GetMapping(EndpointConstants.GET_BEST_PLAYER)
    public ResponseEntity<PlayerDTO> getBestPlayer() {
        Optional<PlayerDTO> optionalBestPlayer = serviceToController.bestPlayer();
        return optionalBestPlayer.map(playerDTO -> new ResponseEntity<>(playerDTO, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @GetMapping(EndpointConstants.GET_WORST_PLAYER)
    public ResponseEntity<PlayerDTO> getWorstPlayer() {
        Optional<PlayerDTO> optionalWorstPlayer = serviceToController.worstPlayer();
        return optionalWorstPlayer.map(playerDTO -> new ResponseEntity<>(playerDTO, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    public class EndpointConstants {
        public static final String ADD_PLAYER = "/player/addplayer";
        public static final String UPDATE_PLAYER = "/player/{id}/update";
        public static final String ADD_GAME = "/player/{id}/newgame";
        public static final String GET_PLAYER_GAMES = "/player/{id}/allgames";
        public static final String GET_ALL_PLAYERS = "/player/allplayers";
        public static final String DELETE_PLAYER = "/player/{id}/deleteplayer";
        public static final String GET_PLAYERS_RANKING = "/player/playersranking";
        public static final String GET_AVERAGE_SUCCESS = "/player/averagesuccess";
        public static final String GET_BEST_PLAYER = "/player/bestplayer";
        public static final String GET_WORST_PLAYER = "/player/worstplayer";
    }

}
