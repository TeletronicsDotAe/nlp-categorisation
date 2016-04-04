package ae.teletronics.nlp.categorisation

/**
  * Created by Boris on 04-04-2016.
  */
case class CategoryMatch(categoryName: String, matches: java.util.List[WordMatch]) {

  def getCategoryName: String = { categoryName }

  def getMatches: java.util.List[WordMatch] = { matches }
}
