package ae.teletronics.nlp.categorisation

/**
  * Created by Boris on 01-04-2016.
  */
class SimpleCategoriser(categories: Array[Category]) extends Categoriser {
  def categorise(text: String) : Array[String] = {
    val textWords = words(text)
    categories.filter(c => textWords.exists(c.words.contains)).map(c => c.name)
  }
}
