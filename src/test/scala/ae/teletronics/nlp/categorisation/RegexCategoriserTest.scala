package ae.teletronics.nlp.categorisation

/**
  * Created by hhravn on 12/04/16.
  */
import org.junit._
import Assert.assertThat
import org.hamcrest.Matchers._
import scala.collection.JavaConversions._

@Test
class RegexCategoriserTest {
  @Test
  def testExactMatch() = {
    val cat = Category("drugs", List("cocaine"))
    val subj = new RegexCategoriser(List(cat))
    val result = subj.categorise("cocaine")

    assertThat(result.categories, contains(cat.name))
  }

  @Test
  def testRegexMatch() = {
    val cat = Category("drugs", List("coc.ine"))
    val subj = new RegexCategoriser(List(cat))
    val result = subj.categorise("cocaine")

    assertThat(result.categories, contains(cat.name))
  }

  @Test
  def testRegex2Match() = {
    val cat = Category("drugs", List("[cCkK]{1}o[ck]{1}ain(e)?"))
    val subj = new RegexCategoriser(List(cat))
    val result = subj.categorise("kokaine")

    assertThat(result.categories, contains(cat.name))
  }

  @Test
  def testRegexDeepMatchPositive() = {
    val cat = Category("drugs", List(".*[cCkK]{1}o[ck]{1}ain(e)?.*"))
    val subj = new RegexCategoriser(List(cat))
    val result = subj.categorise("superkokainehest")

    assertThat(result.categories, contains(cat.name))
  }

  @Test
  def testRegexDeepMatchNegative() = {
    val cat = Category("drugs", List(".*[cCkK]{1}o[ck]{1}ain(e)?.*"))
    val subj = new RegexCategoriser(List(cat))
    val result = subj.categorise("superkokafinehest")

    assertThat(result.categories, not(contains(cat.name)))
  }

  @Test
  def testLicensePlateExpression() = {
    val cat = Category("expressions", List("^[a-zA-Z]{2}\\d{2,5}$"))
    val subj = new RegexCategoriser(List(cat))
    val result = subj.categorise("aa12345")

    assertThat(result.categories, contains(cat.name))
  }

  @Test
  def testLicensePlateExpressionNegative() = {
    val cat = Category("expressions", List("^[a-zA-Z]{2}\\d{2,5}$"))
    val subj = new RegexCategoriser(List(cat))
    val result = subj.categorise("aaa12345")

    assertThat(result.categories, not(contains(cat.name)))
  }
}
