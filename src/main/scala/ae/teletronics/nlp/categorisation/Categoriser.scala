package ae.teletronics.nlp.categorisation

/**
  * Created by hhravn on 31/03/16.
  */
class Categoriser(val categories: Array[Category]) {
  def categorise(text: String) : Array[String] = {
    val textWords = text.split("\\s")
    return categories.filter(c => textWords.exists(c.words.contains)).map(c => c.name)
  }
}
