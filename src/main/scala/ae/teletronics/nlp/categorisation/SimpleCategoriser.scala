package ae.teletronics.nlp.categorisation

/**
  * Created by Boris on 01-04-2016.
  */
class SimpleCategoriser(categories: java.util.List[Category]) extends Categoriser {
  def categorise(text: String) : java.util.List[String] = {
    import scala.collection.JavaConversions._

    val textWords = words(text)
    categories.filter(c => textWords.exists(c.words.contains)).map(c => c.name)
  }
}
