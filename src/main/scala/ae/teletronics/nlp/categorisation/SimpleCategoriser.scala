package ae.teletronics.nlp.categorisation

/**
  * Created by Boris on 01-04-2016.
  */
class SimpleCategoriser(categories: java.util.List[Category]) extends Categoriser(categories) {
  def isMatch(word1: String, word2: String): Boolean = {
    word1.equals(word2)
  }
}
