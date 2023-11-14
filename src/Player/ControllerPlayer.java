import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    // Endpoint para cadastrar um novo jogador (POST)
    @PostMapping
    public ResponseEntity<Player> createPlayer(@RequestBody Player player) {
        Player createdPlayer = playerService.savePlayer(player);
        return new ResponseEntity<>(createdPlayer, HttpStatus.CREATED);
    }

    // Endpoint para recuperar todos os jogadores (GET)
    @GetMapping
    public ResponseEntity<List<Player>> getAllPlayers() {
        List<Player> players = playerService.getAllPlayers();
        return new ResponseEntity<>(players, HttpStatus.OK);
    }

    // Endpoint para recuperar detalhes de um jogador por ID (GET)
    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable Long id) {
        Optional<Player> player = playerService.getPlayerById(id);

        return player.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Endpoint para atualizar dados de um jogador por ID (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<Player> updatePlayer(@PathVariable Long id, @RequestBody Player updatedPlayer) {
        Optional<Player> existingPlayer = playerService.getPlayerById(id);

        if (existingPlayer.isPresent()) {
            Player player = existingPlayer.get();
            player.setPlayerName(updatedPlayer.getPlayerName());
            player.setEmail(updatedPlayer.getEmail());
            player.setNickname(updatedPlayer.getNickname());

            Player savedPlayer = playerService.savePlayer(player);
            return new ResponseEntity<>(savedPlayer, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint para excluir um jogador por ID (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable Long id) {
        Optional<Player> player = playerService.getPlayerById(id);

        if (player.isPresent()) {
            playerService.deletePlayer(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
