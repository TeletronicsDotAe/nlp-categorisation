package ae.teletronics.nlp.categorisation

/**
  * Created by hhravn on 12/04/16.
  */
class RegexCategoriser(categories: java.util.List[Category]) extends Categoriser(categories){
  override def isMatch(regex: String, word2: String): Boolean = {
    word2.matches(regex)
  }

  override def categorise(text: String): CategoriserResult = {
    import scala.collection.JavaConversions._

    val categoryMatches: List[CategoryMatch] = categories.map(c => {
      val matches = c.words.map(w => w.r.findAllIn(text).map(m => (w, m))).map(o => o.map(t => WordMatch(t._1, t._2)))
      CategoryMatch(
        c.name,
        matches.flatMap(wm => wm))
    }).filter(m => !m.matches.isEmpty).toList

    CategoriserResult(categoryMatches, this.getClass.getName, categoriserParameters)
  }
}