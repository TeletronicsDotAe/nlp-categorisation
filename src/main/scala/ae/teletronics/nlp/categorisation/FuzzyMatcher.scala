package ae.teletronics.nlp.categorisation

/**
  * Created by Boris on 2016-04-13.
  * MaxDistanceAsPercentageOfWordLength is a number between 0 and 1, both inclusive.
  *   It is there so that we can prevent e.g. 'Gun' from matching 'cat' or any other three-or-fewer letter words, even if the maxDistance is 3.
  * The matcher matches based on an 'AND' of maxDistance and MaxDistanceAsPercentageOfWordLength:
  *   The distance between words has to be less than or equal to maxDistance AND
  *   The distance between words over the length of one of the words has to be less than or equal to maxDistanceAsPercentageOfWordLength, for both words.
  */
class FuzzyMatcher(val maxDistance: Int, val maxDistanceAsPercentageOfWordLength: Double) extends Matcher {

  override def doMatch(sentence: String, categoryEntry: String): List[Match] = {
    sentence.split("\\s").filter(word => isFuzzyWordMatch(word, categoryEntry)).map(Match).toList
  }

  private def isFuzzyWordMatch(word1: String, word2: String): Boolean = {
    val distance = Levenshtein.distance(word1, word2)
    val term0 = distance <= maxDistance
    val term1 = distance.toDouble / word1.length() <= maxDistanceAsPercentageOfWordLength
    val term2 = distance.toDouble / word2.length() <= maxDistanceAsPercentageOfWordLength

    term0 && term1 && term2

  }
}
