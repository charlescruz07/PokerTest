package com.synacy.poker.hand;

import com.synacy.poker.card.Card;
import com.synacy.poker.hand.types.*;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * A service that is to used to identify the {@link Hand} given the player's cards and the community
 * cards.
 */
@Component
public class HandIdentifier {

    /**
     * Given the player's cards and the community cards, identifies the player's hand.
     *
     * @param playerCards
     * @param communityCards
     * @return The player's {@link Hand} or `null` if no Hand was identified.
     */
    public Hand identifyHand(List<Card> playerCards, List<Card> communityCards) {
        if(playerCards.size() == 0 || playerCards == null
            || communityCards.size() == 0 || communityCards == null)
            return null;

        ArrayList<Card> allCards = new ArrayList<>();
        allCards.addAll(playerCards);
        allCards.addAll(communityCards);

        Collections.sort(allCards);

        if(isRoyalFlush(allCards))
            return straightFlush;
        if(isStraightFlush(allCards))
            return straightFlush;
        else if(isQuads(allCards))
            return fourOfAKind;
        else if(isFullHouse(allCards))
            return fullHouse;
        else if(isFlush(allCards))
            return flush;
        else if(isStraight(allCards))
            return straight;
        else if(isTrio(allCards))
            return threeOfAKind;
        else if(isTwoPair(allCards))
            return twoPair;
        else if(isOnePair(allCards))
            return onePair;
        else
            return new HighCard(allCards);
    }

    public boolean isRoyalFlush(ArrayList<Card> cards) {
        if (isStraight(cards) && isFlush(cards)) {
            boolean aceExists = false, kingExists = false, queenExists = false, jackExists = false, tenExists = false;
            for (Card c : cards) {
                switch (c.getRank().toString()) {
                    case "A":
                        aceExists = true;
                        break;
                    case "K":
                        kingExists = true;
                        break;
                    case "Q":
                        queenExists = true;
                        break;
                    case "J":
                        jackExists = true;
                        break;
                    case "10":
                        tenExists = true;
                        break;

                }
            }
            if(aceExists && kingExists && queenExists && jackExists && tenExists)
            {
                straightFlush = new StraightFlush(straight.getCards());
                straightFlush.setType(StraightFlush.ROYAL_FLUSH);
                return true;
            }
        } else {
            return false;
        }
        return false;
    }

    private boolean isStraightFlush(ArrayList<Card> cards){
        boolean isStraightFlush = false;

        if(isStraight(cards) && isFlush(cards))
        {
            isStraightFlush = true;
            straightFlush = new StraightFlush(straight.getCards());
            straightFlush.setType(StraightFlush.STRAIGHT_FLUSH);
        }

        return isStraightFlush;
    }

    private boolean isStraight(ArrayList<Card> cards) {
        ArrayList<Card> straightCards = new ArrayList<>();
        ArrayList<Card> otherCards = new ArrayList<>();
        otherCards.addAll(cards);

        int noOfCardsInARow = 0;
        int pos = 0;
        boolean isAStraight = false;
        while (pos < cards.size() - 1 && !isAStraight) {
            if (cards.get(pos + 1).getRank().getIntVal() - cards.get(pos).getRank().getIntVal() == -1) {
                noOfCardsInARow++;
                straightCards.add(cards.get(pos));
                if (noOfCardsInARow == 4) {
                    isAStraight = true;
                    otherCards.removeAll(straightCards);
                    straight = new Straight(straightCards);
                } else {
                    pos++;
                }
            } else {
                noOfCardsInARow = 0;
                pos++;
            }
        }
        return isAStraight;
    }

    private boolean isFlush(ArrayList<Card> cards)
    {
        boolean isFlush = false;
        ArrayList<Card> spades = new ArrayList<>();
        ArrayList<Card> clubs = new ArrayList<>();
        ArrayList<Card> diams = new ArrayList<>();
        ArrayList<Card> hearts = new ArrayList<>();
        for(Card card : cards)
        {
            if(card.getSuit().toString().contains("spades"))
                spades.add(card);
            else if(card.getSuit().toString().contains("clubs"))
                clubs.add(card);
            else if(card.getSuit().toString().contains("diams"))
                diams.add(card);
            else
                hearts.add(card);
        }

        if(spades.size() >= 5)
        {
            isFlush = true;
            flush = new Flush(spades);
        }
        else if(clubs.size() >=5)
        {
            isFlush = true;
            flush = new Flush(clubs);
        }
        else if(diams.size() >= 5)
        {
            isFlush = true;
            flush = new Flush(diams);
        }
        else if(hearts.size() >= 5)
        {
            isFlush = true;
            flush = new Flush(hearts);
        }

        return isFlush;
    }

    private boolean isFullHouse(ArrayList<Card> cards)
    {
        if(isTrio(cards) && isOnePair(cards))
        {
            fullHouse = new FullHouse(threeOfAKind.getThreeOfAKindCards(), onePair.getPairCards());
            return true;
        }
        else
        {
            return false;
        }
    }

    private boolean isQuads(ArrayList<Card> cards)
    {
        boolean isQuads = false;
        HashSet set = new HashSet();
        ArrayList<Card> possibleQuads = new ArrayList<>();
        ArrayList<Card> finalQuads = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
        ArrayList<Card> otherCards = new ArrayList<>();

        otherCards.addAll(cards);

        for(Card card : otherCards)
        {
            temp.add(card.getRank().toString());
            // Find the duplicates
            if(!set.add(card.getRank().toString()))
            {
                possibleQuads.add(card);
            }
        }

        for(int i = 0 ; i < possibleQuads.size() ; i++)
        {
            String currentCardRank = possibleQuads.get(i).getRank().toString();
            int occurrences = Collections.frequency(temp, currentCardRank);
            if(occurrences == 4)
            {
                for(Card card : cards)
                {
                    if(card.getRank().toString().equals(currentCardRank))
                    {
                        finalQuads.add(card);
                    }
                }
                otherCards.removeAll(finalQuads);
                fourOfAKind = new FourOfAKind(finalQuads, otherCards);
                return true;
            }
        }

        return isQuads;
    }

    private boolean isTrio(ArrayList<Card> cards)
    {
        boolean isTrio = false;
        HashSet set = new HashSet();
        ArrayList<Card> possibleTrios = new ArrayList<>();
        ArrayList<Card> finalTrios = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
        ArrayList<Card> otherCards = new ArrayList<>();

        otherCards.addAll(cards);

        for(Card card : otherCards)
        {
            temp.add(card.getRank().toString());
            if(!set.add(card.getRank().toString()))
            {
                possibleTrios.add(card);
            }
        }

        for(int i = 0 ; i < possibleTrios.size() ; i++)
        {
            String currentCardRank = possibleTrios.get(i).getRank().toString();
            int occurrences = Collections.frequency(temp, currentCardRank);
            if(occurrences == 3)
            {
                for(Card card : cards)
                {
                    if(card.getRank().toString().equals(currentCardRank))
                    {
                        finalTrios.add(card);
                    }
                }
                otherCards.removeAll(finalTrios);
                threeOfAKind = new ThreeOfAKind(finalTrios, otherCards);
                return true;
            }
        }

        return isTrio;
    }

    private boolean isTwoPair(ArrayList<Card> cards)
    {
        boolean isTwoPair = false;
        HashSet set = new HashSet();
        ArrayList<Card> pair1 = new ArrayList<>();
        ArrayList<Card> pair2 = new ArrayList<>();
        ArrayList<Card> tempCards = new ArrayList<>();
        ArrayList<Card> otherCards = new ArrayList<>();
        otherCards.addAll(cards);

        for(Card card : otherCards)
        {
            if(!set.add(card.getRank().toString()))
            {
                if(pair1.size() == 0)
                    pair1.add(card);
                else
                    pair2.add(card);
            }
        }

        tempCards.addAll(pair1);
        tempCards.addAll(pair2);

        for(int i = 0 ; i < tempCards.size() ; i++)
        {
            for(Card card : otherCards)
            {
                if(tempCards.size() != 0){
                    if(card.getRank().toString().equals(tempCards.get(i).getRank().toString())
                            && !card.getSuit().toString().equals(tempCards.get(i).getSuit().toString()))
                    {
                        if(pair1.size() == 1)
                            pair1.add(card);
                        else
                            pair2.add(card);
                    }
                }
            }
        }

        if(pair1.size() == 2 && pair2.size() == 2)
        {
            isTwoPair = true;
            otherCards.removeAll(tempCards);
            twoPair = new TwoPair(pair1, pair2, otherCards);
        }

        return isTwoPair;
    }

    private boolean isOnePair(ArrayList<Card> cards)
    {
        boolean isPair = false;
        HashSet set = new HashSet();
        ArrayList<Card> possiblePairs = new ArrayList<>();
        ArrayList<Card> finalPairs = new ArrayList<>();
        ArrayList<String> temp = new ArrayList<>();
        ArrayList<Card> otherCards = new ArrayList<>();

        otherCards.addAll(cards);

        for(Card card : otherCards)
        {
            temp.add(card.getRank().toString());
            if(!set.add(card.getRank().toString()))
            {
                possiblePairs.add(card);
            }
        }

        for(int i = 0 ; i < possiblePairs.size() ; i++)
        {
            String currentCardRank = possiblePairs.get(i).getRank().toString();
            int occurrences = Collections.frequency(temp, currentCardRank);
            if(occurrences == 2)
            {
                for(Card card : cards)
                {
                    if(card.getRank().toString().equals(currentCardRank))
                    {
                        finalPairs.add(card);
                    }
                }
                otherCards.removeAll(finalPairs);
                onePair = new OnePair(finalPairs, otherCards);
                return true;
            }
        }

        return isPair;
    }

    private OnePair onePair;
    private TwoPair twoPair;
    private ThreeOfAKind threeOfAKind;
    private FourOfAKind fourOfAKind;
    private FullHouse fullHouse;
    private Flush flush;
    private Straight straight;
    private StraightFlush straightFlush;

}
