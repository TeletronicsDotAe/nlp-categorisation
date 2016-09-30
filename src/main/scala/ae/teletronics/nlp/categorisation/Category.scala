package ae.teletronics.nlp.categorisation

import java.util

/**
  * Created by hhravn on 31/03/16.
  */
case class Category(name: String, entries: java.util.List[String] = new util.ArrayList[String]()) extends Serializable
