package ae.teletronics.nlp.categorisation

/**
  * Created by hhravn on 31/03/16.
  */
abstract class Categoriser(categories: java.util.List[Category]) {

  def categorise(text: String): CategoriserResult = {
    import scala.collection.JavaConversions._

    val textWords = text.split("\\s")

    val categoryMatches: List[CategoryMatch] = categories.map(c => {
      CategoryMatch(
        c.name,
        c.words.flatMap(cw => textWords.filter(tw => isMatch(cw, tw)).map(tw => WordMatch(cw, tw))).toList)
    }).filter(cm => cm.matches.size() != 0).toList

    CategoriserResult(categoryMatches, this.getClass.getName, categoriserParameters)
  }

  def categoriserParameters: Map[String, String] = Map()

  def isMatch(word1: String, word2: String): Boolean
}
