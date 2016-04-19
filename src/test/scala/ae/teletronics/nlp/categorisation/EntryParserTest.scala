package ae.teletronics.nlp.categorisation

/**
  * Created by Boris on 2016-04-14.
  */
import java.util
import org.junit._
import Assert.assertThat
import org.hamcrest.Matchers._
import scala.collection.JavaConversions._

@Test
class EntryParserTest {

  val strict = new StrictMatcher().name
  val fuzzy = new FuzzyMatcher(2, 0.2).name
  val regex = new RegexMatcher().name

  @Test
  def testStrictParserNoLabel() = {
    val entry = "cocaine"
    val result = EntryParser.parse(entry)

    assertThat(result.matcher.name, is(strict))
    assertThat(result.label, is(entry))
    assertThat(result.exactEntry, is(entry))
  }

  @Test
  def testStrictParserWithLabel() = {
    val exact = "coke"
    val label = "cocaine"
    val entry = exact + "/" + label
    val result = EntryParser.parse((entry))

    assertThat(result.matcher.name, is(strict))
    assertThat(result.label, is(label))
    assertThat(result.exactEntry, is(exact))
  }

  def testStrictParserWithLabelMultipleSlash() = {
    val exact = "ac/dc"
    val label = "rock"
    val entry = exact + "/" + label
    val result = EntryParser.parse((entry))

    assertThat(result.matcher.name, is(strict))
    assertThat(result.label, is(label))
    assertThat(result.exactEntry, is(exact))
  }

  @Test
  def testFuzzyParserNoLabel() = {
    val exact = "co~aine"
    val entry = "~" + exact
    val result = EntryParser.parse(entry)

    assertThat(result.matcher.name, is(fuzzy))
    assertThat(result.label, is(exact))
    assertThat(result.exactEntry, is(exact))
  }

  @Test
  def testFuzzyParserWithLabel() = {
    val exact = "co~aine"
    val label = "cocaine"
    val entry = "~" + exact + "/" + label
    val result = EntryParser.parse(entry)

    assertThat(result.matcher.name, is(fuzzy))
    assertThat(result.label, is(label))
    assertThat(result.exactEntry, is(exact))
  }

  @Test
  def testFuzzyParserWithLabelMultipleSlash() = {
    val exact = "ac/dc"
    val label = "rock"
    val entry = "~" + exact + "/" + label
    val result = EntryParser.parse(entry)

    assertThat(result.matcher.name, is(fuzzy))
    assertThat(result.label, is(label))
    assertThat(result.exactEntry, is(exact))
  }

  @Test
  def testFuzzyParserWithLabelAndExplicitParameters() = {
    val exact = "coke"
    val label = "cocaine"
    val maxDist = 2
    val maxDistPercent = 0.125
    val params = maxDist + "," + maxDistPercent
  }

  @Test
  def testRegexParserNoLabel() = {
    val exact = "co.*aine"
    val entry = "/" + exact
    val result = EntryParser.parse(entry)

    assertThat(result.matcher.name, is(regex))
    assertThat(result.label, is(exact))
    assertThat(result.exactEntry, is(exact))
  }

  @Test
  def testRegexParserWithLabel() = {
    val exact = "co.*aine"
    val label = "cocaine"
    val entry = "/" + exact + "/" + label
    val result = EntryParser.parse(entry)

    assertThat(result.matcher.name, is(regex))
    assertThat(result.label, is(label))
    assertThat(result.exactEntry, is(exact))
  }

  @Test
  def testRegexParserWithLabelMultipleSlash() = {
    val exact = "ac/dc"
    val label = "rock"
    val entry = "/" + exact + "/" + label
    val result = EntryParser.parse(entry)

    assertThat(result.matcher.name, is(regex))
    assertThat(result.label, is(label))
    assertThat(result.exactEntry, is(exact))
  }
}


