package ae.teletronics.nlp.categorisation

import com.fasterxml.jackson.annotation.JsonProperty

/**
  * Created by Boris on 2016-04-13.
  */
case class Match(@JsonProperty("word") val word: String) {
  def getWord: String = word
}
