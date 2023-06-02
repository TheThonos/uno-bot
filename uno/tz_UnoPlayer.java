package uno;


 

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class tz_UnoPlayer implements UnoPlayer {

    /**
     * play - This method is called when it's your turn and you need to
     * choose what card to play.
     *
     * The hand parameter tells you what's in your hand. You can call
     * getColor(), getRank(), and getNumber() on each of the cards it
     * contains to see what it is. The color will be the color of the card,
     * or "Color.NONE" if the card is a wild card. The rank will be
     * "Rank.NUMBER" for all numbered cards, and another value (e.g.,
     * "Rank.SKIP," "Rank.REVERSE," etc.) for special cards. The value of
     * a card's "number" only has meaning if it is a number card. 
     * (Otherwise, it will be -1.)
     *
     * The upCard parameter works the same way, and tells you what the 
     * up card (in the middle of the table) is.
     *
     * The calledColor parameter only has meaning if the up card is a wild,
     * and tells you what color the player who played that wild card called.
     *
     * Finally, the state parameter is a GameState object on which you can 
     * invoke methods if you choose to access certain detailed information
     * about the game (like who is currently ahead, what colors each player
     * has recently called, etc.)
     *
     * You must return a value from this method indicating which card you
     * wish to play. If you return a number 0 or greater, that means you
     * want to play the card at that index. If you return -1, that means
     * that you cannot play any of your cards (none of them are legal plays)
     * in which case you will be forced to draw a card (this will happen
     * automatically for you.)
     */
    public int play(List<Card> hand, Card upCard, Color calledColor,
        GameState state) {
        int cardToPlay = -1;
        ArrayList<Card> playedCards = new ArrayList<Card>(state.getPlayedCards());
        Map<Color, ArrayList<Card>> validCards = new HashMap<Color, ArrayList<Card>>();
        Map<Color, ArrayList<Card>> organizedHand = new HashMap<Color, ArrayList<Card>>();
        Map<String, ArrayList<Card>> organizedPile = new HashMap<String, ArrayList<Card>>();
        Map<Color, Boolean> canPlay = new HashMap<Color, Boolean>();
        
        playedCards.add(upCard);
        
        validCards.put(Color.RED, new ArrayList<Card>());
        validCards.put(Color.YELLOW, new ArrayList<Card>());
        validCards.put(Color.GREEN, new ArrayList<Card>());
        validCards.put(Color.BLUE, new ArrayList<Card>());
        validCards.put(Color.NONE, new ArrayList<Card>());
        
        organizedHand.put(Color.RED, new ArrayList<Card>());
        organizedHand.put(Color.YELLOW, new ArrayList<Card>());
        organizedHand.put(Color.GREEN, new ArrayList<Card>());
        organizedHand.put(Color.BLUE, new ArrayList<Card>());
        organizedHand.put(Color.NONE, new ArrayList<Card>());
        
        organizedPile.put("0", new ArrayList<Card>());
        organizedPile.put("1", new ArrayList<Card>());
        organizedPile.put("2", new ArrayList<Card>());
        organizedPile.put("3", new ArrayList<Card>());
        organizedPile.put("4", new ArrayList<Card>());
        organizedPile.put("5", new ArrayList<Card>());
        organizedPile.put("6", new ArrayList<Card>());
        organizedPile.put("7", new ArrayList<Card>());
        organizedPile.put("8", new ArrayList<Card>());
        organizedPile.put("9", new ArrayList<Card>());
        organizedPile.put("SKIP", new ArrayList<Card>());
        organizedPile.put("REVERSE", new ArrayList<Card>());
        organizedPile.put("DRAW_TWO", new ArrayList<Card>());
        organizedPile.put("WILD", new ArrayList<Card>());
        organizedPile.put("WILD_D4", new ArrayList<Card>());
        
        canPlay.put(Color.RED, false);
        canPlay.put(Color.YELLOW, false);
        canPlay.put(Color.GREEN, false);
        canPlay.put(Color.BLUE, false);
        
        for(Card card: hand){
            if(card.canPlayOn(upCard, calledColor)){
                validCards.get(card.getColor()).add(card);
                cardToPlay = hand.indexOf(card);
            }
            if(card.getColor() != Color.NONE)
                canPlay.put(card.getColor(), true);
            organizedHand.get(card.getColor()).add(card);
        }
        
        for(Card card: playedCards){
            if(card.getRank() == Rank.NUMBER)
                organizedPile.get(((Integer)card.getNumber()).toString()).add(card);
            else
                organizedPile.get(card.getRank().toString()).add(card);
        }
        
        Color greatestColorHand = Color.NONE;
        int greatestNumHand = 0;
        
        if(canPlay.get(Color.RED) && organizedHand.get(Color.RED).size() > greatestNumHand){
            greatestColorHand = Color.RED;
            greatestNumHand = organizedHand.get(Color.RED).size();
        }
        if(canPlay.get(Color.YELLOW) && organizedHand.get(Color.YELLOW).size() > greatestNumHand){
            greatestColorHand = Color.YELLOW;
            greatestNumHand = organizedHand.get(Color.YELLOW).size();
        }
        if(canPlay.get(Color.GREEN) && organizedHand.get(Color.GREEN).size() > greatestNumHand){
            greatestColorHand = Color.GREEN;
            greatestNumHand = organizedHand.get(Color.GREEN).size();
        }
        if(canPlay.get(Color.BLUE) && organizedHand.get(Color.BLUE).size() > greatestNumHand){
            greatestColorHand = Color.BLUE;
            greatestNumHand = organizedHand.get(Color.BLUE).size();
        }
        
        if(greatestColorHand == Color.NONE){
            cardToPlay = -1;
        } else if(validCards.get(greatestColorHand).size() == 1){
            cardToPlay = hand.indexOf(validCards.get(greatestColorHand).get(0));
        } else {
            ArrayList<Card> greatestNumsPlayed = new ArrayList<Card>();
            int greatestNumPlayed = 0;
            for(Card card: validCards.get(greatestColorHand)){
                int numPlayed;
                if(card.getRank() == Rank.NUMBER)
                    numPlayed = organizedPile.get(((Integer)card.getNumber()).toString()).size();
                else {
                    // numPlayed = organizedPile.get(card.getRank().toString()).size();
                    continue;
                }
                
                if(numPlayed > greatestNumPlayed){
                    greatestNumsPlayed.clear();
                    greatestNumsPlayed.add(card);
                    greatestNumPlayed = organizedPile.get(((Integer)card.getNumber()).toString()).size();
                } else if(numPlayed == greatestNumPlayed){
                    greatestNumsPlayed.add(card);
                }
            }
            int numToPlay = 0;
            for(Card card: greatestNumsPlayed){
                if(card.getNumber() > numToPlay){
                    cardToPlay = hand.indexOf(card);
                    numToPlay = card.getNumber();
                }
            }
        }
        
        return cardToPlay;
    }


    /**
     * callColor - This method will be called when you have just played a
     * wild card, and is your way of specifying which color you want to 
     * change it to.
     *
     * You must return a valid Color value from this method. You must not
     * return the value Color.NONE under any circumstances.
     */
    public Color callColor(List<Card> hand) {
        Map<Color, ArrayList<Card>> organizedHand = new HashMap<Color, ArrayList<Card>>();
        Map<Color, Boolean> canPlay = new HashMap<Color, Boolean>();
        
        organizedHand.put(Color.RED, new ArrayList<Card>());
        organizedHand.put(Color.YELLOW, new ArrayList<Card>());
        organizedHand.put(Color.GREEN, new ArrayList<Card>());
        organizedHand.put(Color.BLUE, new ArrayList<Card>());
        organizedHand.put(Color.NONE, new ArrayList<Card>());
        
        canPlay.put(Color.RED, false);
        canPlay.put(Color.YELLOW, false);
        canPlay.put(Color.GREEN, false);
        canPlay.put(Color.BLUE, false);
        
        for(Card card: hand){
            organizedHand.get(card.getColor()).add(card);
        }
        
        Color greatestColorHand = Color.NONE;
        int greatestNumHand = 0;
        
        if(canPlay.get(Color.RED) && organizedHand.get(Color.RED).size() > greatestNumHand){
            greatestColorHand = Color.RED;
            greatestNumHand = organizedHand.get(Color.RED).size();
        }
        if(canPlay.get(Color.YELLOW) && organizedHand.get(Color.YELLOW).size() > greatestNumHand){
            greatestColorHand = Color.YELLOW;
            greatestNumHand = organizedHand.get(Color.YELLOW).size();
        }
        if(canPlay.get(Color.GREEN) && organizedHand.get(Color.GREEN).size() > greatestNumHand){
            greatestColorHand = Color.GREEN;
            greatestNumHand = organizedHand.get(Color.GREEN).size();
        }
        if(canPlay.get(Color.BLUE) && organizedHand.get(Color.BLUE).size() > greatestNumHand){
            greatestColorHand = Color.BLUE;
            greatestNumHand = organizedHand.get(Color.BLUE).size();
        }
        
        return greatestColorHand;
    }
 
}