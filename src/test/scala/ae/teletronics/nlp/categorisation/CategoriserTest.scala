package ae.teletronics.nlp.categorisation

/**
  * Created by hhravn on 31/03/16.
  */
import org.junit._
import Assert._

@Test
class CategoriserTest {

  @Test
  def testExactMatch() = {
    val cat = Category(name = "drugs", words = Array("cocaine"))
    val subj = Categoriser(categories = Array(cat))
    val result = subj.categorise("cocaine")

    assertNotNull(result)
    assertTrue(result.length == 1)
    assertTrue(result.contains(cat.name))
  }

  @Test
  def testThatUniqueCategoryIsReturned() = {
    val cat1 = Category(name = "drugs1", words = Array("cocaine"))
    val cat2 = Category(name = "drugs2", words = Array("smack"))
    val cat3 = Category(name = "drugs3", words = Array("heroin"))
    val cat4 = Category(name = "drugs4", words = Array("amphetamine"))
    val subj = Categoriser(categories = Array(cat1, cat2, cat3, cat4))
    val result = subj.categorise("cocaine")

    assertNotNull(result)
    assertTrue(result.length == 1)
    assertTrue(result.contains(cat1.name))
  }
}
