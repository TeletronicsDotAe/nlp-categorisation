package ae.teletronics.nlp.categorisation

import scala.math._

/**
  * Created by Boris on 01-04-2016.
  */
class FuzzyCategoriser(categories: Array[Category], maxDistance: Int) extends Categoriser {

  def categorise(text: String): Array[String] = {
    val textWords = words(text)
    categories.filter(c => textWords.exists(word => fuzzyContains(c.words, word))).map(c => c.name)
  }

  def fuzzyContains(words: Array[String], word: String): Boolean = {
    words.exists(w => Levenshtein.distance(w, word) <= maxDistance)
  }
}
