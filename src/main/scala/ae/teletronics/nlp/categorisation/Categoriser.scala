package ae.teletronics.nlp.categorisation

import java.util.UUID

import ae.teletronics.nlp.categorisation.storage.TopicStore

/**
  * Created by hhravn on 31/03/16.
  */
class Categoriser(store: TopicStore) extends CategoriserTrait {

  import scala.collection.JavaConversions._

  val strictMatcher = new FuzzyMatcher(0, 0)
  val fuzzyMatcher = new FuzzyMatcher(2, 0.05)
  val regexMatcher = new RegexMatcher

  val matchers = store.list().map(c => c -> c.entries.map(EntryParser.parse)).toMap

  def categorise(sentence: String): List[CategoryMatch] = {
    store
      .list
      .map(c => new CategoryMatch(c.name, matchCategory(sentence, c)))
      .filter(_.entryMatches.length > 0)
  }

  def getCategories(): List[Category] = store.list

  def getCategory(id: UUID): Option[Category] =
    store.get(id) match {
      case null => None
      case obj  => Some(obj)
    }

  def createCategory(name: String, entries: List[String]) = store.create(name, entries)

  def deleteCategory(id: UUID): Category = store.delete(id)

  def updateCategory(topic: Category): Category = store.update(topic)

  private def matchCategory(sentence: String, category: Category): List[EntryMatch] = {
    matchers(category)
      .map(p => new EntryMatch(p.label, p.matcher.name, p.exactEntry, p.matcher.doMatch(sentence, p.exactEntry)))
      .filter(_.matches.length > 0)
      .toList
  }
}
