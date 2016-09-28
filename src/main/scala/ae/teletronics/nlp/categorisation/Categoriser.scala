package ae.teletronics.nlp.categorisation

/**
  * Created by hhravn on 31/03/16.
  */
class Categoriser(categories: List[Category]) extends CategoriserTrait {

  import scala.collection.JavaConversions._

  val strictMatcher = new FuzzyMatcher(0, 0)
  val fuzzyMatcher = new FuzzyMatcher(2, 0.05)
  val regexMatcher = new RegexMatcher

  val matchers = categories.map(c => c -> c.entries.map(EntryParser.parse)).toMap

  def categorise(sentence: String): List[CategoryMatch] = {
    categories
      .map(c => CategoryMatch(c.name, matchCategory(sentence, c)))
      .filter(_.entryMatches.length > 0)
  }

  private def matchCategory(sentence: String, category: Category): List[EntryMatch] = {
    matchers(category)
      .map(p => EntryMatch(p.label, p.matcher.name, p.exactEntry, p.matcher.doMatch(sentence, p.exactEntry)))
      .filter(_.matches.length > 0)
      .toList
  }

}