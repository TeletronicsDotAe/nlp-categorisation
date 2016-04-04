package ae.teletronics.nlp.categorisation

import java.util

/**
  * Created by Boris on 01-04-2016.
  */
case class CategoriserResult(categoryMatches: util.List[CategoryMatch], categoriserName: String, categoriserParameters: util.Map[String, String]) {

  import scala.collection.JavaConversions._

  def categories: util.List[String] = {
    categoryMatches.map(cm => cm.categoryName)
  }

  def matchesForCategory(categoryName: String): util.List[WordMatch] = {
    val maybeMatches: Option[util.List[WordMatch]] = categoryMatches.toList.find(cm => cm.categoryName.equals(categoryName)).map(cm => cm.matches)
    val empty: util.List[WordMatch] = List.empty[WordMatch].toList

    maybeMatches.getOrElse(empty)
  }

  def matchesForWordInCategory(categoryName: String, categoryWord: String): util.List[String] = {
    val maybeMatches: Option[util.List[String]] = categoryMatches
      .toList
      .find(cm => cm.categoryName.equals(categoryName))
      .map(cm => cm.matches
        .filter(wm => wm.categoryWord.equals(categoryWord))
        .map(wm => wm.textWord)
        .toList)
    val empty: util.List[String] = List.empty[String].toList

    maybeMatches.getOrElse(empty)
  }

  def getCategoryMatches: util.List[CategoryMatch] = { categoryMatches }

  def getCategoriserName: String = { categoriserName }

  def getCategoriserParameters: util.Map[String, String] = { categoriserParameters }
}
