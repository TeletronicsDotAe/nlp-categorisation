package ae.teletronics.nlp.categorisation

/**
  * Created by Boris on 2016-04-13.
  */
class FuzzyMatcher(val maxDistance: Int, val maxDistanceAsPercentageOfWordLength: Double) extends Matcher {

  override def doMatch(sentence: String, categoryEntry: String): List[Match] = null

}
