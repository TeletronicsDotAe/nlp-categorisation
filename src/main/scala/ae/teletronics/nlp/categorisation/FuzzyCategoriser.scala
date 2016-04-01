package ae.teletronics.nlp.categorisation

import scala.math._

/**
  * Created by Boris on 01-04-2016.
  */
class FuzzyCategoriser(categories: java.util.List[Category], maxDistance: Int) extends Categoriser {

  def categorise(text: String): java.util.List[String] = {
    import scala.collection.JavaConversions._

    val textWords = words(text)
    categories
      .filter(c => textWords.exists(word => fuzzyContains(c.words, word)))
      .map(c => c.name)
  }

  private def fuzzyContains(words: Iterable[String], word: String): Boolean = {
    words.exists(w => Levenshtein.distance(w, word) <= maxDistance)
  }
}
