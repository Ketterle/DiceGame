package cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.controllers;

import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.Game;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.User;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.GameDTO;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.UserDTO;
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
    public ResponseEntity<UserDTO> add(@RequestBody User user) {
        Optional<UserDTO> optionalPlayer = serviceToController.add(user);
        return optionalPlayer.map(playerDTO -> new ResponseEntity<>(playerDTO, HttpStatus.CREATED)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @PutMapping(value = EndpointConstants.UPDATE_PLAYER)
    public ResponseEntity<UserDTO> updatePlayer(@RequestParam String name, @PathVariable int id) {
        try {
            Optional<UserDTO> updatedPlayerOptional = serviceToController.update(name, id);
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
    public ResponseEntity<Game> addGame(@PathVariable int id) {
        Optional<Game> optionalGame = serviceToController.newGame(id);
        return optionalGame.map(game -> new ResponseEntity<>(game, HttpStatus.CREATED)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @GetMapping(EndpointConstants.GET_PLAYER_GAMES)
    public ResponseEntity<List<GameDTO>> getPlayerGames(@PathVariable int id) {
        Optional<List<GameDTO>> optionalGames = serviceToController.getPlayerGames(id);
        return optionalGames.map(games -> new ResponseEntity<>(games, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @GetMapping(EndpointConstants.GET_ALL_PLAYERS)
    public ResponseEntity<List<UserDTO>> getAllPlayers() {
        Optional<List<UserDTO>> optionalPlayers = serviceToController.getAllPlayers();
        return optionalPlayers.map(playerDTOS -> new ResponseEntity<>(playerDTOS, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @DeleteMapping(EndpointConstants.DELETE_PLAYER)
    public ResponseEntity<UserDTO> delete(@PathVariable int id) {
        Optional<UserDTO> optionalPlayer = serviceToController.delete(id);
        return optionalPlayer.map(playerDTO -> new ResponseEntity<>(playerDTO, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @GetMapping(EndpointConstants.GET_PLAYERS_RANKING)
    public ResponseEntity<List<UserDTO>> getPlayersRanking() {
        Optional<List<UserDTO>> optionalPlayers = serviceToController.playersRanking();
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
    public ResponseEntity<UserDTO> getBestPlayer() {
        Optional<UserDTO> optionalBestPlayer = serviceToController.bestPlayer();
        return optionalBestPlayer.map(playerDTO -> new ResponseEntity<>(playerDTO, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @GetMapping(EndpointConstants.GET_WORST_PLAYER)
    public ResponseEntity<UserDTO> getWorstPlayer() {
        Optional<UserDTO> optionalWorstPlayer = serviceToController.worstPlayer();
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
