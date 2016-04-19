package ae.teletronics.nlp.categorisation

/**
  * Created by Boris on 2016-04-15.
  */

import java.util
import org.junit._
import Assert.assertThat
import org.hamcrest.Matchers._
import scala.collection.JavaConversions._

@Test
class StrictCategoriserTest {

  @Test
  def testExactMatch() = {
    val cat = Category("drugs", List("cocaine"))
    val subj = new Categoriser(List(cat))
    val result = subj.categorise("cocaine")

    assertThat(categoryNames(result), contains(cat.name))
  }

  @Test
  def testThatUniqueCategoryIsReturned() = {
    val cat1 = Category("drugs1", List("cocaine"))
    val cat2 = Category("drugs2", List("smack"))
    val cat3 = Category("drugs3", List("heroin"))
    val cat4 = Category("drugs4", List("amphetamine"))
    val subj = new Categoriser(List(cat1, cat2, cat3, cat4))
    val result = subj.categorise("cocaine")

    assertThat(categoryNames(result), contains(cat1.name))
  }

  @Test
  def testNegativeMatch() = {
    val cat = Category("drugs", List("cocaine"))
    val subj = new Categoriser(List(cat))
    val result = subj.categorise("sunshine")

    assertThat(categoryNames(result).length, is(0))
  }

  @Test
  def testCaseInsensitiveMatch() = {
    val cat = Category("drugs", List("CoCaInE"))
    val subj = new Categoriser(List(cat))
    val result = subj.categorise("cocaine")

    assertThat(categoryNames(result), contains(cat.name))
  }

  @Test
  def testLabelMatch() = {
    val cat = Category("drugs", List("coke/cocaine"))
    val subj = new Categoriser(List(cat))
    val result = subj.categorise("coke")

    assertThat(result.length, is(1))
    assertThat(result.get(0).categoryName, is(cat.name))
    assertThat(result.get(0).entryMatches.length, is(1))
    assertThat(result.get(0).entryMatches.get(0).label, is("cocaine"))
    assertThat(result.get(0).entryMatches.get(0).exactEntry, is("coke"))
    assertThat(result.get(0).entryMatches.get(0).matcherName, is(new StrictMatcher().name))
    assertThat(result.get(0).entryMatches.get(0).matches.length, is(1))
    assertThat(result.get(0).entryMatches.get(0).matches.get(0).word, is("coke"))
  }

  def categoryNames(categoryMatches: util.List[CategoryMatch]): util.List[String] = {
    categoryMatches.map(_.categoryName)
  }
}
