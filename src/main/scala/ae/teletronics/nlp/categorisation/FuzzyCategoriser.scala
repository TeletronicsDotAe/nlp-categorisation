package ae.teletronics.nlp.categorisation

import scala.math._

/**
  * Created by Boris on 01-04-2016.
  * MaxDistanceAsPercentageOfWordLength is a number between 0 and 1, both inclusive. It is there so that we can prevent e.g. 'Gun' from matching 'cat' or any other three-or-fewer letter words.
  * The categoriser matches based on an 'AND' of maxDistance and MaxDistanceAsPercentageOfWordLength:
  *   The distance between words has to be less than or equal to maxDistance AND
  *   The distance between words over the length of one of the words has to be less than or equal to maxDistanceAsPercentageOfWordLength, for both words.
  */
class FuzzyCategoriser(categories: java.util.List[Category], maxDistance: Int, maxDistanceAsPercentageOfWordLength: Double) extends Categoriser {

  def categorise(text: String): java.util.List[String] = {
    import scala.collection.JavaConversions._

    val textWords = words(text)
    categories
      .filter(c => textWords.exists(word => fuzzyContains(c.words, word)))
      .map(c => c.name)
  }

  private def fuzzyContains(words: Iterable[String], word: String): Boolean = {
    words.exists(w => isFuzzyMatch(word, w))
  }

  private def isFuzzyMatch(word1: String, word2: String): Boolean = {
    val distance = Levenshtein.distance(word1, word2)
    val term0 = distance <= maxDistance
    val term1 = distance.toDouble / word1.length() <= maxDistanceAsPercentageOfWordLength
    val term2 = distance.toDouble / word2.length() <= maxDistanceAsPercentageOfWordLength

    term0 && term1 && term2
  }
}
