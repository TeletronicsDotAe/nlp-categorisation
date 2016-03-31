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

}
