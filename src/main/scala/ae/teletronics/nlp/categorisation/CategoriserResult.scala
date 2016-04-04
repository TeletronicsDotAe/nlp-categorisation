package ae.teletronics.nlp.categorisation

/**
  * Created by Boris on 01-04-2016.
  */
case class CategoriserResult(categoryMatches: java.util.List[CategoryMatch], categoriserName: String, categoriserParameters: java.util.Map[String, String]) {

  import scala.collection.JavaConversions._

  def categories: java.util.List[String] = {
    categoryMatches.map(cm => cm.categoryName)
  }

  def matchesForCategory(categoryName: String): java.util.List[WordMatch] = {
    val maybeMatches: Option[java.util.List[WordMatch]] = categoryMatches.toList.find(cm => cm.categoryName.equals(categoryName)).map(cm => cm.matches)
    val empty: java.util.List[WordMatch] = List.empty[WordMatch].toList

    maybeMatches.getOrElse(empty)
  }

  def matchesForWordInCategory(categoryName: String, categoryWord: String): java.util.List[String] = {
    val maybeMatches: Option[java.util.List[String]] = categoryMatches
      .toList
      .find(cm => cm.categoryName.equals(categoryName))
      .map(cm => cm.matches
        .filter(wm => wm.categoryWord.equals(categoryWord))
        .map(wm => wm.textWord)
        .toList)
    val empty: java.util.List[String] = List.empty[String].toList

    maybeMatches.getOrElse(empty)
  }
}
