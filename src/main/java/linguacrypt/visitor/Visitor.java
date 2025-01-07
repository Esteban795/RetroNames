package linguacrypt.visitor;

import linguacrypt.model.Deck;
import linguacrypt.model.DeckManager;
import linguacrypt.model.Game;
import linguacrypt.model.GameConfiguration;
import linguacrypt.model.Player;

// Définition de l'interface du patter visitor. Celui-ci sera utilisé pour sérialiser et désérialiser de manière flexible
// les différents composants du jeu si besoin
public interface Visitor {
    void visit(Game game);
    void visit(Player player);
    void visit(GameConfiguration gameConfiguration);
    void visit(Deck deck);
    void visit(DeckManager deckManager);
}
