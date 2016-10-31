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

  var matchers = asMatchers() // TODO make functional (state changing)

  def categorise(sentence: String): List[CategoryMatch] = {
    store
      .list
      .map(c => new CategoryMatch(c.name, matchCategory(sentence, c)))
      .filter(_.entryMatches.nonEmpty)
  }

  def getCategories(): List[Category] = store.list

  def getCategory(id: UUID): Option[Category] =
    store.get(id) match {
      case null => None
      case obj  => Some(obj)
    }

  def createCategory(name: String, entries: List[String]) = updateMatchers(store.create(name, entries))

  def deleteCategory(id: UUID): Category = updateMatchers(store.delete(id))

  def updateCategory(topic: Category): Category = updateMatchers(store.update(topic))

  private def matchCategory(sentence: String, category: Category): List[EntryMatch] = {
    matchers(category)
      .map(p => new EntryMatch(p.label, p.matcher.name, p.exactEntry, p.matcher.doMatch(sentence, p.exactEntry)))
      .filter(_.matches.nonEmpty)
      .toList
  }

  private def updateMatchers[A](f: => A): A = {
    val r = f
    matchers = asMatchers()
    r
  }

  private def asMatchers() = {
    store.list().map(c => c -> c.entries.map(EntryParser.parse)).toMap
  }
}
