package com.synacy.poker.hand.types;

import com.synacy.poker.card.Card;
import com.synacy.poker.hand.HandType;

import java.util.List;

/**
 * @see <a href="https://en.wikipedia.org/wiki/List_of_poker_hands#Straight_flush">What is a Straight Flush?</a>
 */
public class StraightFlush extends Straight {

    private int type;
    public static final int STRAIGHT_FLUSH = 101;
    public static final int ROYAL_FLUSH = 102;

    public StraightFlush(List<Card> cards) {
        super(cards);
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public HandType getHandType() {
        return HandType.STRAIGHT_FLUSH;
    }

    /**
     * @return Royal Flush if the hand is a royal flush, or Straight Flush with the highest rank card,
     * e.g. Straight Flush (K High)
     */
    @Override
    public String toString() {
        if(type == STRAIGHT_FLUSH && getCards().size() > 0)
            return "Straight Flush (" + getCards().get(0).getRank().toString() + " High)";
        else
            return "Royal Flush";
    }

}
