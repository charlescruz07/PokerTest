package com.synacy.poker.hand.types;

import com.synacy.poker.card.Card;
import com.synacy.poker.hand.Hand;
import com.synacy.poker.hand.HandType;

import java.util.List;

/**
 * @see <a href="https://en.wikipedia.org/wiki/List_of_poker_hands#One_pair">What is a One Pair?</a>
 */
public class OnePair extends Hand {

    private List<Card> pairCards;
    private List<Card> otherCards;

    public OnePair(List<Card> pairCards, List<Card> otherCards) {
        this.pairCards = pairCards;
        this.otherCards = otherCards;
    }

    public HandType getHandType() {
        return HandType.ONE_PAIR;
    }

    /**
     * @return The name of the hand plus kickers ordered by descending rank, e.g. One Pair (2) - A,K,Q High,
     * or the name of the hand and rank if there are no community cards yet in play, e.g. One Pair (2)
     */
    @Override
    public String toString() {
        String message = "";
        if(otherCards.size() >= 3)
        {
            for(int i = 0 ; i < 3 ; i++)
            {
                message += otherCards.get(i).getRank() + ",";
            }
        }

        if(message.length() > 0)
            message = " - " + message.substring(0,message.length()-1) + " High";

        return "One Pair (" + pairCards.get(0).getRank() + ")" + message;
    }

    public List<Card> getPairCards() {
        return pairCards;
    }
}
